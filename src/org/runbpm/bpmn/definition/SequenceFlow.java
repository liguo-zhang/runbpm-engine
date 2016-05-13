package org.runbpm.bpmn.definition;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SequenceFlow extends FlowElement{

	protected String sourceRef;
	
	protected String targetRef;
	
	protected ConditionExpression conditionExpression;

	@XmlAttribute
	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	@XmlAttribute
	public String getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}

	@XmlElement
	public ConditionExpression getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(ConditionExpression conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	public SequenceFlow(){
		super("SequenceFlow_"+UUID.randomUUID().toString());
	}
	
	public SequenceFlow(String id){
		super(id);
	}
	
	public SequenceFlow(ActivityDefinition sourceRef, ActivityDefinition targetRef ){
		super("SequenceFlow_"+UUID.randomUUID().toString());
		this.sourceRef = sourceRef.getId();
		this.targetRef = targetRef.getId();
	}
	
	public SequenceFlow(String id,ActivityDefinition sourceRef, ActivityDefinition targetRef ){
		super(id);
		this.sourceRef = sourceRef.getId();
		this.targetRef = targetRef.getId();
	}
	
	public SequenceFlow(String id,String sourceRef , String targetRef ){
		super(id);
		this.sourceRef = sourceRef;
		this.targetRef = targetRef;
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("连接弧定义数据信息：");
		stringBuffer.append("TransitionDefinition Id:[").append(this.getId()).append("]");
		stringBuffer.append("TransitionDefinition Name:[").append(this.getName()).append("]");
		return stringBuffer.toString();
	}
	
}