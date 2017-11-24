package org.runbpm.container.usertask;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.ActivityOfUserTaskContainer;
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
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.resource.User;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class SimpleUserTaskContainerTest3 extends RunBPMTestCase{

	
	@Test
	public void testBack() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		
		//groupB  共有3个任务
		Assert.assertEquals("" , taskInstance_u2.size(),3);
		Assert.assertEquals("" , taskInstance_u2.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u2.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u2.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		//-------------接受任务后，再退回
		TaskInstance taskInstance_in_u2= taskInstance_u2.get(0);
		UserTaskContainer userTaskContainer2_0 = new UserTaskContainer(taskInstance_in_u2);
		userTaskContainer2_0.claim();
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		ActivityContainer u2_instance_activityContainer = ActivityContainer.getActivityContainer(u2_instance);
		u2_instance_activityContainer.terminate(u1_definition);
		//u2节点退回到u1定义后，有 theStart（完成状态） u1（完成状态）u2（终止状态）u1（开始状态） 4个节点
		//工作项的状态为终止
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstanceId).size(),4);
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.TERMINATED);
		Assert.assertEquals("" , taskInstance_in_u2.getState(),TASK_STATE.TERMINATED);
		
		//共2个u1节点
		Assert.assertEquals("" , entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u1").size(),2);
		//有一个u1是活跃状态
		EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		set.add(ACTIVITY_STATE.RUNNING);
		List<ActivityInstance> activityInstance_u1_from_back = entityManager.listActivityInstanceByProcessInstIdAndState(processInstanceId, set);
		Assert.assertEquals("" , activityInstance_u1_from_back.size(),1);
		Assert.assertEquals("" , activityInstance_u1_from_back.get(0).getName(),"u1");
		//这个退回生成的u1下面有3个工作项
		
		List<TaskInstance> taskInstance_u1_afterback = entityManager.listTaskInstanceByActivityInstId(activityInstance_u1_from_back.get(0).getId());
		Assert.assertEquals("" , taskInstance_u1_afterback.size(),3);

		Assert.assertEquals("" , taskInstance_u1_afterback.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u1_afterback.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u1_afterback.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		//------不接收任务，直接提交到u3节点
		ActivityContainer activityInstance_u1_from_back_container = ActivityContainer.getActivityContainer(activityInstance_u1_from_back.get(0));
		ActivityDefinition u3_definition =  processDefinition.getActivityMap().get("u3");
		activityInstance_u1_from_back_container.terminate(u3_definition);
		
		//u1节点直接提交到u3定义后，有 theStart（完成状态） u1（完成状态）u2（终止状态）u1（终止状态）u3（运行状态） 5个节点
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstanceId).size(),5);
		Assert.assertEquals("" , entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u3").size(),1);
		Assert.assertEquals("" , entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u3").get(0).getState(),ACTIVITY_STATE.RUNNING);
		
		Assert.assertEquals("" , taskInstance_u1_afterback.get(0).getState(),EntityConstants.TASK_STATE.TERMINATED);
		Assert.assertEquals("" , taskInstance_u1_afterback.get(1).getState(),EntityConstants.TASK_STATE.TERMINATED);
		Assert.assertEquals("" , taskInstance_u1_afterback.get(2).getState(),EntityConstants.TASK_STATE.TERMINATED);
	}
	
	
	@Test
	public void testTaskCancel() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		
		//状态改变
		List<TaskInstance> taskInstance_user1_after_claim = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		Assert.assertEquals("" , taskInstance_user1_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user1.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		userTaskContainer.cancel();
		//任务取消，但活动依然是活动状态
		List<TaskInstance> taskInstance_user1_after_cancel = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		Assert.assertEquals("" , taskInstance_user1_after_cancel.size(),1);
		Assert.assertEquals("" , taskInstance_user1_after_cancel.get(0).getState(),EntityConstants.TASK_STATE.CANCELED);
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		//终止活动
		ActivityContainer u1_container = ActivityContainer.getActivityContainer(u1_instance);
		u1_container.terminate();
		
		//开始节点1终止后，有 theStart（完成状态） u1（终止状态）u2（运行状态）
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.TERMINATED);
		ActivityInstance u2_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u2").iterator().next();
		List<TaskInstance> taskInstance_u2 = entityManager.listTaskInstanceByActivityInstId(u2_instance.getId());
		
		//groupB  共有3个任务
		Assert.assertEquals("" , taskInstance_u2.size(),3);
		Assert.assertEquals("" , taskInstance_u2.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u2.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u2.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
	}
	

	@Test
	public void testTaskCancelWithoutClaim() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		//不接受任务
		//userTaskContainer.claim();
		
		
		userTaskContainer.cancel();
		//任务取消，但活动依然是活动状态,第一个任务是取消状态，其他两个是未开始状态
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , taskInstance_user1.get(0).getState(),EntityConstants.TASK_STATE.CANCELED);
		Assert.assertEquals("" , taskInstance_u1.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u1.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstanceId).size(),2);
		
		//终止活动
		ActivityContainer u1_container = ActivityContainer.getActivityContainer(u1_instance);
		u1_container.terminate();
		
		//开始节点1终止后，有 theStart（完成状态） u1（终止状态）u2（运行状态）
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.TERMINATED);
		ActivityInstance u2_instance = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u2").iterator().next();
		List<TaskInstance> taskInstance_u2 = entityManager.listTaskInstanceByActivityInstId(u2_instance.getId());
		
		//groupB  共有3个任务
		Assert.assertEquals("" , taskInstance_u2.size(),3);
		Assert.assertEquals("" , taskInstance_u2.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u2.get(1).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_u2.get(2).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		Assert.assertEquals("" , u2_instance.getState(),ACTIVITY_STATE.RUNNING);
		
	}
	
	@Test
	public void testTaskPutback() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		
		//状态改变
		List<TaskInstance> taskInstance_user1_after_claim = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_claim = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_claim = entityManager.listTaskInstanceByUserIdAndState("user3", null);
		Assert.assertEquals("" , taskInstance_user1_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user2_after_claim.size(),0);
		Assert.assertEquals("" , taskInstance_user3_after_claim.size(),0);
		Assert.assertEquals("" , taskInstance_user1.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		userTaskContainer.putBack();
		//任务放回，user1有两个工作项(1个终止、1个未运行，有 theStart（完成状态） u1（运行状态）
		List<TaskInstance> taskInstance_user1_after_putback = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_putback = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_putback = entityManager.listTaskInstanceByUserIdAndState("user3", null);
		
		Assert.assertEquals("" , taskInstance_user1_after_putback.size(),2);
		Assert.assertEquals("" , taskInstance_user2_after_putback.size(),1);
		Assert.assertEquals("" , taskInstance_user3_after_putback.size(),1);
		Assert.assertEquals("" , taskInstance_user2_after_putback.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		Assert.assertEquals("" , taskInstance_user2_after_putback.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		ActivityInstance u1_instance_after_putback = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u1").iterator().next();
		Assert.assertEquals("" , u1_instance_after_putback.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.listActivityInstanceByActivityDefId(processInstanceId, "u1").size(),1);
	}
	
	@Test
	public void testSetAssignee() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		
		//状态改变
		List<TaskInstance> taskInstance_user1_after_claim = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_claim = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_claim = entityManager.listTaskInstanceByUserIdAndState("user3", null);
		Assert.assertEquals("" , taskInstance_user1_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user2_after_claim.size(),0);
		Assert.assertEquals("" , taskInstance_user3_after_claim.size(),0);
		Assert.assertEquals("" , taskInstance_user1.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		userTaskContainer.setAssignee("user2");
		//任务重定向到user2，
		List<TaskInstance> taskInstance_user1_after_setAssignee = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_setAssignee = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_setAssignee= entityManager.listTaskInstanceByUserIdAndState("user3", null);
		
		Assert.assertEquals("" , taskInstance_user1_after_setAssignee.size(),0);
		Assert.assertEquals("" , taskInstance_user2_after_setAssignee.size(),1);
		Assert.assertEquals("" , taskInstance_user3_after_setAssignee.size(),0);
		Assert.assertEquals("" , taskInstance_user2_after_setAssignee.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		
	}
	
	@Test
	public void testRemove() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		userTaskContainer.remove();

		List<TaskInstance> taskInstance_user1_after_remove = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_remove = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_remove= entityManager.listTaskInstanceByUserIdAndState("user3", null);
		
		Assert.assertEquals("" , taskInstance_user1_after_remove.size(),0);
		Assert.assertEquals("" , taskInstance_user2_after_remove.size(),1);
		Assert.assertEquals("" , taskInstance_user3_after_remove.size(),1);
		
	}
	
	@Test
	public void testAddUserTask() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   有 theStart（完成状态） u1（运行状态） 
		processInstanceContainer.start();
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition u1_definition =  processDefinition.getActivityMap().get("u1");
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
		
		ActivityOfUserTaskContainer u2_instance_activityContainer = (ActivityOfUserTaskContainer)ActivityContainer.getActivityContainer(u1_instance);
		u2_instance_activityContainer.addUserTask(new User("user4"), TASK_STATE.NOT_STARTED);
		
		//增加后
		List<TaskInstance> taskInstance_after_add = entityManager.listTaskInstanceByActivityInstId(u1_instance.getId());
		Assert.assertEquals("" , taskInstance_after_add.size(),4);
		
		List<TaskInstance> taskInstance_user1_after_add = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_add = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_add= entityManager.listTaskInstanceByUserIdAndState("user3", null);
		List<TaskInstance> taskInstance_user4_after_add= entityManager.listTaskInstanceByUserIdAndState("user4", null);
		
		Assert.assertEquals("" , taskInstance_user1_after_add.size(),1);
		Assert.assertEquals("" , taskInstance_user2_after_add.size(),1);
		Assert.assertEquals("" , taskInstance_user3_after_add.size(),1);
		Assert.assertEquals("" , taskInstance_user4_after_add.size(),1);
		Assert.assertEquals("" , taskInstance_user4_after_add.get(0).getState(),EntityConstants.TASK_STATE.NOT_STARTED);
		
		//user4接受任务
		UserTaskContainer userTaskContainer = new UserTaskContainer(taskInstance_user4_after_add.get(0));
		userTaskContainer.claim();
		
		//状态改变
		List<TaskInstance> taskInstance_user4_after_claim = entityManager.listTaskInstanceByUserIdAndState("user4", null);
		Assert.assertEquals("" , taskInstance_user4_after_claim.size(),1);
		Assert.assertEquals("" , taskInstance_user4_after_claim.get(0).getState(),EntityConstants.TASK_STATE.RUNNING);
		
		//活动中其他3个task被删除，只剩下一个
		List<TaskInstance> taskInstance_user1_after_claim = entityManager.listTaskInstanceByUserIdAndState("user1", null);
		List<TaskInstance> taskInstance_user2_after_claim = entityManager.listTaskInstanceByUserIdAndState("user2", null);
		List<TaskInstance> taskInstance_user3_after_claim= entityManager.listTaskInstanceByUserIdAndState("user3", null);
		Assert.assertEquals("" , taskInstance_user1_after_claim.size(),0);
		Assert.assertEquals("" , taskInstance_user2_after_claim.size(),0);
		Assert.assertEquals("" , taskInstance_user3_after_claim.size(),0);
		
		List<TaskInstance> taskInstance_after_claim = entityManager.listTaskInstanceByActivityInstId(u1_instance.getId());
		Assert.assertEquals("" , taskInstance_after_claim.size(),1);
		
		
	}

}
