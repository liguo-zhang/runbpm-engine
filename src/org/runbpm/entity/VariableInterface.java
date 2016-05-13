package org.runbpm.entity;

import org.runbpm.entity.EntityConstants.VARIABLE_TYPE;

public interface VariableInterface extends EntityInterface {


	public String getProcessDefinitionId() ;

	public void setProcessDefinitionId(String processDefinitionId) ;

	public Long getProcessInstanceId() ;

	public void setProcessInstanceId(Long processInstanceId) ;
	
	public String getDescription() ;

	public void setDescription(String description) ;

	public VARIABLE_TYPE getType() ;

	public void setType(VARIABLE_TYPE type) ;

	public Object getValue() ;

	public void setValue(Object value) ;
	
	public String getValueString() ;

	public void setValueString(String valueString) ;
	
}
