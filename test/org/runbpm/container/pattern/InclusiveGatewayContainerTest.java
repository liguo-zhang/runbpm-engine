package org.runbpm.container.pattern;


import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;
public class InclusiveGatewayContainerTest extends RunBPMTestCase{
	
	
	@Test
	public void test() throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("InclusiveGatewayContainerTest.xml");
		
		// 内存形式总是1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = entityManager.getProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		entityManager.setProcessVariable(processInstanceId, "shipOrder", new Boolean(false));
		entityManager.setProcessVariable(processInstanceId, "paymentReceived", new Boolean(false));
		
		// 开始节点1结束后，   有 theStart（完成状态）exclusiveGw（完成状态） shipOrder（运行状态） receivePayment（运行状态） 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstance.getId()).size(),4);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_4_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_4_0.getState(),ACTIVITY_STATE.RUNNING);
		
		
	}
	
	@Test
	public void testOne() throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());

		
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
		
		entityManager.setProcessVariable(processInstanceId, "shipOrder", new Boolean(true));
		entityManager.setProcessVariable(processInstanceId, "paymentReceived", new Boolean(false));
		
		// 开始节点1结束后，   有 theStart（完成状态）exclusiveGw（完成状态） receivePayment（运行状态） 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstance.getId()).size(),3);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_4_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_0.getState(),ACTIVITY_STATE.RUNNING);
	}
}
