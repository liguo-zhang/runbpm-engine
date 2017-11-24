package org.runbpm.container;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.runbpm.bpmn.definition.ActivityDefinition;
import org.runbpm.bpmn.definition.ProcessDefinition;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.ActivityInstance;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.entity.ProcessInstance;
import org.runbpm.entity.ProcessModel;
import org.runbpm.entity.Process_;
import org.runbpm.exception.RunBPMException;
import org.runbpm.listener.ListenerManager;
import org.runbpm.persistence.EntityManager;
import org.runbpm.utils.RunBPMUtils;

public class ProcessContainer extends FlowContainer {
	
	protected ProcessDefinition processDefinition; 
	
	protected ProcessContainer(){};
	
	private EntityManager entityManager = Configuration.getContext().getEntityManager();
	
	//创建新的流程使用
	public static ProcessContainer getProcessContainerForNewInstance(){
		ProcessContainer processContainer=  new ProcessContainer();
		return processContainer;
	}
	
	public ProcessInstance createInstance(String processDefinitionId){
		return this.createInstance(processDefinitionId,null);
	}
	
	public ProcessInstance createInstance(long processModelId,String creator){
		ProcessModel processModel = entityManager.loadProcessModelByModelId(processModelId);
		if(processModel==null){
			throw  new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020000_NoResult_For_Such_DefinitionId,",输入的流程模板ID有误:["+processModelId+"]");
		}
		return createInstance(processModel,creator);
	}
	
	public ProcessInstance createInstance(String processDefinitionId,String creator){
		ProcessModel processModel = entityManager.loadLatestProcessModel(processDefinitionId);
		if(processModel==null){
			throw  new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020000_NoResult_For_Such_DefinitionId,",输入的流程定义ID有误:["+processDefinitionId+"]");
		}
		return createInstance(processModel,creator);
		
	}
	
	private ProcessInstance createInstance(ProcessModel processModel,String creator){
		//event begin
		//获取流程定义方式与下面不一样
		if(ListenerManager.getListenerManager().haveProcessEvent(processModel.getId()+"",ListenerManager.Event_Type.beforeProcessInstanceStarted)){
			ProcessContextBean processContextBean = new ProcessContextBean();
			processContextBean.setProcessDefinition(this.processDefinition);
			processContextBean.setProcessInstance(this.processInstance);
			ListenerManager.getListenerManager().invokeProcessListener(processContextBean, ListenerManager.Event_Type.beforeProcessInstanceStarted);
		}
		//event end		
		
		this.processDefinition = processModel.getProcessDefinition();
		this.processInstance = entityManager.produceProcessInstance(processModel.getId());
		processInstance.setProcessModelId(processModel.getId());
		processInstance.setState(PROCESS_STATE.NOT_STARTED);
		processInstance.setProcessDefinitionId(processDefinition.getId());
		processInstance.setName(processDefinition.getName());
		processInstance.setCreator(creator);
		//processInstance.setDescription(processDefinition.getDescription());
		
		//event begin
		invokelistener(ListenerManager.Event_Type.afterProcessInstanceCreated);
		//event end		
		
		return processInstance;
	}
	
	public void startWithKeys(String...strings ){
		setkey(strings);
		this.start();
	}
	
	
	public void start(){
		
		PROCESS_STATE currentState = this.processInstance.getState();
		if(!currentState.equals(PROCESS_STATE.NOT_STARTED)){
			throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020010_INVALID_PROCESSINSTANCE_TO_START);
		}

		//event begin
		invokelistener(ListenerManager.Event_Type.beforeProcessInstanceStarted);
		//event end		
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		processInstance.setState(PROCESS_STATE.RUNNING);
		Long processModelId =  processInstance.getProcessModelId();
		
		ProcessModel processModel= entityManager.loadProcessModelByModelId(processModelId);
		
		ProcessDefinition processDefinition= processModel.getProcessDefinition();
		ActivityDefinition activityDefinition = processDefinition.getStartEvent();
		
		//create new ActivityInstance 
		ActivityInstance activityInstance = createNewActivityInstance(activityDefinition);
		ActivityContainer activityContainer = ActivityContainer.getActivityContainer(activityInstance);
		
		activityContainer.start();
		
		//event begin
		invokelistener(ListenerManager.Event_Type.afterProcessInstanceStarted);
		//event end
	}
	
	
	
	public void complete(){
		//event begin
		invokelistener(ListenerManager.Event_Type.beforeProcessInstanceCompleted);
		//event end	
		
		complete_internal(PROCESS_STATE.COMPLETED);
		
		//event begin
		invokelistener(ListenerManager.Event_Type.afterProcessInstanceCompleted);
		//event end
	}
	
	
	
	public void complete_internal(PROCESS_STATE completeState){
		
		EntityManager entityManager = Configuration.getContext().getEntityManager();
		
		Long parentActivityInstanceId = processInstance.getParentActivityInstanceId();
		
		processInstance.setState(completeState);
		processInstance.setCompleteDate(new Date());
		
		if(RunBPMUtils.notNullLong(parentActivityInstanceId)){
			processInstance.setState(PROCESS_STATE.COMPLETED);
			ActivityInstance parentActivityInstance = entityManager.loadActivityInstance(parentActivityInstanceId);
			ActivityOfCallActivityContainer  parentActivityContainer = (ActivityOfCallActivityContainer)ActivityContainer.getActivityContainer(parentActivityInstance);
			parentActivityContainer.complete(processInstance);
		}else{
			entityManager.archiveProcess(processInstance);
		}
		
	}
	
	protected ActivityDefinition getAcitityInFlowContaine(String toActivityId){
		return processDefinition.getActivity(toActivityId); 
	}
	
	protected Set<ActivityDefinition> getReachableActivitySet(ActivityDefinition outgoingActivity){
		return processDefinition.listReachableActivitySet(outgoingActivity);
	}

	@Override
	protected List<ActivityInstance> getActivityInstanceSetInFlowContainer() {
		EnumSet<ACTIVITY_STATE> set = EnumSet.of(ACTIVITY_STATE.NOT_STARTED,ACTIVITY_STATE.RUNNING,ACTIVITY_STATE.SUSPENDED);
		return Configuration.getContext().getEntityManager().listActivityInstanceByProcessInstIdAndState(processInstance.getId(),set);
	}

	@Override
	protected boolean evalEmptyInContainer(List<ActivityInstance> sameActivityInstance) {
		return sameActivityInstance.size()==0;
	}
	
	
	
	public void setkey(String... strings) {
		Process_ process_ = (Process_)processInstance;
		
		int i = 0;
		for(String string : strings){
			if(i==0){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyA(string);
				}
			}else if(i==1){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyB(string);
				}
			}else if(i==2){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyC(string);
				}
			}else if(i==3){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyD(string);
				}
			}else if(i==4){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyE(string);
				}
			}else if(i==5){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyF(string);
				}
			}else if(i==6){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyG(string);
				}
			}else if(i==7){
				if(RunBPMUtils.notNull(string)){
					process_.setKeyH(string);
				}
			}
			i++;
		}
		
	}
}
