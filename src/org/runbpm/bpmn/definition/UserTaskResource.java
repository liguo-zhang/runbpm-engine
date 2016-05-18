package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlElement;

public class UserTaskResource {

	private UserTaskResourceAssignment userTaskResourceAssignment;
	
	private UserTaskResourcePolicy userTaskResourcePolicy;

	/**
	 * 获取任务分配人的信息，其下可以定义多组执行人、角色或者表达式。
	 * @return
	 */
	@XmlElement(name="resourceAssignment",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public UserTaskResourceAssignment getUserTaskResourceAssignment() {
		return userTaskResourceAssignment;
	}

	public void setUserTaskResourceAssignment(
			UserTaskResourceAssignment userTaskResourceAssignment) {
		this.userTaskResourceAssignment = userTaskResourceAssignment;
	}

	/**
	 * 获取任务分配策略。
	 * @return
	 */
	@XmlElement(name="resourcePolicy",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public UserTaskResourcePolicy getUserTaskResourcePolicy() {
		return userTaskResourcePolicy;
	}

	public void setUserTaskResourcePolicy(
			UserTaskResourcePolicy userTaskResourcePolicy) {
		this.userTaskResourcePolicy = userTaskResourcePolicy;
	}
	
}
