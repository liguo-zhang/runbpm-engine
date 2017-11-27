package org.runbpm.service;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.service.RunBPMService;
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
		
		RunBPMService runBPMService = Configuration.getContext().getRunBPMService();

		String fileName = "ServiceSampleTest.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,ServiceSampleTest.class);
		runBPMService.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessInstance processInstance = runBPMService.createProcessInstance(fileName,null);
		
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.NOT_STARTED);
		// 内存形式总是1
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = runBPMService.loadProcessInstance(processInstanceId);
		Assert.assertNotNull(newProcessInstance.getCreateDate());
		Assert.assertNotNull(newProcessInstance.getModifyDate());
		//Assert.assertEquals("" , newProcessInstance.getProcessModelId(),1);
		Assert.assertEquals("" , newProcessInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		// 开始节点1结束后，   有 theStart（完成状态） fork（完成状态） receivePayment(运行状态) shipOrder（运行状态） 
		ProcessInstance processInstancefromRunBPMService = runBPMService.startProcessInstance(processInstanceId);
		Assert.assertEquals("" , processInstancefromRunBPMService.getState(),PROCESS_STATE.RUNNING);
		// 旧的实例步行啊，还是NOT_STARTED
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.NOT_STARTED);
		
		newProcessInstance = runBPMService.loadProcessInstance(processInstanceId);

	}
		
	
	
}
