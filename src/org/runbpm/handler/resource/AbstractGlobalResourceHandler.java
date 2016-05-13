package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.Execution;

public abstract class AbstractGlobalResourceHandler implements GlobalResourceHandler{

	@Override
	public List<User> getUsersByGroupIdVariableMap(String groupId, Execution handlerContext) {
		return getUsersByGroupId(groupId);
	}
	
	public abstract List<User> getUsersByGroupId(String groupId);

}
