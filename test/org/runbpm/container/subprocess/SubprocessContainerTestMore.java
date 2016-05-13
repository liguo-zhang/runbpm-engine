package org.runbpm.container.subprocess;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class SubprocessContainerTestMore extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   
		//theStart（完成状态） manualTask（receiveOrder 完成状态）
		//subProcess(subProcess 运行状态）
		//		startEvent(完成状态) userTask(subshipOrder 运行状态）
		//
		processInstanceContainer.start();
		
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstanceId).size(),5);
		
		ActivityInstance subProcess_instance = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "subProcess").iterator().next();
		Assert.assertEquals("" , subProcess_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		ActivityInstance subshipOrder_instance = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "subshipOrder").iterator().next();
		Assert.assertEquals("" , subshipOrder_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		ActivityContainer subshipOrderActivityContainer = ActivityContainer.getActivityContainer(subshipOrder_instance);
		subshipOrderActivityContainer.complete();
		
		//theStart（完成状态） manualTask（receiveOrder 完成状态）
		//subProcess(subProcess 完成状态）
		//		startEvent(完成状态) userTask(subshipOrder 完成状态）endEvent(完成状态)
		//prepareAndShipTask(运行状态)     
		
		Assert.assertEquals("" , entityManager.getActivityInstanceByProcessInstId(processInstanceId).size(),7);
		
		ActivityInstance subProcess_instance_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "subProcess").iterator().next();
		Assert.assertEquals("" , subProcess_instance_2.getState(),ACTIVITY_STATE.COMPLETED);
		
		ActivityInstance subshipOrder_instance_2 = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "subshipOrder").iterator().next();
		Assert.assertEquals("" , subshipOrder_instance_2.getState(),ACTIVITY_STATE.COMPLETED);
		
		ActivityInstance prepareAndShipTask_instance = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "prepareAndShipTask").iterator().next();
		Assert.assertEquals("" , prepareAndShipTask_instance.getState(),ACTIVITY_STATE.RUNNING);
		
	}
}
