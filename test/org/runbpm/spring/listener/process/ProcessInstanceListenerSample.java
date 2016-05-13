package org.runbpm.spring.listener.process;

import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.context.Execution;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;

public class ProcessInstanceListenerSample implements ListenerInterface {

	@Override
	public void execute(Execution handlerContext, Enum eventType) {
		
		if(eventType.equals(ListenerManager.Event_Type.afterProcessInstanceStarted)){
			ProcessDefinition processDefinition = handlerContext.getProcessDefinition();
			String pid = processDefinition.getId();
			ProcessInstance processInstance = handlerContext.getProcessInstance();
			
			System.out.println("afterProcessInstanceCreate processDefinition Id:"+pid);
			System.out.println("afterProcessInstanceCreate processInstance Id:"+processInstance.getId());
			
			Configuration.getContext().getEntityManager().setProcessVariable(
					handlerContext.getProcessInstance().getId(),
					ListenerManager.Event_Type.afterProcessInstanceStarted
							.toString(),
							ListenerManager.Event_Type.afterProcessInstanceStarted
							.toString());
		} else {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}
		
	}
	
}

	