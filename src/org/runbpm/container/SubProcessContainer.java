package org.runbpm.container;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.EntityManager;

//XPDL块活动，BPMNSubProcess
public class SubProcessContainer extends FlowContainer {

	private ActivityInstance activityInstanceOfSubProcess;
	private SubProcessDefinition subProcessDefinition;

	public SubProcessContainer(ActivityInstance activityInstanceOfSubProcess,
			SubProcessDefinition subProcessDefinition) {
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		this.activityInstanceOfSubProcess = activityInstanceOfSubProcess;
		this.subProcessDefinition = subProcessDefinition;
		this.processInstance = entityManager.loadProcessInstance(activityInstanceOfSubProcess.getProcessInstanceId());
	}

	public void start(String activityDefinitionId){
		ActivityDefinition activityDefinition; 
		if(activityDefinitionId==null) {
			activityDefinition = subProcessDefinition.getStartEvent();
		}else {
			activityDefinition = subProcessDefinition.getActivity(activityDefinitionId);
			if(activityDefinition==null) {
				throw  new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020010_CANNOT_FIND_ACTIVITY_BY_DEFINITIONID,",输入的活动定义ID有误:["+activityDefinitionId+"]");
			}
		}
		

		ActivityInstance activityInstance = this.createNewActivityInstance(activityDefinition);
		ActivityContainer activityContainer = ContainerTool.getActivityContainer(activityInstance);
		
		activityContainer.start();
	}

	public void start(){
		start(null);
	}
	
	public void complete(){
		ActivityContainer activityInstanceOfSubProcessContainer = ContainerTool.getActivityContainer(activityInstanceOfSubProcess);;
		activityInstanceOfSubProcessContainer.complete();
	}
	
	protected ActivityDefinition getAcitityInFlowContaine(String toActivityId){
		return this.subProcessDefinition.getActivity(toActivityId); 
	}

	protected Set<ActivityDefinition> getReachableActivitySet(ActivityDefinition outgoingActivity){
		return subProcessDefinition.listReachableActivitySet(outgoingActivity);
	}

	@Override
	protected List<ActivityInstance> getActivityInstanceSetInFlowContainer() {
		EnumSet<ACTIVITY_STATE> set = EnumSet.of(ACTIVITY_STATE.NOT_STARTED,ACTIVITY_STATE.RUNNING,ACTIVITY_STATE.SUSPENDED);
		return Configuration.getContext().getEntityManager().listActivityInstanceByProcessInstIdSubrocessIdAndState(processInstance.getId(),subProcessDefinition.getId(),set);
	}

	@Override
	protected boolean evalEmptyInContainer(List<ActivityInstance> sameActivityInstance) {
		//考虑多实例
		for(ActivityInstance a:sameActivityInstance){
			if(a.getParentActivityInstanceId()==activityInstanceOfSubProcess.getId()){
				return false;
			}
		}
		//没有匹配的
		return true;
	}
	
	protected ActivityInstance createNewActivityInstance(ActivityDefinition activityDefinition){
		ActivityInstance activityInstance = super.createNewActivityInstance(activityDefinition);

		//块活动下的子活动
		if(activityInstanceOfSubProcess!=null){
			activityInstance.setParentActivityInstanceId(activityInstanceOfSubProcess.getId());
			activityInstance.setSubProcessBlockId(activityInstanceOfSubProcess.getActivityDefinitionId());
		}
		return activityInstance;
	}

	@Override
	public void complete_internal(PROCESS_STATE terminated) {
		// TODO 
	}
	
}
