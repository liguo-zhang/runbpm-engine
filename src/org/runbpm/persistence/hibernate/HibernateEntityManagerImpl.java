package org.runbpm.persistence.hibernate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibernate.Session;
import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.Definitions;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.entity.ActivityHistory;
import org.runbpm.entity.ActivityHistoryImpl;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.ActivityInstanceImpl;
import org.runbpm.entity.ApplicationInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.ProcessHistory;
import org.runbpm.entity.ProcessHistoryImpl;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessInstanceImpl;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelHistory;
import org.runbpm.entity.ProcessModelHistoryImpl;
import org.runbpm.entity.TaskHistory;
import org.runbpm.entity.TaskHistoryImpl;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.TaskInstanceImpl;
import org.runbpm.entity.VariableHistory;
import org.runbpm.entity.VariableHistoryImpl;
import org.runbpm.entity.VariableInstance;
import org.runbpm.entity.VariableInstanceImpl;
import org.runbpm.persistence.AbstractEntityManager;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;

public class HibernateEntityManagerImpl extends AbstractEntityManager{
	
	//TODO 线程优化
	public static boolean isInit = false;
	
	private static JAXBContext jaxbContext = null;
	
	private static JAXBContext getJaxbContext(){
		if(jaxbContext==null){
			try {
				jaxbContext = JAXBContext.newInstance("org.runbpm.bpmn.definition");
			} catch (JAXBException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return jaxbContext;		
	}
	
	private ProcessDefinition assignProcessDefinition(ProcessModel processModel){
		String xmlContent = processModel.getXmlContent();
		Unmarshaller jaxbUnmarshaller;
		try {
			jaxbUnmarshaller = getJaxbContext().createUnmarshaller();
			Definitions definitions = (Definitions) jaxbUnmarshaller.unmarshal(new StringReader(xmlContent));
			ProcessDefinition process = definitions.getProcess();
			processModel.setProcessDefinition(process);
			return process;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	private void compareProcessModelVersion(List<ProcessModel> processModelList,ProcessModel processModel){
		//---当前列表中有没有相同流程定义的模板--开始
		ProcessModel savedModel = null;
		for(ProcessModel processModelObject:processModelList){
			if(processModelObject.getProcessDefinitionId().equals(processModel.getProcessDefinitionId())){
				savedModel = processModelObject;
			}
		}
		//---当前列表中有没有相同流程定义的模板---结束

		if(savedModel==null){
			processModelList.add(processModel);
		}else{
			//数据库查询得到的id比当前列表的新
			if(processModel.getId()>savedModel.getId()){
				processModelList.remove(savedModel);
				processModelList.add(processModel);
			}
		}
	}
	
	public List<ProcessModel> loadProcessModels(boolean onlyReturnNewVersion){
		List<ProcessModel> processModelList = new ArrayList<ProcessModel>();
		if(!isInit){
			isInit = true;
			
			String hsql = "select p from ProcessModelImpl p";
			Session session = TransactionObjectHolder.get().getSession();
			@SuppressWarnings("unchecked")
			List<ProcessModel> queryList = session.createQuery(hsql).list();
			
			for(ProcessModel processModel:queryList){
				//转换XML内容为流程定义对象
				ProcessDefinition process = assignProcessDefinition(processModel);
				//注册监听器
				parseProcessListener(processModel);
				//如果只返回最新版本
				if(onlyReturnNewVersion){
					compareProcessModelVersion(processModelList,processModel);
				}else{
					processModelList.add(processModel);
				}
				processModelMap.put(processModel.getId(), processModel);
			}
		}else{
			for(Map.Entry<Long, ProcessModel> entry:processModelMap.entrySet()){
				ProcessModel processModelObjectInMap = entry.getValue();
				if(onlyReturnNewVersion){
					compareProcessModelVersion(processModelList,processModelObjectInMap);
				}else{
					processModelList.add(processModelObjectInMap);
				}
			}
		}
		return processModelList;
	}
	
	
	
	protected ProcessModel produceProcessModel(ProcessModel processModel) {
		Session session = TransactionObjectHolder.get().getSession();
		SnowflakeIdWorker idWorker = SnowflakeIdWorker.getSnowflakeIdWorker(0, 0);
        long id = idWorker.nextId();
        processModel.setId(id);
	    session.save(processModel);
	    processModelMap.put(id, processModel);
	    return processModel;
	}

	@Override
	public ProcessInstance loadProcessInstance(long processInstanceId) {
		Session session = TransactionObjectHolder.get().getSession();
		ProcessInstance processInstance= session.get(ProcessInstanceImpl.class, processInstanceId);
		
		return processInstance;
	}

	@Override
	public ActivityInstance loadActivityInstance(long activityInstanceeId) {
		Session session = TransactionObjectHolder.get().getSession();
		ActivityInstance activityInstance= session.get(ActivityInstanceImpl.class, activityInstanceeId);
		return activityInstance;
	}
	
	public List<ActivityInstance> listActivityInstanceByProcessInstId(
			long processInstanceId) {
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		String hsql = "select a from ActivityInstanceImpl a where a.processInstanceId = :processInstanceId";

		Session session = TransactionObjectHolder.get().getSession();
	    @SuppressWarnings("unchecked")
		List<ActivityInstance> queryList = session.createQuery(hsql).setParameter("processInstanceId", processInstanceId).list();
		
		for(ActivityInstance activityInstance:queryList){
			activityList.add(activityInstance);
		}
		return activityList;
	}
	

	@Override
	public TaskInstance loadTaskInstance(long taskInstanceId) {
		Session session = TransactionObjectHolder.get().getSession();
		TaskInstance taskInstance= session.get(TaskInstanceImpl.class, taskInstanceId);
		return taskInstance;
	}

	public List<TaskInstance> listTaskInstanceByActivityInstId(long activityInstanceId){
		String hsql = "select t from TaskInstanceImpl t where t.activityInstanceId = :activityInstanceId";
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<TaskInstance> queryList = session.createQuery(hsql)
				.setParameter("activityInstanceId", activityInstanceId).list();
		
		return queryList;
	}
	

	@Override
	public List<TaskHistory> listTaskHistoryByActivityInstId(long activityHistoryId) {
		String hsql = "select t from TaskHistoryImpl t where t.activityInstanceId = :activityHistoryId";
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<TaskHistory> queryList = session.createQuery(hsql)
				.setParameter("activityHistoryId", activityHistoryId).list();
		
		return queryList;
	}

	
	public List<TaskInstance> listTaskInstanceByUserId(String userId){
		List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>();
		
		String hsql = "select t from TaskInstanceImpl t where t.userId = :userId";
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<TaskInstance> queryList = session.createQuery(hsql)
				.setParameter("userId", userId).list();
		
		for(TaskInstance taskInstance:queryList){
			taskInstanceList.add(taskInstance);
			
		}
		return taskInstanceList;
	}
	

	@Override
	public void removeTaskInstanceExceptClaimInstance(
			TaskInstance claimTaskInstance) {
		String hsql = "select t from TaskInstanceImpl t where t.activityInstanceId = :activityInstanceId";
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<TaskInstance> queryList = session.createQuery(hsql)
				.setParameter("activityInstanceId", claimTaskInstance.getActivityInstanceId()).list();
		
		for(TaskInstance taskInstance:queryList){
			if(taskInstance.getId()!=claimTaskInstance.getId()){
				session.delete(taskInstance);
			}
		}
	}
	
	public void removeTaskInstance(long removeTaskInstanceId){
		TaskInstance taskInstance = this.loadTaskInstance(removeTaskInstanceId);
		Session session = TransactionObjectHolder.get().getSession();
		session.delete(taskInstance);
		
	}

	@Override
	public ApplicationInstance loadApplicationInstance(
			long applicationInstanceId) {
		return null;
	}

	@Override
	public Map<String, VariableInstance> loadVariableMap(long processInstanceId) {
		
		Map<String, VariableInstance> variableInstanceMap = new HashMap<String, VariableInstance> ();
		String hsql = "select v from VariableInstanceImpl v where v.processInstanceId = :processInstanceId";
		
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<VariableInstance> queryList = session.createQuery(hsql)
				.setParameter("processInstanceId", processInstanceId).list();
		
		for(VariableInstance variableInstance:queryList){
			Object value = null;
			if(variableInstance.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Boolean)){
				value = Boolean.parseBoolean(variableInstance.getValueString());
			}else if(variableInstance.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Double)){
				 value = Double.parseDouble(variableInstance.getValueString());
			}else if(variableInstance.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Float)){
				 value = Float.parseFloat(variableInstance.getValueString());
			}else if(variableInstance.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Integer)){
				 value = Integer.parseInt(variableInstance.getValueString());
			}else if(variableInstance.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_Long)){
				 value = Long.parseLong(variableInstance.getValueString());
			}else if(variableInstance.getType().equals(EntityConstants.VARIABLE_TYPE.Basic_String)){
				 value = variableInstance.getValueString().toString();
			}
			variableInstance.setValue(value);
			variableInstanceMap.put(variableInstance.getName(), variableInstance);
			
		}
		return variableInstanceMap;
	}

	@Override
	public ProcessInstance produceProcessInstance(long processModelId) {
		ProcessInstance processInstance= new ProcessInstanceImpl();
		
		SnowflakeIdWorker idWorker = SnowflakeIdWorker.getSnowflakeIdWorker(0, 0);
        long id = idWorker.nextId();
        processInstance.setId(id);
		
		Session session = TransactionObjectHolder.get().getSession();
		session.save(processInstance);
		
		processInstance.setProcessModelId(processModelId);
		return processInstance;
	}

	@Override
	public ActivityInstance produceActivityInstance(long processInstanceId) {
		ActivityInstance activityInstance= new ActivityInstanceImpl();
		
		Session session = TransactionObjectHolder.get().getSession();
		SnowflakeIdWorker idWorker = SnowflakeIdWorker.getSnowflakeIdWorker(0, 0);
        long id = idWorker.nextId();
		activityInstance.setId(id);
		session.save(activityInstance);
		
		activityInstance.setProcessInstanceId(processInstanceId);
		return activityInstance;
	}

	@Override
	public TaskInstance produceTaskInstance(long activityInstanceId,String userId) {
		TaskInstance taskInstance= new TaskInstanceImpl();
		SnowflakeIdWorker idWorker = SnowflakeIdWorker.getSnowflakeIdWorker(0, 0);
        long id = idWorker.nextId();
        taskInstance.setId(id);
		
		Session session = TransactionObjectHolder.get().getSession();
		 session.save(taskInstance);
		
		taskInstance.setActivityInstanceId(activityInstanceId);
		return taskInstance;
	}

	@Override
	public ApplicationInstance produceApplicationInstance() {
		return null;
	}

	@Override
	public void setProcessVariable(long processInstanceId, String name,
			Object value) {
		VariableInstance variableInstance = this.loadVariableInstance(processInstanceId, name);
		if(variableInstance==null){
			variableInstance = new VariableInstanceImpl();
			
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
			
			Session session = TransactionObjectHolder.get().getSession();
			session.save(variableInstance);
		}else{
			variableInstance.setValueString(value.toString());
		}
	}

	@Override
	public void archiveProcess(ProcessInstance processInstance) {
		Session session = TransactionObjectHolder.get().getSession();
		
		//copy ProcessModel
		ProcessModel processModel = this.loadProcessModelByModelId(processInstance.getProcessModelId());
		ProcessDefinition processDefinition = processModel.getProcessDefinition();
		ProcessModelHistory processModelHistory =  session.get(ProcessModelHistoryImpl.class, processModel.getId());
		if(processModelHistory==null){
			processModelHistory = new ProcessModelHistoryImpl();
			BeanUtils.copyProperties(processModel, processModelHistory);
			session.save(processModelHistory);
		}
		
		//copy ProcessHistory
		ProcessHistory processHistory = new ProcessHistoryImpl();
		BeanUtils.copyProperties(processInstance, processHistory);
		session.save(processHistory);
		
		//copy ActivityInstance
		List<ActivityInstance> activityInstanceList = this.listActivityInstanceByProcessInstId(processInstance.getId());
		for(ActivityInstance activityInstance : activityInstanceList){
			ActivityDefinition activityElement = processDefinition.getActivity(activityInstance.getActivityDefinitionId());
			if(activityElement instanceof CallActivity){
				long callProcessInstanceId = activityInstance.getCallActivityProcessInstanceId();
				ProcessInstance  callProcessInstance = this.loadProcessInstance(callProcessInstanceId);
				this.archiveProcess(callProcessInstance);
			}
			ActivityHistory activityHistory = new ActivityHistoryImpl();
			BeanUtils.copyProperties(activityInstance, activityHistory);
			session.save(activityHistory);
		}
		
		
		//copy TaskInstance
		List<TaskInstance> taskInstanceList = this.listTaskInstanceByProcessInstId(processInstance.getId());
		for(TaskInstance taskInstance:taskInstanceList){
			TaskHistory taskHistory = new TaskHistoryImpl();
			BeanUtils.copyProperties(taskInstance, taskHistory);
			session.save(taskHistory);
		}
		 
		//copy VariableInstance 
		Map<String, VariableInstance> variableMap = this.loadVariableMap(processInstance.getId());
		for(Map.Entry<String, VariableInstance> entry:variableMap.entrySet()){
			VariableInstance variableInstance = entry.getValue();
			VariableHistory variableHistory = new VariableHistoryImpl ();
			BeanUtils.copyProperties(variableInstance, variableHistory);
			session.save(variableHistory);
			
			
		}
		
		//delete VariableInstance
		for(Map.Entry<String, VariableInstance> entry:variableMap.entrySet()){
			VariableInstance variableInstance = entry.getValue();
			session.delete(variableInstance);
		}
		
		//delete TaskInstance
		for(TaskInstance taskInstance:taskInstanceList){
			session.delete(taskInstance);
		}
		
		//delete ActivityInstance
		for(ActivityInstance activityInstance : activityInstanceList){
			session.delete(activityInstance);
		}
		
		//delete ProcessInstance
		session.delete(processInstance);
		
		//DONOT DELETE ProcessModel
		
	}

	@Override
	public ProcessModelHistory loadProcessModelHistoryByModelId(long processModelId) {
		Session session = TransactionObjectHolder.get().getSession();
		ProcessModelHistory processModelHistory= session.get(ProcessModelHistoryImpl.class, processModelId);
		return processModelHistory;
	}

	@Override
	public List<ProcessInstance> listProcessInstanceByCreator(String creator) {
		List<ProcessInstance> processlist = new ArrayList<ProcessInstance>();
		String hsql = "select p from ProcessInstanceImpl p where p.creator = :creator";

		Session session = TransactionObjectHolder.get().getSession();
	    @SuppressWarnings("unchecked")
		List<ProcessInstance> queryList = session.createQuery(hsql).setParameter("creator",creator).list();
		
		for(ProcessInstance processInstance:queryList){
			processlist.add(processInstance);
		}
		return processlist;
	}

	@Override
	public List<ProcessHistory> listProcessHistoryByCreator(String creator) {
		List<ProcessHistory> processlist = new ArrayList<ProcessHistory>();
		String hsql = "select p from ProcessHistoryImpl p where p.creator = :creator";

		Session session = TransactionObjectHolder.get().getSession();
	    @SuppressWarnings("unchecked")
		List<ProcessHistory> queryList = session.createQuery(hsql).setParameter("creator",creator).list();
		
		for(ProcessHistory processHistory:queryList){
			processlist.add(processHistory);
		}
		return processlist;

	}

	@Override
	public ProcessHistory loadProcessHistory(long processHistoryId) {
		Session session = TransactionObjectHolder.get().getSession();
		ProcessHistory processHistory= session.get(ProcessHistoryImpl.class, processHistoryId);
		
		return processHistory;
	}

	@Override
	public ActivityHistory loadActivityHistory(long activityHistoryId) {
		Session session = TransactionObjectHolder.get().getSession();
		ActivityHistory activityHistory= session.get(ActivityHistoryImpl.class, activityHistoryId);
		
		return activityHistory;
	}

	@Override
	public List<ActivityHistory> listActivityHistoryByProcessInstId(long processInstanceId) {
		
		List<ActivityHistory> activityList = new ArrayList<ActivityHistory>();
		String hsql = "select a from ActivityHistoryImpl a where a.processInstanceId = :processInstanceId";
	
		Session session = TransactionObjectHolder.get().getSession();
	    @SuppressWarnings("unchecked")
		List<ActivityHistory> queryList = session.createQuery(hsql).setParameter("processInstanceId", processInstanceId).list();
		
		for(ActivityHistory activityHistory:queryList){
			activityList.add(activityHistory);
		}
		return activityList;
	}

	

	@Override
	public TaskHistory loadTaskHistory(long taskHistoryId) {
		Session session = TransactionObjectHolder.get().getSession();
		TaskHistory taskHistory= session.get(TaskHistoryImpl.class, taskHistoryId);
		
		return taskHistory;
	}

	
		
}
