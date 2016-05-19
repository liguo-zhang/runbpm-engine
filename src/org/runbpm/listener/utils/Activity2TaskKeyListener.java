package org.runbpm.listener.utils;

import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.TaskInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;


public class Activity2TaskKeyListener implements ListenerInterface{

	@SuppressWarnings(value={"rawtypes"})
	@Override
	public void execute(ProcessContextBean processContextBean, Enum eventType) {
		if(!(ListenerManager.Event_Type.beforeUserTaskStarted.equals(eventType))){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}
		ActivityInstance activityInstance =processContextBean.getActivityInstance();
		TaskInstance taskInstance = processContextBean.getTaskInstance();
		
		taskInstance.setKeyA(activityInstance.getKeyA());
		taskInstance.setKeyB(activityInstance.getKeyB());
		taskInstance.setKeyC(activityInstance.getKeyC());
		taskInstance.setKeyD(activityInstance.getKeyD());
		
	}

	
	
}
