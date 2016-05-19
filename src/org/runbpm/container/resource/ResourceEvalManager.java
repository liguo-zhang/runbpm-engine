package org.runbpm.container.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.UserTaskResourceAssignment;
import org.runbpm.bpmn.definition.UserTaskResourceExpression;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.resource.GlobalResourceHandler;
import org.runbpm.handler.resource.ResourceHandler;
import org.runbpm.handler.resource.User;

public class ResourceEvalManager {
	
	private static GlobalResourceHandler identityService = Configuration.getContext().getGlobalResourceHandler();
	
	private static Map<String,ResourceHandler> resourceAssignmentHandlerMap = new HashMap<String,ResourceHandler>();

	public List<User> getUserList(UserTaskResourceAssignment resourceAssignment,ProcessContextBean processContextBean){
		
		List<User> userList = new ArrayList<User>();
		
		Map<String, VariableInstance> userTaskContext = processContextBean.getVariableMap();
		
		
		
		List<UserTaskResourceExpression> userTaskResourceExpressionList = resourceAssignment.getUserTaskResourceExpressionList();
		for(UserTaskResourceExpression userTaskResourceExpression : userTaskResourceExpressionList){
			String value = userTaskResourceExpression.getValue();
			if(userTaskResourceExpression.getType()!=null){
				
				//固定人->resourceAssignmentHandler
				if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.user)){
					User user = identityService.getUser(value);
					userList.add(user);
				}
				//固定组->resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.group)){
					userList = identityService.getUsersByGroupIdVariableMap(value, processContextBean);
				}
				//动态人->resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.user_variable)){
					VariableInstance v =  userTaskContext.get(value);
					if(v==null){
						throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020009_GOT_NULL_VAR_FOR_Variable,"variable["+value+"]");
					}
					User user = identityService.getUser(v.getValue()+"");
					userList.add(user);
				}
				//动态组 ->resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.group_variable)){
					VariableInstance v =  userTaskContext.get(value);
					if(v==null){
						throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020009_GOT_NULL_VAR_FOR_Variable,"variable["+value+"]");
					}
					userList = identityService.getUsersByGroupIdVariableMap(v.getValue().toString(), processContextBean);
				}
				//spring bean id -> resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.handler_bean_id)){
					Object springBean = Configuration.getContext().getBean(value);
					if(springBean instanceof ResourceHandler){
						ResourceHandler resourceAssignmentHandler = (ResourceHandler)springBean;
						userList = resourceAssignmentHandler.getUsers(processContextBean);
					}else{
						throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020100_NO_ResourceAssignmentHandler_Impl,"resource id :["+value+"]");
					}
				}
				//class-> resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.handler_bean_class)){
					ResourceHandler resourceAssignmentHandler = resourceAssignmentHandlerMap.get(value);
					if(resourceAssignmentHandler==null){
						try {
							resourceAssignmentHandler = (ResourceHandler)Class.forName(value).newInstance();
							resourceAssignmentHandlerMap.put(value, resourceAssignmentHandler);
						} catch (Exception e) {
							throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020110_CANNT_INIT_resourceAssignmentHandler,"resource id :["+value+"]",e);
						}
					}
					userList = resourceAssignmentHandler.getUsers(processContextBean);
					
					
				}//动态spring bean ->resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.handler_bean_id_variable)){
					VariableInstance v =  userTaskContext.get(value);
					if(v==null){
						throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020009_GOT_NULL_VAR_FOR_Variable,"variable["+value+"]");
					}
					Object springBean = Configuration.getContext().getBean(v.getValue().toString());
					if(springBean instanceof ResourceHandler){
						ResourceHandler resourceAssignmentHandler = (ResourceHandler)springBean;
						userList = resourceAssignmentHandler.getUsers(processContextBean);
					}else{
						throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020100_NO_ResourceAssignmentHandler_Impl,"value:"+value+",resFORMAL_EXPRESSION_TYPEe id :["+v.getValue()+"]");
					}
				}
				//动态class->resourceAssignmentHandler
				else if(userTaskResourceExpression.getType().equals(UserTaskResourceExpression.RESOURCE_EXPRESSION_TYPE.handler_bean_class_variable)){
					VariableInstance v =  userTaskContext.get(value);
					if(v==null){
						throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020009_GOT_NULL_VAR_FOR_Variable,"variable["+value+"]");
					}
					String variableValue = v.getValue()+"";
					ResourceHandler resourceAssignmentHandler = resourceAssignmentHandlerMap.get(variableValue);
					if(resourceAssignmentHandler==null){
						try {
							resourceAssignmentHandler = (ResourceHandler)Class.forName(variableValue).newInstance();
							resourceAssignmentHandlerMap.put(variableValue, resourceAssignmentHandler);
						} catch (Exception e) {
							throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020110_CANNT_INIT_resourceAssignmentHandler,"variable["+value+"],variable's context value resource id :["+variableValue+"]",e);
						}
					}
					userList = resourceAssignmentHandler.getUsers(processContextBean);
				}
			}
		}
		return userList;
	}
}
