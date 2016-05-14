package org.runbpm.context;

import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.resource.GlobalResourceHandler;
import org.runbpm.handler.resource.GlobalResourceHandlerSample;
import org.runbpm.listener.GlobalListener;
import org.runbpm.persistence.EntityManager;
import org.runbpm.persistence.memory.MemoryEntityManagerImpl;
import org.runbpm.service.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class RunBPMSpringContext implements ContextInterface {
	
	private  static final Logger logger = LoggerFactory.getLogger(RunBPMSpringContext.class);
	
	private  EntityManager entityManager;
	private  GlobalResourceHandler globalResourceHandler;
	private  RuntimeService runtimeService;
	
	private GlobalListener globalListener;
	
	private  ApplicationContext appContext;
	
	public RunBPMSpringContext(){
	}
	
	public RunBPMSpringContext(ApplicationContext appContext){
		this.appContext = appContext;
		setApplicationContext_(appContext);
	}
	
	
	private  void setApplicationContext_(ApplicationContext appContext){
		if(appContext==null){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_000000_NO_INIT_RunBPM,"输入的appContext为空");
		}
		
		try{
			if(entityManager!=null){ 
				logger.warn("Spring正在加载并覆盖之前的entityManager。之前entityManager不为空，有可能是调用了getEntityManager()方法，默认内存形式的entityManager加载的数据将全部作废");
			}
			entityManager = (EntityManager) appContext.getBean("entityManager");
		}catch(NoSuchBeanDefinitionException e){
			entityManager = MemoryEntityManagerImpl.getInstance();
			logger.warn("初始化流程引擎上下文时，由于没有在Spring配置文件中配置entityManager，出现NoSuchBeanDefinitionException。如果使用编码方式设置entityManager,则可以忽略该异常。");
		}
		
		try{
			if(globalResourceHandler!=null){
				logger.warn("Spring正在加载并覆盖之前GlobalResourceHandler。之前GlobalResourceHandler不为空，有可能是调用了getGlobalResourceHandler()方法。");
			}
			globalResourceHandler= (GlobalResourceHandler)appContext.getBean("globalResourceHandler");
		}catch(NoSuchBeanDefinitionException e){
			globalResourceHandler = new GlobalResourceHandlerSample();
			logger.warn("初始化流程引擎上下文时，由于没有在Spring配置文件中配置globalResourceHandler，出现NoSuchBeanDefinitionException。如果使用编码方式设置globalResourceHandler,则可以忽略该异常。");
		}
		
		
		try{
			if(globalListener!=null){
				logger.warn("Spring正在加载并覆盖之前globalListener。之前globalListener不为空，有可能是调用了getGlobalListener()方法。");
			}
			globalListener = (GlobalListener)appContext.getBean("globalListener");
		}catch(NoSuchBeanDefinitionException e){
			globalListener = new GlobalListener();
			logger.warn("初始化流程引擎上下文时，由于没有在Spring配置文件中配置globalListener，出现NoSuchBeanDefinitionException。如果使用编码方式设置globalListener或者不需要事件功能,则可以忽略该异常。");
		}
		
		try{
			runtimeService = (RuntimeService) appContext.getBean("runtimeService");
		}catch(NoSuchBeanDefinitionException e){
			logger.warn("Spring正在加载RuntimeService，但是加载失败了。有可能后续不需要runtimeService",e);
		}
		this.appContext = appContext;
	}
	
	//如果之后又调用setRumBPMSpringApplicationContext，则可能导致entityManager发生了变化
	public  EntityManager getEntityManager(){
		if(entityManager==null){
			entityManager = MemoryEntityManagerImpl.getInstance();
		}
		return entityManager;
	}
	
	
	public  RuntimeService getRuntimeService(){
		if(runtimeService==null){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_000000_NO_INIT_RunBPM,"RuntimeService");
		}
		return runtimeService;
	}
	
	public GlobalListener getGlobalListener() {
		if (globalListener == null) {
			globalListener = new GlobalListener();
		}
		return globalListener;
	}
	
	public   GlobalResourceHandler getGlobalResourceHandler(){
		if(globalResourceHandler==null){
			globalResourceHandler = new GlobalResourceHandlerSample();
		}
		return globalResourceHandler;
	}
	
	public  Object getBean(String beanName){
		return appContext.getBean(beanName);
	}
	
	public  ApplicationContext getAppContext(){
		return appContext;
	}
}
