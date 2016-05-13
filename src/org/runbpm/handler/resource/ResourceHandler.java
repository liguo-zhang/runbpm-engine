package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.Execution;

public interface ResourceHandler {

	List<User> getUsers(Execution HandlerContext);
}
