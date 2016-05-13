package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

public class ExtensionElements {
	
	//--extensionProps
	private ExtensionProps extensionProperties = new ExtensionProps();
	
	@XmlElement(name="extensionProps",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public ExtensionProps getExtensionProperties() {
		return extensionProperties;
	}

	public void setExtensionProperties(ExtensionProps extensionProperties) {
		this.extensionProperties = extensionProperties;
	}
	//--//extensionProps
	
	//--userTaskResource
	private UserTaskResource userTaskResource;

	@XmlElement(name="resource",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public UserTaskResource getUserTaskResource() {
		return userTaskResource;
	}

	public void setUserTaskResource(UserTaskResource userTaskResource) {
		this.userTaskResource = userTaskResource;
	}
	//---//userTaskResource
	
	//---Call Activity in and out list
	
	private List<InElement> inElementList = new ArrayList<InElement>();
	
	private List<OutElement> outElementList = new ArrayList<OutElement>();
	
	
	@XmlElement(name="in",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<InElement> getInElementList() {
		return inElementList;
	}

	public void setInElementList(List<InElement> inElementList) {
		this.inElementList = inElementList;
	}

	@XmlElement(name="out",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<OutElement> getOutElementList() {
		return outElementList;
	}

	public void setOutElementList(List<OutElement> outElementList) {
		this.outElementList = outElementList;
	}
	
	
	//---//Call Activity in and out list
	
	//---executionListener
	
	private List<ExtensionExecutionListener> listenerList = new ArrayList<ExtensionExecutionListener>();
	
	@XmlElement(name="executionListener",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<ExtensionExecutionListener> getListenerList() {
		return listenerList;
	}

	public void setListenerList(List<ExtensionExecutionListener> listenerList) {
		this.listenerList = listenerList;
	}

	
	//---//executionListener
	
	//-----end JAXP
	

	public String getPropertyValue(String name){
		ExtensionProps extensionProperties = getExtensionProperties();
		List<ExtensionProperty> list = extensionProperties.getpropertyList();
		for(ExtensionProperty extensionProperty:list){
			String propertyName = extensionProperty.getName();
			if(propertyName.equals(name)){
				String value = extensionProperty.getValue();
				return value;
			}
		}
		return "";
	}
	
	public List<String> getExtensionPropsList(String name){
		ExtensionProps extensionProperties = getExtensionProperties();
		List<ExtensionProperty> list = extensionProperties.getpropertyList();
		for(ExtensionProperty extensionProperty:list){
			String propertyName = extensionProperty.getName();
			if(propertyName.equals(name)){
				ExtensionPropsList extensionPropsList = extensionProperty.getExtensionPropsList();
				List<ExtensionPropsListValue> values = extensionPropsList.getValueList();
				
				List<String> propsList =new ArrayList<String>();
				for(ExtensionPropsListValue entry : values){
					propsList.add(entry.getValue());
				}
				return propsList;
			}
		}
		return null;
	}
	
	public Map<String,String> getExtensionPropsMap(String name){
		
		ExtensionProps extensionProperties = getExtensionProperties();
		List<ExtensionProperty> list = extensionProperties.getpropertyList();
		for(ExtensionProperty extensionProperty:list){
			String propertyName = extensionProperty.getName();
			if(propertyName.equals(name)){
				ExtensionPropsMap extensionPropsMap = extensionProperty.getExtensionPropsMap();
				List<ExtensionPropsMapEntry> mapList = extensionPropsMap.getEntryList();
				
				Map<String,String> map =new HashMap<String,String>();
				for(ExtensionPropsMapEntry entry : mapList){
					map.put(entry.getKey(), entry.getValue());
				}
				return map;
			}
		}
		return null;
	}
	
}
