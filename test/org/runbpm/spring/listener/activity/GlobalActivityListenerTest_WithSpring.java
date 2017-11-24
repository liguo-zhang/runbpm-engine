package org.runbpm.spring.listener.activity;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.runbpm.spring.listener.process.ProcessInstanceListenerSample;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class GlobalActivityListenerTest_WithSpring extends RunBPMTestCase{


	@Test
	public void test() throws Exception{
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				"RunBPM.spring_context.xml",this.getClass());
		RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
		Configuration.setContext(springAppContext);
		
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		ActListenerSample.clearAtomic();
		
		ClassPathResource classPathResource = new ClassPathResource("GlobalActivityListenerTest.xml",this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("GlobalActivityListenerTest.xml");
		
		Long processInstanceId = processInstance.getId();
		
		
		processInstanceContainer.start();
		
		//启动了3个实例
				Assert.assertEquals(
						new Integer(3),
						entityManager
						.loadVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.beforeActivityInstanceStarted
										.toString()).getValue());
				
				Assert.assertEquals(
						new Integer(3),
						entityManager
								.loadVariableInstance(
										processInstance.getId(),
										ListenerManager.Event_Type.afterActivityInstanceStarted
												.toString()).getValue());
				
				//完成了2个实例
				Assert.assertEquals(
						new Integer(2),
						entityManager
						.loadVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.beforeActivityInstanceCompleted
										.toString()).getValue());
				
				Assert.assertEquals(
						new Integer(2),
						entityManager
								.loadVariableInstance(
										processInstance.getId(),
										ListenerManager.Event_Type.afterActivityInstanceCompleted
												.toString()).getValue());
		
		
	}
}
