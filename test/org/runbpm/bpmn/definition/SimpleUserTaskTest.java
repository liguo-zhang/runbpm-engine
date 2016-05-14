package org.runbpm.bpmn.definition;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.bpmn.definition.UserTaskResourceExpression;
import org.runbpm.context.Configuration;
import org.runbpm.context.ContextInterface;
import org.runbpm.entity.ProcessModel;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;

public class SimpleUserTaskTest extends RunBPMTestCase{

	
	@Test
	public void test() throws Exception{
		ContextInterface context = Configuration.getContext();
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) context.getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("SimpleUserTaskTest.xml");
		
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		UserTask userTask = (UserTask) processDefinition.getActivity("u1");
		List<UserTaskResourceExpression> feList = userTask.getUserTaskResource().getUserTaskResourceAssignment().getUserTaskResourceExpressionList();
		
		UserTaskResourceExpression userTaskResourceExpression = feList.get(0);
		
		Assert.assertEquals("",userTaskResourceExpression.getValue(), "groupA");
	}
}
