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
import org.runbpm.context.Execution;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.EntityManager;
import org.runbpm.utils.RunBPMUtils;


public abstract class FlowContainer{
	
	protected ProcessInstance processInstance;
	//������������
	public static FlowContainer getFlowContainer(ProcessInstance processInstance,ActivityInstance activityInstance){
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		if(activityInstance ==null||activityInstance.getParentActivityInstanceId()==0){
			//��ͨ����
			ProcessContainer processContainer=  new ProcessContainer();
			processContainer.processInstance = processInstance;
			processContainer.processDefinition =entityManager.loadProcessModelByModelId(processInstance.getProcessModelId()).getProcessDefinition();

			return processContainer;
		}else{
			//XPDL����BPMNSubProcess
			ProcessDefinition processDefinition = entityManager.loadProcessModelByModelId(processInstance.getProcessModelId()).getProcessDefinition();
			
			ActivityInstance parentActivityInstance = entityManager.getActivityInstance(activityInstance.getParentActivityInstanceId());
			SubProcessDefinition subProcessDefinition = processDefinition.getSubProcessActivityDefinition(parentActivityInstance.getSequenceBlockId());
			
			ActivityInstance activityInstanceOfSubProcess = entityManager.getActivityInstance(activityInstance.getParentActivityInstanceId());
			
			SubProcessContainer subProcessContainer = new SubProcessContainer(activityInstanceOfSubProcess,subProcessDefinition);
			return subProcessContainer;
		}
	}
	
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
		
		//��
		if(activityDefinition instanceof SubProcessDefinition){
			SubProcessDefinition subProcessDefinition = (SubProcessDefinition)activityDefinition;
			activityInstance.setSequenceBlockId(subProcessDefinition.getSequenceBlockId());
		}
		
