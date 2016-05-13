package org.runbpm.container;

import java.util.HashMap;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.ServiceTask;
import org.runbpm.context.Configuration;
import org.runbpm.context.Execution;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.ServiceTaskHandler;

public class ActivityOfServiceTaskContainer extends ActivityContainer{
	
	private static Map<String,ServiceTaskHandler> serviceTaskHandlerMap = new HashMap<String,ServiceTaskHandler>();

	public ActivityOfServiceTaskContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		super(activityDefinition,activityInstance);
	}
	
	@Override
	public void activityStart() {
		Map<String,VariableInstance> context = Configuration.getContext().getEntityManager().getVariableMap(activityInstance.getProcessInstanceId());
		
		ProcessInstance processInstance = Configuration.getContext().getEntityManager().getProcessInstance(activityInstance.getProcessInstanceId());
		ProcessDefinition processDefinition= Configuration.getContext().getEntityManager().loadProcessModelByModelId(activityInstance.getProcessModelId()).getProcessDefinition();
		
		Execution execution = new Execution();
		execution.setProcessInstance(processInstance);
		execution.setProcessDefinition(processDefinition);
		execution.setActivityInstance(activityInstance);
		execution.setActivityDefinition(activityDefinition);
		execution.setVariableMap(context);
		
		ServiceTask serviceTaskDefinition =  (ServiceTask) activityDefinition;
		ServiceTask.SERVICETASK_TYPE type = serviceTaskDefinition.getType();
		String value = serviceTaskDefinition.getValue();
		
		ServiceTaskHandler serviceTaskHandler  = null;
		if(type.equals(ServiceTask.SERVICETASK_TYPE.handler_bean_class)){
			serviceTaskHandler = serviceTaskHandlerMap.get(value);
			if(serviceTaskHandler==null){
				try {
					serviceTaskHandler = (ServiceTaskHandler)Class.forName(value).newInstance();
					serviceTaskHandlerMap.put(value, serviceTaskHandler);
				} catch (Exception e) {
					throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020210_CANNT_INIT_ServiceTaskHandler,"service value :["+value+"]",e);
				}
			}
			
			
		}else if(type.equals(ServiceTask.SERVICETASK_TYPE.handler_bean_class_variable)){
			VariableInstance variableInstance =  context.get(value);
			if(variableInstance==null){
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020211_GOT_NULL_VAR_FOR_ServiceTaskHandler,"variable["+value+"]");
			}
			serviceTaskHandler = serviceTaskHandlerMap.get(variableInstance.getValue());
			if(serviceTaskHandler==null){
				try {
					serviceTaskHandler = (ServiceTaskHandler)Class.forName(variableInstance.getValue().toString()).newInstance();
					serviceTaskHandlerMap.put(variableInstance.getValue().toString(), serviceTaskHandler);
				} catch (Exception e) {
					throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020210_CANNT_INIT_ServiceTaskHandler,"service value :["+value+"]",e);
				}
			}
			
		}else if(type.equals(ServiceTask.SERVICETASK_TYPE.handler_bean_id)){
			Object springBean = Configuration.getContext().getBean(value);
			if(springBean instanceof ServiceTaskHandler){
				serviceTaskHandler = (ServiceTaskHandler)springBean;
			}else{
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020101_NO_ServiceTaskHandler_From_Spring,"resource id :["+value+"]");
			}
		}else if(type.equals(ServiceTask.SERVICETASK_TYPE.handler_bean_id_variable)){
			VariableInstance variableInstance =  context.get(value);
			if(variableInstance==null){
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020211_GOT_NULL_VAR_FOR_ServiceTaskHandler,"variable["+value+"]");
			}
			Object springBean = Configuration.getContext().getBean(variableInstance.getValue().toString());
			if(springBean instanceof ServiceTaskHandler){
				serviceTaskHandler = (ServiceTaskHandler)springBean;
			}else{
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020101_NO_ServiceTaskHandler_From_Spring,"resource id :["+value+"]");
			}
		} 
		
		//Ö´ÐÐ
		//this.newUserTaskInstance(serviceTaskDefinition, EntityConstants.TASK_STATE.RUNNING, "RUNBPM-SERVICETASK");
		serviceTaskHandler.executeService(execution);
		
		super.complete();
		
	}
	

}
