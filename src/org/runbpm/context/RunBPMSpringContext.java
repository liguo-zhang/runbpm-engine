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
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_000000_NO_INIT_RunBPM,"�����appContextΪ��");
		}
		
		try{
			if(entityManager!=null){ 
				logger.warn("Spring���ڼ��ز�����֮ǰ��entityManager��֮ǰentityManager��Ϊ�գ��п����ǵ�����getEntityManager()������Ĭ���ڴ���ʽ��entityManager���ص����ݽ�ȫ������");
			}
			entityManager = (EntityManager) appContext.getBean("entityManager");
		}catch(NoSuchBeanDefinitionException e){
			entityManager = MemoryEntityManagerImpl.getInstance();
			logger.warn("��ʼ����������������ʱ������û����Spring�����ļ�������entityManager������NoSuchBeanDefinitionException�����ʹ�ñ��뷽ʽ����entityManager,����Ժ��Ը��쳣��");
		}
		
		try{
			if(globalResourceHandler!=null){
				logger.warn("Spring���ڼ��ز�����֮ǰGlobalResourceHandler��֮ǰGlobalResourceHandler��Ϊ�գ��п����ǵ�����getGlobalResourceHandler()������");
			}
			globalResourceHandler= (GlobalResourceHandler)appContext.getBean("globalResourceHandler");
		}catch(NoSuchBeanDefinitionException e){
			globalResourceHandler = new GlobalResourceHandlerSample();
			logger.warn("��ʼ����������������ʱ������û����Spring�����ļ�������globalResourceHandler������NoSuchBeanDefinitionException�����ʹ�ñ��뷽ʽ����globalResourceHandler,����Ժ��Ը��쳣��");
		}
		
		
		try{
			if(globalListener!=null){
				logger.warn("Spring���ڼ��ز�����֮ǰglobalListener��֮ǰglobalListener��Ϊ�գ��п����ǵ�����getGlobalListener()������");
			}
			globalListener = (GlobalListener)appContext.getBean("globalListener");
		}catch(NoSuchBeanDefinitionException e){
			globalListener = new GlobalListener();
			logger.warn("��ʼ����������������ʱ������û����Spring�����ļ�������globalListener������NoSuchBeanDefinitionException�����ʹ�ñ��뷽ʽ����globalListener���߲���Ҫ�¼�����,����Ժ��Ը��쳣��");
		}
		
		try{
			runtimeService = (RuntimeService) appContext.getBean("runtimeService");
		}catch(NoSuchBeanDefinitionException e){
			logger.warn("Spring���ڼ���RuntimeService�����Ǽ���ʧ���ˡ��п��ܺ�������ҪruntimeService",e);
		}
		this.appContext = appContext;
	}
	
	//���֮���ֵ���setRumBPMSpringApplicationContext������ܵ���entityManager�����˱仯
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
