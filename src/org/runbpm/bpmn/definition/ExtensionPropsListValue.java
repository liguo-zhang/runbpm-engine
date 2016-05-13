package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlValue;

public class ExtensionPropsListValue {

	private String value;

	@XmlValue 
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
