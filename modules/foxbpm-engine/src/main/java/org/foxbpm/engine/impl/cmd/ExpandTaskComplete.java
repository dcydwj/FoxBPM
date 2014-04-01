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
package org.foxbpm.engine.impl.cmd;



import com.founder.fix.bpmn2extensions.coreconfig.TaskCommandDef;
import org.foxbpm.engine.exception.FixFlowException;
import org.foxbpm.engine.impl.command.AbstractCustomExpandTaskCommand;
import org.foxbpm.engine.impl.command.ExpandTaskCommand;
import org.foxbpm.engine.impl.identity.Authentication;
import org.foxbpm.engine.impl.interceptor.Command;
import org.foxbpm.engine.impl.interceptor.CommandContext;
import org.foxbpm.engine.impl.util.ReflectUtil;

public class ExpandTaskComplete<A extends AbstractCustomExpandTaskCommand,T> implements Command<T>{

	protected ExpandTaskCommand expandTaskCommand;
	
	public ExpandTaskComplete (ExpandTaskCommand expandTaskCommand){
		this.expandTaskCommand=expandTaskCommand;
	}
	
	
	public T execute(CommandContext commandContext) {
		
		if(Authentication.getAuthenticatedUserId()==null||Authentication.getAuthenticatedUserId().equals("")){
			throw new FixFlowException("登录用户不能!");
		}
		// TODO Auto-generated method stub
		Object[] obj = new Object[] {expandTaskCommand};  
		
		
		TaskCommandDef taskCommandDef= commandContext.getProcessEngineConfigurationImpl().getTaskCommandDefMap().get(this.expandTaskCommand.getCommandType());
		if(taskCommandDef!=null){
			String classNameString=taskCommandDef.getCmd();
			if(classNameString==null||classNameString.equals("")){
				throw new FixFlowException("配置文件中ID为 "+this.expandTaskCommand.getCommandType() + " 的扩展配置cmd属性不能为空!");
			}
			
			String commandClassNameString=taskCommandDef.getCommand();
			if(commandClassNameString==null||commandClassNameString.equals("")){
				throw new FixFlowException("配置文件中ID为 "+this.expandTaskCommand.getCommandType() + " 的扩展配置command属性不能为空!");
			}
			
			//commandClassNameString
			AbstractCustomExpandTaskCommand abstractCustomExpandTaskCommand=(AbstractCustomExpandTaskCommand)ReflectUtil.instantiate(commandClassNameString, obj);
			
			Object[] objTemp = new Object[] {abstractCustomExpandTaskCommand};  
			@SuppressWarnings("unchecked")
			AbstractExpandTaskCmd<A,T> abstractExpandTaskCmd=(AbstractExpandTaskCmd<A,T>)ReflectUtil.instantiate(classNameString, objTemp);
			return (T) abstractExpandTaskCmd.execute(commandContext);
			
		}
		else{
			throw new FixFlowException("配置文件中不存在ID为 "+this.expandTaskCommand.getCommandType() + " 的扩展配置.");
		}
	

	}


}