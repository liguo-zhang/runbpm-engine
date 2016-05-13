package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.ACTIVITY_TYPE;

public class Activity_ extends EntityBase {
	
	protected long processModelId;
	
	protected String processDefinitionId;
	
	protected String activityDefinitionId;
	
	protected String sequenceBlockId;
	
	protected String subProcessBlockId;
	
	protected long processInstanceId;
	
	protected long parentActivityInstanceId;
	
	protected String description;
	
	protected ACTIVITY_TYPE type;
	
	protected Date completeDate;
	
	protected long callActivityProcessInstanceId;
	
	protected ACTIVITY_STATE state;
	
	protected ACTIVITY_STATE stateBeforeSuspend;
	
	//八大金刚
	protected String keyA;
	protected String keyB;
	protected String keyC;
	protected String keyD;
	protected String keyE;
	protected String keyF;
	protected String keyG;
	protected String keyH;
	
	public ACTIVITY_TYPE getType() {
		return type;
	}

	public void setType(ACTIVITY_TYPE type) {
		this.type = type;
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

	public ACTIVITY_STATE getState() {
		return state;
	}

	public void setState(ACTIVITY_STATE state) {
		this.state = state;
	}

	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}

	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	//块活动下的子活动 的活动实例ID
	public long getParentActivityInstanceId() {
		return parentActivityInstanceId;
	}

	public void setParentActivityInstanceId(long parentActivityInstanceId) {
		this.parentActivityInstanceId = parentActivityInstanceId;
	}

	/**
	 * 		<subProcess id="subProcess">
			  <startEvent id="subProcessStart" />
			  <sequenceFlow id="subflow1" sourceRef="subProcessStart" targetRef="subshipOrder" />
			  
			  <userTask id="subshipOrder" name="Ship Order" />
			  <sequenceFlow id="subflow2" sourceRef="subshipOrder" targetRef="subProcessEnd" />
			  
			  <endEvent id="subProcessEnd" />
 	 *		 </subProcess>
	 *  块活动下的子活动 所属于的subProcessId
	 * @return
	 */
	public String getSubProcessBlockId() {
		return subProcessBlockId;
	}

	public void setSubProcessBlockId(String subProcessBlockId) {
		this.subProcessBlockId = subProcessBlockId;
	}
	
	
	public String getSequenceBlockId() {
		return sequenceBlockId;
	}

	public void setSequenceBlockId(String sequenceBlockId) {
		this.sequenceBlockId = sequenceBlockId;
	}
	
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	
	public ACTIVITY_STATE getStateBeforeSuspend() {
		return stateBeforeSuspend;
	}

	public void setStateBeforeSuspend(ACTIVITY_STATE stateBeforeSuspend) {
		this.stateBeforeSuspend = stateBeforeSuspend;
	}
	
	
	public long getCallActivityProcessInstanceId() {
		return callActivityProcessInstanceId;
	}

	public void setCallActivityProcessInstanceId(
			long callActivityProcessInstanceId) {
		this.callActivityProcessInstanceId = callActivityProcessInstanceId;
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
		stringBuffer.append("ActivityInstance Id:[").append(this.getId()).append("] ");
		stringBuffer.append("ActivityInstance Name:[").append(this.getName()).append("] ");
		stringBuffer.append("activityDefinitionId Id:[").append(this.getActivityDefinitionId()).append("] ");
		return stringBuffer.toString();
	}
	
}
