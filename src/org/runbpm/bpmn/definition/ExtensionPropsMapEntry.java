package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ExtensionPropsMapEntry {

	private String key;
	
	private String value;

	@XmlAttribute 
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
