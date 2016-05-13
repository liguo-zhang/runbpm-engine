package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

public class ActivityDefinitionImpl extends FlowElement implements ActivityDefinition{

	//����������process������õ�
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

		// �����ȼ�flownode�ڵ㣬�ټ�sequenceFlow;����sequenceFlow��target�ڵ��Ҳ������׿�ָ��
		// ����һ�е������Ѿ��������processdefinition��build�������Ѿ������ж�
		this.processDefinition.addSequenceFlow(sequenceFlow);
		this.processDefinition.addFlowNode(activityDefinition);
		this.processDefinition.build();
		return processDefinition;
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("�����������Ϣ��");
		stringBuffer.append("ActivityDefinition Id:[").append(this.getId()).append("]");
		stringBuffer.append("ActivityDefinition Name:[").append(this.getName()).append("]");
		return stringBuffer.toString();
	}
}
