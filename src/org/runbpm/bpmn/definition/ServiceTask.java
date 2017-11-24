package org.runbpm.bpmn.definition;

import java.util.Map;


public class ServiceTask extends ActivityDefinitionImpl{

	public static final String EXTENSION_NAME = "RunBPM_Internal_ServiceTask_Extension";
	
	public enum SERVICETASK_TYPE{handler_bean_id,handler_bean_class,handler_bean_id_variable,handler_bean_class_variable}
	
	public SERVICETASK_TYPE getType() {
		Map<String,String> map = this.getExtensionElements().getExtensionPropsMap(EXTENSION_NAME);
		String type = map.get("type");
		if(ServiceTask.SERVICETASK_TYPE.handler_bean_class.toString().equals(type)){
			return ServiceTask.SERVICETASK_TYPE.handler_bean_class; 
		}else{
			return null;
		}
	}

	
	public String getValue() {
		Map<String,String> map = this.getExtensionElements().getExtensionPropsMap(EXTENSION_NAME);
		String value = map.get("value");
		return value;
	}
    
}
