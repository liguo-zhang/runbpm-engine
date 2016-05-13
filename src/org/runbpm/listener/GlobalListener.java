package org.runbpm.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager.Event_Type;

public class GlobalListener {

	private Map<String, Set<ListenerInterface>> processMap = new HashMap<String,Set<ListenerInterface>>();
	private Map<String, Set<ListenerInterface>> activityMap = new HashMap<String,Set<ListenerInterface>>();
	private Map<String, Set<ListenerInterface>> userTaskMap = new HashMap<String,Set<ListenerInterface>>();
	
	public void setProcessMap(Map<String, Set<ListenerInterface>> processMap) {
		for(Map.Entry<String, Set<ListenerInterface>> entry:processMap.entrySet()){
			String key = entry.getKey();
			Event_Type[] arrays = Event_Type.values();
			boolean contains = false;
			for(Event_Type at : arrays){
				if(at.toString().equals(key)){
					contains = true;
					break;
				}
			}
			if(!contains){
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020304_INVALID_LISTENER_TYPE);
			}
		}
		this.processMap = processMap;
	}

	public void setActivityMap(Map<String, Set<ListenerInterface>> activityMap) {
		for(Map.Entry<String, Set<ListenerInterface>> entry:activityMap.entrySet()){
			String key = entry.getKey();
			Event_Type[] arrays = Event_Type.values();
			boolean contains = false;
			for(Event_Type at : arrays){
				if(at.toString().equals(key)){
					contains = true;
					break;
				}
			}
			if(!contains){
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020304_INVALID_LISTENER_TYPE);
			}
		}
		this.activityMap = activityMap;
	}

	public void setUserTaskMap(Map<String, Set<ListenerInterface>> userTaskMap) {
		for(Map.Entry<String, Set<ListenerInterface>> entry:userTaskMap.entrySet()){
			String key = entry.getKey();
			Event_Type[] arrays = Event_Type.values();
			boolean contains = false;
			for(Event_Type at : arrays){
				if(at.toString().equals(key)){
					contains = true;
					break;
				}
			}
			if(!contains){
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020304_INVALID_LISTENER_TYPE);
			}
		}
		this.userTaskMap = userTaskMap;
	}

	@SuppressWarnings(value={"rawtypes"})
	public void addListener(Enum listenerType,ListenerInterface instanceListener){
		//if(Arrays.asList(Event_Type.values()).contains(listenerType)){
		if(listenerType.toString().indexOf("Process")!=-1){
			addProcessListener(listenerType.name(),instanceListener);
		}else if(listenerType.toString().indexOf("Activity")!=-1){
			addActivityListener(listenerType.name(),instanceListener);
		}else if(listenerType.toString().indexOf("UserTask")!=-1){
			addUserTaskListener(listenerType.name(),instanceListener);
		}else{
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020304_INVALID_LISTENER_TYPE);
		}
	}

	private void addProcessListener(String key,ListenerInterface instanceListener){
		Set<ListenerInterface> set = processMap.get(key);
		if(set==null){
			set = new HashSet<ListenerInterface>();
			processMap.put(key, set);
		}
		set.add(instanceListener);
	}


	private void addActivityListener(String key,ListenerInterface instanceListener){
		Set<ListenerInterface> set = activityMap.get(key);
		if(set==null){
			set = new HashSet<ListenerInterface>();
			activityMap.put(key, set);
		}
		set.add(instanceListener);
	}
	
	private void addUserTaskListener(String key,ListenerInterface instanceListener){
		Set<ListenerInterface> set = userTaskMap.get(key);
		if(set==null){
			set = new HashSet<ListenerInterface>();
			userTaskMap.put(key, set);
		}
		set.add(instanceListener);
	}

	
	public Map<String, Set<ListenerInterface>> getProcessMap() {
		return processMap;
	}

	public Map<String, Set<ListenerInterface>> getActivityMap() {
		return activityMap;
	}

	public Map<String, Set<ListenerInterface>> getUserTaskMap() {
		return userTaskMap;
	}
	
	public void clearGlobalListenerSet() {
		processMap.clear();
		activityMap.clear();
		userTaskMap.clear();
	}
}
