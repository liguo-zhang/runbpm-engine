package org.runbpm.service;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.container.ActivityContainer;
import org.runbpm.container.FlowContainer;
import org.runbpm.container.ProcessContainer;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;

public class RuntimeServiceImpl extends  AbstractRuntimeService{
	
	
	public ProcessInstance createProcessInstance(String processDefinitionId,String creator){
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processDefinitionId,creator);
		return processInstance;
	}


	public void deployProcessDefinitionFromFile(File file) {
		entityManager.deployProcessDefinitionFromFile(file);
	}
	
	@Override
	public ProcessInstance startProcessInstance(long processInstanceId) {
		ProcessInstance processInstance = entityManager.getProcessInstance(processInstanceId);
		
		FlowContainer processInstanceContainer = ProcessContainer.getFlowContainer(processInstance, null);
		processInstanceContainer.start();
		return processInstance;
	}

	@Override
	public void completeActivityInstance(long activityInstanceId) {
		ActivityInstance activityInstance = entityManager.getActivityInstance(activityInstanceId);
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance);
		
		activityContainer.complete();
		
	}


	@Override
	public ProcessInstance getProcessInstance(long processInstanceId) {
		return entityManager.getProcessInstance(processInstanceId);	
	}


	@Override
	public List<ActivityInstance> getActivityInstanceByProcessInstId(long processInstanceId) {
		return entityManager.getActivityInstanceByProcessInstId(processInstanceId);
	}


	@Override
	public List<ActivityInstance> getActivityInstanceByActivityDefId(
			long processInstanceId, String activityDefinitionId) {
		return entityManager.getActivityInstanceByActivityDefId(processInstanceId, activityDefinitionId);
	}

	@Override
	public List<ProcessInstance> getProcessInstanceByQueryString(
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
			String processDefinitionId,String creator,Map<String, Object> variableMaps) {
		ProcessContainer processContainer = ProcessContainer.getProcessContainerForNewInstance();
		ProcessInstance processInstance= processContainer.createInstance(processDefinitionId,creator);
		this.entityManager.setProcessVariableMap(processInstance.getId(), variableMaps);
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessModel initProcessDefinition(
			ProcessDefinition processDefinition) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void terminateActivityInstance(long activityInstanceId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void terminateActivityInstance(long activityInstanceId,
			ActivityDefinition targetActivityDefinition) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void terminateProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resumeProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void suspendProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resumeActivityInstance(long activityInstanceId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void suspendActivityInstance(long activityInstanceId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void claimUserTask(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void completeUserTask(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void terminateUserTask(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resumeUserTask(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void putbackUserTask(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setUserTaskAssignee(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ActivityInstance> getActivityInstanceByProcessInstIdAndState(
			long processInstanceId, EnumSet<ACTIVITY_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ActivityInstance> getActivityInstanceByProcessInstIdSubrocessIdAndState(
			long processInstanceId, String subProcessId,
			EnumSet<ACTIVITY_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ActivityInstance> getActivityInstanceByActivityDefIdAndState(
			long processInstanceId, String activityDefinitionId,
			EnumSet<ACTIVITY_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> getTaskInstanceByActivityInstId(
			long activityInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> getTaskInstanceByProcessInstId(
			long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> getTaskInstanceByActivityInstIdAndState(
			long activityInstanceId, EnumSet<TASK_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> getTaskInstanceByUserIdAndState(String userId,
			EnumSet<TASK_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> getTaskInstanceByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, VariableInstance> getVariableMap(long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessModel loadLatestProcessModel(String processDefinitionId) {
		return entityManager.loadLatestProcessModel(processDefinitionId);
	}


	@Override
	public ProcessModel getLatestProcessMode(String processDefinitionId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void suspendUserTask(long userTaskId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeUserTask(long userTaskId, boolean autoCommit) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ProcessModel> loadProcessModels(boolean reload) {
		return this.entityManager.loadProcessModels(reload);
		
	}



}
