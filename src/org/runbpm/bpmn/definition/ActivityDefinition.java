package org.runbpm.bpmn.definition;

import java.util.List;

public interface ActivityDefinition {
	
	/**
	 * ��ȡ�û���������ڵ����̶������
	 * @return ����������ڵ����̶�������
	 */
	ProcessDefinition getProcessDefinition();
	
	
	/**
	 * ��ȡ�û������������ӻ�
	 * @return �������������ӻ�
	 */
	List<SequenceFlow> getIncomingSequenceFlowList();
	
	/**
	 * ��ȡ�û�����������ӻ�
	 * @return ������������ӻ�
	 */
	List<SequenceFlow> getOutgoingSequenceFlowList();

	/**
	 * ��ȡ�����ID
	 * @return �����ID
	 */
	String getId();
	
	/**
	 * ��ȡ���������
	 * @return ���������
	 */
	String getName();
	
	/**
	 * ��ȡ�����չ����
	 * @return �����չ����
	 */
	ExtensionElements getExtensionElements(); 
	
	//---domain ��������POJO��ʽ�Ĺ���
	void setProcessDefinition(ProcessDefinition processDefinition);
	ProcessDefinition flowTo(ActivityDefinition activityDefinition);
	//---domain ��������POJO��ʽ�Ĺ���	
	
}
