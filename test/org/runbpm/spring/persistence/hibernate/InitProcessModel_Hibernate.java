package org.runbpm.spring.persistence.hibernate;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ProcessModel;
import org.runbpm.service.RuntimeService;
import org.runbpm.spring.persistence.PersistenceTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class InitProcessModel_Hibernate extends RunBPMTestCase{
	
	@Before
	public void before(){
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
		
		Configuration.setContext(springAppContext);
	}
	
	@Test
	public void ParallelGateway_Hibernate() throws Exception{
		
		RuntimeService runtimeService = Configuration.getContext().getRuntimeService();

		String fileName = "PersistenceTest.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,PersistenceTest.class);
		runtimeService.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		runtimeService.loadProcessModels(true);
		
		ProcessModel processModel = runtimeService.loadLatestProcessModel("PersistenceTest.xml");
		Assert.assertNotNull(processModel.getId());
	}
	
	
}
