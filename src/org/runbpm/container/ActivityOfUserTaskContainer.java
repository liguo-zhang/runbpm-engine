package org.runbpm.container;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.bpmn.definition.UserTask;
import org.runbpm.bpmn.definition.UserTaskResource;
import org.runbpm.bpmn.definition.UserTaskResourceAssignment;
import org.runbpm.container.resource.ResourceEvalManager;
import org.runbpm.context.Configuration;
import org.runbpm.context.Execution;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.TaskInstance;
import org.runbpm.entity.VariableInstance;
import org.runbpm.handler.resource.User;

public class ActivityOfUserTaskContainer extends ActivityContainer{

	public ActivityOfUserTaskContainer(ActivityDefinition activityDefinition,ActivityInstance activityInstance){
		super(activityDefinition,activityInstance);
	}

	@Override
	public void activityStart() {
		activityInstance.setState(ACTIVITY_STATE.RUNNING);
		
		UserTask userTask = (UserTask)activityDefinition;
		UserTaskResource userTaskResource = userTask.getUserTaskResource();
		if(userTaskResource!=null){
			UserTaskResourceAssignment resourceAssignment = userTaskResource.getUserTaskResourceAssignment();
			if(resourceAssignment!=null){
				if(userTask.isMulti()){
					createUserTaskInstance(resourceAssignment,EntityConstants.TASK_STATE.RUNNING);
				}else{
					createUserTaskInstance(resourceAssignment,EntityConstants.TASK_STATE.NOT_STARTED);
				}
			}
		}
	}
	
	private void createUserTaskInstance(UserTaskResourceAssignment resourceAssignment,EntityConstants.TASK_STATE state){
		UserTask userTask = (UserTask)activityDefinition;
		Map<String,VariableInstance> userTaskContext = Configuration.getContext().getEntityManager().getVariableMap(activityInstance.getProcessInstanceId());
		
		ProcessInstance processInstance = Configuration.getContext().getEntityManager().getProcessInstance(activityInstance.getProcessInstanceId());
		ProcessDefinition processDefinition= Configuration.getContext().getEntityManager().loadProcessModelByModelId(activityInstance.getProcessModelId()).getProcessDefinition();
		
		if(resourceAssignment!=null){
				Execution handlerContext = new Execution();
				handlerContext.setProcessInstance(processInstance);
				handlerContext.setProcessDefinition(processDefinition);
				handlerContext.setActivityInstance(activityInstance);
				handlerContext.setActivityDefinition(activityDefinition);
				handlerContext.setVariableMap(userTaskContext);
				
				ResourceEvalManager rem = new ResourceEvalManager();
				List<User> userList =rem.getUserList(resourceAssignment, handlerContext);
				//´´½¨TaskInstance
				for(User user:userList){
					TaskInstance taskInstance = this.newUserTaskInstance(activityDefinition,state,user);
					new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
				}
		}
	}
	
	private TaskInstance newUserTaskInstance(ActivityDefinition activityDefinition,EntityConstants.TASK_STATE state,User user){
		TaskInstance taskInstance = Configuration.getContext().getEntityManager().produceTaskInstance(this.activityInstance.getId(),user.getId());
		taskInstance.setActivityDefinitionId(activityDefinition.getId());
		taskInstance.setCreateDate(new Date());
		taskInstance.setProcessDefinitionId(activityDefinition.getProcessDefinition().getId());
		taskInstance.setProcessModelId(activityInstance.getProcessModelId());
		taskInstance.setState(state);
		
		taskInstance.setActivityInstanceId(activityInstance.getId());
		taskInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
		
		taskInstance.setUserId(user.getId());
		
		return taskInstance;
	}
	

}
