package org.runbpm.entity;

import org.runbpm.entity.EntityConstants.VARIABLE_TYPE;

public class Variable_ extends EntityBase {

	protected String processDefinitionId;
	
	protected long processInstanceId;

	protected String name;
	
	protected String description;
	
	protected VARIABLE_TYPE type;
	
	protected Object value;
	
	protected String valueString;

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VARIABLE_TYPE getType() {
		return type;
	}

	public void setType(VARIABLE_TYPE type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Variable Id:[").append(this.getId()).append("] ");
		stringBuffer.append("Variable Name:[").append(this.getName()).append("] ");
		return stringBuffer.toString();
	}
		
}
