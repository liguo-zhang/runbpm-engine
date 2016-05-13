package org.runbpm.container;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;

public class ActivityOfSubProcessContainer extends ActivityContainer{
	
	
	public ActivityOfSubProcessContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		super(activityDefinition,activityInstance);
	}

	//subProcess的complete方法在SubProcessContainer中实现-> 会回调获取该类的complete方法，然后使用父类的complete方法，驱动父流程往下执行
	@Override
	public void activityStart() {
		this.activityInstance.setState(ACTIVITY_STATE.RUNNING);
		
		SubProcessDefinition subProcess = (SubProcessDefinition)activityDefinition;
		
		SubProcessContainer subProcessContainer = new SubProcessContainer(activityInstance,subProcess);
		subProcessContainer.start();
	}
	

}
