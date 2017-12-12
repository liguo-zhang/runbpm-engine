package org.runbpm.bpmn.definition;

import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessModel;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;
public class ParallelGatewayTest extends RunBPMTestCase{
	@Test
	public void test() throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("ParallelGatewayTest.xml");
		
		Assert.assertNotNull("",processModel.getProcessDefinition());
		
		Assert.assertNotNull("",processModel.getXmlContent());
		
		Assert.assertNotNull(processModel.getProcessDefinition().getId());
		
		UserTask userTask = (UserTask) processModel.getProcessDefinition().getActivity("receivePayment");
		Assert.assertEquals("",userTask.getUserTaskResource().getUserTaskResourceAssignment().getUserTaskResourceExpressionList().size(),1);
		
		Assert.assertEquals("", processModel.getProcessDefinition().getStartEvent().getId(), "theStart");
		
		Assert.assertEquals("", processModel.getProcessDefinition().getStartEvent().getId(), "theStart");
		
		ActivityDefinition activityDefinition = processModel.getProcessDefinition().getActivity("fork");
		
		Assert.assertEquals("", activityDefinition.getOutgoingSequenceFlowList().size(), 2);
		
	}
}
