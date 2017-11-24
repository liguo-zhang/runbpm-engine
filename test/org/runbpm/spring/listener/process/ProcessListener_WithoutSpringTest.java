package org.runbpm.spring.listener.process;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class ProcessListener_WithoutSpringTest extends RunBPMTestCase{

	@Test
	public void test() throws Exception{
		
		
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		ClassPathResource classPathResource = new ClassPathResource("ProcessListenerTest.xml",this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("ProcessListenerTest.xml");
		
		processInstanceContainer.start();
		
		Assert.assertEquals(
				ListenerManager.Event_Type.afterProcessInstanceStarted
						.toString(),
				entityManager
						.loadVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.afterProcessInstanceStarted
										.toString()).getValue());
		
	}
}
