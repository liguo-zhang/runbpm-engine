package org.runbpm.handler.resource;

import java.util.ArrayList;
import java.util.List;


public class GlobalResourceHandlerSample extends AbstractGlobalResourceHandler{

  public List<User> getUsersByGroupId(String groupId){
	  
	  List<User> userList = new ArrayList<User>();
	  if("groupA".equals(groupId)){
		  userList.add(new User("user1"));
		  userList.add(new User("user2"));
		  userList.add(new User("user3"));
	  }else if("groupB".equals(groupId)){
		  userList.add(new User("user4"));
		  userList.add(new User("user5"));
		  userList.add(new User("user6"));
	  }else if("groupC".equals(groupId)){
		  userList.add(new User("user7"));
		  userList.add(new User("user8"));
		  userList.add(new User("user9"));
	  }
	  return userList;
  }

	@Override
	public User getUser(String userId) {
		return new User(userId);
	}
  
}
