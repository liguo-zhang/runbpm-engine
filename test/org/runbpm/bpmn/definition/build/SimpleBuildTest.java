package org.runbpm.bpmn.definition.build;


import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.EndEvent;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.bpmn.definition.StartEvent;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessModel;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;

public class SimpleBuildTest {

	private MemoryEntityManagerImpl entityManager;
	
	@Test
	public void test(){
		entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		ProcessDefinition processDefinition = new ProcessDefinition("simple_process");
		StartEvent startEvent = new StartEvent("s");
		EndEvent endEvent = new EndEvent("e");
		SequenceFlow sequenceFlow = new SequenceFlow(startEvent,endEvent);
		
		processDefinition.addSequenceFlow(sequenceFlow);
		processDefinition.addFlowNode(startEvent);
		processDefinition.addFlowNode(endEvent);
		
		entityManager.deployProcessDefinition(processDefinition);
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("simple_process");
		
		ProcessDefinition getProcessDefinition = processModel.getProcessDefinition();
		
		Assert.assertNotNull(getProcessDefinition);
		
		SequenceFlow getSequenceFlow = getProcessDefinition.getSequenceFlowList().get(0);
		Assert.assertEquals(getSequenceFlow.getProcessDefinition().getId(),"simple_process");
		
		ActivityDefinition activityDefinition = getProcessDefinition.getActivity("s");
		ProcessDefinition  getProcessDefinitionFromA = activityDefinition.getProcessDefinition();
		Assert.assertEquals(getProcessDefinitionFromA.getId(),"simple_process");
		Assert.assertTrue(processModel.getXmlcontent().trim().length()>20);
		
	}
	
	@Test
	public void testImportFromString(){
		entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		ProcessDefinition processDefinition = new ProcessDefinition("simple_process");
		StartEvent startEvent = new StartEvent("s");
		EndEvent endEvent = new EndEvent("e");
		SequenceFlow sequenceFlow = new SequenceFlow(startEvent,endEvent);
		
		processDefinition.addSequenceFlow(sequenceFlow);
		processDefinition.addFlowNode(startEvent);
		processDefinition.addFlowNode(endEvent);
		
		
		entityManager.deployProcessDefinition(processDefinition);
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("simple_process");
		
		String xmlContent = processModel.getXmlcontent();
		entityManager.deployProcessDefinitionFromString(xmlContent);
		
		List<ProcessModel> pmList = entityManager.loadProcessModels(true);
		Assert.assertEquals(pmList.size(),2);
		
		
	}
	
}
