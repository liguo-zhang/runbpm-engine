package org.runbpm.entity;

import java.util.Date;

import org.runbpm.entity.EntityConstants.PROCESS_STATE;

interface ProcessInterface extends EntityInterface{
	
	 /**
	  * 获取流程对象的状态
	 * @return
	 */
	PROCESS_STATE getState() ;
	
	

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */ 
	void setState(PROCESS_STATE state);

	/**
	 * 获取流程对象的描述
	 * @return
	 */
	String getDescription() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setDescription(String description) ;

	/**
	 * 获取流程对象的流程定义ID
	 * @return
	 */
	String getProcessDefinitionId() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setProcessDefinitionId(String processDefinitionId);

	/**
	 * 获取流程对象的流程模板ID
	 * @return
	 */
	long getProcessModelId() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setProcessModelId(long processModelId) ;

	/**
	 * 如果该流程为一个子流程，则可通过此方法获取流程对象所属于的父流程的活动实例ID
	 * @return
	 */
	long getParentActivityInstanceId() ;
	
	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setParentActivityInstanceId(long parentActivityInstanceId) ;
	
	/**
	 * 获取流程的创建者，流程的创建者在创建流程时由引擎API提供参数可指定。
	 * @return
	 */
	String getCreator() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setCreator(String creator) ;
	
	/**
	 * 获取流程实例的完成时间
	 * @return
	 */
	Date getCompleteDate() ;
	
	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	void setCompleteDate(Date completeDate) ;

	/**
	 * 获取流程实例上一次被挂起前的状态
	 * @return
	 */
	PROCESS_STATE getStateBeforeSuspend() ;

	/**
	 * 该方法为流程引擎内部使用的方法
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
