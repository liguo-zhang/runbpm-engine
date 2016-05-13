package org.runbpm.handler;

import org.runbpm.context.Execution;


public interface DecisionHandler {

	boolean evalConditionContext(Execution execution);
	
}
