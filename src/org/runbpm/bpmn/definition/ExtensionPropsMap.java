package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ExtensionPropsMap {

	private List<ExtensionPropsMapEntry> entryList = new ArrayList<ExtensionPropsMapEntry>();

	@XmlElement(name="entry",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<ExtensionPropsMapEntry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<ExtensionPropsMapEntry> entryList) {
		this.entryList = entryList;
	}
	
}
