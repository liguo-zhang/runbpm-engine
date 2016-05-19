package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.ProcessContextBean;

/**
 * ��֯������Ϣ�ӿڡ�����һ��ĵĹ��������䣬ʵ�ֳ��󷽷� {@link #getUsersByGroupId(String)}���ɡ�
 */
public abstract class AbstractGlobalResourceHandler implements GlobalResourceHandler{

	@Override
	public List<User> getUsersByGroupIdVariableMap(String groupId, ProcessContextBean processContextBean) {
		return getUsersByGroupId(groupId);
	}
	
	public abstract List<User> getUsersByGroupId(String groupId);

}
