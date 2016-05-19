package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;

public class SubProcessDefinition extends ProcessDefinition implements ActivityDefinition{

	//以下属性在process类分析得到
	private List<SequenceFlow> incomingSequenceFlowList = new ArrayList<SequenceFlow>();
	private List<SequenceFlow> outgoingSequenceFlowList = new ArrayList<SequenceFlow>();
	
	protected String sequenceBlockId;
	
	/**
	 * ProcessDefinition根下的块活动的sequenceBlockId就是自身块活动的id;<br>
	 * 块活动之内的块活动需要追加所属与的块活动ID。该方法返回一个块活动自ProcessDefinition根之后的完整块活动定义ID。
	 * @return
	 */
	public String getSequenceBlockId() {
		return sequenceBlockId;
	}
	
	public List<SequenceFlow> getIncomingSequenceFlowList() {
		return incomingSequenceFlowList;
	}
	public List<SequenceFlow> getOutgoingSequenceFlowList() {
		return outgoingSequenceFlowList;
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SubProcess活动定义数据信息：");
		stringBuffer.append("ActivityDefinition Id:[").append(this.getId()).append("]");
		stringBuffer.append("ActivityDefinition Name:[").append(this.getName()).append("]");
		return stringBuffer.toString();
	}

	ProcessDefinition processDefinition;  // parent pointer

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
	
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		super.afterUnmarshal(u,parent);
		this.processDefinition = (ProcessDefinition)parent;
		
	}

	@Override
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
		
	}

	@Override
	public ProcessDefinition flowTo(ActivityDefinition activityDefinition) {
		SequenceFlow sequenceFlow = new SequenceFlow(this,activityDefinition);
		this.processDefinition.addSequenceFlow(sequenceFlow);
		this.processDefinition.addFlowNode(activityDefinition);
		processDefinition.build();
		return processDefinition;	
	}
	
}
