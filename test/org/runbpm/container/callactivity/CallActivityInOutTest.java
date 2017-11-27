package org.runbpm.container.callactivity;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.ContainerTool;
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

public class CallActivityInOutTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource1 = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource1.getFile());
		
		ClassPathResource classPathResource2 = new ClassPathResource("CallActivityTest_sub.xml",this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource2.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// 开始节点1结束后，   
		//theStart（完成状态） manualTask（receiveOrder 完成状态）
		//subProcess(subProcess 运行状态）
		//		startEvent(完成状态) userTask(subshipOrder 运行状态）
		//
		
		entityManager.setProcessVariable(processInstanceId, "x", "1");
		
		processInstanceContainer.start();
		
		
		ProcessInstance subProcessInstance = entityManager.loadProcessInstance(new Long(2));
		ActivityInstance u1_instance = entityManager.listActivityInstanceByActivityDefId(subProcessInstance.getId(), "u1").iterator().next();
		Assert.assertEquals("" , u1_instance.getState(),ACTIVITY_STATE.RUNNING);
		
		Assert.assertEquals("" , entityManager.loadVariableInstance(subProcessInstance.getId(), "y").getValue(),"1");
		
		
		entityManager.setProcessVariable(subProcessInstance.getId(), "y", "2");
		
		ActivityContainer u1Container = ContainerTool.getActivityContainer(u1_instance);
		u1Container.complete();
		
		
		Assert.assertEquals("" , entityManager.loadVariableInstance(processInstanceId, "x").getValue(),"2");
		
	}
}
