package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ExtensionProps {
	
	private String name;
	
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<ExtensionProperty> propertyList = new ArrayList<ExtensionProperty>();

	@XmlElement(name="extensionProp", namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<ExtensionProperty> getpropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<ExtensionProperty> propertyList) {
		this.propertyList = propertyList;
	}

	
}
