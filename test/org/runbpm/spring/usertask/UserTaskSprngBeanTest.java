package org.runbpm.spring.usertask;

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
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.EntityManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class UserTaskSprngBeanTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context_addRAHandler.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
		Configuration.setContext(springAppContext);
		
		EntityManager entityManager = Configuration.getContext().getEntityManager();

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel(fileName);
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		
		ActivityInstance u1_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u1").iterator().next();
		
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u1 = entityManager.listTaskInstanceByActivityInstId(u1_instance.getId());
		Assert.assertEquals("" , taskInstance_u1.size(),1);
		Assert.assertEquals("" , taskInstance_u1.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		
		List<TaskInstance> taskInstance_user9 = entityManager.listTaskInstanceByUserIdAndState("user9", null);
		Assert.assertEquals("" , taskInstance_user9.size(),1);
		Assert.assertEquals("" , taskInstance_user9.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer userTaskContainer = new UserTaskContainer(taskInstance_user9.get(0));
		userTaskContainer.claim();
		
		//状态改变
		List<TaskInstance> taskInstance_user9_after_claim = entityManager.listTaskInstanceByUserIdAndState("user9", null);
		Assert.assertEquals("" , taskInstance_user9_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user9_after_claim.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		UserTaskContainer userTaskContainer_after_claim = new UserTaskContainer(taskInstance_user9_after_claim.get(0));
		userTaskContainer_after_claim.complete();
		
		//开始节点1结束后，有 theStart（完成状态） u1（完成状态）u2（运行状态）
		ActivityInstance u2_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u2").iterator().next();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		List<TaskInstance> taskInstance_u2 = entityManager.listTaskInstanceByActivityInstId(u2_instance.getId());
		//user5 共有1个任务
		Assert.assertEquals("" , taskInstance_u2.size(),2);
		Assert.assertEquals("" , taskInstance_u2.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		UserTaskContainer taskInstance_u2_0 = new UserTaskContainer(taskInstance_u2.get(0));
		taskInstance_u2_0.claim();
		taskInstance_u2_0.complete();
		
		//流程完成
		ProcessInstance newProcessInstance = entityManager.getProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
	}
}
