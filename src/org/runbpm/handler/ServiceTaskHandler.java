package org.runbpm.handler;

import org.runbpm.context.ProcessContextBean;

/**
 * ��ServiceTask����Ϊһ��Java��ʱ��Java����Ҫʵ�ֵĽӿ�
 */
public interface ServiceTaskHandler {

	/**
	 * �������������е�ServiceTask����ִ�еķ���
	 * @param processContextBean ������������Ϣ
	 */
	void executeService(ProcessContextBean processContextBean);
	
}
