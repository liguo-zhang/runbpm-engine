package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class UserTaskResourceExpression {

	private RESOURCE_EXPRESSION_TYPE type;
	
	public enum RESOURCE_EXPRESSION_TYPE{user,group,user_variable,group_variable,handler_bean_id,handler_bean_class,handler_bean_id_variable,handler_bean_class_variable};
	
	private String value;
		
	/**
	 * 获取人员分配类型：人、组、变量、接口定义等
	 * @return
	 */
	@XmlAttribute
	public RESOURCE_EXPRESSION_TYPE getType() {
		return type;
	}

	public void setType(RESOURCE_EXPRESSION_TYPE type) {
		this.type = type;
	}

	/**
	 * 对应类型的值
	 * @return
	 */
	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
