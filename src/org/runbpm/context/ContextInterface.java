package org.runbpm.context;

import org.runbpm.handler.resource.GlobalResourceHandler;
import org.runbpm.listener.GlobalListener;
import org.runbpm.persistence.EntityManager;
import org.runbpm.service.RuntimeService;

/**
 * �����Ľӿ�
 *
 */
public interface ContextInterface {

	
	/**
	 * ��ȡ����洢
	 * @return
	 */
	EntityManager getEntityManager();

	/**
	 * ��ȡRuntimeService��
	 * @return
	 */
	RuntimeService getRuntimeService() ;

	/**
	 * ��ȡȫ���¼���������ȡ�˶���󣬿�������ȫ�ֵ����̡���͹������ȫ�ּ����¼���
	 * @return
	 */
	GlobalListener getGlobalListener();

	/**
	 * ��ȡȫ����Ա����
	 * @return
	 */
	GlobalResourceHandler getGlobalResourceHandler() ;

	/**
	 * ���� Bean�����ƻ�ȡָ���Ķ���
	 * @param beanName
	 * @return
	 */
	Object getBean(String beanName) ;
}
