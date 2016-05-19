package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.ProcessContextBean;

/**
 * 组织机构信息接口。对于一般的的工作流分配，实现抽象方法 {@link #getUsersByGroupId(String)}即可。
 */
public abstract class AbstractGlobalResourceHandler implements GlobalResourceHandler{

	@Override
	public List<User> getUsersByGroupIdVariableMap(String groupId, ProcessContextBean processContextBean) {
		return getUsersByGroupId(groupId);
	}
	
	public abstract List<User> getUsersByGroupId(String groupId);

}
