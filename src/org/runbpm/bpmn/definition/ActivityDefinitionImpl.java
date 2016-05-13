package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

public class ActivityDefinitionImpl extends FlowElement implements ActivityDefinition{

	//以下属性在process类分析得到
	private List<SequenceFlow> incomingSequenceFlowList = new ArrayList<SequenceFlow>();
	private List<SequenceFlow> outgoingSequenceFlowList = new ArrayList<SequenceFlow>();
	
	public List<SequenceFlow> getIncomingSequenceFlowList() {
		return incomingSequenceFlowList;
	}
	public List<SequenceFlow> getOutgoingSequenceFlowList() {
		return outgoingSequenceFlowList;
	}

	public ActivityDefinitionImpl(){
		super();
	}
	
	public ActivityDefinitionImpl(String id){
		super(id);
	}
	
	public ProcessDefinition flowTo(ActivityDefinition activityDefinition){
		SequenceFlow sequenceFlow = new SequenceFlow(this,activityDefinition);

		// 必须先加flownode节点，再加sequenceFlow;否则sequenceFlow的target节点找不到会抛空指针
		// 上面一行的限制已经解除，在processdefinition的build方法中已经增加判断
		this.processDefinition.addSequenceFlow(sequenceFlow);
		this.processDefinition.addFlowNode(activityDefinition);
		this.processDefinition.build();
		return processDefinition;
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("活动定义数据信息：");
		stringBuffer.append("ActivityDefinition Id:[").append(this.getId()).append("]");
		stringBuffer.append("ActivityDefinition Name:[").append(this.getName()).append("]");
		return stringBuffer.toString();
	}
}
