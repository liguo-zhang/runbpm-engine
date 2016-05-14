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

public class GlobalProcessListener_WithoutSpring extends RunBPMTestCase{

	@Test
	public void test() throws Exception{
		
		
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		
		Configuration.getContext().getGlobalListener()
		.addListener(ListenerManager.Event_Type.afterProcessInstanceStarted,new ProcessInstanceListenerSample());
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = "GlobalProcessListenerTest.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel(fileName);
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		processInstanceContainer.start();
		
		Assert.assertEquals(
				ListenerManager.Event_Type.afterProcessInstanceStarted
						.toString(),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.afterProcessInstanceStarted
										.toString()).getValue());
		
	}
}
