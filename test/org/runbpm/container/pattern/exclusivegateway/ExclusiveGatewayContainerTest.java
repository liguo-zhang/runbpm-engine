package org.runbpm.container.pattern.exclusivegateway;


import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;
public class ExclusiveGatewayContainerTest extends RunBPMTestCase{
	
	
	@Test
	public void test1() throws Exception {
		
		String fileName = this.getBPMNXMLName();
		test1_(fileName,fileName);
		
		String fileName2 = "ExclusiveGatewayContainerTest_Aviator.xml";
		test1_(fileName2,fileName2);
	}
	
	private void test1_(String fileName,String processDefId)  throws Exception {
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel(processDefId);
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(processDefId);
		
		// �ڴ���ʽ����1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = entityManager.loadProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		entityManager.setProcessVariable(processInstanceId, "input", 1);
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬��exclusiveGw�����״̬�� theTask1������״̬�� 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstance.getId()).size(),3);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theTask1").iterator().next();
		
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		
		// �ڵ�3 �ύ�� ���̽������� theStart�����״̬��exclusiveGw�����״̬�� theTask1�����״̬�� exclusiveGw_end�����״̬�� theEnd�����״̬��
		
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance_Instance_3_0);
		activityContainer.complete();
		  
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstance.getId()).size(),5);
		ActivityInstance activityInstance_Instance_1_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw").iterator().next();
		ActivityInstance activityInstance_Instance_3_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theTask1").iterator().next();
		ActivityInstance activityInstance_Instance_4_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw_end").iterator().next();
		ActivityInstance activityInstance_Instance_5_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
		Assert.assertEquals("" , activityInstance_Instance_1_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_1.getState(),ACTIVITY_STATE.COMPLETED);
		
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
	}
	
	//����һ����֧
	
	@Test
	public void test2() throws Exception {
		String fileName = this.getBPMNXMLName();
		test2_(fileName,fileName);
		
		String fileName2 = "ExclusiveGatewayContainerTest_Aviator.xml";
		test2_(fileName2,fileName2);
	}

	private void test2_(String fileName,String processDefId) throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		


		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		
		
		ProcessModel processModel = entityManager.loadLatestProcessModel(processDefId);
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(processDefId);
		
		// �ڴ���ʽ����1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = entityManager.loadProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		entityManager.setProcessVariable(processInstanceId, "input", 2);
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬��exclusiveGw�����״̬�� theTask2������״̬�� 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstance.getId()).size(),3);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theTask2").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		
		// �ڵ�3 �ύ�� ���̽������� theStart�����״̬��exclusiveGw�����״̬�� theTask1�����״̬�� exclusiveGw_end�����״̬�� theEnd�����״̬��
		
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance_Instance_3_0);
		activityContainer.complete();
		  
		Assert.assertEquals("" , entityManager.listActivityInstanceByProcessInstId(processInstance.getId()).size(),5);
		ActivityInstance activityInstance_Instance_1_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw").iterator().next();
		ActivityInstance activityInstance_Instance_3_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theTask2").iterator().next();
		ActivityInstance activityInstance_Instance_4_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "exclusiveGw_end").iterator().next();
		ActivityInstance activityInstance_Instance_5_1 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
		Assert.assertEquals("" , activityInstance_Instance_1_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_1.getState(),ACTIVITY_STATE.COMPLETED);
		
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
		
	}
	
	//�����쳣
	@Test
	public void testException() throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		
		
		ProcessModel processModel = entityManager.loadLatestProcessModel(fileName);
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		// �ڴ���ʽ����1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = entityManager.loadProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		entityManager.setProcessVariable(processInstanceId, "input", 555);
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬��exclusiveGw�����״̬�� theTask2������״̬�� 
		try{
			processInstanceContainer.start();
		}catch (RunBPMException e){
			System.out.println("----Ԥ���쳣���޷�������һ���,Code_020001_Empty_Activity----��ʼ");
			Assert.assertEquals(e.getException_message(),RunBPMException.EXCEPTION_MESSAGE.Code_020001_Empty_Activity);
		}
		
	}
	
	
}
