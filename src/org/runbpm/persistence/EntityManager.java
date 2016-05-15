package org.runbpm.persistence;


import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ApplicationInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelHistory;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;


public interface EntityManager {
	
	List<ProcessModel> loadProcessModels(boolean onlyLatestVersion);
	
	ProcessModel deployProcessDefinition(ProcessDefinition processDefinition);
	
	ProcessModel deployProcessDefinitionFromFile(File file);
	
	ProcessModel deployProcessDefinitionFromString(String string);
	
	ProcessModel loadLatestProcessModel(String processDefinitionId);	
	
	ProcessModel loadProcessModelByModelId(long processModelId);
	
	ProcessModelHistory loadProcessModelHistoryByModelId(long processModelId);
	
	ProcessInstance getProcessInstance(long processInstanceId);
	List<ProcessInstance> listProcessInstanceByCreator(String creator);
	
	ActivityInstance getActivityInstance(long activityInstanceId);
	List<ActivityInstance>  listActivityInstanceByProcessInstId(long processInstanceId);
	List<ActivityInstance> listActivityInstanceByProcessInstIdAndState(long processInstanceId,EnumSet<ACTIVITY_STATE> stateSet);
	List<ActivityInstance> listActivityInstanceByProcessInstIdSubrocessIdAndState(long processInstanceId,String subProcessId,EnumSet<ACTIVITY_STATE> stateSet);
	List<ActivityInstance>  listActivityInstanceByActivityDefId(long processInstanceId,String activityDefinitionId);
	List<ActivityInstance> listActivityInstanceByActivityDefIdAndState(long processInstanceId,String activityDefinitionId,EnumSet<ACTIVITY_STATE> stateSet);

	TaskInstance getTaskInstance(long taskInstanceId);
	
	List<TaskInstance> listTaskInstanceByActivityInstId(long activityInstanceId);
	
	List<TaskInstance> listTaskInstanceByProcessInstId(long processInstanceId);
	
	List<TaskInstance> listTaskInstanceByActivityInstIdAndState(long activityInstanceId,EnumSet<TASK_STATE> stateSet);
	
	List<TaskInstance> listTaskInstanceByUserIdAndState(String userId,EnumSet<TASK_STATE> stateSet);
	
	List<TaskInstance> listTaskInstanceByUserId(String userId) ;
	
	void removeTaskInstanceExceptClaimInstance(TaskInstance claimTaskInstance);
	
	void removeTaskInstance(long removeTaskInstanceId);
	
	ApplicationInstance getApplicationInstance(long applicationInstanceId);
	
	VariableInstance getVariableInstance(long processInstanceId, String name);
	
	Map<String,VariableInstance> getVariableMap(long processInstanceId);

	ProcessInstance produceProcessInstance(long processModelId);
	ActivityInstance produceActivityInstance(long processInstanceId);
	TaskInstance produceTaskInstance(long activityInstanceId,String userId);
	ApplicationInstance produceApplicationInstance();
	
	void setProcessVariable(long processInstanceId, String name, Object value);
	
	void setProcessVariableMap(long processInstanceId, Map<String, Object>dataFieldMap);
	
	void archiveProcess(ProcessInstance processInstance);
	
	 
}
