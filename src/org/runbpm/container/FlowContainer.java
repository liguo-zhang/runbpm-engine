package org.runbpm.container;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.container.condition.ConditionEvalManager;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.EntityManager;
import org.runbpm.utils.RunBPMUtils;


public abstract class FlowContainer{
	
	protected ProcessInstance processInstance;
	protected ActivityInstance createNewActivityInstance(ActivityDefinition activityDefinition){
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		ActivityInstance activityInstance = entityManager.produceActivityInstance(this.processInstance.getId());
		
		activityInstance.setType(EntityConstants.ACTIVITY_TYPE.getTypeByActivityElment(activityDefinition));
		activityInstance.setState(ACTIVITY_STATE.NOT_STARTED);
		activityInstance.setActivityDefinitionId(activityDefinition.getId());
		activityInstance.setName(activityDefinition.getName());
		activityInstance.setCreateDate(new Date());
		activityInstance.setProcessInstanceId(processInstance.getId());
		activityInstance.setProcessModelId(processInstance.getProcessModelId());
		activityInstance.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		
		//块活动
		if(activityDefinition instanceof SubProcessDefinition){
			SubProcessDefinition subProcessDefinition = (SubProcessDefinition)activityDefinition;
			activityInstance.setSequenceBlockId(subProcessDefinition.getSequenceBlockId());
		}
		
		return activityInstance;
	}
	
	
	public void flowToNext(ActivityInstance activityInstance, ActivityDefinition activityDefinition,ActivityDefinition targetActivityDefinition) {
		
		List<ActivityInstance> activityInstanceSetInProcess = getActivityInstanceSetInFlowContainer();//abstract
		
		//1 寻找将要创建的活动
		Set<ActivityDefinition> outgoingActivitySet = null;
		if(targetActivityDefinition==null){
			outgoingActivitySet = listReachableActivitySet(activityInstance,activityDefinition);
		}else{
			outgoingActivitySet = new HashSet<ActivityDefinition>();
			outgoingActivitySet.add(targetActivityDefinition);
		}
		
		if(outgoingActivitySet.size() == 0){
			Set<Object> exceptionHashSet = new HashSet<Object>();
	    		exceptionHashSet.add(processInstance);
	    		exceptionHashSet.add(activityDefinition);
	    		Map<String,VariableInstance> dataFieldInstanceMap = Configuration.getContext().getEntityManager().loadVariableMap(processInstance.getId());
	    		exceptionHashSet.add(dataFieldInstanceMap);
	    		throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020001_Empty_Activity, exceptionHashSet.toString());
		}
		
		//2 已经是正在运行的活动 + 将要创建的活动
		Set<ActivityDefinition> runningActivityDefSet = new HashSet<ActivityDefinition>(outgoingActivitySet);
		for(ActivityInstance activityInstanceInProcess:activityInstanceSetInProcess){	
			String activityDefinitionIdInProcess = activityInstanceInProcess.getActivityDefinitionId();
			ActivityDefinition activityDefinitionInProcess = this.getAcitityInFlowContaine(activityDefinitionIdInProcess);
			runningActivityDefSet.add(activityDefinitionInProcess);
		}
		
