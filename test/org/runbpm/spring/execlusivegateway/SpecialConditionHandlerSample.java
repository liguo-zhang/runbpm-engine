package org.runbpm.spring.execlusivegateway;

import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.context.Execution;
import org.runbpm.handler.DecisionHandler;


public class SpecialConditionHandlerSample implements DecisionHandler{

	public boolean evalConditionContext(Execution handlerContext){
		SequenceFlow transition = handlerContext.getTransition();
		if("flow2".equals(transition.getId())){
			return true;
		}else{
			return false;
		}
	}
	
}
