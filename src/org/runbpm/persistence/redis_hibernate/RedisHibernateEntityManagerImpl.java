package org.runbpm.persistence.redis_hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityHistoryImpl;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.ProcessHistoryImpl;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelHistory;
import org.runbpm.entity.ProcessModelHistoryImpl;
import org.runbpm.entity.TaskHistory;
import org.runbpm.entity.TaskHistoryImpl;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableHistory;
import org.runbpm.entity.VariableHistoryImpl;
import org.runbpm.entity.VariableInstance;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.persistence.redis.RedisActivityHistory;
import org.runbpm.persistence.redis.RedisEntityManagerImpl;
import org.runbpm.persistence.redis.RedisProcessHistory;
import org.runbpm.persistence.redis.RedisProcessModelHistory;
import org.runbpm.persistence.redis.RedisTaskHistory;
import org.runbpm.persistence.redis.RedisVariableHistory;
import org.springframework.beans.BeanUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisHibernateEntityManagerImpl extends RedisEntityManagerImpl {
	
	
	public void archiveProcess(ProcessInstance processInstance) {
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		Session session = TransactionObjectHolder.get().getSession();
		
		//copy ProcessModel
		ProcessModel processModel = this.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ProcessModelHistory processModelHistory =  this.loadProcessModelHistoryByModelId(processModel.getId());
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
		
		//-------------Hibernate ProcessModelHistoryImpl
		ProcessModelHistoryImpl processModelHistoryImpl =  session.get(ProcessModelHistoryImpl.class, processModel.getId());
		if(processModelHistoryImpl==null){
			processModelHistoryImpl = new ProcessModelHistoryImpl();
			BeanUtils.copyProperties(processModel, processModelHistoryImpl);
			session.save(processModelHistoryImpl);
		}
		//-------------Hibernate ProcessModelHistoryImpl
		
		//copy ProcessHistory
		ProcessHistory processHistory = new RedisProcessHistory();
		processHistory.setId(processInstance.getId());
		
		String procHistDomain = getDomainProcHist(processInstance.getId()+"");
		Map<String,String> procHistMap = new HashMap<String,String>();
		procHistMap.put(proc_id, processModel.getId()+"");
		shardedJedis.hmset(procHistDomain, procHistMap);
		//auto save
		BeanUtils.copyProperties(processInstance, processHistory);
		
		//-------------Hibernate copy ProcessHistory
		ProcessHistoryImpl processHistoryImpl = new ProcessHistoryImpl();
		BeanUtils.copyProperties(processInstance, processHistoryImpl);
		session.save(processHistoryImpl);
		//-------------Hibernate copy ProcessHistory
		
		//copy ActivityInstance
		List<ActivityInstance> activityInstanceList = this.listActivityInstanceByProcessInstId(processInstance.getId());
		for(ActivityInstance activityInstance : activityInstanceList){
			ActivityDefinition activityElement = processDefinition.getActivity(activityInstance.getActivityDefinitionId());
			if(activityElement instanceof CallActivity){
				long callProcessInstanceId = activityInstance.getCallActivityProcessInstanceId();
				ProcessInstance  callProcessInstance = this.getProcessInstance(callProcessInstanceId);
				this.archiveProcess(callProcessInstance);
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
			
			//---------------hibernate copy ActivityInstance
			ActivityHistoryImpl activityHistoryImpl = new ActivityHistoryImpl();
			BeanUtils.copyProperties(activityInstance, activityHistoryImpl);
			session.save(activityHistoryImpl);
			//---------------hibernate copy ActivityInstance
		}
		
		
		//copy TaskInstance
		List<TaskInstance> taskInstanceList = this.listTaskInstanceByProcessInstId(processInstance.getId());
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
			
			//----------hibernate copy TaskInstance
			TaskHistoryImpl taskHistoryImpl = new TaskHistoryImpl();
			BeanUtils.copyProperties(taskInstance, taskHistoryImpl);
			session.save(taskHistoryImpl);
			//----------hibernate copy TaskInstance
			
		}
		 
		//copy VariableInstance 
		Map<String, VariableInstance> variableMap = this.getVariableMap(processInstance.getId());
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
			
			//----------hibernate copy VariableInstance
			VariableHistoryImpl variableHistoryImpl = new VariableHistoryImpl ();
			BeanUtils.copyProperties(variableInstance, variableHistoryImpl);
			session.save(variableHistoryImpl);
			//----------hibernate copy VariableInstance
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

}