		//3 通过 正在运行的活动 + 将要创建的活动， 判断是否启动 输入流大于1的活动实例
		Set<ActivityDefinition> startedJoinActivityDefSet = new HashSet<ActivityDefinition>();
		for(ActivityInstance activityInstanceInProcess:activityInstanceSetInProcess){
			String activityDefinitionIdInProcess = activityInstanceInProcess.getActivityDefinitionId();
			ActivityDefinition activityDefinitionInProcess = this.getAcitityInFlowContaine(activityDefinitionIdInProcess);

			 boolean started = evalJoin(activityDefinitionInProcess,activityInstanceInProcess,runningActivityDefSet);
			 if(started){
				 startedJoinActivityDefSet.add(activityDefinitionInProcess);
			 }
		}
		//4 根据outgoingActivitySet定义创建实例、并判断是否可以启动
		for(ActivityDefinition outgoingActivity:outgoingActivitySet){	
			
			//如果上一个步骤没有启动过
			if(!(startedJoinActivityDefSet.contains(outgoingActivity))){
			
				EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		        set.add(ACTIVITY_STATE.NOT_STARTED);  
		        set.add(ACTIVITY_STATE.RUNNING);
		        set.add(ACTIVITY_STATE.SUSPENDED);
				//没有终止的活动
		        //没有才创建出来
				List<ActivityInstance> sameActivityInstance = Configuration.getContext().getEntityManager().listActivityInstanceByActivityDefIdAndState(processInstance.getId(), outgoingActivity.getId(),set);
				if(evalEmptyInContainer(sameActivityInstance)){
					ActivityInstance newActivityInstance = createNewActivityInstance(outgoingActivity);
					
					//TODO 如果UserTask是XOR类型，不需要判断，直接启动
					if(outgoingActivity.getIncomingSequenceFlowList().size()==1){
						ActivityContainer activityContainer = ContainerTool.getActivityContainer(newActivityInstance);
						activityContainer.start();
					}else{
						//通过 将要创建的活动 + 已经运行的活动， 判断是否启动 输入流大于1的活动实例
						evalJoin(outgoingActivity,newActivityInstance,runningActivityDefSet);
					}
				}
			}
		}
		
	}
	
	public Set<ActivityDefinition> listReachableActivitySet(ActivityInstance activityInstance,ActivityDefinition activityDefinition) {
		EntityManager runBPMEntityManager = Configuration.getContext().getEntityManager();
		Set<ActivityDefinition> outgoingActivitySet = new HashSet<ActivityDefinition>();
		Map<String, VariableInstance> variableMap = runBPMEntityManager.loadVariableMap(processInstance.getId());
		
		List<SequenceFlow> outgoingSequenceFlowList = activityDefinition.getOutgoingSequenceFlowList();
		
		for (SequenceFlow transition : outgoingSequenceFlowList) {
			ProcessContextBean processContextBean = new ProcessContextBean();
			processContextBean.setProcessInstance(processInstance);
			processContextBean.setActivityInstance(activityInstance);
			processContextBean.setActivityDefinition(activityDefinition);
			processContextBean.setVariableMap(variableMap);
			processContextBean.setSequenceFlow(transition);
			
			String toActivityId = transition.getTargetRef();
			ActivityDefinition toActivity = this.getAcitityInFlowContaine(toActivityId);

			// 有条件
			if (transition.getConditionExpression() != null) {
				if (RunBPMUtils.notNull(transition.getConditionExpression().getValue())) {
					ConditionEvalManager em = new ConditionEvalManager();
					Object result = em.eval(processContextBean);
					// 结束
					if (result instanceof Boolean) {
						if ((Boolean) result == true) {
							outgoingActivitySet.add(toActivity);
						}
					} else {
						throw new RunBPMException(
								RunBPMException.EXCEPTION_MESSAGE.Code_020002_Invalid_Result_From_EVAL,
								processContextBean.toString());
					}
				} 
			}else {
				outgoingActivitySet.add(toActivity);
			}
		}
		return outgoingActivitySet;
	}


	/**
	 * 流程引擎的辅助方法，当前只判断 输入流大于1的ParallelGateway
	 * @param activityDefinitionInProcess 该活动定义
	 * @param activityInstanceInProcess 该活动实例
	 * @param processDefinition 流程定义
	 * @param openActivityDefSet 所有处于运行状态的活动实例
	 */
	protected boolean evalJoin(ActivityDefinition activityDefinitionInProcess,ActivityInstance activityInstanceInProcess,Set<ActivityDefinition> openActivityDefSet){
		boolean started = false;
		
			//TODO 如果UserTask是XOR类型，不需要判断 空判断
			if(activityDefinitionInProcess.getIncomingSequenceFlowList().size()>1){
				//遍历 将要创建的活动 + 已经运行的活动 能否到达这个活动实例
				boolean canStart = true;
				for(ActivityDefinition outgoingActivity:openActivityDefSet){
					//只要有一个可以到达，则该节点不可以被激活
					if(getReachableActivitySet(outgoingActivity).contains(activityDefinitionInProcess)){
						canStart = false;
					}
				}
				if(canStart){
					ActivityContainer activityContainer = ContainerTool.getActivityContainer(activityInstanceInProcess);
					activityContainer.start();
					
					//便于下一步不再创建它
					started = true;
				}
			}
		
		return started;
	}
	
	public abstract void start();
	
	protected abstract Set<ActivityDefinition> getReachableActivitySet(ActivityDefinition outgoingActivity);

	protected abstract ActivityDefinition getAcitityInFlowContaine(String activityId);
	
	protected abstract void complete();
	
	protected abstract List<ActivityInstance> getActivityInstanceSetInFlowContainer() ;
	
	//是否有相同的实例
	protected abstract boolean evalEmptyInContainer(List<ActivityInstance> sameActivityInstance);
	
	public void suspend(){
		
		PROCESS_STATE currentState = this.processInstance.getState();
		if(currentState.equals(PROCESS_STATE.NOT_STARTED)||currentState.equals(PROCESS_STATE.RUNNING)){
			

			//event begin
			invokelistener(ListenerManager.Event_Type.beforeProcessInstanceSuspended);
			//event end		
			this.processInstance.setStateBeforeSuspend(currentState);
			processInstance.setState(PROCESS_STATE.SUSPENDED);
			//event begin
			invokelistener(ListenerManager.Event_Type.afterProcessInstanceSuspended);
			//event end
		}else{
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020010_INVALID_PROCESSINSTANCE_TO_SUSPEND);
		}
	}

	public void invokelistener(ListenerManager.Event_Type listenerType) {
		if(ListenerManager.getListenerManager().haveProcessEvent(this.processInstance.getProcessModelId()+"",listenerType)){
			ProcessContextBean processContextBean = new ProcessContextBean();
			
			EntityManager entityManager = Configuration.getContext().getEntityManager();
			ProcessModel processModel = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId());
			ProcessDefinition processDefinition = processModel.getProcessDefinition();
			
			processContextBean.setProcessDefinition(processDefinition);
			processContextBean.setProcessInstance(this.processInstance);
			ListenerManager.getListenerManager().invokeProcessListener(processContextBean, listenerType);
		}
	}
	
	public void resume(){
		
		if(this.processInstance.getState().equals(PROCESS_STATE.SUSPENDED)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020013_INVALID_PROCESSINSTANCE_TO_RESUME);
		}else{

			//event begin
			invokelistener(ListenerManager.Event_Type.beforeProcessInstanceResumed);
			//event end
			
			PROCESS_STATE stateBeforeSuspend = this.processInstance.getStateBeforeSuspend();
			processInstance.setState(stateBeforeSuspend);
			
			//event begin
			invokelistener(ListenerManager.Event_Type.afterProcessInstanceResume);
			//event end
		}
		
	}
	
	public void terminate(){
		
		//event begin
		invokelistener(ListenerManager.Event_Type.beforeProcessInstanceTerminated);
		//event end	RunBPMListenerBroker
		
		complete_internal(PROCESS_STATE.TERMINATED);
		
		//event begin
		invokelistener(ListenerManager.Event_Type.afterProcessInstanceTerminated);
		//event end
	}

	public abstract void complete_internal(PROCESS_STATE terminated) ;
}
