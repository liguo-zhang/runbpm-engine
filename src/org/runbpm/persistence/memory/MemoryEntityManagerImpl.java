package org.runbpm.persistence.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.Session;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ActivityInstanceImpl;
import org.runbpm.entity.ApplicationInstance;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessInstanceImpl;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelHistory;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.TaskInstanceImpl;
import org.runbpm.entity.VariableInstance;
import org.runbpm.entity.VariableInstanceImpl;
import org.runbpm.persistence.AbstractEntityManager;
import org.runbpm.persistence.TransactionObjectHolder;


public class MemoryEntityManagerImpl extends AbstractEntityManager{
	
	private AtomicLong processModelIdCounter = new AtomicLong(1);
	private AtomicLong processInstanceIdCounter = new AtomicLong(1);
	private AtomicLong activityInstanceIdCounter = new AtomicLong(1);
	private AtomicLong workItemInstanceIdCounter = new AtomicLong(1);
	private AtomicLong applicationInstanceIdCounter = new AtomicLong(1);
	private AtomicLong variableIdCounter = new AtomicLong(1);
	
	private Map<Long,ProcessInstance> processInstanceMap = new HashMap<Long,ProcessInstance>();
	private Map<Long,ActivityInstance> activityInstanceMap = new HashMap<Long,ActivityInstance>();
	private Map<Long,TaskInstance> taskInstanceMap = new HashMap<Long,TaskInstance>();
	private Map<Long,ApplicationInstance> applicationInstanceMap = new HashMap<Long,ApplicationInstance>();
	private Map<Long,VariableInstance> variableMap = new HashMap<Long,VariableInstance>();
	
	private static MemoryEntityManagerImpl memoeryEntityManager = new MemoryEntityManagerImpl();
	
	private MemoryEntityManagerImpl(){
	}
	
	public static MemoryEntityManagerImpl getInstance(){
		return memoeryEntityManager;
	}
	
	protected ProcessModel saveProcessModel(ProcessModel processModel) {
		long processModelId = processModelIdCounter.getAndIncrement();
		processModel.setId(processModelId);
	    processModelMap.put(processModelId, processModel);
	    return processModel; 
	}
	
	public ProcessInstance getProcessInstance(long processInstanceId){
		ProcessInstance processInstance = processInstanceMap.get(processInstanceId);
		return processInstance;
	}
	

	public ActivityInstance getActivityInstance(long activityInstanceeId){
		ActivityInstance activityInstance = activityInstanceMap.get(activityInstanceeId);
		return activityInstance;
	}
	

	public TaskInstance getTaskInstance(long workItemInstanceId){
		TaskInstance taskInstance = taskInstanceMap.get(workItemInstanceId);
		return taskInstance;
	}
	
	
	
	public List<TaskInstance> getTaskInstanceByActivityInstId(long activityInstanceId){
		List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>();
		for(Map.Entry<Long, TaskInstance> entry:this.taskInstanceMap.entrySet()){
			TaskInstance taskInstance = entry.getValue();
			if(taskInstance.getActivityInstanceId()==activityInstanceId){
				taskInstanceList.add(taskInstance);
			}
		}
		return taskInstanceList;
	}
	

	
	public void removeTaskInstanceExceptClaimInstance(TaskInstance claimTaskInstance){
		Iterator<Map.Entry<Long, TaskInstance>> it = taskInstanceMap.entrySet().iterator();  
        while(it.hasNext()){
            Map.Entry<Long, TaskInstance> entry=it.next();  
            TaskInstance taskInstance = entry.getValue();
            if(taskInstance.getActivityInstanceId()==claimTaskInstance.getActivityInstanceId()){
				if(taskInstance.getId()==claimTaskInstance.getId()){
					continue;
				}else{
					it.remove();   
				}
			}
        }  
	}
	
	public void removeTaskInstance(long removeTaskInstanceId){
		Iterator<Map.Entry<Long, TaskInstance>> it = taskInstanceMap.entrySet().iterator();  
        while(it.hasNext()){
            Map.Entry<Long, TaskInstance> entry=it.next();  
            TaskInstance taskInstance = entry.getValue();
			if(taskInstance.getId()==removeTaskInstanceId){
					it.remove();
					break;
			}
        }  
	}

	public ApplicationInstance getApplicationInstance(long applicationInstanceId){
		ApplicationInstance applicationInstance = applicationInstanceMap.get(applicationInstanceId);
		return applicationInstance;
	}

	public ProcessInstance produceProcessInstance(long processModelId){
		ProcessInstance processInstance = new ProcessInstanceImpl();
		long processInstanceId = processInstanceIdCounter.getAndIncrement();
		processInstance.setId(processInstanceId);
		processInstance.setProcessModelId(processModelId);
		processInstanceMap.put(processInstanceId,processInstance);
		return processInstance;
		
	}
	

