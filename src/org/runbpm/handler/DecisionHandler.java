package org.runbpm.handler;

import org.runbpm.context.ProcessContextBean;


/**
 * ���ỡ�жϵĽӿڣ��������øýӿڵ�ʵ������Ϊ���ỡ�����Ƿ�������ж���������
 *
 */
public interface DecisionHandler {

	/**
	 * �жϵ�ǰ���ỡ�ھ��������ʵ����תʱ�Ƿ����
	 * @param processContextBean ������������Ϣ
	 * @return
	 */
	boolean evalConditionContext(ProcessContextBean processContextBean);
	
}
