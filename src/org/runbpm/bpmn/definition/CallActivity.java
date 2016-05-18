package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

public class CallActivity extends ActivityDefinitionImpl{

	private String calledElement;
	
	/**
	 * 获取调用的子流程ID
	 * @return
	 */
	@XmlAttribute
	public String getCalledElement() {
		return calledElement;
	}

	public void setCalledElement(String calledElement) {
		this.calledElement = calledElement;
	}
	
	
	public CallActivity(){
		super();
	}
	
	public CallActivity(String id){
		super(id);
	}
	
}
