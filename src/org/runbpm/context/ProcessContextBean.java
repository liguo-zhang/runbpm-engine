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
 * 所有流程插件接口的传递对象，包含当前流程执行过程中的所有对象信息。
 * 对于某次调用，可能获取的对象为空，例如对于一个流程监听事件，其Execution对象不包含活动定义信息。
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
	 * 流程定义对象信息
	 * @return 流程定义对象信息
	 */
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	/**
	 * 活动实例信息
	 * @return
	 */
	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}

	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}

	/**
	 * 活动定义信息
	 * @return
	 */
	public ActivityDefinition getActivityDefinition() {
		return activityDefinition;
	}

	public void setActivityDefinition(ActivityDefinition activityDefinition) {
		this.activityDefinition = activityDefinition;
	}

	/**
	 * 流程变量信息
	 * @return
	 */
	public Map<String, VariableInstance> getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map<String, VariableInstance> variableMap) {
		this.variableMap = variableMap;
	}

	/**
	 * 连结弧信息
	 * @return
	 */
	public SequenceFlow getSequenceFlow() {
		return transition;
	}

	public void setSequenceFlow(SequenceFlow transition) {
		this.transition = transition;
	}

	/**
	 * 任务项定义信息，同 {@link #getActivityDefinition()}。UserTask是ActivityDefinition的一个子类。
	 * @return
	 */
	public UserTask getUserTask() {
		return userTask;
	}

	public void setUserTask(UserTask userTask) {
		this.userTask = userTask;
	}

	/**
	 * 获取任务项实例信息
	 * @return
	 */
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}
	
}
