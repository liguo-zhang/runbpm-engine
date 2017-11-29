package org.runbpm.container;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.exception.RunBPMException;
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
	
	public void addUserTask(String userId) {
		//只能运行状态
		if(this.activityInstance.getState().equals(EntityConstants.ACTIVITY_STATE.RUNNING)) {
			UserTask userTask = (UserTask)activityDefinition;
			if(userTask.isMulti()){
				TaskInstance taskInstance = this.newUserTaskInstance(EntityConstants.TASK_STATE.RUNNING,new User(userId));
				new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
			}else{
				//有not_started的工作项，才能创建
				EnumSet<EntityConstants.TASK_STATE> stateSet = EnumSet.noneOf(EntityConstants.TASK_STATE.class);  
				stateSet.add(EntityConstants.TASK_STATE.NOT_STARTED);
				if(Configuration.getContext().getEntityManager().listTaskInstanceByActivityInstIdAndState(activityInstance.getId(), stateSet).size()>0) {
					TaskInstance taskInstance = this.newUserTaskInstance(EntityConstants.TASK_STATE.NOT_STARTED,new User(userId));
					new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
				}else {
					Set<Object> exceptionHashSet = new HashSet<Object>();
    					exceptionHashSet.add(activityDefinition);
    					exceptionHashSet.add(activityInstance);
    					throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020025_INVALID_ACTIVITYINSTANCE_TO_ADDUSERTASK,exceptionHashSet.toString());
				}
			}	
		}else {
			Set<Object> exceptionHashSet = new HashSet<Object>();
    			exceptionHashSet.add(activityDefinition);
    			exceptionHashSet.add(activityInstance);
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020024_INVALID_ACTIVITYINSTANCE_TO_ADDUSERTASK,exceptionHashSet.toString());
		}
		
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
	
	public TaskInstance addUserTask(User user,EntityConstants.TASK_STATE state) {
		UserTask userTask = (UserTask)activityDefinition;
		TaskInstance taskInstance = this.newUserTaskInstance(state,user);
		new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
		return taskInstance;
	}
	
	
	
	
	private void createUserTaskInstance(EntityConstants.TASK_STATE state){
		UserTask userTask = (UserTask)activityDefinition;
		
		List<User> userList =ContainerTool.evalUserList(activityInstance.getProcessInstanceId(),userTask);
		//创建TaskInstance
		for(User user:userList){
			TaskInstance taskInstance = this.newUserTaskInstance(state,user);
			new UserTaskContainer(this.activityInstance,userTask,taskInstance).start();
		}
	}
	

}
