package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ConditionExpression {
	
	private CONDITION_EXPRESSION_TYPE advancedType;
	
	public enum CONDITION_EXPRESSION_TYPE{juel,aviator_el,spring_el,handler_bean_id,handler_bean_class,handler_bean_id_variable,handler_bean_class_variable};
	
	private String value;
	
	/**
	 * 获取高级类型
	 * @return
	 */
	@XmlAttribute (namespace = "http://runbpm.org/schema/1.0/bpmn")
	public CONDITION_EXPRESSION_TYPE getAdvancedType() {
		return advancedType;
	}
	
	public void setAdvancedType(CONDITION_EXPRESSION_TYPE advancedType) {
		this.advancedType = advancedType;
	}

	/**
	 * 如果是条件表达式或者高级类型（永真类型），条件表达式或者高级类型的值是什么。
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
