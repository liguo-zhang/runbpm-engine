package org.runbpm.bpmn.definition;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class UserTaskResourceAssignment {

	private List<UserTaskResourceExpression> userTaskResourceExpressionList;
	
	

	@XmlElement(name="resourceExpression",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<UserTaskResourceExpression> getUserTaskResourceExpressionList() {
		return userTaskResourceExpressionList;
	}

	public void setUserTaskResourceExpressionList(
			List<UserTaskResourceExpression> userTaskResourceExpressionList) {
		this.userTaskResourceExpressionList = userTaskResourceExpressionList;
	}
	
}
