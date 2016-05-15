package org.runbpm.entity;

import org.runbpm.bpmn.definition.ProcessDefinition;

public class ProcessModel_ extends EntityBase {
	
	private String processDefinitionId;
	
	//数据库的大对象保存
	private String xmlcontent;
	
	private ProcessDefinition processDefinition;
	
	private int version;
	
	public String getXmlcontent() {
		return xmlcontent;
	}

	public void setXmlcontent(String xmlcontent) {
		this.xmlcontent = xmlcontent;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}
	

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ProcessModel Id:[").append(this.getId()).append("] ");
		return stringBuffer.toString();
	}
	
}
