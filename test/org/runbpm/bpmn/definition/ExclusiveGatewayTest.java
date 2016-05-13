package org.runbpm.bpmn.definition;


import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ConditionExpression;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessModel;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;
public class ExclusiveGatewayTest extends RunBPMTestCase{
	@Test
	public void test() throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.initProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("ExclusiveGatewayTest.xml");
		
		Assert.assertNotNull("",processModel.getProcessDefinition());
		
		SequenceFlow sequenceFlow = processModel.getProcessDefinition().getSequenceFlow("flow2");
		SequenceFlow sequenceFlow3 = processModel.getProcessDefinition().getSequenceFlow("flow3");
		
		ActivityDefinition activityDefinition = processModel.getProcessDefinition().getActivity("theStart");
		ProcessDefinition processDefinition= activityDefinition.getProcessDefinition();
		Assert.assertEquals("",processDefinition.getId(),"ExclusiveGatewayTest.xml");
		
		Assert.assertEquals(sequenceFlow.getConditionExpression().getValue(), "specialConditionHandlerSample_for_unittest");
		Assert.assertEquals(sequenceFlow.getConditionExpression().getAdvancedType(),ConditionExpression.CONDITION_EXPRESSION_TYPE.handler_bean_class_variable);
		
		Assert.assertEquals(sequenceFlow3.getConditionExpression().getAdvancedType(),ConditionExpression.CONDITION_EXPRESSION_TYPE.juel);
		
		
		
	}
}
