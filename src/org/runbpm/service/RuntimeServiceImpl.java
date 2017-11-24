package org.runbpm.service;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.ActivityOfUserTaskContainer;
import org.runbpm.container.FlowContainer;
import org.runbpm.container.ProcessContainer;
import org.runbpm.container.UserTaskContainer;
import org.runbpm.entity.ActivityHistory;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskHistory;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.resource.User;

public class RuntimeServiceImpl extends  AbstractRuntimeService{
	
	
	public ProcessInstance createProcessInstance(String processDefinitionId,String creator){
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processDefinitionId,creator);
		return processInstance;
	}


	public ProcessModel deployProcessDefinitionFromFile(File file) {
		return entityManager.deployProcessDefinitionFromFile(file);
	}
	
	@Override
	public ProcessInstance startProcessInstance(long processInstanceId) {
		ProcessInstance processInstance = entityManager.loadProcessInstance(processInstanceId);
		
		FlowContainer processInstanceContainer = ProcessContainer.getFlowContainer(processInstance, null);
		processInstanceContainer.start();
		return processInstance;
	}

	@Override
	public void completeActivityInstance(long activityInstanceId) {
		ActivityInstance activityInstance = entityManager.loadActivityInstance(activityInstanceId);
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance);
		
		activityContainer.complete();
		
	}


	@Override
	public ProcessInstance loadProcessInstance(long processInstanceId) {
		return entityManager.loadProcessInstance(processInstanceId);	
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByProcessInstId(long processInstanceId) {
		return entityManager.listActivityInstanceByProcessInstId(processInstanceId);
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByActivityDefId(
			long processInstanceId, String activityDefinitionId) {
		return entityManager.listActivityInstanceByActivityDefId(processInstanceId, activityDefinitionId);
	}

	@Override
	public List<ProcessInstance> listProcessInstanceByQueryString(
			String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance createAndStartProcessInstance(
			String processDefinitionId,String creator) {
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processDefinitionId,creator);
		processContainer.start();
		return processInstance;
	}

	@Override
	public ProcessInstance createAndStartProcessInstance(
			String processDefinitionId,String creator,Map<String, Object> variableMap) {
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processDefinitionId,creator);
		this.entityManager.setProcessVariableMap(processInstance.getId(), variableMap);
		processContainer.start();
		return processInstance;
	}
	
	public void setProcessVariable(long processInstanceId, String name, Object value){
		this.entityManager.setProcessVariable(processInstanceId, name, value);
	}
	
	public void setProcessVariableMap(long processInstanceId, Map<String, Object>dataFieldMap){
		this.entityManager.setProcessVariableMap(processInstanceId, dataFieldMap);
	}


	@Override
	public ProcessModel loadProcessModelByModelId(long processModelId) {

		return this.entityManager.loadProcessModelByModelId(processModelId);
	}


	@Override
	public ProcessModel deployProcessDefinition(
			ProcessDefinition processDefinition) {
		return  this.entityManager.deployProcessDefinition(processDefinition);
	}


	@Override
	public void terminateActivityInstance(long activityInstanceId) {

		ActivityInstance activityInstance = this.entityManager.loadActivityInstance(activityInstanceId);
		ActivityContainer activityContainer= ActivityContainer.getActivityContainer(activityInstance);
		activityContainer.terminate();
		
	}


	@Override
	public void terminateActivityInstance(long activityInstanceId,
			String targetActivityDefinitionId) {
		ActivityInstance activityInstance = this.entityManager.loadActivityInstance(activityInstanceId);
		ActivityContainer activityContainer= ActivityContainer.getActivityContainer(activityInstance);
		
		ProcessModel processModel =this.entityManager.loadProcessModelByModelId(activityInstance.getProcessModelId());
		
		ActivityDefinition targetActivityDefinition = processModel.getProcessDefinition().getActivity(targetActivityDefinitionId);
		activityContainer.terminate(targetActivityDefinition);
	}


	@Override
	public void terminateProcessInstance(long processInstanceId) {


		ProcessInstance processInstance = entityManager.loadProcessInstance(processInstanceId);
		
		FlowContainer processInstanceContainer = ProcessContainer.getFlowContainer(processInstance, null);
		processInstanceContainer.terminate();
		
	}


	@Override
	public void resumeProcessInstance(long processInstanceId) {

		ProcessInstance processInstance = entityManager.loadProcessInstance(processInstanceId);
		
		FlowContainer processInstanceContainer = ProcessContainer.getFlowContainer(processInstance, null);
		processInstanceContainer.resume();		
	}


	@Override
	public void suspendProcessInstance(long processInstanceId) {
		ProcessInstance processInstance = entityManager.loadProcessInstance(processInstanceId);
		
		FlowContainer processInstanceContainer = ProcessContainer.getFlowContainer(processInstance, null);
		processInstanceContainer.suspend();
	}


	@Override
	public void resumeActivityInstance(long activityInstanceId) {
		ActivityInstance activityInstance = this.entityManager.loadActivityInstance(activityInstanceId);
		ActivityContainer activityContainer= ActivityContainer.getActivityContainer(activityInstance);
		activityContainer.resume();
		
	}


	@Override
	public void suspendActivityInstance(long activityInstanceId) {

		ActivityInstance activityInstance = this.entityManager.loadActivityInstance(activityInstanceId);
		ActivityContainer activityContainer= ActivityContainer.getActivityContainer(activityInstance);
		activityContainer.suspend();
		
	}


	@Override
	public void claimUserTask(long userTaskId) {
		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.claim();
	}


	@Override
	public void completeUserTask(long userTaskId) {

		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.complete();
		
	}


	@Override
	public void terminateUserTask(long userTaskId) {

		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.terminate();
		
	}


	@Override
	public void resumeUserTask(long userTaskId) {

		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.resume();
		
	}


	@Override
	public void putbackUserTask(long userTaskId) {

		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.putBack();
		
	}


	@Override
	public void setUserTaskAssignee(long userTaskId,String userId) {
		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.setAssignee(userId);
		
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByProcessInstIdAndState(
			long processInstanceId, EnumSet<ACTIVITY_STATE> stateSet) {

		return this.entityManager.listActivityInstanceByProcessInstIdAndState(processInstanceId, stateSet);
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByProcessInstIdSubrocessIdAndState(
			long processInstanceId, String subProcessId,
			EnumSet<ACTIVITY_STATE> stateSet) {

		return this.entityManager.listActivityInstanceByProcessInstIdSubrocessIdAndState(processInstanceId, subProcessId, stateSet);
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByActivityDefIdAndState(
			long processInstanceId, String activityDefinitionId,
			EnumSet<ACTIVITY_STATE> stateSet) {

		return this.entityManager.listActivityInstanceByActivityDefIdAndState(processInstanceId, activityDefinitionId, stateSet);
	}


	@Override
	public List<TaskInstance> listTaskInstanceByActivityInstId(
			long activityInstanceId) {

		return this.entityManager.listTaskInstanceByActivityInstId(activityInstanceId);
	}


	@Override
	public List<TaskInstance> listTaskInstanceByProcessInstId(
			long processInstanceId) {
		return this.entityManager.listTaskInstanceByProcessInstId(processInstanceId);
	}


	@Override
	public List<TaskInstance> listTaskInstanceByActivityInstIdAndState(
			long activityInstanceId, EnumSet<TASK_STATE> stateSet) {
		return this.entityManager.listTaskInstanceByActivityInstIdAndState(activityInstanceId,stateSet);
	}


	@Override
	public List<TaskInstance> listTaskInstanceByUserIdAndState(String userId,
			EnumSet<TASK_STATE> stateSet) {
		return this.entityManager.listTaskInstanceByUserIdAndState(userId, stateSet);
	}


	@Override
	public List<TaskInstance> listTaskInstanceByUserId(String userId) {

		return this.entityManager.listTaskInstanceByUserId(userId);
	}


	@Override
	public Map<String, VariableInstance> loadVariableMap(long processInstanceId) {
		return this.entityManager.loadVariableMap(processInstanceId);
	}


	@Override
	public ProcessModel loadLatestProcessModel(String processDefinitionId) {
		return entityManager.loadLatestProcessModel(processDefinitionId);
	}


	@Override
	public ProcessModel loadLatestProcessMode(String processDefinitionId) {

		return this.entityManager.loadLatestProcessModel(processDefinitionId);
	}


	@Override
	public void suspendUserTask(long userTaskId) {
		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.suspend();
		
	}




	@Override
	public List<ProcessModel> loadProcessModels(boolean onlyLatestVersion) {
		return this.entityManager.loadProcessModels(onlyLatestVersion);
		
	}


	@Override
	public ProcessModel deployProcessDefinitionFromString(String string) {
		return this.entityManager.deployProcessDefinitionFromString(string);
	}


	@Override
	public ProcessInstance createProcessInstance(long processModelId,
			String creator) {
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processModelId,creator);
		return processInstance;
	}


	@Override
	public ProcessInstance createAndStartProcessInstance(long processModelId,
			String creator) {
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processModelId,creator);
		processContainer.start();
		return processInstance;
	}


	@Override
	public ProcessInstance createAndStartProcessInstance(long processModelId,
			String creator, Map<String, Object> variableMap) {

		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processModelId,creator);
		this.entityManager.setProcessVariableMap(processInstance.getId(), variableMap);
		processContainer.start();
		return processInstance;
	}


	@Override
	public List<ProcessInstance> listProcessInstanceByCreator(String creator) {
		return this.entityManager.listProcessInstanceByCreator(creator);
	}


	@Override
	public ActivityInstance loadActivityInstance(long activityInstanceId) {
		return this.entityManager.loadActivityInstance(activityInstanceId);
	}


	@Override
	public TaskInstance loadTaskInstance(long taskInstanceId) {
		return this.entityManager.loadTaskInstance(taskInstanceId);
	}


	@Override
	public List<ProcessHistory> listProcessHistoryByCreator(String creator) {
		return this.entityManager.listProcessHistoryByCreator(creator);
	}


	@Override
	public ProcessHistory loadProcessHistory(long processHistoryId) {
		return this.entityManager.loadProcessHistory(processHistoryId);
	}


	@Override
	public ActivityHistory loadActivityHistory(long activityHistoryId) {
		return this.entityManager.loadActivityHistory(activityHistoryId);
	}


	@Override
	public TaskHistory loadTaskHistory(long taskHistoryId) {
		return this.entityManager.loadTaskHistory(taskHistoryId);
	}


	@Override
	public List<ActivityHistory> listActivityHistoryByProcessInstId(long processHistoryId) {
		return this.entityManager.listActivityHistoryByProcessInstId(processHistoryId);
	}


	@Override
	public List<ActivityHistory> listActivityHistoryByActivityDefId(long processHistoryId,
			String activityDefinitionId) {
		return this.entityManager.listActivityHistoryByActivityDefId(processHistoryId,activityDefinitionId);
	}


	@Override
	public List<TaskHistory> listTaskHistoryByProcessInstId(long processHistoryId) {
		return this.entityManager.listTaskHistoryByProcessInstId(processHistoryId);
	}


	@Override
	public List<TaskHistory> listTaskHistoryByActivityInstId(long activityHistoryId) {
		return this.entityManager.listTaskHistoryByActivityInstId(activityHistoryId);

	}


	@Override
	public Set<ActivityDefinition> listReachableActivitySet(long activityInstanceId) {
		ActivityInstance activityInstance = this.entityManager.loadActivityInstance(activityInstanceId);
		ProcessInstance processInstance = this.entityManager.loadProcessInstance(activityInstance.getProcessInstanceId());
		FlowContainer processContainer = ProcessContainer.getFlowContainer(processInstance, activityInstance);
		
		ProcessModel processModel = this.entityManager.loadProcessModelByModelId(activityInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ActivityDefinition activityDefinition = processDefinition.getActivityMap().get(activityInstance.getActivityDefinitionId());
		
		Set<ActivityDefinition> set =processContainer.listReachableActivitySet(activityInstance, activityDefinition);
		return set;
	}


	@Override
	public void cancelUserTask(long userTaskId) {
		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.cancel();
		
	}


	@Override
	public void removeUserTask(long userTaskId) {
		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.remove();
	}


	@Override
	public void setAssignee(long userTaskId,String userId) {
		TaskInstance taskInstance = this.entityManager.loadTaskInstance(userTaskId);
		UserTaskContainer userTaskContainer = UserTaskContainer.getUserTaskContainer(taskInstance);
		userTaskContainer.setAssignee(userId);
	}


	@Override
	public void addUserTask(long activityInstanceId, String userId,EntityConstants.TASK_STATE state) {
		ActivityInstance activityInstance = this.entityManager.loadActivityInstance(activityInstanceId);
		ActivityContainer activityContainer= ActivityContainer.getActivityContainer(activityInstance);
		if(activityContainer instanceof ActivityOfUserTaskContainer) {
			ActivityOfUserTaskContainer activityOfUserTaskContainer = (ActivityOfUserTaskContainer)activityContainer;
			activityOfUserTaskContainer.addUserTask(new User(userId), state);
		}else {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020007_Cannot_addUser_Task_for_NOT_UserTask,"»î¶¯ÊµÀýID£º"+activityInstanceId);
		}
		
	}


}
