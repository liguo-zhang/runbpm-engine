package org.runbpm.entity;

import org.runbpm.bpmn.definition.ProcessDefinition;

public interface ProcessModelInterface extends EntityInterface {
	
	/**
	 * 获取流程模板对应流程定义的XML文本
	 * @return
	 */
	public String getXmlContent() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	public void setXmlContent(String xmlcontent) ;

	/**
	 * 获取流程模板对应流程定义对象
	 * @return
	 */
	public ProcessDefinition getProcessDefinition() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	public void setProcessDefinition(ProcessDefinition processDefinition) ;
	
	/**
	 * 获取流程模板对应流程定义的ID
	 * @return
	 */
	public String getProcessDefinitionId() ;

	/**
	 * 该方法为流程引擎内部使用的方法
	 * @param 
	 */
	public void setProcessDefinitionId(String processDefinitionId) ;
	
	/**
	 * 获取流程模板的版本
	 * @return
	 */
	public int getVersion() ;
	
	/**
	 * 该方法为流程引擎内部使用的方法
	 * @return
	 */
	public void setVersion(int version) ;

}
