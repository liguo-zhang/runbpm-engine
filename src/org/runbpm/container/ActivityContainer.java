package org.runbpm.container;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.EndEvent;
import org.runbpm.bpmn.definition.ExclusiveGateway;
import org.runbpm.bpmn.definition.InclusiveGateway;
import org.runbpm.bpmn.definition.ManualTask;
import org.runbpm.bpmn.definition.ParallelGateway;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.ServiceTask;
import org.runbpm.bpmn.definition.StartEvent;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.context.Configuration;
import org.runbpm.context.Execution;
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
	
	//��������
	public void complete(){
		
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
        //�Ѿ������Ļ����ִ����ɲ���
		EnumSet<EntityConstants.ACTIVITY_STATE> notActive = EnumSet.noneOf(EntityConstants.ACTIVITY_STATE.class);  
		notActive.add(EntityConstants.ACTIVITY_STATE.COMPLETED);  
		notActive.add(EntityConstants.ACTIVITY_STATE.TERMINATED);
		if(notActive.contains(this.activityInstance.getState())){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020016_INVALID_ACTIVITYINSTANCE_TO_COMPLETE);
		}
		
        //���ΪUserTask������Ҫ�ж�û��δ��ɵĹ�����,�����׳��쳣
		
        if(this.activityDefinition instanceof UserTask){
        	EnumSet<EntityConstants.TASK_STATE> set = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
            set.add(EntityConstants.TASK_STATE.NOT_STARTED);  
            set.add(EntityConstants.TASK_STATE.RUNNING);
			List<TaskInstance> notCompleteTaskInstanceList = entityManager.getTaskInstanceByActivityInstIdAndState(this.activityInstance.getId(), set);
	        if(notCompleteTaskInstanceList.size()>0){
	        	throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020016_WORKITEM_NOT_COMPLETE);
	        }
        }
        
        //before event begin
		invokelistener(ListenerManager.Event_Type.beforeActivityInstanceCompleted);
		//before event end
		
    	commit_internal(null,ACTIVITY_STATE.COMPLETED);

		//after event begin
		invokelistener(ListenerManager.Event_Type.afterActivityInstanceCompleted);
		//after event end
	}
	
	//�쳣��ֹ
	public void terminate(){
		terminate(null);
	}
	
	//�쳣��ֹ�������ύ��һ��Ŀ��ڵ㶨�壬�����δ������UserTask����UserTaskҲ��ֹ
	public void terminate(ActivityDefinition targetActivityDefinition){
		//�Ѿ������Ļ����ִ����ɲ���
		EnumSet<EntityConstants.ACTIVITY_STATE> notActive = EnumSet.noneOf(EntityConstants.ACTIVITY_STATE.class);  
		notActive.add(EntityConstants.ACTIVITY_STATE.COMPLETED);  
		notActive.add(EntityConstants.ACTIVITY_STATE.TERMINATED);
		if(notActive.contains(this.activityInstance.getState())){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020017_INVALID_ACTIVITYINSTANCE_TO_TERMINATE);
		}
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		//��������û����ɵ�TaskInstance
		//���ΪUserTask������Ҫ�ж�û��δ��ɵĹ�����,�еĻ���Ҫ��һ��ֹ�����ִ����ֹ���������ִ�����һ����ֹ��������Զ��ύ�
		boolean commit_internal = true;
        if(this.activityDefinition instanceof UserTask){
        	EnumSet<EntityConstants.TASK_STATE> set = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
            set.add(EntityConstants.TASK_STATE.NOT_STARTED);  
            set.add(EntityConstants.TASK_STATE.RUNNING);
			List<TaskInstance> notCompleteTaskInstanceList = entityManager.getTaskInstanceByActivityInstIdAndState(this.activityInstance.getId(), set);
	        if(notCompleteTaskInstanceList.size()>0){
	        	//���һ��uc���Զ����û��ֹ
	        	commit_internal = false;
	    		for (TaskInstance taskInstance : notCompleteTaskInstanceList) {
	    			UserTaskContainer uc = new UserTaskContainer(taskInstance);
	    			uc.terminate();
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
	
	//�������͵Ľڵ�Ľ�����������ø÷���
	private void commit_internal(ActivityDefinition targetActivityDefinition,ACTIVITY_STATE commitState){
		activityInstance.setState(commitState);
		activityInstance.setCompleteDate(new Date());
		
		ProcessInstance processInstance = Configuration.getContext().getEntityManager().getProcessInstance(activityInstance.getProcessInstanceId());
		
		FlowContainer flowContainer = ProcessContainer.getFlowContainer(processInstance,activityInstance);
		if (activityDefinition instanceof EndEvent && targetActivityDefinition==null) {
			flowContainer.complete();
		}else{
			
			flowContainer.flowToNext(activityInstance,activityDefinition,targetActivityDefinition);
		}
	}
	
	
	
	public static ActivityContainer getActivityContainer(ActivityInstance activityInstance){
		ProcessDefinition processDefinition= Configuration.getContext().getEntityManager().loadProcessModelByModelId(activityInstance.getProcessModelId()).getProcessDefinition();
		ActivityDefinition activityDefinition = null;
		if(activityInstance.getParentActivityInstanceId()!=0){
			//��
			ActivityInstance parentActivityInstance = Configuration.getContext().getEntityManager().getActivityInstance(activityInstance.getParentActivityInstanceId());
			SubProcessDefinition subProcessDefinition =processDefinition.getSubProcessActivityDefinition(parentActivityInstance.getSequenceBlockId());
			if(subProcessDefinition==null){
				throw new RunBPMException("cannot find subprocess .blockId["+activityInstance.getSubProcessBlockId()+"],current activity definitionid:["+activityInstance.getActivityDefinitionId()+"]");
			}
			activityDefinition = subProcessDefinition.getActivity(activityInstance.getActivityDefinitionId());
		}else{
			activityDefinition = processDefinition.getActivity(activityInstance.getActivityDefinitionId());
		}
		
		if(activityDefinition instanceof StartEvent 
				|| activityDefinition instanceof EndEvent 
				|| activityDefinition instanceof ManualTask 
				|| activityDefinition instanceof ParallelGateway
				|| activityDefinition instanceof ExclusiveGateway
				||activityDefinition instanceof InclusiveGateway
				){
			return new ActivityOfRouteContainer(activityDefinition,activityInstance); 
		}else if(activityDefinition instanceof UserTask){
			return new ActivityOfUserTaskContainer(activityDefinition,activityInstance);
		}else if(activityDefinition instanceof CallActivity){
			return new ActivityOfCallActivityContainer(activityDefinition,activityInstance);
		}else if(activityDefinition instanceof ServiceTask){
			return new ActivityOfServiceTaskContainer(activityDefinition,activityInstance);
		}else if(activityDefinition instanceof SubProcessDefinition){
			return new ActivityOfSubProcessContainer(activityDefinition,activityInstance);
		}
		
		throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_100001_Invalid_ActityDefinition_Type,"���Ϸ��Ļ���壬��������Ϊ:["+activityDefinition.getClass()+"]");
	}
	
	private void invokelistener(ListenerManager.Event_Type eventType) {
		Execution handlerContext = null;
		String pid = activityDefinition.getProcessDefinition().getId();
		String aid = activityDefinition.getId();
		if(ListenerManager.getListenerManager().haveActivityEvent(pid+":"+aid,eventType)){
			handlerContext = new Execution();
			ProcessInstance processInstance = Configuration.getContext().getEntityManager().getProcessInstance(activityInstance.getProcessInstanceId());
			Map<String,VariableInstance> variableMap = Configuration.getContext().getEntityManager().getVariableMap(processInstance.getId());
			handlerContext.setVariableMap(variableMap);
			handlerContext.setProcessInstance(processInstance);
			handlerContext.setProcessDefinition(activityDefinition.getProcessDefinition());
			handlerContext.setActivityDefinition(activityDefinition);
			handlerContext.setActivityInstance(activityInstance);
			ListenerManager.getListenerManager().invokeActivityListener(handlerContext, eventType);
		}
	}
}
