package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlElement;

public class UserTaskResource {

	private UserTaskResourceAssignment userTaskResourceAssignment;
	
	private UserTaskResourcePolicy userTaskResourcePolicy;

	@XmlElement(name="resourceAssignment",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public UserTaskResourceAssignment getUserTaskResourceAssignment() {
		return userTaskResourceAssignment;
	}

	public void setUserTaskResourceAssignment(
			UserTaskResourceAssignment userTaskResourceAssignment) {
		this.userTaskResourceAssignment = userTaskResourceAssignment;
	}

	@XmlElement(name="resourcePolicy",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public UserTaskResourcePolicy getUserTaskResourcePolicy() {
		return userTaskResourcePolicy;
	}

	public void setUserTaskResourcePolicy(
			UserTaskResourcePolicy userTaskResourcePolicy) {
		this.userTaskResourcePolicy = userTaskResourcePolicy;
	}
	
}
