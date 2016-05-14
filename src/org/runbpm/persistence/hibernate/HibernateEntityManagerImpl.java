package org.runbpm.persistence.hibernate;

import java.io.File;
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
import org.springframework.beans.BeanUtils;

public class HibernateEntityManagerImpl extends AbstractEntityManager{
	
	public List<ProcessModel> loadProcessModels(boolean reload){
		List<ProcessModel> processModelList = new ArrayList<ProcessModel>();
		if(reload){
			processModelMap.clear();
		}
		
		
		String hsql = "select p from ProcessModelImpl p";
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<ProcessModel> queryList = session.createQuery(hsql).list();
		
		for(ProcessModel processModel:queryList){
			String xmlContent = processModel.getXmlcontent();
			
			JAXBContext jaxbContext;
			ProcessDefinition process =null;
			try {
				jaxbContext = JAXBContext.newInstance("org.runbpm.bpmn.definition");
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				
				Definitions definitions = (Definitions) jaxbUnmarshaller.unmarshal(new StringReader(xmlContent));
				process = definitions.getProcess();
				
				processModel.setProcessDefinition(process);
				processModelList.add(processModel);
			} catch (JAXBException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			parseProcessListener(process);
			processModelMap.put(processModel.getId(), processModel);
		}
		return processModelList;
	}

	@Override
	public ProcessModel deployProcessDefinitionFromFile(File file) {
		ProcessModel processModel = this.deployProcessDefinitionFromFile_(file);
	    return saveProcessModel(processModel);
	}
	
	public ProcessModel initProcessDefinition(ProcessDefinition processDefinition){
		ProcessModel processModel = this.deployProcessDefinition_(processDefinition);
		return saveProcessModel(processModel);
	}
	
	private ProcessModel saveProcessModel(ProcessModel processModel) {
		Session session = TransactionObjectHolder.get().getSession();
	    
	    long processModelId =  (Long) session.save(processModel);
	    processModelMap.put(processModelId, processModel);
	    return processModel;
	}

	@Override
	public ProcessInstance getProcessInstance(long processInstanceId) {
		Session session = TransactionObjectHolder.get().getSession();
		ProcessInstance processInstance= session.get(ProcessInstanceImpl.class, processInstanceId);
		
		return processInstance;
	}

	@Override
	public ActivityInstance getActivityInstance(long activityInstanceeId) {
		Session session = TransactionObjectHolder.get().getSession();
		ActivityInstance activityInstance= session.get(ActivityInstanceImpl.class, activityInstanceeId);
		return activityInstance;
	}
	
	public List<ActivityInstance> getActivityInstanceByProcessInstId(
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
	public TaskInstance getTaskInstance(long taskInstanceId) {
		Session session = TransactionObjectHolder.get().getSession();
		TaskInstance taskInstance= session.get(TaskInstanceImpl.class, taskInstanceId);
		return taskInstance;
	}

	public List<TaskInstance> getTaskInstanceByActivityInstId(long activityInstanceId){
		String hsql = "select t from TaskInstanceImpl t where t.activityInstanceId = :activityInstanceId";
		Session session = TransactionObjectHolder.get().getSession();
		@SuppressWarnings("unchecked")
		List<TaskInstance> queryList = session.createQuery(hsql)
				.setParameter("activityInstanceId", activityInstanceId).list();
		
		return queryList;
	}
	
	public List<TaskInstance> getTaskInstanceByUserId(String userId){
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
		TaskInstance taskInstance = this.getTaskInstance(removeTaskInstanceId);
		Session session = TransactionObjectHolder.get().getSession();
		session.delete(taskInstance);
		
	}

	@Override
	public ApplicationInstance getApplicationInstance(
			long applicationInstanceId) {
		return null;
	}

	@Override
	public Map<String, VariableInstance> getVariableMap(long processInstanceId) {
		
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
		
		//long id = (Long) session.save(processInstance);
		
		Session session = TransactionObjectHolder.get().getSession();
		long id = (Long) session.save(processInstance);
		
		processInstance.setId(id);
		processInstance.setProcessModelId(processModelId);
		return processInstance;
	}

	@Override
	public ActivityInstance produceActivityInstance(long processInstanceId) {
		ActivityInstance activityInstance= new ActivityInstanceImpl();
		
		Session session = TransactionObjectHolder.get().getSession();
		long id = (Long) session.save(activityInstance);
		activityInstance.setId(id);
		activityInstance.setProcessInstanceId(processInstanceId);
		return activityInstance;
	}

	@Override
	public TaskInstance produceTaskInstance(long activityInstanceId,String userId) {
		TaskInstance taskInstance= new TaskInstanceImpl();
		
		Session session = TransactionObjectHolder.get().getSession();
		long id = (Long) session.save(taskInstance);
		taskInstance.setId(id);
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
		VariableInstance variableInstance = this.getVariableInstance(processInstanceId, name);
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
		List<ActivityInstance> activityInstanceList = this.getActivityInstanceByProcessInstId(processInstance.getId());
		for(ActivityInstance activityInstance : activityInstanceList){
			ActivityDefinition activityElement = processDefinition.getActivity(activityInstance.getActivityDefinitionId());
			if(activityElement instanceof CallActivity){
				long callProcessInstanceId = activityInstance.getCallActivityProcessInstanceId();
				ProcessInstance  callProcessInstance = this.getProcessInstance(callProcessInstanceId);
				this.archiveProcess(callProcessInstance);
			}
			ActivityHistory activityHistory = new ActivityHistoryImpl();
			BeanUtils.copyProperties(activityInstance, activityHistory);
			session.save(activityHistory);
		}
		
		
		//copy TaskInstance
		List<TaskInstance> taskInstanceList = this.getTaskInstanceByProcessInstId(processInstance.getId());
		for(TaskInstance taskInstance:taskInstanceList){
			TaskHistory taskHistory = new TaskHistoryImpl();
			BeanUtils.copyProperties(taskInstance, taskHistory);
			session.save(taskHistory);
		}
		 
		//copy VariableInstance 
		Map<String, VariableInstance> variableMap = this.getVariableMap(processInstance.getId());
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
		
}
