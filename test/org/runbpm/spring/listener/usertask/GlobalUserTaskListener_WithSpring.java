package org.runbpm.spring.listener.usertask;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.container.UserTaskContainer;
import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class GlobalUserTaskListener_WithSpring extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		UserTaskListenerSample.clearAtomic();
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
		Configuration.setContext(springAppContext);
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();

		String fileName = "GlobalUserTaskListener.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());

		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		
		ActivityInstance u1_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u1").iterator().next();
		
		
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u1 = entityManager.listTaskInstanceByActivityInstId(u1_instance.getId());
		Assert.assertEquals("" , taskInstance_u1.size(),3);
		Assert.assertEquals("" , taskInstance_u1.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u1.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u1.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
//		EnumSet<TaskInstance.STATE> stateSet = EnumSet.noneOf(TaskInstance.STATE.class);
//		stateSet.add(TaskInstance.STATE.NOT_STARTED);
		List<TaskInstance> taskInstance_user1 = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		Assert.assertEquals("" , taskInstance_user1.size(),1);
		Assert.assertEquals("" , taskInstance_user1.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer userTaskContainer = new UserTaskContainer(taskInstance_user1.get(0));
		userTaskContainer.claim();
		
		//启动了3个任务
		Assert.assertEquals(
				new Integer(3),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.beforeUserTaskStarted
										.toString()).getValue());
		
		Assert.assertEquals(
				new Integer(3),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.afterUserTaskStarted
										.toString()).getValue());
		//只领取了一个任务
		Assert.assertEquals(
				new Integer(1),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.beforeUserTaskClaimed
										.toString()).getValue());
		
		Assert.assertEquals(
				new Integer(1),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.afterUserTaskClaimed
										.toString()).getValue());
		
	}
	
	@Test
	public void testException() throws Exception{
		
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		
		try{
			ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
					"RunBPM.spring_context_error_definition.xml",this.getClass());
			RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
			Configuration.setContext(springAppContext);
		}catch( org.springframework.beans.factory.BeanCreationException e){
			//ok
			return;
		}
		
	}
}
