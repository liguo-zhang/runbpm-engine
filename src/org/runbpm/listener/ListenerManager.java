package org.runbpm.listener;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.runbpm.context.Configuration;
import org.runbpm.context.Execution;
import org.runbpm.exception.RunBPMException;

public class ListenerManager {

	public enum Event_Type{
		beforeProcessInstanceCreated,
		afterProcessInstanceCreated,
		beforeProcessInstanceStarted,
		afterProcessInstanceStarted,
		beforeProcessInstanceTerminated,
		afterProcessInstanceTerminated,
		beforeProcessInstanceSuspended,
		afterProcessInstanceSuspended,
		beforeProcessInstanceResumed,
		afterProcessInstanceResume,
		beforeProcessInstanceCompleted,
		afterProcessInstanceCompleted,
	
		beforeActivityInstanceStarted,
		afterActivityInstanceStarted,
		beforeActivityInstanceTerminated,
		afterActivityInstanceTerminated,
		beforeActivityInstanceSuspended,
		afterActivityInstanceSuspended,
		beforeActivityInstanceResumed,
		afterActivityInstanceResumed,
		beforeActivityInstanceCompleted,
		afterActivityInstanceCompleted,
		beforeActivityInstanceTerminateAndTargeted,
		afterActivityInstanceTerminateAndTargeted,
	
		beforeUserTaskStarted,
		afterUserTaskStarted,
		beforeUserTaskClaimed,
		afterUserTaskClaimed,
		beforeUserTaskTerminated,
		afterUserTaskTerminated,
		beforeUserTaskSuspended,
		afterUserTaskSuspended,
		beforeUserTaskResumed,
		afterUserTaskResumed,
		beforeUserTaskCompleted,
		afterUserTaskCompleted,
		beforeUserTaskReassigned,
		afterUserTaskReassigned
	};
	
	private static ListenerManager listenerManager = new ListenerManager();
	
	public static ListenerManager getListenerManager(){
		return listenerManager;
	}
	
	private Map<String,HashMap<String,Set<ListenerInterface>>> processListenerMap = new HashMap<String,HashMap<String,Set<ListenerInterface>>>();
	//key是 "流程定义ID:活动定义ID"
	private Map<String,HashMap<String,Set<ListenerInterface>>> activityListenerMap = new HashMap<String,HashMap<String,Set<ListenerInterface>>>();
	//key是 "流程定义ID:活动定义ID(UserTask ID)"
	private Map<String,HashMap<String,Set<ListenerInterface>>> taskListenerMap = new HashMap<String,HashMap<String,Set<ListenerInterface>>>();
	
