package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ExtensionProperty {

	private String name;
	
	private String value;
	
	private ExtensionPropsList extensionPropsList = new ExtensionPropsList();
	
	private ExtensionPropsMap extensionPropsMap = new ExtensionPropsMap();

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlElement(name="list",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public ExtensionPropsList getExtensionPropsList() {
		return extensionPropsList;
	}

	public void setExtensionPropsList(ExtensionPropsList extensionPropsList) {
		this.extensionPropsList = extensionPropsList;
	}

	@XmlElement(name="map",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public ExtensionPropsMap getExtensionPropsMap() {
		return extensionPropsMap;
	}

	public void setExtensionPropsMap(ExtensionPropsMap extensionPropsMap) {
		this.extensionPropsMap = extensionPropsMap;
	}
	
	
}
