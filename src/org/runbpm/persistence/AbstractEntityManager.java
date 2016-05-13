package org.runbpm.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.Definitions;
import org.runbpm.bpmn.definition.ExtensionElements;
import org.runbpm.bpmn.definition.ExtensionExecutionListener;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.ProcessModelImpl;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager;
import org.runbpm.listener.ListenerManager.Event_Type;


public abstract class AbstractEntityManager implements EntityManager{
	
	protected Map<Long,ProcessModel> processModelMap = new HashMap<Long,ProcessModel>();
	
	public ProcessModel initProcessDefinition_(ProcessDefinition processDefinition){
		//解析流程监听器
		parseProcessListener(processDefinition);
		
		ProcessModel processModel = new  ProcessModelImpl();
		processModel.setProcessDefinition(processDefinition);
		processModel.setProcessDefinitionId(processDefinition.getId());
		
	    return processModel; 
	}
	
	
	protected ProcessModel initProcessDefinitionFromFile_(File file){
		InputStream is = null;
		BufferedReader reader = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("org.runbpm.bpmn.definition");
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			Definitions definitions = (Definitions) jaxbUnmarshaller.unmarshal(file);
			
			ProcessDefinition process = definitions.getProcess();
			
			//解析流程监听器
			parseProcessListener(process);
			
			ProcessModel processModel = new  ProcessModelImpl();
			processModel.setProcessDefinition(process);
			processModel.setProcessDefinitionId(process.getId());
			
			StringBuffer buffer = new StringBuffer();
	        is = new FileInputStream(file);
	        String line; // 用来保存每行读取的内容
	        reader = new BufferedReader(new InputStreamReader(is));
	        line = reader.readLine(); // 读取第一行
	        while (line != null) { // 如果 line 为空说明读完了
	            buffer.append(line); // 将读到的内容添加到 buffer 中
	            buffer.append("\n"); // 添加换行符
	            line = reader.readLine(); // 读取下一行
	        }
	        processModel.setXmlcontent(buffer.toString());
			
			return processModel;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	        
		}
	}
	
	
	
	protected void parseProcessListener(ProcessDefinition process) {
		//---listener
		ExtensionElements extensionElements = process.getExtensionElements();
		List<ExtensionExecutionListener> processlistenerList = extensionElements.getListenerList();
		for(ExtensionExecutionListener processListener:processlistenerList){
			Event_Type eventType = processListener.getEvent();
			String classValue = processListener.getClassValue();
			ListenerManager.getListenerManager().registerProcessInstanceListener(process.getId(), classValue, eventType);
		}
		
		//注册活动以及UserTask的监听
		registerListeners(process);
		//---//listener
		
	}

	private void registerListeners(ProcessDefinition process) {
		List<ActivityDefinition> activityList = process.getActivityList();
		for (ActivityDefinition activityDefinition : activityList) {
			String pid = process.getId();
			String aid = activityDefinition.getId();
			
			ExtensionElements elements = activityDefinition.getExtensionElements();
			 List<ExtensionExecutionListener> listenerList = elements.getListenerList();
			 for (ExtensionExecutionListener extensionExecutionListener : listenerList) {
					Event_Type eventType = extensionExecutionListener.getEvent();
					String classValue = extensionExecutionListener.getClassValue();
					if(eventType.toString().indexOf("Activity")!=-1){
						ListenerManager.getListenerManager().registerActivityInstanceListener(pid+":"+aid, classValue, eventType);
					}else if(eventType.toString().indexOf("UserTask")!=-1){
						ListenerManager.getListenerManager().registerUserTaskListener(pid+":"+aid, classValue, eventType);
					}
			}
			 
			 //快活动SubProcess嵌套调动
			 if(activityDefinition instanceof SubProcessDefinition){
				 SubProcessDefinition processDefinition = (SubProcessDefinition)activityDefinition;
				 registerListeners(processDefinition);
			 }
		}
			
		
	}

	public ProcessModel loadProcessModelByModelId(long processModelId){
		ProcessModel processModel = processModelMap.get(processModelId);
		return processModel; 
	}
	
	
	@Override
	public void setProcessVariableMap(long processInstanceId,Map<String, Object>dataFieldMap) {
		if(dataFieldMap!=null){
			Iterator<String> it = dataFieldMap.keySet().iterator();
			while(it.hasNext()){
				String name = it.next();
				Object value = dataFieldMap.get(name);
				this.setProcessVariable(processInstanceId, name, value);
			}
		}
	}
	
	public ProcessModel loadLatestProcessModel(String processDefinitionId){
		ProcessModel processModel = null;
		
		for(Map.Entry<Long, ProcessModel> entry:processModelMap.entrySet()){
			ProcessModel processModelObject = entry.getValue();
			if(processModelObject.getProcessDefinitionId().equals(processDefinitionId)){
				if(processModel==null || processModel.getId()<processModelObject.getId()){
					processModel = processModelObject;
				}
			}
		}
		if(processModel == null){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_100002_Invalid_ProcessModel,processDefinitionId);
		}
		
		return processModel; 
	}
	
	public List<ActivityInstance> getActivityInstanceByProcessInstIdAndState(long processInstanceId,EnumSet<ACTIVITY_STATE> stateSet) {
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		List<ActivityInstance> parentList = getActivityInstanceByProcessInstId(processInstanceId);
		for(ActivityInstance activityInstance:parentList){
			if(stateSet==null){
				activityList.add(activityInstance);
			}	else if(stateSet.contains(activityInstance.getState())){
				activityList.add(activityInstance);
			}
		}
		return activityList;
	}
	

	public List<ActivityInstance> getActivityInstanceByProcessInstIdSubrocessIdAndState(long processInstanceId,String subProcessId,EnumSet<ACTIVITY_STATE> stateSet){
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		List<ActivityInstance> parentList = getActivityInstanceByProcessInstId(processInstanceId);
		for(ActivityInstance activityInstance:parentList){
			if(subProcessId.equals(activityInstance.getSubProcessBlockId())){
				if(stateSet==null){
					activityList.add(activityInstance);
				}	else if(stateSet.contains(activityInstance.getState())){
					activityList.add(activityInstance);
				}
			}
		}
		return activityList;
	}
	
	public List<ActivityInstance> getActivityInstanceByActivityDefId(
			long processInstanceId, String activityDefinitionId) {
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		List<ActivityInstance> parentList = getActivityInstanceByProcessInstId(processInstanceId);
		for(ActivityInstance activityInstance:parentList){
			
			if(activityInstance.getActivityDefinitionId().equals(activityDefinitionId)){
				activityList.add(activityInstance);
			}
			
		}
		return activityList;
	}
	
	public List<ActivityInstance> getActivityInstanceByActivityDefIdAndState(long processInstanceId,String activityDefinitionId,EnumSet<ACTIVITY_STATE> stateSet) {
		List<ActivityInstance> activityList = new ArrayList<ActivityInstance>();
		List<ActivityInstance> parentList = getActivityInstanceByProcessInstId(processInstanceId);
		for(ActivityInstance activityInstance:parentList){
			
			//符合流程定义
			if(activityInstance.getActivityDefinitionId().equals(activityDefinitionId)){
				if(stateSet==null){
					activityList.add(activityInstance);
				}	else if(stateSet.contains(activityInstance.getState())){
					activityList.add(activityInstance);
				}
			}
			
		}
		return activityList;
	}
	
	public List<TaskInstance> getTaskInstanceByActivityInstIdAndState(long activityInstanceId,EnumSet<TASK_STATE> stateSet){
		List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>();
		List<TaskInstance> parentList = this.getTaskInstanceByActivityInstId(activityInstanceId);
		for(TaskInstance taskInstance:parentList){
			if(taskInstance.getActivityInstanceId()==activityInstanceId){
				if(stateSet==null){
					taskInstanceList.add(taskInstance);
				}	else if(stateSet.contains(taskInstance.getState())){
					taskInstanceList.add(taskInstance);
				}
			}
		}
		return taskInstanceList;
	}
	
	@Override
	public List<TaskInstance> getTaskInstanceByProcessInstId(
			long processInstanceId) {
		List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>();
		List<ActivityInstance> activityList = this.getActivityInstanceByProcessInstId(processInstanceId);
		
		for(ActivityInstance activityInstance:activityList){
			taskInstanceList.addAll(this.getTaskInstanceByActivityInstId(activityInstance.getId()));
		}
		return taskInstanceList;
	}
	
	@Override
	public List<TaskInstance> getTaskInstanceByUserIdAndState(String userId,
			EnumSet<TASK_STATE> stateSet) {
		List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>();
		List<TaskInstance> parentList = this.getTaskInstanceByUserId(userId);
		for(TaskInstance taskInstance:parentList){
			if(stateSet==null){
				taskInstanceList.add(taskInstance);
			}	else if(stateSet.contains(taskInstance.getState())){
				taskInstanceList.add(taskInstance);
			}
		}
		return taskInstanceList;	
	}
	
	@Override
	public VariableInstance getVariableInstance(long processInstanceId,
			String name) {
		Map<String,VariableInstance> variableInstanceMap =this.getVariableMap(processInstanceId);
		if(variableInstanceMap!=null&&(!variableInstanceMap.isEmpty())){
			VariableInstance dataFieldInstance = variableInstanceMap.get(name);
			return dataFieldInstance;
		}else{ 
			return null;
		}
	}
	
}
