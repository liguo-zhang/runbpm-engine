package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Definitions {
	

	protected String id;
	
	protected String name;
	
	protected ExtensionElements extensionAttributes = new ExtensionElements();

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public ExtensionElements getExtensionAttributes() {
		return extensionAttributes;
	}

	public void setExtensionAttributes(ExtensionElements extensionAttributes) {
		this.extensionAttributes = extensionAttributes;
	}
	
	private ProcessDefinition process;

	@XmlElement(name="process")
	public ProcessDefinition getProcess() {
		return process;
	}

	public void setProcess(ProcessDefinition process) {
		this.process = process;
	}
	
}
