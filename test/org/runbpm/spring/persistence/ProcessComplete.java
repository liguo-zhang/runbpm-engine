package org.runbpm.spring.persistence;

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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

public class ProcessComplete extends RunBPMTestCase{

	
	public static void test() throws Exception{
		
		RuntimeService runtimeService =  Configuration.getContext().getRuntimeService();

		String fileName = "ProcessCompleteTest.xml";
		ClassPathResource classPathResource = new ClassPathResource(fileName,ProcessComplete.class);
		ClassPathResource classPathResource1 = new ClassPathResource("ProcessCompleteTest_sub.xml",ProcessComplete.class);
		ClassPathResource classPathResource2 = new ClassPathResource("ProcessCompleteTest_sub_sub.xml",ProcessComplete.class);
		runtimeService.initProcessDefinitionFromFile(classPathResource.getFile());
		runtimeService.initProcessDefinitionFromFile(classPathResource1.getFile());
		runtimeService.initProcessDefinitionFromFile(classPathResource2.getFile());
		
		ProcessInstance processInstance = runtimeService.createProcessInstance("ProcessComplete",null);
		
		Long processInstanceId = processInstance.getId();
		ProcessInstance newProcessInstance = runtimeService.getProcessInstance(processInstanceId);
		runtimeService.startProcessInstance(processInstanceId);
		
		newProcessInstance = runtimeService.getProcessInstance(processInstanceId);
		ActivityInstance activityInstance_Instance_2_0 = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "callCheckCreditProcess").iterator().next();
		long subProcessInstanceId = activityInstance_Instance_2_0.getCallActivityProcessInstanceId();
		
		ProcessInstance subProcessInstance = runtimeService.getProcessInstance(subProcessInstanceId);
		
		//提交子流程
		ActivityInstance u1_instance = runtimeService.getActivityInstanceByActivityDefId(subProcessInstanceId, "u1").iterator().next();
		runtimeService.completeActivityInstance(u1_instance.getId());
		
		//提交父流程
		ActivityInstance prepareAndShipTask_instance = runtimeService.getActivityInstanceByActivityDefId(processInstanceId, "prepareAndShipTask").iterator().next();
		runtimeService.completeActivityInstance(prepareAndShipTask_instance.getId());
		
		ProcessInstance newProcessInstance1 = runtimeService.getProcessInstance(processInstanceId);
		Assert.assertNull(newProcessInstance1);
		
	}
	
}
