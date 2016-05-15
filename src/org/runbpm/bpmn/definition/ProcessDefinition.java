package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.runbpm.exception.RunBPMException;
import org.runbpm.utils.RunBPMUtils;


public class ProcessDefinition extends Element{
	
	protected String id;
	protected String name;
	protected String documentation;
	
	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}



	//-----Activity start
	protected StartEvent startEvent;
	protected EndEvent endEvent;
	protected List<UserTask> userTaskList = new ArrayList<UserTask>();
	protected List<CallActivity> callActivityList = new ArrayList<CallActivity>();
	protected List<ManualTask> manualTaskList = new ArrayList<ManualTask>();
	protected List<ServiceTask> serviceTaskList = new ArrayList<ServiceTask>();
	protected List<SubProcessDefinition> subProcessDefinitionList = new ArrayList<SubProcessDefinition>();
	
	protected List<ParallelGateway> parallelGatewayList = new ArrayList<ParallelGateway>();
	protected List<ExclusiveGateway> exclusiveGatewayList = new ArrayList<ExclusiveGateway>();
	protected List<InclusiveGateway> inclusiveGatewayList = new ArrayList<InclusiveGateway>();
	
	//-----Activity start
	
	protected List<SequenceFlow> sequenceFlowList = new ArrayList<SequenceFlow>();
	
	@XmlElement(name="startEvent")
	public StartEvent getStartEvent() {
		return startEvent;
	}

	public void setStartEvent(StartEvent startEvent) {
		this.startEvent = startEvent;
	}

	@XmlElement(name="endEvent")
	public EndEvent getEndEvent() {
		return endEvent;
	}

	public void setEndEvent(EndEvent endEvent) {
		this.endEvent = endEvent;
	}

	@XmlElement(name="sequenceFlow")
	public List<SequenceFlow> getSequenceFlowList() {
		return sequenceFlowList;
	}

	public void setSequenceFlowList(List<SequenceFlow> sequenceFlowList) {
		this.sequenceFlowList = sequenceFlowList;
	}

	@XmlElement(name="userTask")
	public List<UserTask> getUserTaskList() {
		return userTaskList;
	}

	public void setUserTaskList(List<UserTask> userTaskList) {
		this.userTaskList = userTaskList;
	}

	@XmlElement(name="parallelGateway")
	public List<ParallelGateway> getParallelGatewayList() {
		return parallelGatewayList;
	}

	public void setParallelGatewayList(List<ParallelGateway> parallelGatewayList) {
		this.parallelGatewayList = parallelGatewayList;
	}
	
	@XmlElement(name="exclusiveGateway")
	public List<ExclusiveGateway> getExclusiveGatewayList() {
		return exclusiveGatewayList;
	}

	public void setExclusiveGatewayList(List<ExclusiveGateway> exclusiveGatewayList) {
		this.exclusiveGatewayList = exclusiveGatewayList;
	}
	
	@XmlElement(name="inclusiveGateway")
	public List<InclusiveGateway> getInclusiveGatewayList() {
		return inclusiveGatewayList;
	}

	public void setInclusiveGatewayList(List<InclusiveGateway> inclusiveGatewayList) {
		this.inclusiveGatewayList = inclusiveGatewayList;
	}
	
	@XmlElement(name="callActivity")
	public List<CallActivity> getCallActivityList() {
		return callActivityList;
	}

	public void setCallActivityList(List<CallActivity> callActivityList) {
		this.callActivityList = callActivityList;
	}

	@XmlElement(name="manualTask")
	public List<ManualTask> getManualTaskList() {
		return manualTaskList;
	}

	public void setManualTaskList(List<ManualTask> manualTaskList) {
		this.manualTaskList = manualTaskList;
	}
	
	
	@XmlElement(name="serviceTask")
	public List<ServiceTask> getServiceTaskList() {
		return serviceTaskList;
	}

	public void setServiceTaskList(List<ServiceTask> serviceTaskList) {
		this.serviceTaskList = serviceTaskList;
	}

	@XmlElement(name="subProcess")
	public List<SubProcessDefinition> getSubProcessDefinitionList() {
		return subProcessDefinitionList;
	}

	public void setSubProcessDefinitionList(
			List<SubProcessDefinition> subProcessDefinitionList) {
		this.subProcessDefinitionList = subProcessDefinitionList;
	}
	

	//------------------------------------------------------------------------------------------JAXB Mapping END
	
	
	protected List<ActivityDefinition> activityList = new ArrayList<ActivityDefinition>();
	
	
	public List<ActivityDefinition> getActivityList() {
		return activityList;
	}
	
	
	protected Map<String,ActivityDefinition> activityMap = new HashMap<String,ActivityDefinition>();
	
	
	public Map<String, ActivityDefinition> getActivityMap() {
		return activityMap;
	}
	
	
	protected Map<String,SequenceFlow> sequenceFlowMap = new HashMap<String,SequenceFlow>();
	
	
	public Map<String, SequenceFlow> getSequenceFlowMap() {
		return sequenceFlowMap;
	}
	
	public ActivityDefinition getActivity(String activityId){
		return this.activityMap.get(activityId);
	}   

	
    public SequenceFlow getSequenceFlow(String sequenceFlowId){
		return this.sequenceFlowMap.get(sequenceFlowId);
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		build();
	}

	public ProcessDefinition build() {
		//clear
		activityList.clear();
		activityMap.clear();
		sequenceFlowMap.clear();
		
		//build
		if(startEvent!=null){
			activityList.add(startEvent);
		}
		if(endEvent!=null){
			activityList.add(endEvent);
		}
		activityList.addAll(userTaskList);
		activityList.addAll(parallelGatewayList);
		activityList.addAll(exclusiveGatewayList);
		activityList.addAll(inclusiveGatewayList);
		activityList.addAll(callActivityList);
		activityList.addAll(manualTaskList);
		activityList.addAll(serviceTaskList);
		activityList.addAll(subProcessDefinitionList);
		
		for(ActivityDefinition activity:activityList){
			activityMap.put(activity.getId(),activity);
		}
		
		for(SequenceFlow sequenceFlow:sequenceFlowList){
			sequenceFlowMap.put(sequenceFlow.getId(),sequenceFlow);
			

			String sourceRef = sequenceFlow.getSourceRef();
			ActivityDefinition sourceA = activityMap.get(sourceRef);
			//此时可能只追加了sequenceFlow,没有增加sequenceFlow的两端节点，所有需要判断是否为空
			if(sourceA!=null){
				sourceA.getOutgoingSequenceFlowList().add(sequenceFlow);
			}
			
			String targetRef = sequenceFlow.getTargetRef();
			ActivityDefinition targetA = activityMap.get(targetRef);
			//此时可能只追加了sequenceFlow,没有增加sequenceFlow的两端节点，所有需要判断是否为空
			if(targetA!=null){
				targetA.getIncomingSequenceFlowList().add(sequenceFlow);
			}
		}
		
		//为可能嵌套的子流程设置blockId
		if(!(this instanceof SubProcessDefinition)){
			setSequenceBlockId(subProcessDefinitionList,"");
		}
		
		return this;
	}

	//ProcessDefinition根下的块活动的sequenceBlockId就是自身块活动的id;
	//下面的快活动需要追加所属与的快活动
	private void setSequenceBlockId(
			List<SubProcessDefinition> searchList,String pre) {
		
		for(SubProcessDefinition s :searchList){
			if(RunBPMUtils.notNull(pre)){
				s.sequenceBlockId = pre +","+ s.id;
			}else{
				s.sequenceBlockId = s.id;
			}
			
			List<SubProcessDefinition> ss = s.subProcessDefinitionList;
			setSequenceBlockId(ss,s.sequenceBlockId);
		}
	}

	
	public Set<ActivityDefinition> getReachableActivitySet(ActivityDefinition activity) {
		// 初始化访问列表 
        Map<ActivityDefinition,Boolean> activityVisitMap = new HashMap<ActivityDefinition,Boolean>();
        for(ActivityDefinition a:activityList){
            activityVisitMap.put(a, new Boolean(false));
        }

        Set<ActivityDefinition> set = new HashSet<ActivityDefinition>();
        //搜索下一个
        searchNext(activity, activityVisitMap, set);

        return set;
	}
	
	    
    private void searchNext(ActivityDefinition activity, Map<ActivityDefinition,Boolean> activityVisitMap, Set<ActivityDefinition> set) {
         
    	List<SequenceFlow> outgoingSequenceFlowList = activity.getOutgoingSequenceFlowList();
        
        for(SequenceFlow sequenceFlow:outgoingSequenceFlowList){
        	ActivityDefinition toActivity = getActivity(sequenceFlow.getTargetRef());
        	boolean isVisit = activityVisitMap.get(toActivity).booleanValue();
            if (!isVisit) {
                activityVisitMap.put(toActivity, new Boolean(true));
                set.add(toActivity);
                searchNext(toActivity, activityVisitMap, set);
            }
        }
        
    }
    
    public SubProcessDefinition getSubProcessActivityDefinition(String sequenceBlockId){
    	
    	return searchSubProcessDefinition(this.activityMap,sequenceBlockId);
    }
    
    private SubProcessDefinition searchSubProcessDefinition( Map<String,ActivityDefinition> searchActivityMap ,String sequenceBlockId) {
		StringTokenizer st = new StringTokenizer(sequenceBlockId,",");
    	
		if(st.countTokens()<1){
    		throw new RunBPMException("the sequenceBlockId is invalid");
    	}
    	
    	if(st.countTokens()==1){
    		//在根目录，取第一个
    		String firstElement = st.nextElement().toString();
    		return getSubProcess(searchActivityMap,firstElement);
    	}else{
    		//在子目录
    		StringBuffer resetSequenceBlockId = new StringBuffer();
    		int i =0 ;
    		Map<String,ActivityDefinition> resetActivityMp= null;
    		while(st.hasMoreElements()){
    			String element = st.nextElement().toString();
    			
    			if(i==0){
    				//当i=0时，获得所属的块活动
    				resetActivityMp = getSubProcess(searchActivityMap,element).getActivityMap();
    				i++;
    			}else{
    				//当i>1时，获得除了根目录之外的r estSequenceBlockId
	    			if(resetSequenceBlockId.toString().length()==0){
	    				resetSequenceBlockId.append(element);
	    			}else{
	    				resetSequenceBlockId.append(",");
	    				resetSequenceBlockId.append(element);
	    			}	
    			}
    		}
    		return searchSubProcessDefinition(resetActivityMp,resetSequenceBlockId.toString());
    	}
	}
	
	//获取在子流程
	protected SubProcessDefinition getSubProcess(Map<String,ActivityDefinition> searchActivityMap,String subProcessId){
		SubProcessDefinition foundSubProcessDefinition = null;
		ActivityDefinition ad = searchActivityMap.get(subProcessId);
		if(!(ad instanceof SubProcessDefinition)){
			throw new RunBPMException("the subProcessId is not subprocess definitionId.["+ad+"]");
		}else{
			foundSubProcessDefinition = (SubProcessDefinition)ad;
		}
		return foundSubProcessDefinition;
	}

	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("流程定义数据信息：");
		stringBuffer.append("ProcessDefinition Id:[").append(this.getId()).append("]");
		stringBuffer.append("ProcessDefinition Name:[").append(this.getName()).append("]");
		return stringBuffer.toString();
	}
	
	//---for POJO build process---
	public ProcessDefinition(){
		this.id = "ProcessDefinition_"+UUID.randomUUID().toString();
	}
	
	public ProcessDefinition(String id){
		this.id = id;
	}
	
		public ActivityDefinition addFlowNode(ActivityDefinition activityDefinition){
			addFlowNode_(activityDefinition);
			activityDefinition.setProcessDefinition(this);
			this.build();
			return activityDefinition;
		}
		
		public ProcessDefinition addSequenceFlow(SequenceFlow sequenceFlow){
			this.sequenceFlowList.add(sequenceFlow);
			sequenceFlow.setProcessDefinition(this);
			this.build();
			return this;
		}
		

		private void addFlowNode_(ActivityDefinition activityDefinition){
			if(activityDefinition instanceof StartEvent ){
				this.startEvent =  (StartEvent) activityDefinition;
			}else if(activityDefinition instanceof EndEvent){
				this.endEvent = (EndEvent) activityDefinition;
			}
			else if(activityDefinition instanceof ManualTask){
				this.manualTaskList.add((ManualTask) activityDefinition);
			}
			else if(activityDefinition instanceof ParallelGateway){
				this.parallelGatewayList.add((ParallelGateway) activityDefinition);
			}
			else if(activityDefinition instanceof ExclusiveGateway){
				this.exclusiveGatewayList.add((ExclusiveGateway) activityDefinition);
			}
			else if(activityDefinition instanceof InclusiveGateway){
				this.inclusiveGatewayList.add((InclusiveGateway) activityDefinition);
			}else if(activityDefinition instanceof UserTask){
				this.userTaskList.add((UserTask) activityDefinition);
			}else if(activityDefinition instanceof CallActivity){
				this.callActivityList.add((CallActivity) activityDefinition);
			}else if(activityDefinition instanceof ServiceTask){
				this.serviceTaskList.add((ServiceTask) activityDefinition);
			}else if(activityDefinition instanceof SubProcessDefinition){
				this.subProcessDefinitionList.add((SubProcessDefinition) activityDefinition);
			}
		}
	//---for POJO build process---
}
