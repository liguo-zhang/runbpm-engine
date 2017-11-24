package org.runbpm.service;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.service.RuntimeService;
import org.runbpm.spring.persistence.PersistenceTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class ServiceSampleTest extends RunBPMTestCase{
	

	@Test
	public void SampleTest() throws Exception{
		ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(springContext);
		
		Configuration.setContext(springAppContext);
		
		RuntimeService runtimeService = Configuration.getContext().getRuntimeService();

		String fileName = "ServiceSampleTest.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,ServiceSampleTest.class);
		runtimeService.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessInstance processInstance = runtimeService.createProcessInstance(fileName,null);
		
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.NOT_STARTED);
		// �ڴ���ʽ����1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = runtimeService.loadProcessInstance(processInstanceId);
		Assert.assertNotNull(newProcessInstance.getCreateDate());
		Assert.assertNotNull(newProcessInstance.getModifyDate());
		//Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		// ��ʼ�ڵ�1������   �� theStart�����״̬�� fork�����״̬�� receivePayment(����״̬) shipOrder������״̬�� 
		ProcessInstance processInstancefromRunTimeService = runtimeService.startProcessInstance(processInstanceId);
		Assert.assertEquals("" , processInstancefromRunTimeService.getState(),PROCESS_STATE.RUNNING);
		// �ɵ�ʵ�����а�������NOT_STARTED
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		newProcessInstance = runtimeService.loadProcessInstance(processInstanceId);

	}
		
	
	
}
