package org.runbpm.container.pattern;

import java.io.IOException;

import junit.framework.Assert;

import org.hibernate.Session;
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
import org.runbpm.persistence.EntityManager;
import org.runbpm.persistence.hibernate.HibernateEntityManagerImpl;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

public class ParallelGatewayContainerTest extends RunBPMTestCase{
	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();

		String fileName = this.getBPMNXMLName();
		
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		// �ڴ���ʽ����1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = entityManager.getProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬�� fork�����״̬�� receivePayment(����״̬) shipOrder������״̬�� 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),4);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_4_0.getState(),ACTIVITY_STATE.RUNNING);
		
		// �ڵ�3 �ύ�� �½�join����� theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder������״̬�� join��δ��ʼ״̬��
		
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance_Instance_3_0);
		activityContainer.complete();
		  
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),5);
		ActivityInstance activityInstance_Instance_1_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		Assert.assertEquals("" , activityInstance_Instance_1_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_1.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_5_1.getState(),ACTIVITY_STATE.NOT_STARTED);
		
		// �ڵ�4 �ύ�� �� theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder�����״̬�� join�����״̬��archiveOrder������״̬��
		
		activityContainer = ActivityContainer.
				getActivityContainer(activityInstance_Instance_4_1);
		activityContainer.complete();
		
		
		ActivityInstance activityInstance_Instance_1_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		ActivityInstance activityInstance_Instance_6_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "archiveOrder").iterator().next();
		
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),6);
		Assert.assertEquals("" , activityInstance_Instance_1_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_6_2.getState(),ACTIVITY_STATE.RUNNING);
	
		// �ڵ�6 �ύ�� ���½��ڵ�endState��endState�ǽ����ڵ㣬���Զ���ɡ�  
		//theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder�����״̬�� join�����״̬��archiveOrder�����״̬�� theEnd�����״̬��
		// �����ǽ���״̬
		try{
		activityContainer = ActivityContainer.
				getActivityContainer(activityInstance_Instance_6_2);
		activityContainer.complete();
		}catch(RunBPMException e){
			e.printStackTrace();
		}
		
		ActivityInstance activityInstance_Instance_1_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		ActivityInstance activityInstance_Instance_6_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "archiveOrder").iterator().next();
		ActivityInstance activityInstance_Instance_7_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theEnd").iterator().next();
		  
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),7);
		Assert.assertEquals("" , activityInstance_Instance_1_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_6_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_7_3.getState(),ACTIVITY_STATE.COMPLETED);
		
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
		
	}
	
	@Test
	public void testBack() throws IOException{
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
		ProcessInstance newProcessInstance = entityManager.getProcessInstance(processInstanceId);
		Assert.assertEquals("" , newProcessInstance.getId(),1);
		Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬�� fork�����״̬�� receivePayment(����״̬) shipOrder������״̬�� 
		processInstanceContainer.start();
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.RUNNING);
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),4);
		ActivityInstance activityInstance_Instance_1_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_0 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_1_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_0.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_0.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_4_0.getState(),ACTIVITY_STATE.RUNNING);
		
		// �ڵ�3 �ύ�� �½�join����� theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder������״̬�� join��δ��ʼ״̬��
		
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance_Instance_3_0);
		activityContainer.complete();
		  
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),5);
		ActivityInstance activityInstance_Instance_1_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_1 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		Assert.assertEquals("" , activityInstance_Instance_1_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_1.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_1.getState(),ACTIVITY_STATE.RUNNING);
		Assert.assertEquals("" , activityInstance_Instance_5_1.getState(),ACTIVITY_STATE.NOT_STARTED);
		
		// �ڵ�4 �ύ�� �� theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder�����״̬�� join�����״̬��archiveOrder������״̬��
		
		activityContainer = ActivityContainer.
				getActivityContainer(activityInstance_Instance_4_1);
		activityContainer.complete();
		
		
		ActivityInstance activityInstance_Instance_1_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		ActivityInstance activityInstance_Instance_4_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		ActivityInstance activityInstance_Instance_6_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "archiveOrder").iterator().next();
		
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),6);
		Assert.assertEquals("" , activityInstance_Instance_1_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_4_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_5_2.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_6_2.getState(),ACTIVITY_STATE.RUNNING);
	
		// �ڵ�6 �˻ص� 4 shipOrder �� ���½�� 4 shipOrder��  
		//theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder�����״̬�� join�����״̬��archiveOrder�����״̬�� ��shipOrder������״̬��
		try{
		activityContainer = ActivityContainer.
				getActivityContainer(activityInstance_Instance_6_2);
		activityContainer.terminate(
				processDefinition.getActivity(activityInstance_Instance_4_2.getActivityDefinitionId()));
		}catch(RunBPMException e){
			e.printStackTrace();
		}
		
		ActivityInstance activityInstance_Instance_1_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "theStart").iterator().next();
		ActivityInstance activityInstance_Instance_2_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "fork").iterator().next();
		ActivityInstance activityInstance_Instance_3_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receivePayment").iterator().next();
		//ActivityInstance activityInstance_Instance_4_3 = entityManager.loadActivityInstanceByDefinition(processInstanceId, "shipOrder").iterator().next();
		ActivityInstance activityInstance_Instance_5_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "join").iterator().next();
		ActivityInstance activityInstance_Instance_6_3 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "archiveOrder").iterator().next();
		
		  
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),7);
		Assert.assertEquals("" , activityInstance_Instance_1_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_2_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_3_3.getState(),ACTIVITY_STATE.COMPLETED);
		//Assert.assertEquals("" , activityInstance_Instance_4_3.getState(),ACTIVITY_INSTANCE_STATE.COMPLETED);
		Assert.assertEquals("",entityManager.getActivityInstanceByActivityDefId(processInstanceId, "shipOrder").size(),2);
		Assert.assertEquals("" , activityInstance_Instance_5_3.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_6_3.getState(),ACTIVITY_STATE.TERMINATED);
		
		//���������� �½�� 4 shipOrder
		ActivityInstance activityInstance_Instance_4_3_2 = entityManager.getActivityInstance(new Long(7));
		Assert.assertEquals("" , activityInstance_Instance_4_3_2.getState(),ACTIVITY_STATE.RUNNING);

		// �ڵ�4 �ύ�� 
		//theStart�����״̬�� fork�����״̬�� receivePayment(���״̬) shipOrder�����״̬�� join�����״̬��archiveOrder�����״̬��
		//��shipOrder�����״̬����join�����״̬��archiveOrder������״̬�� 
		
		activityContainer = ActivityContainer.
				getActivityContainer(activityInstance_Instance_4_3_2);
		activityContainer.complete();
		
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstance.getId()).size(),9);
		Assert.assertEquals("" ,entityManager.getActivityInstance(new Long(9)).getState(),ACTIVITY_STATE.RUNNING);
		
	}
}
