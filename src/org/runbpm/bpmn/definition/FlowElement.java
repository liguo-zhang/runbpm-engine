package org.runbpm.bpmn.definition;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


public class FlowElement extends Element{
	
	
	protected String id;
	
	protected String name;
	
	protected String documentation;
	
	
	/**
	 * 获取ID
	 * @return
	 */
	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * @return
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * 获取描述
	 * @return
	 */
	@XmlElement
	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
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
	
	//不追加该属性， 在 bean->xml 时候将导致异常：
	//Caused by: com.sun.istack.internal.SAXException2: A cycle is detected in the object graph. This will cause infinitely deep XML: 
	@XmlTransient
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
