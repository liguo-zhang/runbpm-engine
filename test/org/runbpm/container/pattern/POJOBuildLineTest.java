package org.runbpm.container.pattern;


import org.junit.Assert;
import org.junit.Test;
import org.runbpm.bpmn.definition.EndEvent;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.bpmn.definition.StartEvent;
import org.runbpm.container.ProcessContainer;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;

public class POJOBuildLineTest {
	
	MemoryEntityManagerImpl entityManager = null;
	
	@Test
	public void test(){
		entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
	
		
		ProcessDefinition processDefinition = new ProcessDefinition("simple_process");
		StartEvent startEvent = new StartEvent("s");
		processDefinition.addFlowNode(startEvent);
		EndEvent endEvent = new EndEvent("e");
		processDefinition.addFlowNode(endEvent);
		SequenceFlow sequenceFlow = new SequenceFlow(startEvent,endEvent);
		processDefinition.addSequenceFlow(sequenceFlow);
		
		entityManager.deployProcessDefinition(processDefinition);
		
		executeTest();
	}
	
	@Test
	public void test2(){
		entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		ProcessDefinition processDefinition = new ProcessDefinition(
				"simple_process").addFlowNode(new StartEvent("s"))
				.flowTo(new EndEvent("e"));
		
		entityManager.deployProcessDefinition(processDefinition);
		
		executeTest();
	}
	
	private void executeTest(){
		ProcessContainer processInstanceContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance = processInstanceContainer.createInstance("simple_process");
		processInstanceContainer.start();
		
		long processInstanceId = processInstance.getId();
		
		ActivityInstance activityInstance_Instance_s = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "s").iterator().next();
		ActivityInstance activityInstance_Instance_e = entityManager.listActivityInstanceByActivityDefId(processInstanceId, "e").iterator().next();
		
		Assert.assertEquals("" , activityInstance_Instance_s.getState(),ACTIVITY_STATE.COMPLETED);
		Assert.assertEquals("" , activityInstance_Instance_e.getState(),ACTIVITY_STATE.COMPLETED);
		
		Assert.assertEquals("" , processInstance.getState(),PROCESS_STATE.COMPLETED);
	}
	
}