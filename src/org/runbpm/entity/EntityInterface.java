package org.runbpm.entity;

import java.util.Date;

 interface EntityInterface {
	
	 /**
	  * 获取流程实例、活动实例或者任务项实例的ID。
	 * @return
	 */
	long getId();
	
	 /**
	  * 该方法为工作流引擎内部方法
	 * @param id
	 */
	void setId(long id);
	
	 /**
	  * 获取流程实例、活动实例或者任务项实例的名称。
	 * @return
	 */
	String getName() ;

	 /**
	  * 该方法为工作流引擎内部方法
	 * @param name
	 */
	void setName(String name) ;

	 /**
	  * 获取流程实例、活动实例或者任务项实例的创建时间
	 * @return
	 */
	Date getCreateDate() ;

	 /**
	  * 该方法为工作流引擎内部方法
	 * @param createDate
	 */
	void setCreateDate(Date createDate) ;

	 /**
	  * 获取流程实例、活动实例或者任务项实例上一次状态发生改变的日期
	 * @return
	 */
	Date getModifyDate() ;

	 /**
	  * 该方法为工作流引擎内部方法
	 * @param modifyDate
	 */
	void setModifyDate(Date modifyDate) ;
}
