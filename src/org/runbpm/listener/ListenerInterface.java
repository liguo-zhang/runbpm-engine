package org.runbpm.listener;

import org.runbpm.context.ProcessContextBean;

/**
 * 在特定的工作流事件时，触发的方法。<br>
 * 该类的具体实现定义在流程定义特定位置后，流程引擎在特定的事件处会触发该类的实例。
 *
 */
public interface ListenerInterface {

	/**
	 * 事件触发的方法
	 * @param processContextBean
	 * @param listenerType
	 */
	@SuppressWarnings(value={"rawtypes"})
	void execute(ProcessContextBean processContextBean,java.lang.Enum listenerType);
	
}
