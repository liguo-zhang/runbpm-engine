package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.EntityConstants.TASK_TYPE;

interface TaskInterface extends EntityInterface {

	/**
	 * ��ȡ�����������Ļ����ID
	 * @return �����������ڵĻ����ID
	 */
	String getActivityDefinitionId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setActivityDefinitionId(String activityDefinitionId);

	/**
	 * ��ȡ�����������ڵ����̶���ID
	 * @return ���̶���ID
	 */
	String getProcessDefinitionId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param processDefinitionId
	 */
	void setProcessDefinitionId(String processDefinitionId);

	/**
	 * ��ȡ�����������ڵ�����ģ��ID
	 * @return ����ģ��ID
	 */
	long getProcessModelId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param processModelId
	 */
	void setProcessModelId(long processModelId);

	/**
	 * ��ȡ�����������
	 * @return �����������
	 */
	String getDescription();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param description 
	 */
	void setDescription(String description);

	/**
	 * ��ȡ�������״̬
	 * @return
	 */
	TASK_STATE getState();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param state
	 */
	void setState(TASK_STATE state);

	/**
	 * ��ȡ��������û�ID
	 * @return
	 */
	String getUserId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param userId
	 */
	void setUserId(String userId);

	/**
	 * ��ȡ�����������ڵĻʵ��ID
	 * @return �����������ڵĻʵ��ID
	 */
	long getActivityInstanceId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param activityInstanceId
	 */
	void setActivityInstanceId(long activityInstanceId);

	/**
	 * ��ȡ�����������ڵ�����ʵ��ID
	 * @return �����������ڵ�����ʵ��ID
	 */
	long getProcessInstanceId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param processInstanceId
	 */
	void setProcessInstanceId(long processInstanceId);

	/**
	 * ��ȡ����������ʱ��
	 * @return ����������ʱ��
	 */
	Date getCompleteDate();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param completeDate
	 */
	void setCompleteDate(Date completeDate);

	/**
	 * ��ȡ���������֮ǰ��״̬
	 * @return
	 */
	TASK_STATE getStateBeforeSuspend();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param stateBeforeSuspend
	 */
	void setStateBeforeSuspend(TASK_STATE stateBeforeSuspend);
	
	/**
	 * ��ȡ�����������
	 * @return
	 */
	TASK_TYPE getType();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
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
