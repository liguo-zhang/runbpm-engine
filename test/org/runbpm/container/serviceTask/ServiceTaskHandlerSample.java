package org.runbpm.container.serviceTask;

import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.handler.ServiceTaskHandler;

public class ServiceTaskHandlerSample implements ServiceTaskHandler{

	@Override
	public void executeService(ProcessContextBean processContextBean) {
		ProcessInstance processInstance = processContextBean.getProcessInstance();
		ActivityInstance activityInstance = processContextBean.getActivityInstance();
		ActivityDefinition activityDefinition = processContextBean.getActivityDefinition();
		Map<String, VariableInstance> map = processContextBean.getVariableMap();
		Integer i = (Integer) map.get("a").getValue();
		
		System.out.println("ServiceTaskHandlerSample activityInstance id:["+activityInstance.getId()+"],");
		System.out.println("activityDefinition  id:["+activityDefinition.getId()+"],");
		System.out.println("before set map value :["+i+"],");
		
		Configuration.getContext().getEntityManager().setProcessVariable(processInstance.getId(), "a", 100);
		
	}

}
