/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author ych
 */
package org.foxbpm.rest.common.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.foxbpm.engine.db.PersistentObject;
import org.foxbpm.engine.impl.util.StringUtil;
import org.foxbpm.engine.query.Query;
import org.foxbpm.rest.common.RestConstants;
import org.restlet.Request;
import org.restlet.data.Cookie;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

/**
 * foxbpm rest资源基础实现，包含基础方法
 * @author ych
 *
 */
public abstract class AbstractRestResource extends ServerResource {

	protected int pageIndex = 1;
	protected int pageSize = 15;
	protected String userId;
	protected String getQueryParameter(String name, Form query) {
		return query.getFirstValue(name);
	}

	protected String getAttribute(String name) {
		return decode((String) getRequest().getAttributes().get(name));
	}

	protected String decode(String string) {
		if (string != null) {
			try {
				return URLDecoder.decode(string, "UTF-8");
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalStateException("JVM does not support UTF-8 encoding.", uee);
			}
		}
		return null;
	}
	
	/**
	 * 验证登陆，从cookie中获取用户编号
	 * @return
	 */
	protected boolean validationUser(){
//		Request request = getRequest();
//		for (Cookie cookie : request.getCookies()) {
//			if("foxSid".equals(cookie.getName())){
//				userId = cookie.getValue();
//			}
//		}
//		
//		if(StringUtil.isEmpty(userId)){
//			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
//			return false;
//		}
		userId = "admin";
		return true;
	}
	
	/**
	 * foxbpm rest接口的分页机制
	 * 接收参数：
	 * start:起始行数
	 * lenth:每页条数
	 * pageIndex:当前页
	 * pageSize:每页条数
	 * 冲突解决：优先处理pageIndex、pageSize
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DataResult paginateList(Query query) {

		Form form = getQuery();
		Set<String> names = form.getNames();
		
		if (names.contains(RestConstants.PAGE_START)) {
			if(names.contains(RestConstants.PAGE_LENGTH)){
				pageSize = StringUtil.getInt(getQueryParameter(RestConstants.PAGE_LENGTH, form));
			}
			pageIndex = StringUtil.getInt(getQueryParameter(RestConstants.PAGE_START, form))/pageSize + 1;
		}
		
		if(names.contains(RestConstants.PAGE_INDEX)){
			pageIndex = StringUtil.getInt(getQueryParameter(RestConstants.PAGE_INDEX, form));
		}
		if(names.contains(RestConstants.PAGE_SIZE)){
			pageSize = StringUtil.getInt(getQueryParameter(RestConstants.PAGE_SIZE, form));
		}
		
		List<PersistentObject> resultObjects = query.listPagination(pageIndex,pageSize);
		List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(resultObjects != null){
			Iterator<PersistentObject> iterator = resultObjects.iterator();
			while(iterator.hasNext()){
				dataMap.add(iterator.next().getPersistentState());
			}
		}
		
		long resultCount = query.count();
		DataResult result = new DataResult();
		result.setData(dataMap);
		result.setPageIndex(pageIndex);
		result.setPageSize(pageSize);
		result.setRecordsTotal(resultCount);
		result.setRecordsFiltered(resultCount);
		return result;
	}
}
