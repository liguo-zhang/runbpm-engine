package org.runbpm.spring.execlusivegateway;

import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.handler.DecisionHandler;


public class SpecialConditionHandlerSample implements DecisionHandler{

	public boolean evalConditionContext(ProcessContextBean processContextBean){
		SequenceFlow transition = processContextBean.getSequenceFlow();
		if("flow2".equals(transition.getId())){
			return true;
		}else{
			return false;
		}
	}
	
}
