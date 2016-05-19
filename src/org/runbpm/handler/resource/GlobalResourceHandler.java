package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.ProcessContextBean;


/**
 * 组织机构接口。
 *
 */
public interface GlobalResourceHandler {
	
	User getUser(String userId);	

	List<User> getUsersByGroupIdVariableMap(String groupId,
			ProcessContextBean processContextBean);

}
