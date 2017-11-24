package org.runbpm.persistence;


import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityHistory;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ApplicationInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelHistory;
import org.runbpm.entity.TaskHistory;
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
	
	ProcessInstance loadProcessInstance(long processInstanceId);
	ProcessHistory loadProcessHistory(long processHistoryId);
	
	List<ProcessInstance> listProcessInstanceByCreator(String creator);
	List<ProcessHistory> listProcessHistoryByCreator(String creator);
	
	ActivityInstance loadActivityInstance(long activityInstanceId);
	ActivityHistory loadActivityHistory(long activityHistoryId);
	
	List<ActivityInstance>  listActivityInstanceByProcessInstId(long processInstanceId);
	List<ActivityHistory>  listActivityHistoryByProcessInstId(long processHistoryId);
	
	List<ActivityInstance> listActivityInstanceByProcessInstIdAndState(long processInstanceId,EnumSet<ACTIVITY_STATE> stateSet);
	List<ActivityInstance> listActivityInstanceByProcessInstIdSubrocessIdAndState(long processInstanceId,String subProcessId,EnumSet<ACTIVITY_STATE> stateSet);
	List<ActivityInstance>  listActivityInstanceByActivityDefId(long processInstanceId,String activityDefinitionId);
	List<ActivityHistory>  listActivityHistoryByActivityDefId(long processHistoryId,String activityDefinitionId);
	
	List<ActivityInstance> listActivityInstanceByActivityDefIdAndState(long processInstanceId,String activityDefinitionId,EnumSet<ACTIVITY_STATE> stateSet);

	TaskInstance loadTaskInstance(long taskInstanceId);
	TaskHistory loadTaskHistory(long taskHistoryId);
	
	List<TaskInstance> listTaskInstanceByActivityInstId(long activityInstanceId);
	List<TaskHistory> listTaskHistoryByActivityInstId(long activityHistoryId);
	
	List<TaskInstance> listTaskInstanceByProcessInstId(long processInstanceId);
	List<TaskHistory> listTaskHistoryByProcessInstId(long processHistoryId);
	
	List<TaskInstance> listTaskInstanceByActivityInstIdAndState(long activityInstanceId,EnumSet<TASK_STATE> stateSet);
	
	List<TaskInstance> listTaskInstanceByUserIdAndState(String userId,EnumSet<TASK_STATE> stateSet);
	
	List<TaskInstance> listTaskInstanceByUserId(String userId) ;
	
	void removeTaskInstanceExceptClaimInstance(TaskInstance claimTaskInstance);
	
	void removeTaskInstance(long removeTaskInstanceId);
	
	ApplicationInstance loadApplicationInstance(long applicationInstanceId);
	
	VariableInstance loadVariableInstance(long processInstanceId, String name);
	
	Map<String,VariableInstance> loadVariableMap(long processInstanceId);

	ProcessInstance produceProcessInstance(long processModelId);
	ActivityInstance produceActivityInstance(long processInstanceId);
	TaskInstance produceTaskInstance(long activityInstanceId,String userId);
	ApplicationInstance produceApplicationInstance();
	
	void setProcessVariable(long processInstanceId, String name, Object value);
	
	void setProcessVariableMap(long processInstanceId, Map<String, Object>dataFieldMap);
	
	void archiveProcess(ProcessInstance processInstance);
	
	 
}