	//供局部的流程定义调用
	public void registerProcessInstanceListener(String definitionId,String listenerClassName,Event_Type listenerType){
		ListenerInterface processInstanceListener = null;
		try {
			processInstanceListener = (ListenerInterface) Class.forName(listenerClassName).newInstance();
		} catch (Exception e) {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020303_INVALID_LISTENER_CLASSNAME,e);
		} 
		registerProcess(definitionId,processInstanceListener,listenerType);
	}
	
	private void registerProcess(String definitionId,ListenerInterface processInstanceListener,Event_Type listenerType){
		HashMap<String,Set<ListenerInterface>> map = processListenerMap.get(definitionId);
		if(map==null){
			map = new HashMap<String,Set<ListenerInterface>>();
			processListenerMap.put(definitionId, map);
		}
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		if(listenerSet==null){
			listenerSet = new HashSet<ListenerInterface>();
			map.put(listenerType.name(), listenerSet);
		}
		listenerSet.add(processInstanceListener);
	}
	
	//供局部的活动定义调用
	public void registerActivityInstanceListener(String definitionId,String listenerClassName,Event_Type listenerType){
		ListenerInterface activityInstanceListener = null;
		try {
			activityInstanceListener = (ListenerInterface) Class.forName(listenerClassName).newInstance();
		} catch (Exception e) {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020303_INVALID_LISTENER_CLASSNAME,e);
		} 
		registerActivity(definitionId,activityInstanceListener,listenerType);
	}
	
	private void registerActivity(String definitionId,ListenerInterface activityInstanceListener,Event_Type listenerType){
		HashMap<String,Set<ListenerInterface>> map = activityListenerMap.get(definitionId);
		if(map==null){
			map = new HashMap<String,Set<ListenerInterface>>();
			activityListenerMap.put(definitionId, map);
		}
		Set<ListenerInterface> listenerSet = map.get(listenerType.toString());
		if(listenerSet==null){
			listenerSet = new HashSet<ListenerInterface>();
			map.put(listenerType.toString(), listenerSet);
		}
		listenerSet.add(activityInstanceListener);
	}
	//供局部的userTask调用
	public void registerUserTaskListener(String definitionId,String listenerClassName,Event_Type listenerType){
		ListenerInterface userTaskListener = null;
		try {
			userTaskListener = (ListenerInterface) Class.forName(listenerClassName).newInstance();
		} catch (Exception e) {
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020303_INVALID_LISTENER_CLASSNAME,e);
		} 
		registerUserTask(definitionId,userTaskListener,listenerType);
	}
	
	private void registerUserTask(String definitionId,ListenerInterface userTaskListener,Event_Type listenerType){
		HashMap<String,Set<ListenerInterface>> map = taskListenerMap.get(definitionId);
		if(map==null){
			map = new HashMap<String,Set<ListenerInterface>>();
			taskListenerMap.put(definitionId, map);
		}
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		if(listenerSet==null){
			listenerSet = new HashSet<ListenerInterface>();
			map.put(listenerType.name(), listenerSet);
		}
		listenerSet.add(userTaskListener);
	}
	
	public boolean haveProcessEvent(String processDefinitionId,Event_Type listenerType){
		HashMap<String,Set<ListenerInterface>> map = processListenerMap.get(processDefinitionId);
		if(map==null){
			map = new HashMap<String,Set<ListenerInterface>>();
			processListenerMap.put(processDefinitionId, map);
		}
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		if(listenerSet==null){
			listenerSet = new HashSet<ListenerInterface>();
			map.put(listenerType.name(), listenerSet);
		}
		Map<String, Set<ListenerInterface>> golbalMap = Configuration.getContext().getGlobalListener().getProcessMap();
		Set<ListenerInterface> gobalProcessInstanceEventSet =  golbalMap.get(listenerType.name());
		if(gobalProcessInstanceEventSet==null){
			gobalProcessInstanceEventSet = new HashSet<ListenerInterface>();
			golbalMap.put(listenerType.name(), gobalProcessInstanceEventSet);
		}
		return listenerSet.size()>0 || gobalProcessInstanceEventSet.size()>0;
	}
	
	public boolean haveActivityEvent(String processDefinitionIdAndActivityId,Event_Type listenerType){
		
		HashMap<String,Set<ListenerInterface>> map = activityListenerMap.get(processDefinitionIdAndActivityId);
		if(map==null){
			map = new HashMap<String,Set<ListenerInterface>>();
			activityListenerMap.put(processDefinitionIdAndActivityId, map);
		}
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		if(listenerSet==null){
			listenerSet = new HashSet<ListenerInterface>();
			map.put(listenerType.name(), listenerSet);
		}
		Map<String, Set<ListenerInterface>> golbalMap = Configuration.getContext().getGlobalListener().getActivityMap();
		Set<ListenerInterface> gobalActivityInstanceEventSet =  golbalMap.get(listenerType.name());
		if(gobalActivityInstanceEventSet==null){
			gobalActivityInstanceEventSet = new HashSet<ListenerInterface>();
			golbalMap.put(listenerType.name(), gobalActivityInstanceEventSet);
		}
		return listenerSet.size()>0 || gobalActivityInstanceEventSet.size()>0;
	}
	
	public boolean haveTaskEvent(String processDefinitionIdAndActivityId,Event_Type listenerType){
		//special
		HashMap<String,Set<ListenerInterface>> map = taskListenerMap.get(processDefinitionIdAndActivityId);
		if(map==null){
			map = new HashMap<String,Set<ListenerInterface>>();
			taskListenerMap.put(processDefinitionIdAndActivityId, map);
		}
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		if(listenerSet==null){
			listenerSet = new HashSet<ListenerInterface>();
			map.put(listenerType.name(), listenerSet);
		}
		
		//global
		Map<String, Set<ListenerInterface>> golbalMap = Configuration.getContext().getGlobalListener().getUserTaskMap();
		Set<ListenerInterface> gobalUserTaskInstanceEventSet =  golbalMap.get(listenerType.name());
		if(gobalUserTaskInstanceEventSet==null){
			gobalUserTaskInstanceEventSet = new HashSet<ListenerInterface>();
			golbalMap.put(listenerType.name(), gobalUserTaskInstanceEventSet);
		}
		return listenerSet.size()>0 || gobalUserTaskInstanceEventSet.size()>0;
	}
	
	public void invokeProcessListener(Execution handlerContext,Event_Type listenerType){
		String processDefinitionId = handlerContext.getProcessDefinition().getId();
		HashMap<String,Set<ListenerInterface>> map = processListenerMap.get(processDefinitionId);
		Set<ListenerInterface> processInstanceListenerSet = map.get(listenerType.name());
		this.invoke(handlerContext, processInstanceListenerSet, listenerType);
		
		Set<ListenerInterface> gobalProcessInstanceEventSet =  Configuration.getContext().getGlobalListener().getProcessMap().get(listenerType.name());
		this.invoke(handlerContext, gobalProcessInstanceEventSet, listenerType);
	}
	
	public void invokeActivityListener(Execution handlerContext,Event_Type listenerType){
		String processDefinitionId = handlerContext.getActivityDefinition().getProcessDefinition().getId();
		String activityId = handlerContext.getActivityDefinition().getId();
		HashMap<String,Set<ListenerInterface>> map = activityListenerMap.get(processDefinitionId+":"+activityId);
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		//触发
		this.invoke(handlerContext, listenerSet, listenerType);
		
		Set<ListenerInterface> gobalActivityInstanceEventSet =  Configuration.getContext().getGlobalListener().getActivityMap().get(listenerType.name());
		//触发
		this.invoke(handlerContext, gobalActivityInstanceEventSet, listenerType);
	}
	
	public void invokeTaskListener(Execution handlerContext,Event_Type listenerType){
		String processDefinitionId = handlerContext.getActivityDefinition().getProcessDefinition().getId();
		String activityId = handlerContext.getActivityDefinition().getId();
		HashMap<String,Set<ListenerInterface>> map = taskListenerMap.get(processDefinitionId+":"+activityId);
		Set<ListenerInterface> listenerSet = map.get(listenerType.name());
		//触发
		this.invoke(handlerContext, listenerSet, listenerType);
		
		Set<ListenerInterface> gobalTaskInstanceEventSet =  Configuration.getContext().getGlobalListener().getUserTaskMap().get(listenerType.name());
		//触发
		this.invoke(handlerContext, gobalTaskInstanceEventSet, listenerType);
	}
	
	@SuppressWarnings(value={"rawtypes"})
	private void invoke(Execution handlerContext,Set<ListenerInterface> set,Enum listenerType) {
		for(ListenerInterface instanceListener:set){
			Method method;
			try {
				
				method = ListenerInterface.class.getMethod("execute", Execution.class,Enum.class);
				method.invoke(instanceListener, handlerContext,listenerType);
			} catch (Exception e) {
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020302_INVOKE_LISTENER_EXCEPTION,e);
			}
		}
		
	}
}
