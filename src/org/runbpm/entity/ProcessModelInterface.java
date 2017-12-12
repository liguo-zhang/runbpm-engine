package org.runbpm.entity;

import org.runbpm.bpmn.definition.ProcessDefinition;

public interface ProcessModelInterface extends EntityInterface {
	
	/**
	 * ��ȡ����ģ���Ӧ���̶����XML�ı�
	 * @return
	 */
	public String getXmlContent() ;

	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @param 
	 */
	public void setXmlContent(String xmlcontent) ;

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
	
	/**
	 * ��ȡ����ģ��İ汾
	 * @return
	 */
	public int getVersion() ;
	
	/**
	 * �÷���Ϊ���������ڲ�ʹ�õķ���
	 * @return
	 */
	public void setVersion(int version) ;

}
