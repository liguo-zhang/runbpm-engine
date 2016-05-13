package org.runbpm.entity;

import org.runbpm.bpmn.definition.ProcessDefinition;

public interface ProcessModelInterface extends EntityInterface {
	
	/**
	 * ��ȡ����ģ���Ӧ���̶����XML�ı�
	 * @return
	 */
	public String getXmlcontent() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	public void setXmlcontent(String xmlcontent) ;

	/**
	 * ��ȡ����ģ���Ӧ���̶������
	 * @return
	 */
	public ProcessDefinition getProcessDefinition() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	public void setProcessDefinition(ProcessDefinition processDefinition) ;
	
	/**
	 * ��ȡ����ģ���Ӧ���̶����ID
	 * @return
	 */
	public String getProcessDefinitionId() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	public void setProcessDefinitionId(String processDefinitionId) ;

}