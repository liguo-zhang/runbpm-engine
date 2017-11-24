package org.runbpm.bpmn.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

public class ExtensionElements {
	
	//--extensionProps
	private ExtensionProps extensionProperties = new ExtensionProps();
	
	/**
	 * ��ȡRunBPM�涨�ĵı�׼��չ���Զ���<br>
	 * ����ʹ�� {@link #getPropertyValue(String)} {@link #getExtensionPropsList(String)} {@link #getExtensionPropsMap(String)} ������Ļ�ȡRunBPM�涨�ĵı�׼��չ���Զ���
	 * @return
	 */
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

	/**
	 * �������һ��UserTask,�����ͨ���÷�����ȡ��Ա������Ϣ
	 * @return
	 */
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
	
	
	/**
	 * ����ڵ���CallActivity������ͨ���˲�����ȡ���������Ϣ��
	 * @return
	 */
	@XmlElement(name="in",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<InElement> getInElementList() {
		return inElementList;
	}

	public void setInElementList(List<InElement> inElementList) {
		this.inElementList = inElementList;
	}

	/**
	 * ����ڵ���CallActivity������ͨ���˲�����ȡ���������Ϣ��
	 * @return
	 */
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
	
	/**
	 * �����һ�����̶�����󡢽ڵ㶨��������ͨ���÷�����ȡ��չ���ԡ�
	 * @return
	 */
	@XmlElement(name="executionListener",namespace = "http://runbpm.org/schema/1.0/bpmn")
	public List<ExtensionExecutionListener> getListenerList() {
		return listenerList;
	}

	public void setListenerList(List<ExtensionExecutionListener> listenerList) {
		this.listenerList = listenerList;
	}

	
	//---//executionListener
	
	//-----end JAXP
	

	/**
	 * ͨ��ָ�������ƣ���ȡlist��ʽ����չ����.�������ͨ��testList��ȡһ��list�ṹ����չ���ԡ�
	 * <pre>
	 *	 &lt;extensionElements&gt;
	      	&lt;runbpm:extensionProps&gt;
			  &lt;runbpm:extensionProp name=&quot;testPropValue&quot; value=&quot;text&quot;&gt;&lt;/runbpm:extensionProp&gt;
  		  	&lt;/runbpm:extensionProps&gt;
  		  &lt;/extensionElements&gt;
  		</pre>
	 * @param name map��ʽ�Ķ�����Ϣ
	 * @return
	 */
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
	
	/**
	 * ͨ��ָ�������ƣ���ȡlist��ʽ����չ����.�������ͨ��testList��ȡһ��list�ṹ����չ���ԡ�
	 * <pre>
			&lt;extensionElements&gt;
				&lt;runbpm:extensionProps&gt;
					&lt;runbpm:extensionProp name=&quot;testList&quot;&gt;
				            &lt;runbpm:list&gt;
				              &lt;runbpm:value&gt;11&lt;/runbpm:value&gt;
				              &lt;runbpm:value&gt;22&lt;/runbpm:value&gt;
				              &lt;runbpm:value&gt;33&lt;/runbpm:value&gt;
				            &lt;/runbpm:list&gt;
		          		&lt;/runbpm:extensionProp&gt;
		          &lt;/runbpm:extensionProps&gt;
  			&lt;/extensionElements&gt;
  		</pre>
	 * @param name map��ʽ�Ķ�����Ϣ
	 * @return
	 */
	public List<String> getExtensionPropsList(String name){
		return searchExtensionPropsList(name,false);
	}
	
	public List<String> getExtensionPropsListByPrefix(String name){
		return searchExtensionPropsList(name,true);
	}
	
	public List<String> searchExtensionPropsList(String name,boolean isPrefix){
		ExtensionProps extensionProperties = getExtensionProperties();
		List<ExtensionProperty> list = extensionProperties.getpropertyList();
		for(ExtensionProperty extensionProperty:list){
			String propertyName = extensionProperty.getName();
			
			//�ж�����
			boolean result = false;
			if(isPrefix) {
				result  = propertyName.startsWith(name);
			}else {
				result  = propertyName.equals(name);
			}
			
			if(result){
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
	
	/**
	 * ͨ��ָ�������ƣ���ȡmap��ʽ����չ����.�������ͨ��testMap��ȡһ��map�ṹ����չ���ԡ�
	 * <pre>
			&lt;extensionElements&gt;
				&lt;runbpm:extensionProps&gt;
				&lt;runbpm:extensionProp name=&quot;testMap&quot;&gt;
			            &lt;runbpm:map&gt;
			              &lt;runbpm:entry key=&quot;11&quot;&gt;22&lt;/runbpm:entry&gt;
			            &lt;/runbpm:map&gt;
			          &lt;/runbpm:extensionProp&gt;
			     &lt;/runbpm:extensionProps&gt;
  			&lt;/extensionElements&gt;
  		</pre>
	 * @param name map��ʽ�Ķ�����Ϣ
	 * @return
	 */
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
