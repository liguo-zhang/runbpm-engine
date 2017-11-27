package org.runbpm.context;

import org.runbpm.handler.resource.GlobalResourceHandler;
import org.runbpm.handler.resource.GlobalResourceHandlerSample;
import org.runbpm.listener.GlobalListener;
import org.runbpm.persistence.EntityManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.runbpm.service.RunBPMService;
import org.runbpm.service.RunBPMServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultContext implements ContextInterface {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultContext.class);

	private EntityManager entityManager;
	private GlobalResourceHandler globalResourceHandler;
	private RunBPMService runBPMService;

	private GlobalListener globalInstanceListenerSet;

	public DefaultContext() {
		entityManager = MemoryEntityManagerImpl.getInstance();
	}

	public EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = MemoryEntityManagerImpl.getInstance();
			logger.info("∆Ù”√:"+entityManager.getClass());
		}
		return entityManager;
	}

	public RunBPMService getRunBPMService() {
		if (runBPMService == null) {
			runBPMService = new RunBPMServiceImpl();
			runBPMService.setEntityManager(entityManager);
		}
		return runBPMService;
	}

	public GlobalListener getGlobalListener() {
		if (globalInstanceListenerSet == null) {
			globalInstanceListenerSet = new GlobalListener();
		}
		return globalInstanceListenerSet;
	}
	
	

	public GlobalResourceHandler getGlobalResourceHandler() {
		if (globalResourceHandler == null) {
			globalResourceHandler = new GlobalResourceHandlerSample();
		}
		return globalResourceHandler;
	}

	public Object getBean(String beanName) {
		return null;
	}

}
