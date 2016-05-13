package org.runbpm.container;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.entity.ActivityInstance;

// start / end / gateway / manualtask
public class ActivityOfRouteContainer extends ActivityContainer{
	
	
	public ActivityOfRouteContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		super(activityDefinition,activityInstance);
	}

	@Override
	public void activityStart() {
		super.complete();
	}
	
	

}
