package org.runbpm.spring.execlusivegateway;

import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
public class ExclusiveGatewaySpringBeanTest extends RunBPMTestCase{
	
	
	@Test
	public void test() throws Exception {
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context_addConditionHandler.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
		Configuration.setContext(springAppContext);
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel(fileName);
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		// 内存形式总是1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = entityManager.getProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		entityManager.setProcessVariable(processInstanceId, "input", 1);
		
		// 开始节点1结束后，   有 theStart（完成状态）exclusiveGw（完成状态） theTask1（运行状态） 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),3);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theTask1").iterator().next();
		
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		
		// 节点3 提交后 流程结束，有 theStart（完成状态）exclusiveGw（完成状态） theTask1（完成状态） exclusiveGw_end（完成状态） theEnd（完成状态）
		
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance_Instance_3_0);
		activityContainer.complete();
		  
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),5);
		ActivityInstance activityInstance_Instance_1_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw").iterator().next();
		ActivityInstance activityInstance_Instance_3_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theTask1").iterator().next();
		ActivityInstance activityInstance_Instance_4_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw_end").iterator().next();
		ActivityInstance activityInstance_Instance_5_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
		Assert.assertEquals("" , activityInstance_Instance_1_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_1.getState(),ACTIVITY_STATE.COMPLETED);
		
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
		
	}
	
	
	
}
