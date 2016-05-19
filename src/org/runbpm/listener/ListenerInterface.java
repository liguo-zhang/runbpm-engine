package org.runbpm.listener;

import org.runbpm.context.ProcessContextBean;

/**
 * ���ض��Ĺ������¼�ʱ�������ķ�����<br>
 * ����ľ���ʵ�ֶ��������̶����ض�λ�ú������������ض����¼����ᴥ�������ʵ����
 *
 */
public interface ListenerInterface {

	/**
	 * �¼������ķ���
	 * @param processContextBean
	 * @param listenerType
	 */
	@SuppressWarnings(value={"rawtypes"})
	void execute(ProcessContextBean processContextBean,java.lang.Enum listenerType);
	
}
