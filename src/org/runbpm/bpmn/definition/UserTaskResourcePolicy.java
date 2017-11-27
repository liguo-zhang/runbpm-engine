package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

public class UserTaskResourcePolicy {

	private RESOURCE_POLICY_TYPE type;
	
	public enum RESOURCE_POLICY_TYPE{single,multi};
	
	/**
	 * ��ȡ���������ԣ�Ŀǰ������:
	 * <ul>
	 *   <li>������single����һ���ڵ��������������������������������ˣ���һ��ִ���� {@link org.runbpm.service.RunBPMService#claimUserTask()}��,���������������ϡ���</li>
	 *   <li>��ǩmulti����һ���ڵ��������������������������������ˣ��������Զ��������� ״̬{@link org.runbpm.entity.EntityConstants.TASK_STATE.RUNNING}������˹�ͬ���һ������</li>
	 * </ul>
	 * @return
	 */
	@XmlAttribute
	public RESOURCE_POLICY_TYPE getType() {
		return type;
	}

	public void setType(RESOURCE_POLICY_TYPE type) {
		this.type = type;
	}
	
}
