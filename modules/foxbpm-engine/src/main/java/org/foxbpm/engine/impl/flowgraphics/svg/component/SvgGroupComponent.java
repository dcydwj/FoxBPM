/**
 * Copyright 1996-2014 FoxBPM Co.,Ltd.
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
 * @author kenshin
 */
package org.foxbpm.engine.impl.flowgraphics.svg.component;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;


import org.foxbpm.engine.impl.flowgraphics.svg.FlowSvgUtil;
import org.foxbpm.engine.impl.flowgraphics.svg.SvgBench;
import org.foxbpm.engine.impl.flowgraphics.svg.to.SvgBaseTo;
import org.foxbpm.engine.impl.flowgraphics.svg.to.SvgGroupTo;
import org.foxbpm.engine.exception.FixFlowException;
import org.foxbpm.engine.impl.util.StringUtil;
import org.foxbpm.engine.impl.util.XmlUtil;

public class SvgGroupComponent implements ISvgComponent {

	private static String comPath = "/svgcomponent/group.xml";
	
	public String createComponent(SvgBaseTo svgTo) {
		String result = null;
		try {
			SvgGroupTo group = (SvgGroupTo)svgTo;
			InputStream in = SvgBench.class.getResourceAsStream(comPath);
			Document doc = XmlUtil.read(in);
			String str = doc.getRootElement().asXML();
			str = FlowSvgUtil.replaceAll(str, local_x, StringUtil.getString(group.getX()));
			str = FlowSvgUtil.replaceAll(str, local_y, StringUtil.getString(group.getY()));
			str = FlowSvgUtil.replaceAll(str, id, group.getId());
			str = FlowSvgUtil.replaceAll(str, text, group.getLabel());
			str = FlowSvgUtil.replaceAll(str, width, StringUtil.getString(group.getWidth()));
			str = FlowSvgUtil.replaceAll(str, hight, StringUtil.getString(group.getHeight()));
			result = str;
		} catch (DocumentException e) {
			throw new FixFlowException("",e);
		}
		return result;
	}

}