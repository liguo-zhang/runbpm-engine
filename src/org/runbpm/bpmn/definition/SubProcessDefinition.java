package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;

public class SubProcessDefinition extends ProcessDefinition implements ActivityDefinition{

	//����������process������õ�
	private List<SequenceFlow> incomingSequenceFlowList = new ArrayList<SequenceFlow>();
	private List<SequenceFlow> outgoingSequenceFlowList = new ArrayList<SequenceFlow>();
	
	protected String sequenceBlockId;
	
	/**
	 * ProcessDefinition���µĿ���sequenceBlockId�����������id;<br>
	 * ��֮�ڵĿ���Ҫ׷��������Ŀ�ID���÷�������һ������ProcessDefinition��֮�������������ID��
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
		stringBuffer.append("SubProcess�����������Ϣ��");
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
