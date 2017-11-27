package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;

public class UserTaskResourcePolicy {

	private RESOURCE_POLICY_TYPE type;
	
	public enum RESOURCE_POLICY_TYPE{single,multi};
	
	/**
	 * 获取任务分配策略，目前有两种:
	 * <ul>
	 *   <li>抢任务single：将一个节点的任务分配给符合任务分配条件的所有人，第一个执行了 {@link org.runbpm.service.RunBPMService#claimUserTask()}后,其他人任务项作废。　</li>
	 *   <li>会签multi：将一个节点的任务分配给符合任务分配条件的所有人，且任务自动处于运行 状态{@link org.runbpm.entity.EntityConstants.TASK_STATE.RUNNING}，多个人共同完成一项任务。</li>
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
