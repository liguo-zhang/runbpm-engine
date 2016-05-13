package org.runbpm.container;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.ExtensionElements;
import org.runbpm.bpmn.definition.InElement;
import org.runbpm.bpmn.definition.OutElement;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.VariableInstance;


public class ActivityOfCallActivityContainer extends ActivityContainer{
	
	public ActivityOfCallActivityContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		super(activityDefinition,activityInstance);
	}

	@Override
	public void activityStart() {
		CallActivity callActivity = (CallActivity)activityDefinition;
		
		String calledElement = callActivity.getCalledElement();
		ProcessContainer processContainer= ProcessContainer.getProcessContainerForNewInstance();
		
		ProcessInstance subProcessInstance = processContainer.createInstance(calledElement);
		subProcessInstance.setParentActivityInstanceId(activityInstance.getId());
		activityInstance.setCallActivityProcessInstanceId(subProcessInstance.getId());
		
		//对in赋值
		ExtensionElements callActivityExtensionElements= callActivity.getExtensionElements();
		if(callActivityExtensionElements!=null){
			for(InElement cIN:callActivityExtensionElements.getInElementList()){
				
				String source = cIN.getSource();
				VariableInstance v = Configuration.getContext().getEntityManager().getVariableInstance(activityInstance.getProcessInstanceId(), source);
				Object value = v.getValue();
				
				String target = cIN.getTarget();
				Configuration.getContext().getEntityManager().setProcessVariable(subProcessInstance.getId(), target, value);
			}
		}
		
		
		processContainer.start();
		
	}
	
	public void complete(ProcessInstance subProcessInstance){
		//对out赋值
		CallActivity callActivity = (CallActivity)activityDefinition;
		ExtensionElements callActivityExtensionElements= callActivity.getExtensionElements();
		if(callActivityExtensionElements!=null){
			for(OutElement cOut:callActivityExtensionElements.getOutElementList()){	

				String source = cOut.getSource();
				VariableInstance v = Configuration.getContext().getEntityManager().getVariableInstance(subProcessInstance.getId(), source);
				Object value = v.getValue();
				
				String target = cOut.getTarget();
				Configuration.getContext().getEntityManager().setProcessVariable(activityInstance.getProcessInstanceId(), target, value);
			}
		}
		
		super.complete();
	}
	
	

}
