package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class UserTaskResourceExpression {

	private RESOURCE_EXPRESSION_TYPE type;
	
	public enum RESOURCE_EXPRESSION_TYPE{user,group,user_variable,group_variable,handler_bean_id,handler_bean_class,handler_bean_id_variable,handler_bean_class_variable};
	
	private String value;
		
	@XmlAttribute
	public RESOURCE_EXPRESSION_TYPE getType() {
		return type;
	}

	public void setType(RESOURCE_EXPRESSION_TYPE type) {
		this.type = type;
	}

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
