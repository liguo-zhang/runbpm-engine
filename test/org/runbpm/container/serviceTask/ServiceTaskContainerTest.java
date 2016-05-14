package org.runbpm.container.serviceTask;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class ServiceTaskContainerTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("ServiceTaskContainerTest.xml");
		
		Long processInstanceId = processInstance.getId();
		
		entityManager.setProcessVariable(processInstanceId, "a", 9999);
		
		// 开始节点1结束后，   
		//theStart（完成状态） manualTask（receiveOrder 完成状态）
		//subProcess(subProcess 运行状态）
		//		startEvent(完成状态) userTask(subshipOrder 运行状态）
		//
		processInstanceContainer.start();
		
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.COMPLETED);
		
		
	}
}
