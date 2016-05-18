package org.runbpm.context;

import org.runbpm.handler.resource.GlobalResourceHandler;
import org.runbpm.listener.GlobalListener;
import org.runbpm.persistence.EntityManager;
import org.runbpm.service.RuntimeService;

/**
 * 上下文接口
 *
 */
public interface ContextInterface {

	
	/**
	 * 获取对象存储
	 * @return
	 */
	EntityManager getEntityManager();

	/**
	 * 获取RuntimeService，
	 * @return
	 */
	RuntimeService getRuntimeService() ;

	/**
	 * 获取全局事件监听，获取此对象后，可以增加全局的流程、活动和工作项等全局监听事件。
	 * @return
	 */
	GlobalListener getGlobalListener();

	/**
	 * 获取全局人员定义
	 * @return
	 */
	GlobalResourceHandler getGlobalResourceHandler() ;

	/**
	 * 根据 Bean的名称获取指定的对象
	 * @param beanName
	 * @return
	 */
	Object getBean(String beanName) ;
}
