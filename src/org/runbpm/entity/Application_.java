package org.runbpm.entity;

public class Application_ extends EntityBase {
	
	private String activityDefinitionId;
	
	private String processDefinitionId;
	
	private String processModelId;

	private String description;
	
	//完成次数
	private Integer completeTimes;

	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}

	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessModelId() {
		return processModelId;
	}

	public void setProcessModelId(String processModelId) {
		this.processModelId = processModelId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getCompleteTimes() {
		return completeTimes;
	}

	public void setCompleteTimes(Integer completeTimes) {
		this.completeTimes = completeTimes;
	}

	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ApplicationInstance Id:[").append(this.getId()).append("] ");
		stringBuffer.append("ApplicationInstance Name:[").append(this.getName()).append("] ");
		return stringBuffer.toString();
	}
	
}
