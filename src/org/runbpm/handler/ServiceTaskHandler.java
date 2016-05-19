package org.runbpm.handler;

import org.runbpm.context.ProcessContextBean;

/**
 * 当ServiceTask定义为一个Java类时，Java类需要实现的接口
 */
public interface ServiceTaskHandler {

	/**
	 * 工作流引擎运行到ServiceTask，所执行的方法
	 * @param processContextBean 流程上下文信息
	 */
	void executeService(ProcessContextBean processContextBean);
	
}
