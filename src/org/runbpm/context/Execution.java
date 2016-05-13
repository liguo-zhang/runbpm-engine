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

public class Execution {
	
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

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}

	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}

	public ActivityDefinition getActivityDefinition() {
		return activityDefinition;
	}

	public void setActivityDefinition(ActivityDefinition activityDefinition) {
		this.activityDefinition = activityDefinition;
	}

	public Map<String, VariableInstance> getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map<String, VariableInstance> variableMap) {
		this.variableMap = variableMap;
	}

	public SequenceFlow getTransition() {
		return transition;
	}

	public void setTransition(SequenceFlow transition) {
		this.transition = transition;
	}

	public UserTask getUserTask() {
		return userTask;
	}

	public void setUserTask(UserTask userTask) {
		this.userTask = userTask;
	}

	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}
	
}
