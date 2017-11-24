package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.PROCESS_STATE;

interface ProcessInterface extends EntityInterface{
	
	 /**
	  * ��ȡ���̶����״̬
	 * @return
	 */
	PROCESS_STATE getState() ;
	
	

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */ 
	void setState(PROCESS_STATE state);

	/**
	 * ��ȡ���̶��������
	 * @return
	 */
	String getDescription() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setDescription(String description) ;

	/**
	 * ��ȡ���̶�������̶���ID
	 * @return
	 */
	String getProcessDefinitionId() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setProcessDefinitionId(String processDefinitionId);

	/**
	 * ��ȡ���̶��������ģ��ID
	 * @return
	 */
	long getProcessModelId() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setProcessModelId(long processModelId) ;

	/**
	 * ���������Ϊһ�������̣����ͨ���˷�����ȡ���̶��������ڵĸ����̵Ļʵ��ID
	 * @return
	 */
	long getParentActivityInstanceId() ;
	
	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setParentActivityInstanceId(long parentActivityInstanceId) ;
	
	/**
	 * ��ȡ���̵Ĵ����ߣ����̵Ĵ������ڴ�������ʱ������API�ṩ������ָ����
	 * @return
	 */
	String getCreator() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setCreator(String creator) ;
	
	/**
	 * ��ȡ����ʵ�������ʱ��
	 * @return
	 */
	Date getCompleteDate() ;
	
	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setCompleteDate(Date completeDate) ;

	/**
	 * ��ȡ����ʵ����һ�α�����ǰ��״̬
	 * @return
	 */
	PROCESS_STATE getStateBeforeSuspend() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	void setStateBeforeSuspend(PROCESS_STATE stateBeforeSuspend) ;

	String getKeyA() ;

	void setKeyA(String keyA) ;

	String getKeyB() ;

	void setKeyB(String keyB) ;

	String getKeyC() ;

	void setKeyC(String keyC) ;
	
	String getKeyD() ;

	void setKeyD(String keyD) ;

}
