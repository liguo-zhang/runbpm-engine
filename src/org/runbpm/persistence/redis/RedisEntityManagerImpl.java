package org.runbpm.persistence.redis;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.Activity_;
import org.runbpm.entity.ApplicationInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.ACTIVITY_TYPE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelHistory;
import org.runbpm.entity.ProcessModel_;
import org.runbpm.entity.Process_;
import org.runbpm.entity.TaskHistory;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.Task_;
import org.runbpm.entity.VariableHistory;
import org.runbpm.entity.VariableInstance;
import org.runbpm.entity.Variable_;
import org.runbpm.persistence.AbstractEntityManager;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.RunBPMUtils;
import org.springframework.beans.BeanUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisEntityManagerImpl extends AbstractEntityManager{

	public static final String domain_mapping_model_proc = "rb:mapmp";
	public static final String domain_mapping_proc_act = "rb:mappa";
	public static final String domain_mapping_proc_val = "rb:mappv";
	public static final String domain_mapping_act_task = "rb:mapat";
	public static final String domain_mapping_user_task = "rb:maput";
	public static final String domain_mapping_task_app = "rb:mapta";
	
	public static final String domain_mapping_model_proc_hist = "rb:mapmp:hi";
	public static final String domain_mapping_proc_act_hist = "rb:mappa:hi";
	public static final String domain_mapping_proc_val_hist = "rb:mappv:hi";
	public static final String domain_mapping_act_task_hist = "rb:mapat:hi";
	public static final String domain_mapping_user_task_hist = "rb:maput:hi";
	public static final String domain_mapping_task_app_hist = "rb:mapta:hi";
	
	public static final String domain_procmodel = "rb:procmodel";
	public static final String domain_procmodel_hist = "rb:procmodel:hi";
	
	public static final String procmodel_id = "id";
	public static final String procmodel_name = "name";
	public static final String procmodel_crdate = "crdate";
	public static final String procmodel_mddate = "mddate";
	public static final String procmodel_proddefid = "proddefid";
	public static final String procmodel_xmlcontent = "xmlcontent";
	
	public static final String domain_proc_inst = "rb:proc:in";
	public static final String domain_proc_hist = "rb:proc:hi";
	
	public static final String proc_id = "id";
	public static final String proc_crdate = "crdate";
	public static final String proc_mddate = "mddate";
	public static final String proc_modelid = "modelid";
	public static final String proc_procdefid = "procdefid";
	public static final String proc_creator = "creator";
	public static final String proc_pareactinstid = "pareactinstid";
	public static final String proc_name = "name";
	public static final String proc_desc = "desc";
	public static final String proc_state = "state";
	public static final String proc_cldate = "cldate";
	public static final String proc_stbfsp = "stbfsp";
	public static final String proc_keya = "keya";
	public static final String proc_keyb = "keyb";
	public static final String proc_keyc = "keyc";
	public static final String proc_keyd = "keyd";
	public static final String proc_keye = "keye";
	public static final String proc_keyf = "keyf";
	public static final String proc_keyg = "keyg";
	public static final String proc_keyh = "keyh";
	
	
	public static final String domain_act_inst = "rb:act:in";
	public static final String domain_act_hist = "rb:act:hi";
	
	public static final String act_id = "id";
	public static final String act_crdate = "crdate";
	public static final String act_mddate = "mddate";
	public static final String act_modelid = "modelid";
	public static final String act_procdefid = "procdefid";
	public static final String act_actcdefid = "actdefid";
	public static final String act_sqblockId = "sqblockId";
	public static final String act_subprocblockId = "subprocblockId";
	public static final String act_procinstid = "procinstid";
	public static final String act_pareactinstid = "pareactinstid";
	public static final String act_name = "name";
	public static final String act_type = "type";
	public static final String act_desc = "desc";
	public static final String act_state = "state";
	public static final String act_cldate = "cldate";
	public static final String act_stbfsp = "stbfsp";
	public static final String act_callprocid = "calprocid";
	public static final String act_keya = "keya";
	public static final String act_keyb = "keyb";
	public static final String act_keyc = "keyc";
	public static final String act_keyd = "keyd";
	public static final String act_keye = "keye";
	public static final String act_keyf = "keyf";
	public static final String act_keyg = "keyg";
	public static final String act_keyh = "keyh";
	
	public static final String domain_task_inst = "rb:task:in";
	public static final String domain_task_hist = "rb:task:hi";
	
	public static final String task_id = "id";
	public static final String task_crdate = "crdate";
	public static final String task_mddate = "mddate";
	public static final String task_modelid = "modelid";
	public static final String task_procdefid = "procdefid";
	public static final String task_actcdefid = "actdefid";
	public static final String task_procinstid = "procinstid";
	public static final String task_actinstid = "actinstid";
	public static final String task_name = "name";
	//public static final String task_type = "type";
	public static final String task_desc = "desc";
	public static final String task_state = "state";
	public static final String task_cldate = "cldate";
	public static final String task_stbfsp = "stbfsp";
	public static final String task_type = "type";
	public static final String task_userid = "userid";
	public static final String task_keya = "keya";
	public static final String task_keyb = "keyb";
	public static final String task_keyc = "keyc";
	public static final String task_keyd = "keyd";
	public static final String task_keye = "keye";
	public static final String task_keyf = "keyf";
	public static final String task_keyg = "keyg";
	public static final String task_keyh = "keyh";
	
	public static final String domain_val_inst = "rb:val:in";
	public static final String domain_val_hist = "rb:val:hi";
	
	public static final String val_id = "id";
	public static final String val_crdate = "crdate";
	public static final String val_mddate = "mddate";
	public static final String val_procdefid = "procdefid";
	public static final String val_procinstid = "procinstid";
	public static final String val_name = "name";
	public static final String val_strvalue = "strvalue";
	public static final String val_valuestr = "valuestr";
	public static final String val_desc = "desc";
	public static final String val_type = "type";

	public ShardedJedis getSharedJedis(){;
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		return shardedJedis; 
	}
	
	@Override
	public ProcessModel initProcessDefinitionFromFile(File file) {
		
		ProcessModel processModel = initProcessDefinitionFromFile_(file);
		
		return saveProcess(processModel);
	}
	
	@Override
	public ProcessModel initProcessDefinition(ProcessDefinition processDefinition) {
		ProcessModel processModel = initProcessDefinition_(processDefinition);
		return saveProcess(processModel);
	}	
	
	private ProcessModel saveProcess(ProcessModel processModel) {
		ShardedJedis shardedJedis = getSharedJedis();
		Long processModelId = shardedJedis.incr(domain_procmodel);
		
		processModel.setId(processModelId);
		Date now = new Date();
		processModel.setCreateDate(now);
		processModel.setModifyDate(now);
		
		Map<String, String> valueMap = new HashMap<String,String>();
		valueMap.put(procmodel_proddefid, processModel.getId()+"");
		valueMap.put(procmodel_xmlcontent, processModel.getXmlcontent());
		valueMap.put(procmodel_crdate, processModel.getCreateDate().getTime()+"");
		valueMap.put(procmodel_mddate, processModel.getModifyDate().getTime()+"");
		
		String domain = getDomainProcModel(processModelId.toString());
		shardedJedis.hmset(domain, valueMap);
		
		processModelMap.put(processModelId, processModel);
		return processModel;
	}

	@Override
	public ProcessInstance getProcessInstance(long processInstanceId) {
		
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainProcInst(processInstanceId+"");
		Map<String,String> valueMap= shardedJedis.hgetAll(domain);
		if(valueMap!=null&&(!valueMap.isEmpty())){
			Process_ processEntity = convertMapToProcessEntity(processInstanceId,valueMap);
			
			RedisProcessInstance processInstance = new RedisProcessInstance();
			processInstance.setId(processInstanceId);
			
			processInstance.setFlushRedis(false);
			BeanUtils.copyProperties(processEntity, processInstance);
			processInstance.setFlushRedis(true);
			return processInstance;	
		}else{
			return null;
		}
	}

	private  Process_ convertMapToProcessEntity(long processInstanceId,Map<String,String> valueMap) {
		Process_ process_ = new Process_();
		process_.setId(processInstanceId);
		process_.setName(valueMap.get(proc_name));
		process_.setCreateDate(RunBPMUtils.convertLongToDate(valueMap.get(proc_crdate)));
		process_.setModifyDate(RunBPMUtils.convertLongToDate(valueMap.get(proc_mddate)));
		process_.setProcessModelId(Long.parseLong(valueMap.get(proc_modelid)));
		process_.setProcessDefinitionId(valueMap.get(proc_procdefid));
		process_.setCreator(valueMap.get(proc_creator));
		process_.setParentActivityInstanceId(RunBPMUtils.parseLong(valueMap.get(proc_pareactinstid)));
		process_.setDescription(valueMap.get(proc_desc));
		process_.setState(PROCESS_STATE.getState(valueMap.get(proc_state)));
		process_.setCompleteDate(RunBPMUtils.convertLongToDate(valueMap.get(proc_cldate)));
		process_.setStateBeforeSuspend(PROCESS_STATE.getState(valueMap.get(proc_stbfsp)));
		return process_;
	}



	@Override
	public ActivityInstance getActivityInstance(long activityInstanceId) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainActInst(activityInstanceId+"");
		Map<String,String> valueMap= shardedJedis.hgetAll(domain);
		if(valueMap!=null&&(!valueMap.isEmpty())){
			Activity_ activityEntity = convertMapToActivityEntity(activityInstanceId,valueMap);
			
			RedisActivityInstance activityInstance= new RedisActivityInstance();
			activityInstance.setId(activityInstanceId);
			
			activityInstance.setFlushRedis(false);
			BeanUtils.copyProperties(activityEntity, activityInstance);
			activityInstance.setFlushRedis(true);
			return activityInstance;
		}else{
			return null;
		}
	}


	private Activity_ convertMapToActivityEntity(long activityInstanceeId,
			Map<String, String> valueMap) {
		Activity_ activity_ = new Activity_();
		activity_.setId(activityInstanceeId);
		activity_.setName(valueMap.get(act_name));
		activity_.setCreateDate(RunBPMUtils.convertLongToDate(valueMap.get(act_crdate)));
		activity_.setModifyDate(RunBPMUtils.convertLongToDate(valueMap.get(act_mddate)));
		activity_.setProcessModelId(Long.parseLong(valueMap.get(act_modelid)));
		activity_.setProcessDefinitionId(valueMap.get(act_procdefid));
		activity_.setActivityDefinitionId(valueMap.get(act_actcdefid));
		activity_.setSequenceBlockId(valueMap.get(act_sqblockId));
		activity_.setSubProcessBlockId(valueMap.get(act_subprocblockId));
		activity_.setProcessInstanceId(Long.parseLong(valueMap.get(act_procinstid)));
		activity_.setParentActivityInstanceId(RunBPMUtils.parseLong(valueMap.get(act_pareactinstid)));
		activity_.setDescription(valueMap.get(act_desc));
		activity_.setType(ACTIVITY_TYPE.getTypeByString(valueMap.get(act_type)));
		activity_.setState(ACTIVITY_STATE.getState(valueMap.get(act_state)));
		activity_.setCompleteDate(RunBPMUtils.convertLongToDate(valueMap.get(act_cldate)));
		activity_.setStateBeforeSuspend(ACTIVITY_STATE.getState(valueMap.get(act_stbfsp)));
		activity_.setCallActivityProcessInstanceId(RunBPMUtils.parseLong(valueMap.get(act_callprocid)));
		return activity_;
	}


	@Override
	public List<ActivityInstance> getActivityInstanceByProcessInstId(long processInstanceId) {
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainMappingProcAct(processInstanceId+"");
		Set<String> set = shardedJedis.smembers(domain);
		for(String activityInstanceId:set){
			activityList.add(getActivityInstance(Long.parseLong(activityInstanceId)));
		}
		return activityList;
	}



	@Override
	public TaskInstance getTaskInstance(long taskInstanceId) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainTaskInst(taskInstanceId+"");
		Map<String,String> valueMap= shardedJedis.hgetAll(domain.toString());
		if(valueMap!=null&&(!valueMap.isEmpty())){
			Task_ taskEntity = convertMapToTaskEntity(taskInstanceId,valueMap);
			
			RedisTaskInstance taskInstance= new RedisTaskInstance();
			taskInstance.setId(taskInstanceId);
			
			taskInstance.setFlushRedis(false);
			BeanUtils.copyProperties(taskEntity, taskInstance);
			taskInstance.setFlushRedis(true);
			return taskInstance;	
		}else{
			return null;
		}
	}



	private Task_ convertMapToTaskEntity(long taskInstanceId,
			Map<String, String> valueMap) {
		
		Task_ task_ = new Task_();
		task_.setId(taskInstanceId);
		task_.setName(valueMap.get(task_name));
		
		task_.setCreateDate(RunBPMUtils.convertLongToDate(valueMap.get(task_crdate)));
		task_.setModifyDate(RunBPMUtils.convertLongToDate(valueMap.get(task_mddate)));
		
		task_.setProcessModelId(Long.parseLong(valueMap.get(task_modelid)));
		task_.setProcessDefinitionId(valueMap.get(task_procdefid));
		task_.setActivityDefinitionId(valueMap.get(task_actcdefid));
		
		task_.setProcessInstanceId(Long.parseLong(valueMap.get(task_procinstid)));
		
		task_.setDescription(valueMap.get(task_desc));
		
		task_.setState(TASK_STATE.getState(valueMap.get(task_state)));
		task_.setCompleteDate(RunBPMUtils.convertLongToDate(valueMap.get(task_cldate)));
		task_.setStateBeforeSuspend(TASK_STATE.getState(valueMap.get(task_stbfsp)));
		task_.setUserId(valueMap.get(task_userid));
		return task_;
	}



	@Override
	public List<TaskInstance> getTaskInstanceByActivityInstId(
			long activityInstanceId) {
		List<TaskInstance> taskList = new ArrayList<TaskInstance>();
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainMappingActTask(activityInstanceId+"");
		Set<String> set = shardedJedis.smembers(domain.toString());
		for(String taskId:set){
			taskList.add(getTaskInstance(Long.parseLong(taskId)));
		}
		return taskList;
	}

	@Override
	public List<TaskInstance> getTaskInstanceByUserId(String userId) {
		List<TaskInstance> taskList = new ArrayList<TaskInstance>();
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainMappingUserTask(userId);
		Set<String> set = shardedJedis.smembers(domain.toString());
		for(String taskId:set){
			taskList.add(getTaskInstance(Long.parseLong(taskId)));
		}
		return taskList;
	}

	
	@Override
	public void removeTaskInstanceExceptClaimInstance(
			TaskInstance claimTaskInstance) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		
		List<TaskInstance> list = getTaskInstanceByActivityInstId(claimTaskInstance.getActivityInstanceId());
		for (TaskInstance taskInstance : list) {
			if(taskInstance.getId()!=claimTaskInstance.getId()){
				String domain = getDomainTaskInst(taskInstance.getId()+"");
				shardedJedis.del(domain);
			}
		}
	}

	
	@Override
	public void removeTaskInstance(long removeTaskInstaceId) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainTaskInst(removeTaskInstaceId+"");
		shardedJedis.del(domain);
	}

	@Override
	public ApplicationInstance getApplicationInstance(long applicationInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}


	public VariableInstance getVariable(long variableInstanceId) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainValInst(variableInstanceId+"");
		Map<String,String> valueMap= shardedJedis.hgetAll(domain.toString());
		if(valueMap!=null&&(!valueMap.isEmpty())){
			Variable_ variable_ = convertMapToVariable(variableInstanceId,valueMap);
			
			RedisVariableInstance variableInstance = new RedisVariableInstance();
			variableInstance.setId(variableInstanceId);
			
			variableInstance.setFlushRedis(false);
			BeanUtils.copyProperties(variable_, variableInstance);
			variableInstance.setFlushRedis(true);
			return variableInstance;	
		}else{
			return null;
		}
	}
	
	private Variable_ convertMapToVariable(long variableInstanceId,Map<String, String> valueMap) {
		Variable_ variable_ = new Variable_();
		variable_.setId(variableInstanceId);
		variable_.setName(valueMap.get(val_name));
		variable_.setValueString(valueMap.get(val_strvalue));
		
		variable_.setCreateDate(RunBPMUtils.convertLongToDate(valueMap.get(val_crdate)));
		variable_.setModifyDate(RunBPMUtils.convertLongToDate(valueMap.get(val_mddate)));
		
		variable_.setProcessDefinitionId(valueMap.get(val_procdefid));
		variable_.setProcessInstanceId(Long.parseLong(valueMap.get(val_procinstid)));
		variable_.setDescription(valueMap.get(val_desc));
		
		Object value = null;
		if(variable_.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Boolean)){
			value = Boolean.parseBoolean(variable_.getValueString());
		}else if(variable_.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Double)){
			 value = Double.parseDouble(variable_.getValueString());
		}else if(variable_.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Float)){
			 value = Float.parseFloat(variable_.getValueString());
		}else if(variable_.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Integer)){
			 value = Integer.parseInt(variable_.getValueString());
		}else if(variable_.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Long)){
			 value = Long.parseLong(variable_.getValueString());
		}else if(variable_.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_String)){
			 value = variable_.getValueString().toString();
		}
		variable_.setValue(value);
		
		return variable_;
	}



	@Override
	public Map<String, VariableInstance> getVariableMap(long processInstanceId) {
		Map<String, VariableInstance> variableInstanceMap = new HashMap<String, VariableInstance> ();
		
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainMappingProcVal(processInstanceId+"");
		Set<String> set = shardedJedis.smembers(domain);
		for(String valId:set){
			VariableInstance variableInstance = getVariable(Long.parseLong(valId));
			variableInstanceMap.put(variableInstance.getName(), variableInstance);
		}
		return variableInstanceMap;	
	}



	@Override
	public ProcessInstance produceProcessInstance(long processModelId) {
		ProcessInstance processInstance= new RedisProcessInstance();
		
		ShardedJedis shardedJedis = getSharedJedis();
		Long id = shardedJedis.incr(domain_proc_inst);
		processInstance.setId(id);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(proc_id, id+"");
		
		String domain = getDomainProcInst(id+"");
		shardedJedis.hmset(domain, map);
		
		String mapKey = getDomainMappingModelProc(processModelId+"");
		shardedJedis.sadd(mapKey.toString(), id.toString()+"");
		
		return processInstance;
	}



	@Override
	public ActivityInstance produceActivityInstance(long processInstanceId) {
		ActivityInstance activityInstance = new RedisActivityInstance();
		
		//incr and setid
		ShardedJedis shardedJedis = getSharedJedis();
		Long id = shardedJedis.incr(domain_act_inst);
		activityInstance.setId(id);
		activityInstance.setProcessInstanceId(processInstanceId);

		//add to jedis
		Map<String,String> map = new HashMap<String,String>();
		map.put(act_id, id+"");
		String domain = getDomainActInst(id+"");
		shardedJedis.hmset(domain, map);
		
		//add to process-activitymap
		String mapKey = getDomainMappingProcAct(processInstanceId+"");
		shardedJedis.sadd(mapKey.toString(), id.toString()+"");
		
		return activityInstance;
	}



	@Override
	public TaskInstance produceTaskInstance(long activityInstanceId,String userId) {
		TaskInstance taskInstance = new RedisTaskInstance();
		
		ShardedJedis shardedJedis = getSharedJedis();
		Long id = shardedJedis.incr(domain_task_inst);
		taskInstance.setId(id);
		taskInstance.setActivityInstanceId(activityInstanceId);
		
		//add to jedis
		Map<String,String> map = new HashMap<String,String>();
		map.put(task_id, id+"");
		String domain = getDomainTaskInst(id+"");
		shardedJedis.hmset(domain, map);
		
		
		String mapKey = getDomainMappingActTask(activityInstanceId+"");
		shardedJedis.sadd(mapKey, id.toString()+"");
		
		String mapKeyUserTask = getDomainMappingUserTask(activityInstanceId+"");
		shardedJedis.sadd(mapKeyUserTask.toString(), id.toString()+"");
		
		return taskInstance;
	}



	@Override
	public ApplicationInstance produceApplicationInstance() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setProcessVariable(long processInstanceId, String name,
			Object value) {
		ShardedJedis shardedJedis = getSharedJedis();
		Map<String, VariableInstance> variableInstanceMap = getVariableMap(processInstanceId);
		
		VariableInstance variableInstance = variableInstanceMap.get(name);
		if(variableInstance==null){
			variableInstance = new RedisVariableInstance();
			
			Long id = shardedJedis.incr(domain_val_inst);
			variableInstance.setId(id);

			//add to jedis
			String domain = getDomainValInst(variableInstance.getId()+"");
			Map<String,String> map = new HashMap<String,String>();
			map.put(val_id, id+"");
			shardedJedis.hmset(domain, map);
			
			//关联流程
			String mapKeyProcVal = getDomainMappingProcVal(processInstanceId+"");
			shardedJedis.sadd(mapKeyProcVal, id.toString()+"");
			
			variableInstance.setProcessInstanceId(processInstanceId);
			variableInstance.setName(name);
			variableInstance.setValueString(value.toString());
			if(value instanceof Boolean){
				variableInstance.setType(EntityConstants.VARIABLE_TYPE.Basic_Boolean);
			}else if(value instanceof String){
				variableInstance.setType(EntityConstants.VARIABLE_TYPE.Basic_String);
			}else if(value instanceof Double){
				variableInstance.setType(EntityConstants.VARIABLE_TYPE.Basic_Double);
			} else if(value instanceof Float){
				variableInstance.setType(EntityConstants.VARIABLE_TYPE.Basic_Float);
			} else if(value instanceof Integer){
				variableInstance.setType(EntityConstants.VARIABLE_TYPE.Basic_Integer);
			} else if(value instanceof Long){
				variableInstance.setType(EntityConstants.VARIABLE_TYPE.Basic_Long);
			}
		}else{
			variableInstance.setValueString(value.toString());			
		}
	}



	@Override
	public void archiveProcess(ProcessInstance processInstance) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		//copy ProcessModel
		ProcessModel processModel = loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ProcessModelHistory processModelHistory =  loadProcessModelHistoryByModelId(processModel.getId());
		if(processModelHistory==null){
			processModelHistory = new RedisProcessModelHistory();
			processModelHistory.setId(processModel.getId());
			//add to jedis
			
			String procModelHistdomain= getDomainProcModelHist(processModel.getId()+"");
			Map<String,String> procModelHistMap = new HashMap<String,String>();
			procModelHistMap.put(procmodel_id, processModel.getId()+"");
			
			shardedJedis.hmset(procModelHistdomain, procModelHistMap);
			
			//auto set in Redis
			BeanUtils.copyProperties(processModel, processModelHistory);
			
		}
		
		//copy ProcessHistory
		ProcessHistory processHistory = new RedisProcessHistory();
		processHistory.setId(processInstance.getId());
		
		String procHistDomain = getDomainProcHist(processInstance.getId()+"");
		Map<String,String> procHistMap = new HashMap<String,String>();
		procHistMap.put(proc_id, processModel.getId()+"");
		shardedJedis.hmset(procHistDomain, procHistMap);
		//auto save
		BeanUtils.copyProperties(processInstance, processHistory);
		
		
		//copy ActivityInstance
		List<ActivityInstance> activityInstanceList = getActivityInstanceByProcessInstId(processInstance.getId());
		for(ActivityInstance activityInstance : activityInstanceList){
			ActivityDefinition activityElement = processDefinition.getActivity(activityInstance.getActivityDefinitionId());
			if(activityElement instanceof CallActivity){
				long callProcessInstanceId = activityInstance.getCallActivityProcessInstanceId();
				ProcessInstance  callProcessInstance = getProcessInstance(callProcessInstanceId);
				archiveProcess(callProcessInstance);
			}
			RedisActivityHistory activityHistory = new RedisActivityHistory();
			activityHistory.setId(activityInstance.getId());
			String actHistDomain = getDomainActHist(activityInstance.getId()+"");
			Map<String,String> actHistMap = new HashMap<String,String>();
			actHistMap.put(act_id, activityInstance.getId()+"");
			shardedJedis.hmset(actHistDomain.toString(), actHistMap);
			//auto save
			BeanUtils.copyProperties(activityInstance, activityHistory);
			
			
			//mapping proc-act
			String procActKey = getDomainMappingProcActHist(processInstance.getId()+"");
			shardedJedis.sadd(procActKey, activityInstance.getId()+"");
		}
		
		
		//copy TaskInstance
		List<TaskInstance> taskInstanceList = getTaskInstanceByProcessInstId(processInstance.getId());
		for(TaskInstance taskInstance:taskInstanceList){
			TaskHistory taskHistory = new RedisTaskHistory();
			taskHistory.setId(taskInstance.getId());
			String taskHistDomain = getDomainTaskHist(taskInstance.getId()+"");
			Map<String,String> taskHistMap = new HashMap<String,String>();
			taskHistMap.put(task_id, taskInstance.getId()+"");
			shardedJedis.hmset(taskHistDomain, taskHistMap);
			//auto save
			BeanUtils.copyProperties(taskInstance, taskHistory);
			
			//mapping act-task
			String actTaskKey = getDomainMappingActTaskHist(taskInstance.getActivityInstanceId()+"");
			shardedJedis.sadd(actTaskKey, taskInstance.getId()+"");
			
			//mapping userid-task
			String userTaskKey = getDomainMappingUserTaskHist(taskInstance.getUserId());
			shardedJedis.sadd(userTaskKey, taskInstance.getId()+"");
			
		}
		 
		//copy VariableInstance 
		Map<String, VariableInstance> variableMap = getVariableMap(processInstance.getId());
		for(Map.Entry<String, VariableInstance> entry:variableMap.entrySet()){
			VariableInstance variableInstance = entry.getValue();
			
			VariableHistory variableHistory = new RedisVariableHistory();
			variableHistory.setId(variableInstance.getId());
			
			String valHistDomain = getDomainValHist(variableInstance.getId()+"");
			Map<String,String> taskHistMap = new HashMap<String,String>();
			taskHistMap.put(val_id, variableInstance.getId()+"");
			shardedJedis.hmset(valHistDomain, taskHistMap);
			//auto save
			BeanUtils.copyProperties(variableInstance, variableHistory);
		}
		
		//delete VariableInstance
		for(Map.Entry<String, VariableInstance> entry:variableMap.entrySet()){
			VariableInstance variableInstance = entry.getValue();
			
			shardedJedis.del(getDomainValInst(variableInstance.getId()+""));
		}
		
		//delete TaskInstance
		for(TaskInstance taskInstance:taskInstanceList){
			shardedJedis.del(getDomainTaskInst(taskInstance.getId()+""));
		}
		
		//delete ActivityInstance
		for(ActivityInstance activityInstance : activityInstanceList){
			shardedJedis.del(getDomainActInst(activityInstance.getId()+""));
		}
		
		//delete ProcessInstance
		shardedJedis.del(getDomainProcInst(processInstance.getId()+""));
		
		//DONOT DELETE ProcessModel
		
		
	}
	
	



	@Override
	public ProcessModelHistory loadProcessModelHistoryByModelId(
			long processModelId) {
		ShardedJedis shardedJedis = getSharedJedis();
		
		String domain = getDomainProcModelHist(processModelId+"");
		
		Map<String,String> valueMap= shardedJedis.hgetAll(domain.toString());
		if(valueMap!=null&&(!valueMap.isEmpty())){
			ProcessModel_ processModel_ = convertMapToProcessModelEntity(processModelId,valueMap);
			
			ProcessModelHistory processModelHistory= new RedisProcessModelHistory();
			processModelHistory.setId(processModelId);
			
			BeanUtils.copyProperties(processModel_, processModelHistory);
			return processModelHistory;
		}else{
			return null;
		}
	}



	private ProcessModel_ convertMapToProcessModelEntity(long processModelId,
			Map<String, String> valueMap) {
		ProcessModel_ processModel_ = new ProcessModel_();
		processModel_.setId(processModelId);
		processModel_.setName(valueMap.get(procmodel_name));
		
		processModel_.setCreateDate(RunBPMUtils.convertLongToDate(valueMap.get(procmodel_crdate)));
		processModel_.setModifyDate(RunBPMUtils.convertLongToDate(valueMap.get(procmodel_mddate)));
		
		processModel_.setProcessDefinitionId(valueMap.get(procmodel_proddefid));
		processModel_.setXmlcontent(valueMap.get(procmodel_xmlcontent));
		return processModel_;
	}
	
	public static String getDomainValInst(String variableInstanceId){
		StringBuffer valHistDomain = new StringBuffer();
		valHistDomain.append(domain_val_inst);
		valHistDomain.append(":");
		valHistDomain.append(variableInstanceId);
		return valHistDomain.toString();
	}
	

	public static String getDomainProcModel(String procModelId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_procmodel);
		domain.append(":");
		domain.append(procModelId);
		return domain.toString();
	}
	
	public static String getDomainProcModelHist(String procModelHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_procmodel_hist);
		domain.append(":");
		domain.append(procModelHistId);
		return domain.toString();
	}
	
	public static String getDomainProcInst(String processInstanceId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_proc_inst);
		domain.append(":");
		domain.append(processInstanceId);
		return domain.toString();
	}
	

	public static String getDomainProcHist(String processHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_proc_hist);
		domain.append(":");
		domain.append(processHistId);
		return domain.toString();
	}
	
	public static String getDomainActInst(String activityInstanceId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_act_inst);
		domain.append(":");
		domain.append(activityInstanceId);
		return domain.toString();
	}
	

	public static String getDomainActHist(String activityHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_act_hist);
		domain.append(":");
		domain.append(activityHistId);
		return domain.toString();
	}
	

	public static String getDomainMappingProcAct(String processInstanceId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_proc_act);
		domain.append(":");
		domain.append(processInstanceId);
		return domain.toString();
	}
	


	public static String getDomainMappingProcActHist(String processHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_proc_act_hist);
		domain.append(":");
		domain.append(processHistId);
		return domain.toString();
	}

	public static String getDomainTaskInst(String taskInstanceId){

		StringBuffer domain = new StringBuffer();
		domain.append(domain_task_inst);
		domain.append(":");
		domain.append(taskInstanceId);
		return domain.toString();
	}
	

	public static String getDomainTaskHist(String taskHistId){

		StringBuffer domain = new StringBuffer();
		domain.append(domain_task_hist);
		domain.append(":");
		domain.append(taskHistId);
		return domain.toString();
	}
	


	public static String getDomainMappingActTask(String activityInstanceId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_act_task);
		domain.append(":");
		domain.append(activityInstanceId);
		return domain.toString();
	}
	
	
	public static String getDomainMappingActTaskHist(String activityHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_act_task_hist);
		domain.append(":");
		domain.append(activityHistId);
		return domain.toString();
	}

	public static String getDomainMappingUserTask(String userId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_user_task);
		domain.append(":");
		domain.append(userId);
		return domain.toString();
	}
	


	public static String getDomainMappingUserTaskHist(String userId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_user_task_hist);
		domain.append(":");
		domain.append(userId);
		return domain.toString();
	}
	

	public static String getDomainValHist(String variableHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_val_hist);
		domain.append(":");
		domain.append(variableHistId);
		return domain.toString();
	}
	
	public static String getDomainMappingProcVal(String processInstanceId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_proc_val);
		domain.append(":");
		domain.append(processInstanceId);
		return domain.toString();
	}
	

	
	public static String getDomainMappingProcValHist(String processHistId){
		StringBuffer domain = new StringBuffer();
		domain.append(domain_mapping_proc_val_hist);
		domain.append(":");
		domain.append(processHistId);
		return domain.toString();
	}
	
	public static String getDomainMappingModelProc(String processModelId){
		StringBuffer mapKey = new StringBuffer();
		mapKey.append(domain_mapping_model_proc);
		mapKey.append(":");
		mapKey.append(processModelId);
		return mapKey.toString();
	}
	

	
	public static String getDomainMappingModelProcHist(String processModelId){
		StringBuffer mapKey = new StringBuffer();
		mapKey.append(domain_mapping_model_proc_hist);
		mapKey.append(":");
		mapKey.append(processModelId);
		return mapKey.toString();
	}

	@Override
	public List<ProcessModel> loadProcessModels(boolean reload){
		return null;
	}
	
}
