package org.runbpm.service;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 对外公开REST服务接口。
 * 例如通过该种方式启动一个流程(已经测试通过)：<br>
 * http://localhost:9080/RunBPMWeb/createProcessInstance?processDefinitionId=Payment
 *
 */
@RestController
public class RuntimeServiceRestImpl extends  AbstractRuntimeService{

	//取org.runbpm.service.RuntimeServiceImpl
	
	private  RuntimeService runtimeService = Configuration.getContext().getRuntimeService();
	
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}


	@RequestMapping("/createProcessInstance")
	public ProcessInstance createProcessInstance(@RequestParam(value="processDefinitionId") String processDefinitionId,@RequestParam(value="creator") String creator){
		return runtimeService.createProcessInstance(processDefinitionId,creator);
	}


	@Override
	public ProcessModel deployProcessDefinitionFromFile(File file) {
		return null;
	}


	@Override
	public ProcessModel loadProcessModelByModelId(long processModelId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessModel deployProcessDefinition(
			ProcessDefinition processDefinition) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public ProcessInstance startProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessInstance createAndStartProcessInstance(
			String processDefinitionId, String creator) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessInstance createAndStartProcessInstance(
			String processDefinitionId, String creator,
			Map<String, Object> variableMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ProcessInstance> listProcessInstanceByQueryString(
			String queryString) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void completeActivityInstance(long activityInstanceId) {
		// TODO Auto-generated method stub
		
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
	public ProcessInstance getProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByProcessInstId(long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setProcessVariable(long processInstanceId, String name,
			Object value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setProcessVariableMap(long processInstanceId,
			Map<String, Object> dataFieldMap) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByProcessInstIdAndState(
			long processInstanceId, EnumSet<ACTIVITY_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByProcessInstIdSubrocessIdAndState(
			long processInstanceId, String subProcessId,
			EnumSet<ACTIVITY_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByActivityDefId(
			long processInstanceId, String activityDefinitionId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ActivityInstance> listActivityInstanceByActivityDefIdAndState(
			long processInstanceId, String activityDefinitionId,
			EnumSet<ACTIVITY_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> listTaskInstanceByActivityInstId(
			long activityInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> listTaskInstanceByProcessInstId(
			long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> listTaskInstanceByActivityInstIdAndState(
			long activityInstanceId, EnumSet<TASK_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> listTaskInstanceByUserIdAndState(String userId,
			EnumSet<TASK_STATE> stateSet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaskInstance> listTaskInstanceByUserId(String userId) {
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
		// TODO Auto-generated method stub
		return null;
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
	public List<ProcessModel> loadProcessModels(boolean reload){
		return null;
		
	}


	@Override
	public ProcessModel deployProcessDefinitionFromString(String string) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessInstance createProcessInstance(long processModelId,
			String creator) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessInstance createAndStartProcessInstance(long processModelId,
			String creator) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ProcessInstance createAndStartProcessInstance(long processModelId,
			String creator, Map<String, Object> variableMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ProcessInstance> listProcessInstanceByCreator(String creator) {
		// TODO Auto-generated method stub
		return null;
	}

}
