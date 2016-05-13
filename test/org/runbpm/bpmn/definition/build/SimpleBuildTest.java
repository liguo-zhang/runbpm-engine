package org.runbpm.bpmn.definition.build;


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
	
	@Before
	public void before(){
		entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
	}	
	
	@Test
	public void test(){
		ProcessDefinition processDefinition = new ProcessDefinition("simple_process");
		StartEvent startEvent = new StartEvent("s");
		EndEvent endEvent = new EndEvent("e");
		SequenceFlow sequenceFlow = new SequenceFlow(startEvent,endEvent);
		
		processDefinition.addSequenceFlow(sequenceFlow);
		processDefinition.addFlowNode(startEvent);
		processDefinition.addFlowNode(endEvent);
		
		
		entityManager.initProcessDefinition(processDefinition);
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("simple_process");
		
		ProcessDefinition getProcessDefinition = processModel.getProcessDefinition();
		
		Assert.assertNotNull(getProcessDefinition);
		
		SequenceFlow getSequenceFlow = getProcessDefinition.getSequenceFlowList().get(0);
		Assert.assertEquals(getSequenceFlow.getProcessDefinition().getId(),"simple_process");
		
		ActivityDefinition activityDefinition = getProcessDefinition.getActivity("s");
		ProcessDefinition  getProcessDefinitionFromA = activityDefinition.getProcessDefinition();
		Assert.assertEquals(getProcessDefinitionFromA.getId(),"simple_process");
		
	}
	
}
