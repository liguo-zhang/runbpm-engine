package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.ACTIVITY_TYPE;

interface ActivityInterface extends EntityInterface {

	/**
	 * ��ȡ���������̶���ID��
	 * @return ���������̶���ID
	 */
	String getProcessDefinitionId();
	
	/**
	 * ��ȡ�����Ļ����ID��
	 * @return �����Ļ����ID
	 */
	String getActivityDefinitionId();
	
	/**
	 * ��ȡ����������ģ��ID��
	 * @return ����������ģ��ID
	 */
	long getProcessModelId();
	
	/**
	 * ��ȡ����������ʵ��ID��
	 * @return ����������ʵ��ID
	 */
	long getProcessInstanceId();
	
	/**
	 * ��ȡ���������͡�
	 * @return ����������
	 */
	ACTIVITY_TYPE getType();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param type
	 */
	void setType(ACTIVITY_TYPE type);


	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param processDefinitionId
	 */
	void setProcessDefinitionId(String processDefinitionId);

	
	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param processModelId
	 */
	void setProcessModelId(long processModelId);

	/**
	 * ��ȡ������������
	 * @return ����������
	 */
	String getDescription();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * ��ȡ������״̬��
	 * @return ������״̬
	 */
	ACTIVITY_STATE getState();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param state
	 */
	void setState(ACTIVITY_STATE state);

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param activityDefinitionId
	 */
	void setActivityDefinitionId(String activityDefinitionId);

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
	 * @param processInstanceId
	 */
	void setProcessInstanceId(long processInstanceId);

	// 
	/**
	 * ���һ�������һ��SubProcess(XPDL�淶���ActivitySet����)�������ͨ���˷�����ȡ�������Ŀ�����ĵĻʵ��ID��<br>
	 * @return  �����Ŀ�����ĵĻʵ��ID
	 */
	long getParentActivityInstanceId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ�����
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
	  ������µ��ӻ,����subshipOrder�����ڵ�SubProcessBlockIdΪ subProcessSampleId
	 */
	
	/**
	 * ���һ�������һ��SubProcess(XPDL�淶���ActivitySet����)�������ͨ���˷�����ȡ�������Ŀ�����ID��<br>
	 * @return
	 */
	String getSubProcessBlockId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param subProcessBlockId
	 */
	void setSubProcessBlockId(String subProcessBlockId);

	/**
	 * ���һ�������һ��SubProcess(XPDL�淶���ActivitySet����)�������ͨ���˷�����ȡ�������ļ���������ID��<br>
	 * �÷����Ѿ����ǵ�SubProcessǶ�ס�<br>
	 * ���SubProcessû��Ƕ�ף���˷����ķ���ֵͬ{@link #getSubProcessBlockId()}<br>
	 * @return
	 */
	String getSequenceBlockId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param sequenceBlockId
	 */
	void setSequenceBlockId(String sequenceBlockId);

	/**
	 * ��ȡ���������ʱ��
	 * @return
	 */
	Date getCompleteDate();

	/**�÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param completeDate
	 */
	void setCompleteDate(Date completeDate);

	/**
	 * ��ȡ�������һ�α�����֮ǰ��״̬
	 * @return
	 */
	ACTIVITY_STATE getStateBeforeSuspend();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param stateBeforeSuspend
	 */
	void setStateBeforeSuspend(ACTIVITY_STATE stateBeforeSuspend);

	/**
	 * ����û����Ϊһ��CallActivity,������ô˷�����ȡ��Ӧ�������̵�����ʵ��ID
	 * @return
	 */
	long getCallActivityProcessInstanceId();

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
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
