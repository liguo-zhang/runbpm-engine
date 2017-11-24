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
import org.runbpm.persistence.EntityManager;

//XPDL����BPMNSubProcess
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

	public void start(){
		ActivityDefinition activityDefinition = subProcessDefinition.getStartEvent();

		ActivityInstance activityInstance = this.createNewActivityInstance(activityDefinition);
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance);
		
		activityContainer.start();
	}

	public void complete(){
		ActivityContainer activityInstanceOfSubProcessContainer = ActivityContainer.getActivityContainer(activityInstanceOfSubProcess);;
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
		//���Ƕ�ʵ��
		for(ActivityInstance a:sameActivityInstance){
			if(a.getParentActivityInstanceId()==activityInstanceOfSubProcess.getId()){
				return false;
			}
		}
		//û��ƥ���
		return true;
	}
	
	protected ActivityInstance createNewActivityInstance(ActivityDefinition activityDefinition){
		ActivityInstance activityInstance = super.createNewActivityInstance(activityDefinition);

		//���µ��ӻ
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
