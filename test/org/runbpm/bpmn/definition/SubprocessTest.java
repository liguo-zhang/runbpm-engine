package org.runbpm.bpmn.definition;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessModel;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class SubprocessTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
		
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("SubprocessTest.xml");
		
		SubProcessDefinition subprocess = (SubProcessDefinition) processModel.getProcessDefinition().getActivity("subProcess");
		
		ActivityDefinition start = subprocess.getActivity("subProcessStart");
		Assert.assertEquals("",start.getId(), "subProcessStart");
	}
}
