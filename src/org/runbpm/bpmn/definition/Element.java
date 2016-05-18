package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlElement;

public class Element {
	
	protected ExtensionElements extensionElements = new ExtensionElements();
	

	/**
	 * 获取扩展属性
	 * @return
	 */
	@XmlElement
	public ExtensionElements getExtensionElements() {
		return extensionElements;
	}

	public void setExtensionElements(ExtensionElements extensionElements) {
		this.extensionElements = extensionElements;
	}

}
