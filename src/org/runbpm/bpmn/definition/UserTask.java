package org.runbpm.bpmn.definition;

import org.runbpm.bpmn.definition.UserTaskResourcePolicy.RESOURCE_POLICY_TYPE;


public class UserTask extends ActivityDefinitionImpl{

	
	//----

	public UserTask(){
		super();
	}
	
	
	public UserTask(String id){
		super(id);
	}
	
	public UserTaskResource getUserTaskResource() {
		UserTaskResource userTaskResource = this.getExtensionElements().getUserTaskResource();
		return userTaskResource;
	}
	
	// «∑Òª·«©
	public boolean isMulti(){
		boolean isMulti = false;
	
		UserTaskResource userTaskResource = this.getUserTaskResource();
		if(userTaskResource!=null){
			UserTaskResourcePolicy userTaskResourcePolicy = userTaskResource.getUserTaskResourcePolicy();
			RESOURCE_POLICY_TYPE policyType =null;
			if(userTaskResourcePolicy!=null){
				 policyType =  userTaskResourcePolicy.getType();
			}
			if(policyType!=null&&policyType.equals(RESOURCE_POLICY_TYPE.multi)){
				isMulti = true;
			}
		}
		
		return isMulti;
	}
	
	  
}
