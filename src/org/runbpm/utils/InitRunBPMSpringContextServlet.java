package org.runbpm.utils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.runbpm.context.Configuration;
import org.runbpm.context.RunBPMSpringContext;
import org.runbpm.service.RuntimeService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//todo 移除这个类到web工程
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
	        
			RuntimeService runtimeService = Configuration.getContext().getRuntimeService();
			//从数据库中重新加载流程模板到内存
			runtimeService.loadProcessModels(true);
	    } 
	 
}