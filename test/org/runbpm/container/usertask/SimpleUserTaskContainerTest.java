package org.runbpm.container.usertask;

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
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class SimpleUserTaskContainerTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		process(entityManager,fileName);
		
	}
	

	@Test
	public void testSingle() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = "SimpleUserTaskContainerTest_single.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		process(entityManager,fileName);
		
	}
	


	private void process(MemoryEntityManagerImpl entityManager,String fileName) {
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
		
		//状态改变
		List<TaskInstance> taskInstance_user1_after_claim = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		Assert.assertEquals("" , taskInstance_user1_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user1.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		//活动中其他两个task被删除，只剩下一个
		List<TaskInstance> taskInstance_u1_after_claim = entityManager.listTaskInstanceByActivityInstId(u1_instance.getId());
		Assert.assertEquals("" , taskInstance_u1_after_claim.size(),1);
		
		UserTaskContainer userTaskContainer_after_claim = new UserTaskContainer(taskInstance_user1_after_claim.get(0));
		userTaskContainer_after_claim.complete();
		
		//开始节点1结束后，有 theStart（完成状态） u1（完成状态）u2（运行状态）
		ActivityInstance u2_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u2").iterator().next();
		List<TaskInstance> taskInstance_u2 = entityManager.listTaskInstanceByActivityInstId(u2_instance.getId());
		
		//groupB + user9 共有4个任务
		Assert.assertEquals("" , taskInstance_u2.size(),4);
		Assert.assertEquals("" , taskInstance_u2.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		Assert.assertEquals("" , taskInstance_u2.get(1).getState(),EntityConstants.TASK_STATE.RUNNING);
		Assert.assertEquals("" , taskInstance_u2.get(2).getState(),EntityConstants.TASK_STATE.RUNNING);
		Assert.assertEquals("" , taskInstance_u2.get(3).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		UserTaskContainer userTaskContainer2_0 = new UserTaskContainer(taskInstance_u2.get(0));
		userTaskContainer2_0.complete();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		UserTaskContainer userTaskContainer2_1 = new UserTaskContainer(taskInstance_u2.get(1));
		userTaskContainer2_1.complete();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		UserTaskContainer userTaskContainer2_2 = new UserTaskContainer(taskInstance_u2.get(2));
		userTaskContainer2_2.complete();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		UserTaskContainer userTaskContainer2_3 = new UserTaskContainer(taskInstance_u2.get(3));
		userTaskContainer2_3.complete();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.COMPLETED);
		
		//开始节点1结束后，有 theStart（完成状态） u1（完成状态）u2（完成状态）theEnd(完成状态)
		ActivityInstance ac_theend = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
		Assert.assertEquals("" , ac_theend.getState(),ACTIVITY_STATE.COMPLETED);
		
		//流程完成
		ProcessInstance newProcessInstance = entityManager.loadProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
	}
}
