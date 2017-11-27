package org.runbpm.container;

import java.util.ArrayList;
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
import org.runbpm.context.ProcessContextBean;
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
	
	private TaskInstance newUserTaskInstance(EntityConstants.TASK_STATE state,User user){
		TaskInstance taskInstance = Configuration.getContext().getEntityManager().produceTaskInstance(this.activityInstance.getId(),user.getId());
		
		taskInstance.setName(activityDefinition.getName());
		
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

	@Override
	public void activityStart() {
		activityInstance.setState(ACTIVITY_STATE.RUNNING);
		
		UserTask userTask = (UserTask)activityDefinition;
		UserTaskResource userTaskResource = userTask.getUserTaskResource();
		if(userTaskResource!=null){
			UserTaskResourceAssignment resourceAssignment = userTaskResource.getUserTaskResourceAssignment();
			if(resourceAssignment!=null){
				if(userTask.isMulti()){
					createUserTaskInstance(EntityConstants.TASK_STATE.RUNNING);
				}else{
					createUserTaskInstance(EntityConstants.TASK_STATE.NOT_STARTED);
				}
			}
		}
	}
	
	public void addUserTask(User user,EntityConstants.TASK_STATE state) {
		UserTask userTask = (UserTask)activityDefinition;
		TaskInstance taskInstance = this.newUserTaskInstance(state,user);
		new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
	}
	
	
	
	
	private void createUserTaskInstance(EntityConstants.TASK_STATE state){
		UserTask userTask = (UserTask)activityDefinition;
		
		List<User> userList =ContainerTool.evalUserList(activityInstance.getProcessInstanceId(),userTask);
		//´´½¨TaskInstance
		for(User user:userList){
			TaskInstance taskInstance = this.newUserTaskInstance(state,user);
			new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
		}
	}
	

}
