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
import org.runbpm.listener.utils.Process2ActivityKeyListener;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class CopyKeyTest_WithoutSpring extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		UserTaskListenerSample.clearAtomic();
		
		//增加一个监听
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		Configuration.getContext().getGlobalListener().addListener(ListenerManager.Event_Type.beforeActivityInstanceStarted, new Process2ActivityKeyListener());
		Configuration.getContext().getGlobalListener().addListener(ListenerManager.Event_Type.beforeUserTaskStarted, new Activity2TaskKeyListener());
		

		String fileName = "GlobalUserTaskListener.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());

		
		ProcessModel processModel = entityManager.loadLatestProcessModel("GlobalUserTaskListener.xml");
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("GlobalUserTaskListener.xml");
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.startWithKeys("0","1","2","3");
		
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
		
		Assert.assertEquals( processInstance.getKeyA(),"0");
		Assert.assertEquals( processInstance.getKeyB(),"1");
		Assert.assertEquals( processInstance.getKeyC(),"2");
		Assert.assertEquals( processInstance.getKeyD(),"3");
		
		Assert.assertEquals( u1_instance.getKeyA(),"0");
		Assert.assertEquals( u1_instance.getKeyB(),"1");
		Assert.assertEquals( u1_instance.getKeyC(),"2");
		Assert.assertEquals( u1_instance.getKeyD(),"3");
		

		Assert.assertEquals( taskInstance_user1.get(0).getKeyA(),"0");
		Assert.assertEquals( taskInstance_user1.get(0).getKeyB(),"1");
		Assert.assertEquals( taskInstance_user1.get(0).getKeyC(),"2");
		Assert.assertEquals( taskInstance_user1.get(0).getKeyD(),"3");
		
	}
}
