package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.PROCESS_STATE;

public class Process_ extends EntityBase {
	
	protected long processModelId;
	
	protected String processDefinitionId;
	
	protected String creator;
	
	protected long parentActivityInstanceId ;

	protected String description;
	
	protected PROCESS_STATE state;
	
	protected Date completeDate;
	
	protected PROCESS_STATE stateBeforeSuspend;
	
	//°Ë´ó½ð¸Õ
	protected String keyA;
	protected String keyB;
	protected String keyC;
	protected String keyD;
	protected String keyE;
	protected String keyF;
	protected String keyG;
	protected String keyH;
	
	public PROCESS_STATE getState() {
		return state;
	}

	public void setState(PROCESS_STATE state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public long getParentActivityInstanceId() {
		return parentActivityInstanceId;
	}

	public void setParentActivityInstanceId(long parentActivityInstanceId) {
		this.parentActivityInstanceId = parentActivityInstanceId;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	

	public PROCESS_STATE getStateBeforeSuspend() {
		return stateBeforeSuspend;
	}

	public void setStateBeforeSuspend(PROCESS_STATE stateBeforeSuspend) {
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

	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ProcessInstance Id:[").append(this.getId()).append("] ");
		stringBuffer.append("ProcessInstance Name:[").append(this.getName()).append("] ");
		return stringBuffer.toString();
	}
}
