package org.runbpm.container;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;

public class ActivityOfSubProcessContainer extends ActivityContainer{
	
	
	public ActivityOfSubProcessContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		super(activityDefinition,activityInstance);
	}

	//subProcess��complete������SubProcessContainer��ʵ��-> ��ص���ȡ�����complete������Ȼ��ʹ�ø����complete��������������������ִ��
	@Override
	public void activityStart() {
		this.activityInstance.setState(ACTIVITY_STATE.RUNNING);
		
		SubProcessDefinition subProcess = (SubProcessDefinition)activityDefinition;
		
		SubProcessContainer subProcessContainer = new SubProcessContainer(activityInstance,subProcess);
		subProcessContainer.start();
	}
	

}
