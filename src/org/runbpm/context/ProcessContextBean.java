package org.runbpm.context;

import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;

/**
 * �������̲���ӿڵĴ��ݶ��󣬰�����ǰ����ִ�й����е����ж�����Ϣ��
 * ����ĳ�ε��ã����ܻ�ȡ�Ķ���Ϊ�գ��������һ�����̼����¼�����Execution���󲻰����������Ϣ��
 */
public class ProcessContextBean {
	
	private ProcessDefinition processDefinition;
	private ProcessInstance processInstance;
	
	private ActivityDefinition  activityDefinition;
	private ActivityInstance activityInstance;
	
	private UserTask userTask;
	private TaskInstance taskInstance;
	
	private Map<String, VariableInstance> variableMap;
	private SequenceFlow transition;
	
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	/**
	 * ���̶��������Ϣ
	 * @return ���̶��������Ϣ
	 */
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	/**
	 * �ʵ����Ϣ
	 * @return
	 */
	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}

	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}

	/**
	 * �������Ϣ
	 * @return
	 */
	public ActivityDefinition getActivityDefinition() {
		return activityDefinition;
	}

	public void setActivityDefinition(ActivityDefinition activityDefinition) {
		this.activityDefinition = activityDefinition;
	}

	/**
	 * ���̱�����Ϣ
	 * @return
	 */
	public Map<String, VariableInstance> getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map<String, VariableInstance> variableMap) {
		this.variableMap = variableMap;
	}

	/**
	 * ���ỡ��Ϣ
	 * @return
	 */
	public SequenceFlow getSequenceFlow() {
		return transition;
	}

	public void setSequenceFlow(SequenceFlow transition) {
		this.transition = transition;
	}

	/**
	 * ���������Ϣ��ͬ {@link #getActivityDefinition()}��UserTask��ActivityDefinition��һ�����ࡣ
	 * @return
	 */
	public UserTask getUserTask() {
		return userTask;
	}

	public void setUserTask(UserTask userTask) {
		this.userTask = userTask;
	}

	/**
	 * ��ȡ������ʵ����Ϣ
	 * @return
	 */
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}
	
}
