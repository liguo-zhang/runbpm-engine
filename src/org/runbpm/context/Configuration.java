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
			logger.info("∆Ù”√"+applicationContextInterface.getClass());
			
		}
		return applicationContextInterface;
	}

	public static void setContext(
			ContextInterface context) {
		applicationContextInterface = context;
	}
	
}
