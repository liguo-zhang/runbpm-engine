package org.runbpm.handler.resource;

import java.util.List;

import org.runbpm.context.ProcessContextBean;

/**
 * 如果任务分配指定的是一个类，则该类需要实现该接口。
 * 将具体的类以及接口配置在流程定义之后，流程引擎运行到具体的任务分配时，会自动调用该类的实现。
 *
 */
public interface ResourceHandler {

	/**
	 * 工作流引擎在执行分配时，所调用的方法
	 * @param processContextBean 流程上下文信息
	 * @return 人员列表，工作流引擎会根据该返回值创建任务项，一个人和一个任务项一一对应。
	 */
	List<User> getUsers(ProcessContextBean processContextBean);
}
