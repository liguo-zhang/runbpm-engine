package org.runbpm.listener.utils;

import org.runbpm.context.Execution;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;

public class Process2ActivityKeyListener  implements ListenerInterface{

	@SuppressWarnings(value={"rawtypes"})
	@Override
	public void execute(Execution handlerContext, Enum eventType) {
		if(!(ListenerManager.Event_Type.beforeActivityInstanceStarted.equals(eventType))){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}
		ProcessInstance processInstance = handlerContext.getProcessInstance();
		ActivityInstance activityInstance =handlerContext.getActivityInstance();
		
		activityInstance.setKeyA(processInstance.getKeyA());
		activityInstance.setKeyB(processInstance.getKeyB());
		activityInstance.setKeyC(processInstance.getKeyC());
		activityInstance.setKeyD(processInstance.getKeyD());
		
	}
}
