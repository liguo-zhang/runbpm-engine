package org.runbpm.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
	
	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	private static ContextInterface applicationContextInterface ;

	public static ContextInterface getContext(){
		if(applicationContextInterface==null){
			
			//applicationContextInterface = new DefaultApplicationContext();
			applicationContextInterface = new RunBPMSpringContext();
			logger.info("启用"+applicationContextInterface.getClass());
			
		}
		return applicationContextInterface;
	}

	/**
	 * 设置一个系统上下文对象，目前支持 两种： {@link org.runbpm.context.DefaultContext} 和 {@link org.runbpm.context.RunBPMSpringContext}
	 * @param context
	 */
	public static void setContext(
			ContextInterface context) {
		applicationContextInterface = context;
	}
	
}
