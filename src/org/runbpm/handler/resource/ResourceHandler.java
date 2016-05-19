package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.ProcessContextBean;

/**
 * ����������ָ������һ���࣬�������Ҫʵ�ָýӿڡ�
 * ����������Լ��ӿ����������̶���֮�������������е�������������ʱ�����Զ����ø����ʵ�֡�
 *
 */
public interface ResourceHandler {

	/**
	 * ������������ִ�з���ʱ�������õķ���
	 * @param processContextBean ������������Ϣ
	 * @return ��Ա�б��������������ݸ÷���ֵ���������һ���˺�һ��������һһ��Ӧ��
	 */
	List<User> getUsers(ProcessContextBean processContextBean);
}
