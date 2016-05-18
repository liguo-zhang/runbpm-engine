package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Definitions {
	

	protected String id;
	
	protected String name;
	
	protected ExtensionElements extensionAttributes = new ExtensionElements();

	/**
	 * 获取ID
	 * @return
	 */
	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * @return
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取扩展属性
	 * @return
	 */
	@XmlElement
	public ExtensionElements getExtensionAttributes() {
		return extensionAttributes;
	}

	public void setExtensionAttributes(ExtensionElements extensionAttributes) {
		this.extensionAttributes = extensionAttributes;
	}
	
	private ProcessDefinition process;

	/**
	 * 获取流程定义对象。目前只允许一个Definitions下只允许定义一个流程定义。
	 * @return
	 */
	@XmlElement(name="process")
	public ProcessDefinition getProcess() {
		return process;
	}

	public void setProcess(ProcessDefinition process) {
		this.process = process;
	}
	
}
