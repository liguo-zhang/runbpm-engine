package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.Execution;


public interface GlobalResourceHandler {
	
	User getUser(String userId);	

	List<User> getUsersByGroupIdVariableMap(String groupId,
			Execution handlerContext);

}
