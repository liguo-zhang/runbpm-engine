package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.EntityConstants.TASK_TYPE;

interface TaskInterface extends EntityInterface {

	/**
	 * 获取工作项所属的活动定义ID
	 * @return 工作项所属于的活动定义ID
	 */
	String getActivityDefinitionId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setActivityDefinitionId(String activityDefinitionId);

	/**
	 * 获取工作项所属于的流程定义ID
	 * @return 流程定义ID
	 */
	String getProcessDefinitionId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param processDefinitionId
	 */
	void setProcessDefinitionId(String processDefinitionId);

	/**
	 * 获取工作项所属于的流程模板ID
	 * @return 流程模板ID
	 */
	long getProcessModelId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param processModelId
	 */
	void setProcessModelId(long processModelId);

	/**
	 * 获取工作项的描述
	 * @return 工作项的描述
	 */
	String getDescription();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param description 
	 */
	void setDescription(String description);

	/**
	 * 获取工作项的状态
	 * @return
	 */
	TASK_STATE getState();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param state
	 */
	void setState(TASK_STATE state);

	/**
	 * 获取工作项的用户ID
	 * @return
	 */
	String getUserId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param userId
	 */
	void setUserId(String userId);

	/**
	 * 获取工作项所属于的活动实例ID
	 * @return 工作项所属于的活动实例ID
	 */
	long getActivityInstanceId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param activityInstanceId
	 */
	void setActivityInstanceId(long activityInstanceId);

	/**
	 * 获取工作项所属于的流程实例ID
	 * @return 工作项所属于的流程实例ID
	 */
	long getProcessInstanceId();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param processInstanceId
	 */
	void setProcessInstanceId(long processInstanceId);

	/**
	 * 获取工作项的完成时间
	 * @return 工作项的完成时间
	 */
	Date getCompleteDate();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param completeDate
	 */
	void setCompleteDate(Date completeDate);

	/**
	 * 获取工作项挂起之前的状态
	 * @return
	 */
	TASK_STATE getStateBeforeSuspend();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param stateBeforeSuspend
	 */
	void setStateBeforeSuspend(TASK_STATE stateBeforeSuspend);
	
	/**
	 * 获取工作项的类型
	 * @return
	 */
	TASK_TYPE getType();

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param type
	 */
	void setType(TASK_TYPE type);

	String getKeyA();

	void setKeyA(String keyA);

	String getKeyB();

	void setKeyB(String keyB);

	String getKeyC();

	void setKeyC(String keyC);

	String getKeyD();

	void setKeyD(String keyD);

}
