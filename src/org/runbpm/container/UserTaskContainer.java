package org.runbpm.container;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.EntityManager;

public class UserTaskContainer {

	
	private ActivityInstance activityInstance;
	private UserTask userTask;
	private TaskInstance taskInstance;
	
	public static UserTaskContainer getUserTaskContainer(TaskInstance taskInstance) {
		return new UserTaskContainer(taskInstance);
	}
	
	public UserTaskContainer(ActivityInstance activityInstance, UserTask userTask, TaskInstance taskInstance){
		this.activityInstance = activityInstance;
		this.userTask = userTask;
		this.taskInstance = taskInstance;
	}
	
	public UserTaskContainer(TaskInstance taskInstance){
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		this.activityInstance = entityManager.loadActivityInstance(taskInstance.getActivityInstanceId());
		ProcessModel processModel = entityManager.loadProcessModelByModelId(taskInstance.getProcessModelId());
		this.userTask = (UserTask) processModel.getProcessDefinition().getActivity(taskInstance.getActivityDefinitionId());
		this.taskInstance = taskInstance;
	}
	
	public void start(){
		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeUserTaskStarted);
		//before event end	
		
		//TODO create app instance
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.afterUserTaskStarted);
		//before event end	
			
	}
	
	public void suspend(){
		
		EntityConstants.TASK_STATE currentState = this.taskInstance.getState();
		if(currentState.equals(EntityConstants.TASK_STATE.NOT_STARTED)||currentState.equals(EntityConstants.TASK_STATE.RUNNING)){
			//befor event begin
			invokelistener(ListenerManager.Event_Type.beforeUserTaskSuspended);
			//before event end	
			
			this.taskInstance.setStateBeforeSuspend(currentState);
			this.taskInstance.setModifyDate(new Date());
			taskInstance.setState(EntityConstants.TASK_STATE.SUSPENDED);
			

			//after event begin
			invokelistener(ListenerManager.Event_Type.afterUserTaskSuspended);
			//after event end
		}else{
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020012_INVALID_TASK_TO_SUSPEND);
		}
		
		
		
	}
	
	public void resume(){

		
		if(this.taskInstance.getState().equals(EntityConstants.TASK_STATE.SUSPENDED)){
			//befor event begin
			invokelistener(ListenerManager.Event_Type.beforeUserTaskResumed);
			//before event end	
			
			EntityConstants.TASK_STATE stateBeforeSuspend = this.taskInstance.getStateBeforeSuspend();
			taskInstance.setModifyDate(new Date());
			taskInstance.setState(stateBeforeSuspend);
			
			//befor event begin
			invokelistener(ListenerManager.Event_Type.afterUserTaskResumed);
			//before event end
		}else{
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020015_INVALID_TASKINSTANCE_TO_RESUME);
			
		}
	}
	
	public void complete(){
		
		if(!this.taskInstance.getState().equals(EntityConstants.TASK_STATE.RUNNING)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020008_Cannot_Complete_Task_for_Invalid_State,"当前状态是："+taskInstance.getState());
		}
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeUserTaskCompleted);
		//before event end	
		
		completeTaskInstance(null,EntityConstants.TASK_STATE.COMPLETED,true);
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.afterUserTaskCompleted);
		//before event end
		
	}
	
	public void terminate(){
		
		terminate(null);
		
	}
	
	//该方法可能被活动的终止调用，而活动的终止可能带有目标活动定义对象，所以传入
	public void terminate(ActivityDefinition targetActivityDefinition) {
		if(this.taskInstance.getState().equals(EntityConstants.TASK_STATE.TERMINATED)||this.taskInstance.getState().equals(EntityConstants.TASK_STATE.CANCELED)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020008_Cannot_Complete_Task_for_Invalid_State,"当前状态是："+taskInstance.getState());
		}

		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeUserTaskTerminated);
		//before event end	
		
		completeTaskInstance(targetActivityDefinition,EntityConstants.TASK_STATE.TERMINATED,true);
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.afterUserTaskTerminated);
		//before event end
	}
	
	//与取消不同，remove方法直接删除工作项目实例
	public void remove() {
		
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		//删除其他任务
		entityManager.removeTaskInstance(this.taskInstance.getId());
	}
	
	//finishPolicy true:自动提交活动 false:不提交活动
	private void completeTaskInstance(ActivityDefinition targetActivityDefinition,EntityConstants.TASK_STATE completeState,boolean finishPolicy){
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		this.taskInstance.setState(completeState);
		this.taskInstance.setCompleteDate(new Date());
		this.taskInstance.setModifyDate(new Date());
		
		EnumSet<EntityConstants.TASK_STATE> set = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
        set.add(EntityConstants.TASK_STATE.NOT_STARTED);  
        set.add(EntityConstants.TASK_STATE.RUNNING);
        
        //在自由模式下，如果该活动下没有活跃的工作项，则结束，
        if(finishPolicy) {
	        List<TaskInstance> notCompleteTaskInstanceList = entityManager.listTaskInstanceByActivityInstIdAndState(taskInstance.getActivityInstanceId(), set);
	        if(notCompleteTaskInstanceList.size()==0){
		        	ActivityInstance activityInstance = entityManager.loadActivityInstance(taskInstance.getActivityInstanceId());
		        	ActivityContainer activityContainer = ContainerTool.getActivityContainer(activityInstance);
		        	if(completeState.equals(TASK_STATE.TERMINATED)){
		        		activityContainer.terminate(targetActivityDefinition);
		        	}else if(completeState.equals(TASK_STATE.COMPLETED)){
		        		activityContainer.complete();
		        	}
		     }
        }
	}
	
	public void claim(){
		
		if(!this.taskInstance.getState().equals(EntityConstants.TASK_STATE.NOT_STARTED)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020007_Cannot_Claim_Task_for_Invalid_State,"当前状态是："+taskInstance.getState());
		}

		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeUserTaskClaimed);
		//before event end	
		this.taskInstance.setModifyDate(new Date());
		this.taskInstance.setState(EntityConstants.TASK_STATE.RUNNING);
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		//删除其他任务
		entityManager.removeTaskInstanceExceptClaimInstance(this.taskInstance);
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.afterUserTaskClaimed);
		//before event end
	}
	
	public void setAssignee(String userId){

		if(!this.taskInstance.getState().equals(EntityConstants.TASK_STATE.RUNNING)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020007_Cannot_setAssignee_Task_for_Invalid_State,"当前状态是："+taskInstance.getState());
		}
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeUserTaskReassigned);
		//before event end	
		this.taskInstance.setModifyDate(new Date());
		this.taskInstance.setUserId(userId);
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.afterUserTaskReassigned);
		//before event end
	}
	
	private void invokelistener(ListenerManager.Event_Type listenerType) {
		String pid = this.taskInstance.getProcessModelId()+"";
		String uid = userTask.getId();
		if(ListenerManager.getListenerManager().haveTaskEvent(pid+":"+uid,listenerType)){
			ProcessContextBean processContextBean = new ProcessContextBean();
			ProcessInstance processInstance = Configuration.getContext().getEntityManager().loadProcessInstance(activityInstance.getProcessInstanceId());
			//对于afterComplete事件，有可能到历史库中
			if(processInstance!=null){
				Map<String,VariableInstance> variableMap = Configuration.getContext().getEntityManager().loadVariableMap(processInstance.getId());
				processContextBean.setVariableMap(variableMap);
			}
			processContextBean.setUserTask(userTask);
			processContextBean.setTaskInstance(taskInstance);
			processContextBean.setActivityDefinition(userTask);
			processContextBean.setActivityInstance(activityInstance);
			ListenerManager.getListenerManager().invokeTaskListener(processContextBean, listenerType);
		}
	}
	
	//放回工作项
	public void putBack(){
		//活动不是running状态不允许放回
		if(!this.taskInstance.getState().equals(EntityConstants.TASK_STATE.RUNNING)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020007_Cannot_putback_Task_for_Invalid_State,"当前状态是："+taskInstance.getState());
		}
		//会签不允许放回
		if(this.userTask.isMulti()){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020007_Cannot_putback_Task_for_Multi_UserTask);
		}
		
		//设置该任务状态为终止
		
		taskInstance.setModifyDate(new Date());
		taskInstance.setState(EntityConstants.TASK_STATE.CANCELED);
		
		//重启活动
		ActivityContainer activityContainer =  ContainerTool.getActivityContainer(activityInstance);
		activityContainer.start();
		
	}

	public void cancel() {
		if(this.taskInstance.getState().equals(EntityConstants.TASK_STATE.TERMINATED)||this.taskInstance.getState().equals(EntityConstants.TASK_STATE.CANCELED)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020008_Cannot_Complete_Task_for_Invalid_State,"当前状态是："+taskInstance.getState());
		}

		//befor event begin
		invokelistener(ListenerManager.Event_Type.beforeUserTaskCanceled);
		//before event end	
		
		completeTaskInstance(null,EntityConstants.TASK_STATE.CANCELED,false);
		
		//befor event begin
		invokelistener(ListenerManager.Event_Type.afterUserTaskCanceled);
		//before event end

		
	}
	
	
}
