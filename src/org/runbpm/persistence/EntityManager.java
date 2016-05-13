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
	
	ProcessModel initProcessDefinition(ProcessDefinition processDefinition);
	
	ProcessModel initProcessDefinitionFromFile(File file);
	
	ProcessModel loadLatestProcessModel(String processDefinitionId);	
	
	ProcessModel loadProcessModelByModelId(long processModelId);
	
	ProcessModelHistory loadProcessModelHistoryByModelId(long processModelId);
	
	ProcessInstance getProcessInstance(long processInstanceId);
	
	ActivityInstance getActivityInstance(long activityInstanceId);
	List<ActivityInstance>  getActivityInstanceByProcessInstId(long processInstanceId);
	List<ActivityInstance> getActivityInstanceByProcessInstIdAndState(long processInstanceId,EnumSet<ACTIVITY_STATE> stateSet);
	List<ActivityInstance> getActivityInstanceByProcessInstIdSubrocessIdAndState(long processInstanceId,String subProcessId,EnumSet<ACTIVITY_STATE> stateSet);
	List<ActivityInstance>  getActivityInstanceByActivityDefId(long processInstanceId,String activityDefinitionId);
	List<ActivityInstance> getActivityInstanceByActivityDefIdAndState(long processInstanceId,String activityDefinitionId,EnumSet<ACTIVITY_STATE> stateSet);

	TaskInstance getTaskInstance(long taskInstanceId);
	
	List<TaskInstance> getTaskInstanceByActivityInstId(long activityInstanceId);
	
	List<TaskInstance> getTaskInstanceByProcessInstId(long processInstanceId);
	
	List<TaskInstance> getTaskInstanceByActivityInstIdAndState(long activityInstanceId,EnumSet<TASK_STATE> stateSet);
	
	List<TaskInstance> getTaskInstanceByUserIdAndState(String userId,EnumSet<TASK_STATE> stateSet);
	
	List<TaskInstance> getTaskInstanceByUserId(String userId) ;
	
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
