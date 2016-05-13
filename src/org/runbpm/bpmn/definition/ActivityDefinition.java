package org.runbpm.bpmn.definition;

import java.util.List;

public interface ActivityDefinition {
	
	/**
	 * 获取该活动定义所属于的流程定义对象
	 * @return 活动定义所属于的流程定对象义
	 */
	ProcessDefinition getProcessDefinition();
	
	
	/**
	 * 获取该活动定义的输入连接弧
	 * @return 活动定义的输入连接弧
	 */
	List<SequenceFlow> getIncomingSequenceFlowList();
	
	/**
	 * 获取该活动定义的输出连接弧
	 * @return 活动定义的输出连接弧
	 */
	List<SequenceFlow> getOutgoingSequenceFlowList();

	/**
	 * 获取活动定义ID
	 * @return 活动定义ID
	 */
	String getId();
	
	/**
	 * 获取活动定义名称
	 * @return 活动定义名称
	 */
	String getName();
	
	/**
	 * 获取活动的扩展属性
	 * @return 活动的扩展属性
	 */
	ExtensionElements getExtensionElements(); 
	
	//---domain 用于流程POJO形式的构建
	void setProcessDefinition(ProcessDefinition processDefinition);
	ProcessDefinition flowTo(ActivityDefinition activityDefinition);
	//---domain 用于流程POJO形式的构建	
	
}
