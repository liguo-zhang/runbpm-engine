package org.runbpm.spring.usertask;

import java.util.ArrayList;
import java.util.List;

import org.runbpm.context.ProcessContextBean;
import org.runbpm.handler.resource.ResourceHandler;
import org.runbpm.handler.resource.User;

public class SpecialResourceHandlerSample implements ResourceHandler{
	
	public List<User> getUsers(ProcessContextBean processContextBean){
		List<User> userList = new ArrayList<User>();
		userList.add(new User("user8"));
		userList.add(new User("user9"));
		return userList;
	}

}