package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.ProcessContextBean;


/**
 * ��֯�����ӿڡ�
 *
 */
public interface GlobalResourceHandler {
	
	User getUser(String userId);	

	List<User> getUsersByGroupIdVariableMap(String groupId,
			ProcessContextBean processContextBean);

}
