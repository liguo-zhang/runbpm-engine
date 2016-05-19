package org.runbpm.handler;

import org.runbpm.context.ProcessContextBean;


/**
 * 连结弧判断的接口，可以设置该接口的实现类作为连结弧条件是否成立的判断条件输入
 *
 */
public interface DecisionHandler {

	/**
	 * 判断当前连结弧在具体的流程实例运转时是否成立
	 * @param processContextBean 流程上下文信息
	 * @return
	 */
	boolean evalConditionContext(ProcessContextBean processContextBean);
	
}
