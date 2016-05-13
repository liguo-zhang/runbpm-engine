package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

public class OutElement {
	
	private String source;
	
	private String target;
	
	@XmlAttribute
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	@XmlAttribute
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	
	
}
