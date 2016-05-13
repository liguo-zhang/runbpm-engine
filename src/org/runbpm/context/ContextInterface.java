package org.runbpm.context;

import org.runbpm.handler.resource.GlobalResourceHandler;
import org.runbpm.listener.GlobalListener;
import org.runbpm.persistence.EntityManager;
import org.runbpm.service.RuntimeService;

public interface ContextInterface {

	
	EntityManager getEntityManager();

	RuntimeService getRuntimeService() ;

	GlobalListener getGlobalListener();

	GlobalResourceHandler getGlobalResourceHandler() ;

	Object getBean(String beanName) ;
}
