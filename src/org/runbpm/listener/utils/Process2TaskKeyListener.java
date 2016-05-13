package org.runbpm.listener.utils;

import org.runbpm.context.Execution;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.TaskInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;


public class Process2TaskKeyListener  implements ListenerInterface{

	@SuppressWarnings(value={"rawtypes"})
	@Override
	public void execute(Execution handlerContext, Enum eventType) {
		if(!(ListenerManager.Event_Type.beforeUserTaskStarted.equals(eventType))){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}
		ProcessInstance processInstance = handlerContext.getProcessInstance();
		TaskInstance taskInstance = handlerContext.getTaskInstance();
		
		taskInstance.setKeyA(processInstance.getKeyA());
		taskInstance.setKeyB(processInstance.getKeyB());
		taskInstance.setKeyC(processInstance.getKeyC());
		taskInstance.setKeyD(processInstance.getKeyD());
		
	}
	
}
