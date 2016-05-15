package org.runbpm.spring.listener.usertask;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.container.UserTaskContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager;
import org.runbpm.listener.utils.Activity2TaskKeyListener;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.runbpm.spring.listener.activity.ActListenerSample;
import org.springframework.core.io.ClassPathResource;

public class UserTaskListener_WithoutSpring extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		UserTaskListenerSample.clearAtomic();
		
		
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		
		String fileName = "UserTaskListener.xml";
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
}
