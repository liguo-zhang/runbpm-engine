package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.ACTIVITY_TYPE;

interface ActivityInterface extends EntityInterface {

	/**
	 * 获取活动对象的流程定义ID。
	 * @return 活动对象的流程定义ID
	 */
	String getProcessDefinitionId();
	
	/**
	 * 获取活动对象的活动定义ID。
	 * @return 活动对象的活动定义ID
	 */
	String getActivityDefinitionId();
	
	/**
	 * 获取活动对象的流程模板ID。
	 * @return 活动对象的流程模板ID
	 */
	long getProcessModelId();
	
	/**
	 * 获取活动对象的流程实例ID。
	 * @return 活动对象的流程实例ID
	 */
	long getProcessInstanceId();
	
	/**
	 * 获取活动对象的类型。
	 * @return 活动对象的类型
	 */
	ACTIVITY_TYPE getType();

	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param type
	 */
	void setType(ACTIVITY_TYPE type);


	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param processDefinitionId
	 */
	void setProcessDefinitionId(String processDefinitionId);

	
	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param processModelId
	 */
	void setProcessModelId(long processModelId);

	/**
	 * 获取活动对象的描述。
	 * @return 活动对象的描述
	 */
	String getDescription();

	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * 获取活动对象的状态。
	 * @return 活动对象的状态
	 */
	ACTIVITY_STATE getState();

	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param state
	 */
	void setState(ACTIVITY_STATE state);

	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param activityDefinitionId
	 */
	void setActivityDefinitionId(String activityDefinitionId);

	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param processInstanceId
	 */
	void setProcessInstanceId(long processInstanceId);

	// 
	/**
	 * 如果一个活动属于一个SubProcess(XPDL规范里的ActivitySet，块活动)，则可以通过此方法获取，所属的块活动定义的的活动实例ID。<br>
	 * @return  所属的块活动定义的的活动实例ID
	 */
	long getParentActivityInstanceId();

	/**
	 * 该方法为流程引擎内部使用的方法。
	 * @param parentActivityInstanceId
	 */
	void setParentActivityInstanceId(long parentActivityInstanceId);

	/*
	 * <pre>
		 <subProcess id="subProcessSampleId"> 
		 		<startEvent id="subProcessStart" />
		 		<sequenceFlow id="subflow1" sourceRef="subProcessStart" targetRef="subshipOrder" />
		  
		  		<userTask id="subshipOrder" name="Ship Order" /> 
		  		<sequenceFlow id="subflow2" sourceRef="subshipOrder" targetRef="subProcessEnd" />
		  
		  		<endEvent id="subProcessEnd" /> 
		  </subProcess>
	  </pre>
	  这个块活动下的子活动,例如subshipOrder所属于的SubProcessBlockId为 subProcessSampleId
	 */
	
	/**
	 * 如果一个活动属于一个SubProcess(XPDL规范里的ActivitySet，块活动)，则可以通过此方法获取，所属的块活动定义ID。<br>
	 * @return
	 */
	String getSubProcessBlockId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param subProcessBlockId
	 */
	void setSubProcessBlockId(String subProcessBlockId);

	/**
	 * 如果一个活动属于一个SubProcess(XPDL规范里的ActivitySet，块活动)，则可以通过此方法获取，所属的级联块活动定义ID。<br>
	 * 该方法已经考虑到SubProcess嵌套。<br>
	 * 如果SubProcess没有嵌套，则此方法的返回值同{@link #getSubProcessBlockId()}<br>
	 * @return
	 */
	String getSequenceBlockId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param sequenceBlockId
	 */
	void setSequenceBlockId(String sequenceBlockId);

	/**
	 * 获取活动对象的完成时间
	 * @return
	 */
	Date getCompleteDate();

	/**该方法为流程引擎内部使用的方法
	 * @param completeDate
	 */
	void setCompleteDate(Date completeDate);

	/**
	 * 获取活动对象上一次被挂起之前的状态
	 * @return
	 */
	ACTIVITY_STATE getStateBeforeSuspend();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param stateBeforeSuspend
	 */
	void setStateBeforeSuspend(ACTIVITY_STATE stateBeforeSuspend);

	/**
	 * 如果该活动定义为一个CallActivity,则可以用此方法获取对应的子流程的流程实例ID
	 * @return
	 */
	long getCallActivityProcessInstanceId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param callActivityProcessInstanceId
	 */
	void setCallActivityProcessInstanceId(long callActivityProcessInstanceId);

	String getKeyA();

	void setKeyA(String keyA);

	String getKeyB();

	void setKeyB(String keyB);

	String getKeyC();

	void setKeyC(String keyC);

	String getKeyD();

	void setKeyD(String keyD);
}
