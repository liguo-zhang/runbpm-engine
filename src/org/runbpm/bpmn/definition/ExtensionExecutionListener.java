package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

import org.runbpm.listener.ListenerManager.Event_Type;

public class ExtensionExecutionListener {

	public enum CONDITION_EXPRESSION_TYPE{juel,aviator_el,spring_el,handler_bean_id,handler_bean_class,handler_bean_id_variable,handler_bean_class_variable};
	
	private Event_Type event;
	
	private String classValue;
	
	@XmlAttribute
	public Event_Type getEvent() {
		return event;
	}

	public void setEvent(Event_Type event) {
		this.event = event;
	}

	@XmlAttribute(name="class")
	public String getClassValue() {
		return classValue;
	}

	public void setClassValue(String classValue) {
		this.classValue = classValue;
	}
	
}
