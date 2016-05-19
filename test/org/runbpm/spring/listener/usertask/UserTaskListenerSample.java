package org.runbpm.spring.listener.usertask;

import java.util.concurrent.atomic.AtomicInteger;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;

public class UserTaskListenerSample implements ListenerInterface{

	private static AtomicInteger beforeUserTaskStart = new AtomicInteger(0);
	
	private static AtomicInteger afterUserTaskStart = new AtomicInteger(0);
	
	private static AtomicInteger beforeUserTaskClaim = new AtomicInteger(0);
	
	private static AtomicInteger afterUserTaskClaim = new AtomicInteger(0);
	
	public static void clearAtomic(){
		beforeUserTaskStart.set(0);
		afterUserTaskStart.set(0);
		beforeUserTaskClaim.set(0);
		afterUserTaskClaim.set(0);
	}

	@Override
	public void execute(ProcessContextBean processContextBean, Enum eventType) {

		if(eventType.equals(ListenerManager.Event_Type.beforeUserTaskStarted)){
			Configuration.getContext().getEntityManager().setProcessVariable(
					processContextBean.getActivityInstance().getProcessInstanceId(),
					ListenerManager.Event_Type.beforeUserTaskStarted
							.toString(),
							beforeUserTaskStart.incrementAndGet());

			ActivityDefinition activityDefinition = processContextBean
					.getActivityDefinition();
			String pid = activityDefinition.getProcessDefinition().getId();

			System.out.println("beforeUserTaskStarted processDefinition Id:["
					+ pid +"],activity id,[" + activityDefinition.getId() +"]");
			
		}else if(eventType.equals(ListenerManager.Event_Type.afterUserTaskStarted)){
			Configuration.getContext().getEntityManager().setProcessVariable(
					processContextBean.getActivityInstance().getProcessInstanceId(),
					ListenerManager.Event_Type.afterUserTaskStarted
							.toString(),
							afterUserTaskStart.incrementAndGet());

			ActivityDefinition activityDefinition = processContextBean
					.getActivityDefinition();
			String pid = activityDefinition.getProcessDefinition().getId();

			System.out.println("afterUserTaskStart processDefinition Id:["
					+ pid +"],activity id,[" + activityDefinition.getId() +"]");
			
		} else if(eventType.equals(ListenerManager.Event_Type.beforeUserTaskClaimed)){
			Configuration.getContext().getEntityManager().setProcessVariable(
					processContextBean.getActivityInstance().getProcessInstanceId(),
					ListenerManager.Event_Type.beforeUserTaskClaimed
							.toString(),
							beforeUserTaskClaim.incrementAndGet());

			ActivityDefinition activityDefinition = processContextBean
					.getActivityDefinition();
			String pid = activityDefinition.getProcessDefinition().getId();

			System.out.println("beforeUserTaskClaim processDefinition Id:["
					+ pid +"],activity id,[" + activityDefinition.getId() +"]");
			
		}else if(eventType.equals(ListenerManager.Event_Type.afterUserTaskClaimed)){
			Configuration.getContext().getEntityManager().setProcessVariable(
					processContextBean.getActivityInstance().getProcessInstanceId(),
					ListenerManager.Event_Type.afterUserTaskClaimed
							.toString(),
							afterUserTaskClaim.incrementAndGet());
			ActivityDefinition activityDefinition = processContextBean
					.getActivityDefinition();
			String pid = activityDefinition.getProcessDefinition().getId();

			System.out.println("afterUserTaskClaim processDefinition Id:["
					+ pid +"],activity id,[" + activityDefinition.getId() +"]");
			
		}else {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}
		
	}
	
	
}
