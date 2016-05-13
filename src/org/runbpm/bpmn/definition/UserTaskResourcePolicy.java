package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

public class UserTaskResourcePolicy {

	private RESOURCE_POLICY_TYPE type;
	
	public enum RESOURCE_POLICY_TYPE{single,multi};
	
	@XmlAttribute
	public RESOURCE_POLICY_TYPE getType() {
		return type;
	}

	public void setType(RESOURCE_POLICY_TYPE type) {
		this.type = type;
	}
	
}
