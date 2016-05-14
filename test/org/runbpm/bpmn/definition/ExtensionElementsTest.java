package org.runbpm.bpmn.definition;


import org.junit.Assert;
import org.junit.Test;
import org.runbpm.RunBPMTestCase;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.ExtensionElements;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ProcessModel;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.springframework.core.io.ClassPathResource;
public class ExtensionElementsTest extends RunBPMTestCase{
	@Test
	public void test() throws Exception {
		
		MemoryEntityManagerImpl entityManager = (MemoryEntityManagerImpl) Configuration.getContext().getEntityManager();
		entityManager.clearMemory();
		
		String fileName = this.getBPMNXMLName();
		ClassPathResource classPathResource = new ClassPathResource(fileName,this.getClass());
		entityManager.deployProcessDefinitionFromFile(classPathResource.getFile());
		
		ProcessModel processModel = entityManager.loadLatestProcessModel("ExtensionAttributesTest.xml");
		
		Assert.assertNotNull("",processModel.getProcessDefinition());
		CallActivity activityDefinition = (CallActivity) processModel.getProcessDefinition().getActivity("callCheckCreditProcess");
		
		ExtensionElements extensionElements = activityDefinition.getExtensionElements();
		
		
		Assert.assertEquals( extensionElements.getPropertyValue("testPropValue"),"text");
		Assert.assertEquals( extensionElements.getExtensionPropsList("testList").size(), 3);
		Assert.assertEquals( extensionElements.getExtensionPropsMap("testMap").entrySet().size(), 1);
		Assert.assertEquals( extensionElements.getExtensionPropsMap("testMap").get("11"), "22");
		
		
	}
}
