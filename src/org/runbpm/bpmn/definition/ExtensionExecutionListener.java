package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

import org.runbpm.listener.ListenerManager.Event_Type;

public class ExtensionExecutionListener {

	private Event_Type event;
	
	private String classValue;
	
	/* 
	 * �¼�����
	 * @return
	 */
	@XmlAttribute
	public Event_Type getEvent() {
		return event;
	}

	public void setEvent(Event_Type event) {
		this.event = event;
	}

	/**
	 * ִ���¼����͵�����
	 * @return
	 */
	@XmlAttribute(name="class")
	public String getClassValue() {
		return classValue;
	}

	public void setClassValue(String classValue) {
		this.classValue = classValue;
	}
	
}
