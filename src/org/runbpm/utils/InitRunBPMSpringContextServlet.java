package org.runbpm.utils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class InitRunBPMSpringContextServlet extends HttpServlet {
	 
	 /**
	 * 
	 */
	private static final long serialVersionUID = -931933836750539862L;

	public void init() throws ServletException {          
	        super.init();
	        
	        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
					"RunBPM.spring_context.xml",this.getClass());
			RunBPMSpringContext springAppContext = new RunBPMSpringContext(appContext);
			Configuration.setContext(springAppContext);
	        
	    }   
	 
}