package org.runbpm.bpmn.definition;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class UserTaskResourceAssignment {

	private List<UserTaskResourceExpression> userTaskResourceExpressionList;
	

	/**
	 * 获取任务执行人的定义信息，可以为一个人、一个角色、一个动态人、一个动态角色或者符合指定接口的实现类：{@link org.runbpm.handler.resource.ResourceHandler}
	 * @return 资源定义信息
	 */
	@XmlElement(name="resourceExpression",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<UserTaskResourceExpression> getUserTaskResourceExpressionList() {
		return userTaskResourceExpressionList;
	}

	public void setUserTaskResourceExpressionList(
			List<UserTaskResourceExpression> userTaskResourceExpressionList) {
		this.userTaskResourceExpressionList = userTaskResourceExpressionList;
	}
	
}
