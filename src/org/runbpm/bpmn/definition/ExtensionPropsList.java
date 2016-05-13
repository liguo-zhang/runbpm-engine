package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ExtensionPropsList {

	private List<ExtensionPropsListValue> valueList = new ArrayList<ExtensionPropsListValue>();

	@XmlElement(name="value",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<ExtensionPropsListValue> getValueList() {
		return valueList;
	}

	public void setValueList(List<ExtensionPropsListValue> valueList) {
		this.valueList = valueList;
	}
	
}
