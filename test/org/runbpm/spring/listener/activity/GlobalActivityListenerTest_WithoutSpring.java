package org.runbpm.spring.listener.activity;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class GlobalActivityListenerTest_WithoutSpring extends RunBPMTestCase{


	@Test
	public void test() throws Exception{
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		ActListenerSample.clearAtomic();
		Configuration.getContext().getGlobalListener().clearGlobalListenerSet();
		
		Configuration.getContext().getGlobalListener().addListener(ListenerManager.Event_Type.beforeActivityInstanceStarted, new ActListenerSample());
		Configuration.getContext().getGlobalListener().addListener(ListenerManager.Event_Type.afterActivityInstanceStarted, new ActListenerSample());
		Configuration.getContext().getGlobalListener().addListener(ListenerManager.Event_Type.beforeActivityInstanceCompleted, new ActListenerSample());
		Configuration.getContext().getGlobalListener().addListener(ListenerManager.Event_Type.afterActivityInstanceCompleted, new ActListenerSample());
		
		ClassPathResource classPathResource = new ClassPathResource("GlobalActivityListenerTest.xml",this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("GlobalActivityListenerTest.xml");
		
		Long processInstanceId = processInstance.getId();
		
		processInstanceContainer.start();
		
		//启动了3个实例
		Assert.assertEquals(
				new Integer(3),
				entityManager
				.getVariableInstance(
						processInstance.getId(),
						ListenerManager.Event_Type.beforeActivityInstanceStarted
								.toString()).getValue());
		
		Assert.assertEquals(
				new Integer(3),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.afterActivityInstanceStarted
										.toString()).getValue());
		
		//完成了2个实例
		Assert.assertEquals(
				new Integer(2),
				entityManager
				.getVariableInstance(
						processInstance.getId(),
						ListenerManager.Event_Type.beforeActivityInstanceCompleted
								.toString()).getValue());
		
		Assert.assertEquals(
				new Integer(2),
				entityManager
						.getVariableInstance(
								processInstance.getId(),
								ListenerManager.Event_Type.afterActivityInstanceCompleted
										.toString()).getValue());
		
		
	}
}
