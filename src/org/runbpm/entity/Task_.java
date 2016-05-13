package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.EntityConstants.TASK_TYPE;


public class Task_ extends EntityBase {

	protected String userId;

	protected long processModelId;
	
	protected String processDefinitionId;
	
	protected String activityDefinitionId;
	
	protected long processInstanceId;
	
	protected long activityInstanceId;

	protected String description;
	
	protected Date completeDate;
	
	protected TASK_TYPE type;

	protected TASK_STATE state;
	
	protected TASK_STATE stateBeforeSuspend;
	
	//°Ë´ó½ð¸Õ
	protected String keyA;
	protected String keyB;
	protected String keyC;
	protected String keyD;
	protected String keyE;
	protected String keyF;
	protected String keyG;
	protected String keyH;

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

	public long getProcessModelId() {
		return processModelId;
	}

	public void setProcessModelId(long processModelId) {
		this.processModelId = processModelId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TASK_STATE getState() {
		return state;
	}

	public void setState(TASK_STATE state) {
		this.state = state;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public long getActivityInstanceId() {
		return activityInstanceId;
	}

	public void setActivityInstanceId(long activityInstanceId) {
		this.activityInstanceId = activityInstanceId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public TASK_STATE getStateBeforeSuspend() {
		return stateBeforeSuspend;
	}

	public void setStateBeforeSuspend(TASK_STATE stateBeforeSuspend) {
		this.stateBeforeSuspend = stateBeforeSuspend;
	}
	
	public String getKeyA() {
		return keyA;
	}

	public void setKeyA(String keyA) {
		this.keyA = keyA;
	}

	public String getKeyB() {
		return keyB;
	}

	public void setKeyB(String keyB) {
		this.keyB = keyB;
	}

	public String getKeyC() {
		return keyC;
	}

	public void setKeyC(String keyC) {
		this.keyC = keyC;
	}

	public String getKeyD() {
		return keyD;
	}

	public void setKeyD(String keyD) {
		this.keyD = keyD;
	}

	public String getKeyE() {
		return keyE;
	}

	public void setKeyE(String keyE) {
		this.keyE = keyE;
	}

	public String getKeyF() {
		return keyF;
	}

	public void setKeyF(String keyF) {
		this.keyF = keyF;
	}

	public String getKeyG() {
		return keyG;
	}

	public void setKeyG(String keyG) {
		this.keyG = keyG;
	}

	public String getKeyH() {
		return keyH;
	}

	public void setKeyH(String keyH) {
		this.keyH = keyH;
	}
	
	public TASK_TYPE getType() {
		return type;
	}

	public void setType(TASK_TYPE type) {
		this.type = type;
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("TaskInstance Id:[").append(this.getId())
				.append("] ");
		stringBuffer.append("TaskInstance Name:[").append(this.getName())
				.append("] ");
		return stringBuffer.toString();
	}

}