	public ActivityInstance produceActivityInstance(long processInstanceId){
		ActivityInstance activityInstance = new ActivityInstanceImpl();

		long activityInstanceId = activityInstanceIdCounter.getAndIncrement();
		activityInstance.setId(activityInstanceId);
		activityInstance.setProcessInstanceId(processInstanceId);
		activityInstanceMap.put(activityInstanceId,activityInstance);
		return activityInstance;
	}

	public TaskInstance produceTaskInstance(long activityInstanceId,String userId){
		TaskInstance workItemInstance  = new TaskInstanceImpl();
		
		long workItemInstanceId = workItemInstanceIdCounter.getAndIncrement();
		workItemInstance.setId(workItemInstanceId);
		workItemInstance.setActivityInstanceId(activityInstanceId);
		taskInstanceMap.put(workItemInstanceId,workItemInstance);

		return workItemInstance ;
	}


	public  ApplicationInstance produceApplicationInstance(){
		ApplicationInstance applicationInstance = new ApplicationInstance();
		
		long applicationInstanceId = applicationInstanceIdCounter.getAndIncrement();;
		applicationInstance.setId(applicationInstanceId);
		applicationInstanceMap.put(applicationInstanceId,applicationInstance);
		
		return applicationInstance;
	}

	

	@Override
	public Map<String, VariableInstance> getVariableMap(
			long processInstanceId) {
		Map<String,VariableInstance> map = new HashMap<String,VariableInstance>();
		for(Map.Entry<Long, VariableInstance> entry:variableMap.entrySet()){	
			VariableInstance dataFieldInstance = entry.getValue();
			if(dataFieldInstance.getProcessInstanceId().equals(processInstanceId)){
				map.put(dataFieldInstance.getName(), dataFieldInstance);
			}
		}
		return map;
	}

	@Override
	public void setProcessVariable(long processInstanceId, String id,
			Object value) {

		boolean found = false;
		
		for(Map.Entry<Long, VariableInstance> entry:variableMap.entrySet()){	
			VariableInstance dataFieldInstance = entry.getValue();
			if(dataFieldInstance!=null){
				if(dataFieldInstance.getName().equalsIgnoreCase(id)&&dataFieldInstance.getProcessInstanceId().equals(processInstanceId)){
					found = true;
					dataFieldInstance.setValue(value);
					break;
				}
			}
		}
		
		if(!found){
			VariableInstance dataFieldInstance = new VariableInstanceImpl();
			
			long dataFieldInstanceId = this.variableIdCounter.getAndIncrement();
			variableMap.put(dataFieldInstanceId,dataFieldInstance);
			
			dataFieldInstance.setId(dataFieldInstanceId);
			dataFieldInstance.setProcessInstanceId(processInstanceId);
			dataFieldInstance.setName(id);
			dataFieldInstance.setValue(value);
		}
			
	}

	
	
	public List<ActivityInstance>  getActivityInstanceByProcessInstId(long processInstanceId) {
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		for(Map.Entry<Long, ActivityInstance> entry:activityInstanceMap.entrySet()){
			ActivityInstance activityInstance = entry.getValue();
			if(activityInstance.getProcessInstanceId()==processInstanceId){
				activityList.add(activityInstance);
			}
		}
		return activityList;
	}

		
	public void clearMemory(){
		this.processModelIdCounter.set(1); 
		this.processInstanceIdCounter.set(1);
		this.activityInstanceIdCounter.set(1);;
		this.workItemInstanceIdCounter.set(1);
		this.applicationInstanceIdCounter.set(1);
		this.variableIdCounter.set(1);
		
		this.processModelMap = new HashMap<Long,ProcessModel>();
		
		this.processInstanceMap = new HashMap<Long,ProcessInstance>();
		this.activityInstanceMap = new HashMap<Long,ActivityInstance>();
		this.taskInstanceMap = new HashMap<Long,TaskInstance>();
		this.applicationInstanceMap = new HashMap<Long,ApplicationInstance>();
		this.variableMap = new HashMap<Long,VariableInstance>();
	}

	@Override
	public void archiveProcess(ProcessInstance processInstance) {
		// do nothing
	}

	@Override
	public List<TaskInstance> getTaskInstanceByUserId(String userId) {
		List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>();
		for(Map.Entry<Long, TaskInstance> entry:this.taskInstanceMap.entrySet()){
			TaskInstance taskInstance = entry.getValue();
			if(taskInstance.getUserId().equals(userId)){
				taskInstanceList.add(taskInstance);
			}
		}
		return taskInstanceList;
	}

	@Override
	public ProcessModelHistory loadProcessModelHistoryByModelId(
			long processModelId) {
		//do nothing in memory model
		return null;
	}

	@Override
	public List<ProcessModel> loadProcessModels(boolean reload){
		List<ProcessModel> processModelList = new ArrayList<ProcessModel>();
		Set<Long> set = processModelMap.keySet();
		for(Long key:set){
			ProcessModel ProcessModel = processModelMap.get(key);
			processModelList.add(ProcessModel);
		}
		return processModelList;
	}

}
