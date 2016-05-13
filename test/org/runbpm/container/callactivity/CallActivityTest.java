package org.runbpm.container.callactivity;

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
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class CallActivityTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource1 = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource1.getFile());
		
		ClassPathResource classPathResource2 = new ClassPathResource("CallActivityTest_sub.xml",this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource2.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   
		//theStart（完成状态） manualTask（receiveOrder 完成状态）
		//subProcess(subProcess 运行状态）
		//		startEvent(完成状态) userTask(subshipOrder 运行状态）
		//
		processInstanceContainer.start();
		
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.RUNNING);
		
		ActivityInstance receiveOrder_instance = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "receiveOrder").iterator().next();
		Assert.assertEquals("" , receiveOrder_instance.getState(),ACTIVITY_STATE.COMPLETED);
		
		ProcessInstance subProcessInstance = entityManager.getProcessInstance(new Long(2));
		Assert.assertEquals("" , subProcessInstance.getState(),PROCESS_STATE.RUNNING);
		
		ActivityInstance u1_instance = entityManager.getActivityInstanceByActivityDefId(subProcessInstance.getId(), "u1").iterator().next();
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		ActivityContainer u1Container = ActivityContainer.getActivityContainer(u1_instance);
		u1Container.complete();
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.COMPLETED);
		
		Assert.assertEquals("" , subProcessInstance.getState(),PROCESS_STATE.COMPLETED);
		
		
		ActivityInstance prepareAndShipTask_instance = entityManager.getActivityInstanceByActivityDefId(processInstanceId, "prepareAndShipTask").iterator().next();
		Assert.assertEquals("" , prepareAndShipTask_instance.getState(),ACTIVITY_STATE.RUNNING);
		ActivityContainer.getActivityContainer(prepareAndShipTask_instance).complete();;
		
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.COMPLETED);
		
		
	}
}
