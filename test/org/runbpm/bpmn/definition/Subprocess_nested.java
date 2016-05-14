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

public class Subprocess_nested extends RunBPMTestCase{


	@Test
	public void test() throws Exception{
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("Subprocess_nested.xml");
		
		ActivityDefinition subProcess1 = processModel.getProcessDefinition().getSubProcessActivityDefinition("subProcess,subProcess1");
		
		Assert.assertEquals("",subProcess1.getId(), "subProcess1");
		
		SubProcessDefinition s= (SubProcessDefinition)subProcess1;
		Assert.assertEquals("",s.getActivity("subshipOrder1").getName(),"Ship Order");
		
		Assert.assertEquals("",s.getSequenceBlockId(),"subProcess,subProcess1");
		//
		SubProcessDefinition s2 = processModel.getProcessDefinition().getSubProcessActivityDefinition("subProcess,subProcess2");
		
		Assert.assertEquals("",s2.getSequenceBlockId(),"subProcess,subProcess2");
		
		SubProcessDefinition s3 = processModel.getProcessDefinition().getSubProcessActivityDefinition("subProcess,subProcess1,subProcess11");
		
		Assert.assertEquals("",s3.getSequenceBlockId(),"subProcess,subProcess1,subProcess11");
	}
	
	
	
}
