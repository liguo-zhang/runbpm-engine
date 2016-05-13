package org.runbpm.spring.persistence.redis;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.EntityManager;
import org.runbpm.persistence.hibernate.HibernateEntityManagerImpl;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.runbpm.service.RuntimeService;
import org.runbpm.spring.persistence.ProcessComplete;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

public class ProcessComplete_Redis extends RunBPMTestCase{

	@Before
	public void before(){
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
		
		Configuration.setContext(springAppContext);
		
	}
	
	@Test
	public void ParallelGateway_Hibernate() throws Exception{
		ProcessComplete.test();
		
	}
	
}