		return activityInstance;
	}
	
	
	public void flowToNext(ActivityInstance activityInstance, ActivityDefinition activityDefinition,ActivityDefinition targetActivityDefinition) {
		
		List<ActivityInstance> activityInstanceSetInProcess = getActivityInstanceSetInFlowContainer();//abstract
		
		//1 Ѱ�ҽ�Ҫ�����Ļ
		Set<ActivityDefinition> outgoingActivitySet = null;
		if(targetActivityDefinition==null){
			outgoingActivitySet = getOutActivitySet(activityInstance,activityDefinition);
		}else{
			outgoingActivitySet = new HashSet<ActivityDefinition>();
			outgoingActivitySet.add(targetActivityDefinition);
		}
		
		if(outgoingActivitySet.size() == 0){
			Set<Object> exceptionHashSet = new HashSet<Object>();
    		exceptionHashSet.add(processInstance);
    		exceptionHashSet.add(activityDefinition);
    		Map<String,VariableInstance> dataFieldInstanceMap = Configuration.getContext().getEntityManager().getVariableMap(processInstance.getId());
    		exceptionHashSet.add(dataFieldInstanceMap);
    		throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020001_Empty_Activity, exceptionHashSet.toString());
		}
		
		//2 �Ѿ����������еĻ + ��Ҫ�����Ļ
		Set<ActivityDefinition> runningActivityDefSet = new HashSet<ActivityDefinition>(outgoingActivitySet);
		for(ActivityInstance activityInstanceInProcess:activityInstanceSetInProcess){	
			String activityDefinitionIdInProcess = activityInstanceInProcess.getActivityDefinitionId();
			ActivityDefinition activityDefinitionInProcess = this.getAcitityInFlowContaine(activityDefinitionIdInProcess);
			runningActivityDefSet.add(activityDefinitionInProcess);
		}
		
		//3 ͨ�� �������еĻ + ��Ҫ�����Ļ�� �ж��Ƿ����� ����������1�Ļʵ��
		Set<ActivityDefinition> startedJoinActivityDefSet = new HashSet<ActivityDefinition>();
		for(ActivityInstance activityInstanceInProcess:activityInstanceSetInProcess){
			String activityDefinitionIdInProcess = activityInstanceInProcess.getActivityDefinitionId();
			ActivityDefinition activityDefinitionInProcess = this.getAcitityInFlowContaine(activityDefinitionIdInProcess);

			 boolean started = evalJoin(activityDefinitionInProcess,activityInstanceInProcess,runningActivityDefSet);
			 if(started){
				 startedJoinActivityDefSet.add(activityDefinitionInProcess);
			 }
		}
		//4 ����outgoingActivitySet���崴��ʵ�������ж��Ƿ��������
		for(ActivityDefinition outgoingActivity:outgoingActivitySet){	
			
			//�����һ������û��������
			if(!(startedJoinActivityDefSet.contains(outgoingActivity))){
			
				EnumSet<ACTIVITY_STATE> set = EnumSet.noneOf(ACTIVITY_STATE.class);  
		        set.add(ACTIVITY_STATE.NOT_STARTED);  
		        set.add(ACTIVITY_STATE.RUNNING);
		        set.add(ACTIVITY_STATE.SUSPENDED);
				//û����ֹ�Ļ
		        //û�вŴ�������
				List<ActivityInstance> sameActivityInstance = Configuration.getContext().getEntityManager().getActivityInstanceByActivityDefIdAndState(processInstance.getId(), outgoingActivity.getId(),set);
				if(evalEmptyInContainer(sameActivityInstance)){
					ActivityInstance newActivityInstance = createNewActivityInstance(outgoingActivity);
					
					//TODO ���UserTask��XOR���ͣ�����Ҫ�жϣ�ֱ������
					if(outgoingActivity.getIncomingSequenceFlowList().size()==1){
						ActivityContainer activityContainer = ActivityContainer.getActivityContainer(newActivityInstance);
						activityContainer.start();
					}else{
						//ͨ�� ��Ҫ�����Ļ + �Ѿ����еĻ�� �ж��Ƿ����� ����������1�Ļʵ��
						evalJoin(outgoingActivity,newActivityInstance,runningActivityDefSet);
					}
				}
			}
		}
		
	}
	
	protected Set<ActivityDefinition> getOutActivitySet(ActivityInstance activityInstance,ActivityDefinition activityDefinition) {
		EntityManager runBPMEntityManager = Configuration.getContext().getEntityManager();
		Set<ActivityDefinition> outgoingActivitySet = new HashSet<ActivityDefinition>();
		Map<String, VariableInstance> variableMap = runBPMEntityManager.getVariableMap(processInstance.getId());
		
		List<SequenceFlow> outgoingSequenceFlowList = activityDefinition.getOutgoingSequenceFlowList();
		
		for (SequenceFlow transition : outgoingSequenceFlowList) {
			Execution handlerContext = new Execution();
			handlerContext.setProcessInstance(processInstance);
			handlerContext.setActivityInstance(activityInstance);
			handlerContext.setActivityDefinition(activityDefinition);
			handlerContext.setVariableMap(variableMap);
			handlerContext.setTransition(transition);
			
			String toActivityId = transition.getTargetRef();
			ActivityDefinition toActivity = this.getAcitityInFlowContaine(toActivityId);

			// ������
			if (transition.getConditionExpression() != null) {
				if (RunBPMUtils.notNull(transition.getConditionExpression().getValue())) {
					ConditionEvalManager em = new ConditionEvalManager();
					Object result = em.eval(handlerContext);
					// ����
					if (result instanceof Boolean) {
						if ((Boolean) result == true) {
							outgoingActivitySet.add(toActivity);
						}
					} else {
						throw new RunBPMException(
								RunBPMException.EXCEPTION_MESSAGE.Code_020002_Invalid_Result_From_EVAL,
								handlerContext.toString());
					}
				} 
			}else {
				outgoingActivitySet.add(toActivity);
			}
		}
		return outgoingActivitySet;
	}


	/**
	 * ��������ĸ�����������ǰֻ�ж� ����������1��ParallelGateway
	 * @param activityDefinitionInProcess �û����
	 * @param activityInstanceInProcess �ûʵ��
	 * @param processDefinition ���̶���
	 * @param openActivityDefSet ���д�������״̬�Ļʵ��
	 */
	protected boolean evalJoin(ActivityDefinition activityDefinitionInProcess,ActivityInstance activityInstanceInProcess,Set<ActivityDefinition> openActivityDefSet){
		boolean started = false;
		
			//TODO ���UserTask��XOR���ͣ�����Ҫ�ж� ���ж�
			if(activityDefinitionInProcess.getIncomingSequenceFlowList().size()>1){
				//���� ��Ҫ�����Ļ + �Ѿ����еĻ �ܷ񵽴�����ʵ��
				boolean canStart = true;
				for(ActivityDefinition outgoingActivity:openActivityDefSet){
					//ֻҪ��һ�����Ե����ýڵ㲻���Ա�����
					if(getReachableActivitySet(outgoingActivity).contains(activityDefinitionInProcess)){
						canStart = false;
					}
				}
				if(canStart){
					ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstanceInProcess);
					activityContainer.start();
					
					//������һ�����ٴ�����
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
	
	//�Ƿ�����ͬ��ʵ��
	protected abstract boolean evalEmptyInContainer(List<ActivityInstance> sameActivityInstance);
}
