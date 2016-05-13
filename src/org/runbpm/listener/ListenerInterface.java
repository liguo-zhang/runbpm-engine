package org.runbpm.listener;

import org.runbpm.context.Execution;

public interface ListenerInterface {

	@SuppressWarnings(value={"rawtypes"})
	void execute(Execution handlerContext,java.lang.Enum listenerType);
	
}
