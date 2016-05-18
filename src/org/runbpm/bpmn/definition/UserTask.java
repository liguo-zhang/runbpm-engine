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
	
	
	/**
	 * 是否是会签活动，如果是会签活动，则会为所有符合条件的执行人创建任务项，任务项的初始状态就是running状态。
	 * @return 是否是会签活动
	 */
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
