package org.runbpm.bpmn.definition;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;


public class FlowElement extends Element{
	
	
	protected String id;
	
	protected String name;
	
	

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public FlowElement(){
		
	}

	public FlowElement(String id){
		this.id = id;
	}
	
	
	public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.id == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        FlowElement d = (FlowElement) o;
        if (d.id == null)
            return false;
        return id.equals(d.id);
    }
	
	protected ProcessDefinition processDefinition;  // parent pointer
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
	
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		this.processDefinition = (ProcessDefinition)parent;
	}

    public String toString(){
		StringBuffer stringBuffer = new  StringBuffer();
		String className = this.getClass().getName();
		stringBuffer.append(className).append(" Id:[").append(this.getId()).append("] ");
		stringBuffer.append(className).append(" Name:[").append(this.getName()).append("] ");
		return stringBuffer.toString();
	}
	
}
