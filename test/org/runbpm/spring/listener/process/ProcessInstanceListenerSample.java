package org.runbpm.spring.listener.process;

import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;

public class ProcessInstanceListenerSample implements ListenerInterface {

	@Override
	public void execute(ProcessContextBean processContextBean, Enum eventType) {
		
		if(eventType.equals(ListenerManager.Event_Type.afterProcessInstanceStarted)){
			ProcessDefinition processDefinition = processContextBean.getProcessDefinition();
			String pid = processDefinition.getId();
			ProcessInstance processInstance = processContextBean.getProcessInstance();
			
			System.out.println("afterProcessInstanceCreate processDefinition Id:"+pid);
			System.out.println("afterProcessInstanceCreate processInstance Id:"+processInstance.getId());
			
			Configuration.getContext().getEntityManager().setProcessVariable(
					processContextBean.getProcessInstance().getId(),
					ListenerManager.Event_Type.afterProcessInstanceStarted
							.toString(),
							ListenerManager.Event_Type.afterProcessInstanceStarted
							.toString());
		} else {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}
		
	}
	
}

	