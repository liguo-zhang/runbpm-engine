package org.runbpm.container.subprocess;

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
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class SubprocessContainerTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();

		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance(fileName);
		
		Long processInstanceId = processInstance.getId();
		
		// ��ʼ�ڵ�1������   
		//theStart�����״̬�� manualTask��receiveOrder ���״̬��
		//subProcess(subProcess ����״̬��
		//		startEvent(���״̬) userTask(subshipOrder ����״̬��
		//
		processInstanceContainer.start();
		
		List<ActivityInstance> userTaskList = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "subshipOrder");
		Assert.assertEquals("",userTaskList.size(),1);
		
		for(ActivityInstance a : userTaskList){
			ActivityContainer activityContainer = ContainerTool.getActivityContainer(a);
			activityContainer.complete();
		}
		
		List<ActivityInstance> userTaskList2 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "subshipOrder11");
		Assert.assertEquals("",userTaskList2.size(),1);
		
		ActivityContainer activityContainer = ContainerTool.getActivityContainer(userTaskList2.get(0));
		activityContainer.complete();
		
		List<ActivityInstance> userTaskList3 = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "prepareAndShipTask");
		Assert.assertEquals("",userTaskList3.size(),1);
		
		
	}
}
