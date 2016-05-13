package org.runbpm.entity;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.CallActivity;
import org.runbpm.bpmn.definition.EndEvent;
import org.runbpm.bpmn.definition.ExclusiveGateway;
import org.runbpm.bpmn.definition.InclusiveGateway;
import org.runbpm.bpmn.definition.ManualTask;
import org.runbpm.bpmn.definition.ParallelGateway;
import org.runbpm.bpmn.definition.ServiceTask;
import org.runbpm.bpmn.definition.StartEvent;
import org.runbpm.bpmn.definition.SubProcessDefinition;
import org.runbpm.bpmn.definition.UserTask;

public class EntityConstants {
	

	/**
	 * 流程对象的状态常量
	 *
	 */
	public enum PROCESS_STATE {
		
		 NOT_STARTED("0"), RUNNING("1"),COMPLETED("2"),TERMINATED("3"),SUSPENDED("4");
		 
		 private String value;
		 
		 private PROCESS_STATE(String value){
			 this.value = value;
		 }
		 
		 public String toString(){
			 return this.value;
		 }
		 
		 public static PROCESS_STATE getState(String state){
			 if("0".equals(state)){
				 return NOT_STARTED;
			 }else if("1".equals(state)){
				 return RUNNING;
			 }else if("2".equals(state)){
				 return COMPLETED;
			 }else if("3".equals(state)){
				 return TERMINATED;
			 }else if("4".equals(state)){
				 return SUSPENDED;
			 }else {
				 return null;
			 }
		 }
	}
	

	/**
	 * 活动对象的状态常量
	 *
	 */
	public enum ACTIVITY_STATE {
		
		 NOT_STARTED("0"), RUNNING("1"), COMPLETED("2"),TERMINATED("3"),SUSPENDED("4"); 
		 
		 private String value;
		 
		 private ACTIVITY_STATE(String value){
			 this.value = value;
		 }
		 
		 public String toString(){
			 return this.value;
		 }
		 
		 public static ACTIVITY_STATE getState(String state){
			 if("0".equals(state)){
				 return NOT_STARTED;
			 }else if("1".equals(state)){
				 return RUNNING;
			 }else if("2".equals(state)){
				 return COMPLETED;
			 }else if("3".equals(state)){
				 return TERMINATED;
			 }else if("4".equals(state)){
				 return SUSPENDED;
			 }else {
				 return null;
			 }
		 }
	}


	/**
	 * 任务项常量的状态对象
	 *
	 */
	public enum TASK_STATE {
		
		NOT_STARTED("0"), RUNNING("1"), COMPLETED("2"),TERMINATED("3"),SUSPENDED("4");
		
		private String value;
		 
		 private TASK_STATE(String value){
			 this.value = value;
		 }
		 
		 public String toString(){
			 return this.value;
		 }
		 
		 public static TASK_STATE getState(String state){
			 if("0".equals(state)){
				 return NOT_STARTED;
			 }else if("1".equals(state)){
				 return RUNNING;
			 }else if("2".equals(state)){
				 return COMPLETED;
			 }else if("3".equals(state)){
				 return TERMINATED;
			 }else if("4".equals(state)){
				 return SUSPENDED;
			 }else {
				 return null;
			 }
		 }
	}
	
	/**
	 * 任务类型常量
	 *
	 */
	public enum TASK_TYPE {
		
		DEFAULT("0"), EXTRA_USER_TASK("1");
		
		private String value;
		 
		 private TASK_TYPE(String value){
			 this.value = value;
		 }
		 
		 public String toString(){
			 return this.value;
		 }
		 
		 public static TASK_TYPE getState(String type){
			 if("0".equals(type)){
				 return DEFAULT;
			 }else if("1".equals(EXTRA_USER_TASK)){
				 return EXTRA_USER_TASK;
			 }else{
				 return null;
			 }
		 }
	}
	

	/**
	 * 流程变量的类型
	 *
	 */
	public enum VARIABLE_TYPE {
		
		 Basic_Long("0"), Basic_String("1"), Basic_Integer("2"), Basic_Float("3"),Basic_Double("4"),Basic_Boolean("5");
		 
		 private String value;
		 
		 private VARIABLE_TYPE(String value){
			 this.value = value;
		 }
		 
		 public String toString(){
			 return this.value;
		 }
		 
		 public VARIABLE_TYPE getState(String state){
			 if("0".equals(state)){
				 return Basic_Long;
			 }else if("1".equals(state)){
				 return Basic_String;
			 }else if("2".equals(state)){
				 return Basic_Integer;
			 }else if("3".equals(state)){
				 return Basic_Float;
			 }else if("4".equals(state)){
				 return Basic_Double;
			 }else if("5".equals(state)){
				 return Basic_Boolean;
			 }else {
				 return null;
			 }
		 }
	}
	
	/**
	 * 活动对象的类型常量
	 *
	 */
	public enum ACTIVITY_TYPE {
		
		StartEvent("0"), EndEvent("1"), UserTask("2"), ServiceTask("3"),  ManualTask(
				"4"),ExclusiveGateway("5"), InclusiveGateway("6"), ParallelGateway("7"),CallActivity("8"),SubProcessDefinition("9");
		 
		 private String value;
		 
		 private ACTIVITY_TYPE(String value){
			 this.value = value;
		 }
		 
		 public String toString(){
			 return this.value;
		 }
		 
		 public static ACTIVITY_TYPE getTypeByActivityElment(ActivityDefinition activityDefinition){
			 if(activityDefinition instanceof StartEvent ){
					return StartEvent; 
				}else if(activityDefinition instanceof EndEvent){
					return EndEvent;
				}else if(activityDefinition instanceof UserTask){
					return UserTask;
				}else if(activityDefinition instanceof ServiceTask){
					return ServiceTask;
				}else if(activityDefinition instanceof ManualTask){
					return ManualTask;
				}else if(activityDefinition instanceof ExclusiveGateway){
					return ExclusiveGateway;
				}else if(activityDefinition instanceof InclusiveGateway){
					return InclusiveGateway;
				}else if(activityDefinition instanceof ParallelGateway){
					return ParallelGateway;
				}else if(activityDefinition instanceof CallActivity){
					return CallActivity;
				}else if(activityDefinition instanceof SubProcessDefinition){
					return SubProcessDefinition;
				}else{
					return null;
				}
			 
		 }
		 
		 public static ACTIVITY_TYPE getTypeByString(String state){
			 if("0".equals(state)){
				 return StartEvent;
			 }else if("1".equals(state)){
				 return EndEvent;
			 }else if("2".equals(state)){
				 return UserTask;
			 }else if("3".equals(state)){
				 return ServiceTask;
			 }else if("4".equals(state)){
				 return ManualTask;
			 }else if("5".equals(state)){
				 return ExclusiveGateway;
			 }else if("6".equals(state)){
				 return InclusiveGateway;
			 }else if("7".equals(state)){
				 return ParallelGateway;
			 }else if("8".equals(state)){
				 return CallActivity;
			 }else if("9".equals(state)){
				 return SubProcessDefinition;
			 }else {
				 return null;
			 }
		 }
	}
	
}
