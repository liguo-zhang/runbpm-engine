package org.runbpm.spring.listener.activity;

import java.util.concurrent.atomic.AtomicInteger;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerInterface;
import org.runbpm.listener.ListenerManager;

public class ActListenerSample implements ListenerInterface {

	private static AtomicInteger bStartCounter = new AtomicInteger(0);
	private static AtomicInteger aStartCounter = new AtomicInteger(0);

	private static AtomicInteger bCompleteCounter = new AtomicInteger(0);
	private static AtomicInteger aCompleteCounter = new AtomicInteger(0);
	
	public static void clearAtomic(){
		bStartCounter.set(0);
		aStartCounter.set(0);
		bCompleteCounter.set(0);
		aCompleteCounter.set(0);
	}

	@Override
	public void execute(ProcessContextBean processContextBean, Enum eventType) {
		System.out.println("eventType:---["+eventType.toString()+"]");
		if (ListenerManager.Event_Type.beforeActivityInstanceStarted
				.equals(eventType)) {
			ActivityDefinition activityDefinition = processContextBean
					.getActivityDefinition();
			String pid = activityDefinition.getProcessDefinition().getId();

			System.out
			.println("beforeActivityInstanceStart processDefinition Id:["
					+ pid +"],activity id,[" + activityDefinition.getId() +"]");

			Configuration
					.getContext()
					.getEntityManager()
					.setProcessVariable(
							processContextBean.getActivityInstance()
									.getProcessInstanceId(),
							ListenerManager.Event_Type.beforeActivityInstanceStarted
									.toString(),
							bStartCounter.incrementAndGet());
		} else if (ListenerManager.Event_Type.afterActivityInstanceStarted
				.equals(eventType)) {
			ActivityDefinition activityDefinition = processContextBean
					.getActivityDefinition();
			String pid = activityDefinition.getProcessDefinition().getId();

			System.out
			.println("afterActivityInstanceStarted processDefinition Id:["
					+ pid +"],activity id,[" + activityDefinition.getId() +"]");

			Configuration
					.getContext()
					.getEntityManager()
					.setProcessVariable(
							processContextBean.getActivityInstance()
									.getProcessInstanceId(),
							ListenerManager.Event_Type.afterActivityInstanceStarted
									.toString(),
							aStartCounter.incrementAndGet());
		} else if (ListenerManager.Event_Type.beforeActivityInstanceCompleted
				.equals(eventType)) {
			Configuration
					.getContext()
					.getEntityManager()
					.setProcessVariable(
							processContextBean.getActivityInstance()
									.getProcessInstanceId(),
							ListenerManager.Event_Type.beforeActivityInstanceCompleted
									.toString(),
							bCompleteCounter.incrementAndGet());
		} else if (ListenerManager.Event_Type.afterActivityInstanceCompleted
				.equals(eventType)) {
			Configuration
					.getContext()
					.getEntityManager()
					.setProcessVariable(
							processContextBean.getActivityInstance()
									.getProcessInstanceId(),
							ListenerManager.Event_Type.afterActivityInstanceCompleted
									.toString(),
							aCompleteCounter.incrementAndGet());
		} else {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020301_UNEXPECTED_LISTENER);
		}

	}
}
