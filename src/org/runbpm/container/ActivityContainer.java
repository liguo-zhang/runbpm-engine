package org.runbpm.container;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.EndEvent;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ActivityContainer {

	private static final Logger logger = LoggerFactory.getLogger(ActivityContainer.class);
	
	protected ActivityInstance activityInstance;
	
	protected ActivityDefinition activityDefinition;
	
	public ActivityContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		this.activityDefinition =  activityDefinition;
		this.activityInstance =  activityInstance;
	}

	public void start(){
		logger.debug("begin ActivityContainer start");
		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeActivityInstanceStarted);
		//before event end		
		
		this.activityStart();
		
		//after event begin
		invokelistener(ListenerManager.Event_Type.afterActivityInstanceStarted);
		//after event end	
		logger.debug("end  ActivityContainer start");
	}
	
	public abstract void activityStart();
	
	public void suspend(){
		//before event begin
		invokelistener(ListenerManager.Event_Type.beforeActivityInstanceSuspended);
		//before event end
		
		ACTIVITY_STATE currentState = this.activityInstance.getState();
		if(currentState.equals(ACTIVITY_STATE.NOT_STARTED)||currentState.equals(ACTIVITY_STATE.RUNNING)){
			this.activityInstance.setStateBeforeSuspend(currentState);
			activityInstance.setState(ACTIVITY_STATE.SUSPENDED);
		}else{
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020011_INVALID_ACTIVITYINSTANCE_TO_SUSPEND);
		}
		
		//after event begin
		invokelistener(ListenerManager.Event_Type.afterActivityInstanceSuspended);
		//after event end
	}
	
	public void resume(){
		//before event begin
		invokelistener(ListenerManager.Event_Type.beforeActivityInstanceResumed);
		//before event end
		
		if(this.activityInstance.getState().equals(ACTIVITY_STATE.SUSPENDED)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020014_INVALID_ACTIVITYINSTANCE_TO_RESUME);
		}else{
			ACTIVITY_STATE stateBeforeSuspend = this.activityInstance.getStateBeforeSuspend();
			activityInstance.setState(stateBeforeSuspend);
		}
		
		//after event begin
		invokelistener(ListenerManager.Event_Type.afterActivityInstanceResumed);
		//after event end
	}
	
	//正常结束
	public void complete(){
		
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
        //已经结束的活动不能执行完成操作
		EnumSet<EntityConstants.ACTIVITY_STATE> notActive = EnumSet.noneOf(EntityConstants.ACTIVITY_STATE.class);  
		notActive.add(EntityConstants.ACTIVITY_STATE.COMPLETED);  
		notActive.add(EntityConstants.ACTIVITY_STATE.TERMINATED);
		if(notActive.contains(this.activityInstance.getState())){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020016_INVALID_ACTIVITYINSTANCE_TO_COMPLETE);
		}
		
        //如果为UserTask，则需要判断没有未完成的工作项,否则抛出异常
		
        if(this.activityDefinition instanceof UserTask){
        	EnumSet<EntityConstants.TASK_STATE> set = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
            set.add(EntityConstants.TASK_STATE.NOT_STARTED);  
            set.add(EntityConstants.TASK_STATE.RUNNING);
			List<TaskInstance> notCompleteTaskInstanceList = entityManager.listTaskInstanceByActivityInstIdAndState(this.activityInstance.getId(), set);
	        if(notCompleteTaskInstanceList.size()>0){
	        	throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020020_WORKITEM_NOT_COMPLETE);
	        }
        }
        
        //before event begin
		invokelistener(ListenerManager.Event_Type.beforeActivityInstanceCompleted);
		//before event end
		
    	commit_internal(null,ACTIVITY_STATE.COMPLETED);

		//after event begin 此时可能流程已经到历史库。
		invokelistener(ListenerManager.Event_Type.afterActivityInstanceCompleted);
		//after event end
	}
	
	//异常终止
	public void terminate(){
		terminate(null);
	}
	
	//异常终止，而且提交到一个目标节点定义，如果有未结束的UserTask，则将UserTask也终止
	public void terminate(ActivityDefinition targetActivityDefinition){
		//已经结束的活动不能执行完成操作
		EnumSet<EntityConstants.ACTIVITY_STATE> notActive = EnumSet.noneOf(EntityConstants.ACTIVITY_STATE.class);  
		notActive.add(EntityConstants.ACTIVITY_STATE.COMPLETED);  
		notActive.add(EntityConstants.ACTIVITY_STATE.TERMINATED);
		if(notActive.contains(this.activityInstance.getState())){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020017_INVALID_ACTIVITYINSTANCE_TO_TERMINATE);
		}
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		//看看有无没有完成的TaskInstance
		//如果为UserTask，则需要判断没有未完成的工作项,有的话需要逐一终止；最后一个终止工作项，不会自动提交活动
		boolean commit_internal = true;
        if(this.activityDefinition instanceof UserTask){
        	EnumSet<EntityConstants.TASK_STATE> set = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
            set.add(EntityConstants.TASK_STATE.NOT_STARTED);  
            set.add(EntityConstants.TASK_STATE.RUNNING);
			List<TaskInstance> notCompleteTaskInstanceList = entityManager.listTaskInstanceByActivityInstIdAndState(this.activityInstance.getId(), set);
	        if(notCompleteTaskInstanceList.size()>0){
	            	//放置标志位，使得，最后一个uc,不会自动调用活动终止
	        	    commit_internal = false;
	    		    for (TaskInstance taskInstance : notCompleteTaskInstanceList) {
	    		    	    UserTaskContainer uc = new UserTaskContainer(taskInstance);
	    			    uc.terminate(targetActivityDefinition);
				}
	        }
        }
		
        if(commit_internal){
	        	//before event begin
	    		if(targetActivityDefinition==null){
	    			invokelistener(ListenerManager.Event_Type.beforeActivityInstanceTerminated);
	    		}else{
	    			invokelistener(ListenerManager.Event_Type.beforeActivityInstanceTerminateAndTargeted);
	    		}
	    		//before event end	
	    		
	    		commit_internal(targetActivityDefinition,ACTIVITY_STATE.TERMINATED);
	    		
	    		//before event begin
	    		if(targetActivityDefinition==null){
	    			invokelistener(ListenerManager.Event_Type.afterActivityInstanceTerminated);
	    		}else{
	    			invokelistener(ListenerManager.Event_Type.afterActivityInstanceTerminateAndTargeted);
	    		}
	    		//before event end
        }
		
	}
	
	//所有类型的节点的结束，都会调用该方法
	private void commit_internal(ActivityDefinition targetActivityDefinition,ACTIVITY_STATE commitState){
		activityInstance.setState(commitState);
		activityInstance.setCompleteDate(new Date());
		
		ProcessInstance processInstance = Configuration.getContext().getEntityManager().loadProcessInstance(activityInstance.getProcessInstanceId());
		
		FlowContainer flowContainer = ContainerTool.getFlowContainer(processInstance,activityInstance);
		if (activityDefinition instanceof EndEvent && targetActivityDefinition==null) {
			flowContainer.complete();
		}else{
			
			flowContainer.flowToNext(activityInstance,activityDefinition,targetActivityDefinition);
		}
	}
	
	
	
	private void invokelistener(ListenerManager.Event_Type eventType) {
		ProcessContextBean processContextBean = null;
		String pid = this.activityInstance.getProcessModelId()+"";
		String aid = activityDefinition.getId();
		if(ListenerManager.getListenerManager().haveActivityEvent(pid+":"+aid,eventType)){
			processContextBean = new ProcessContextBean();
			
			//如果是afterActivityInstanceCompleted类型，此时可能已经到历史库，processInstance为null
			ProcessInstance processInstance = Configuration.getContext().getEntityManager().loadProcessInstance(activityInstance.getProcessInstanceId());
			if(processInstance!=null){
				Map<String,VariableInstance> variableMap = Configuration.getContext().getEntityManager().loadVariableMap(processInstance.getId());
				processContextBean.setVariableMap(variableMap);
			}
			processContextBean.setProcessInstance(processInstance);
			processContextBean.setProcessDefinition(activityDefinition.getProcessDefinition());
			processContextBean.setActivityDefinition(activityDefinition);
			processContextBean.setActivityInstance(activityInstance);
			ListenerManager.getListenerManager().invokeActivityListener(processContextBean, eventType);
		}
	}
}
