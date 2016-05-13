package org.runbpm.bpmn.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class DataField {
	
	private String id;
	
	private String description;
	
	private String type;
	
	public enum TYPE {
		Integer("java.lang.Integer","Basic:java.lang.Integer"),
		String("java.lang.String","Basic:java.lang.String"),
		Double("java.lang.Double","Basic:java.lang.Double"),
		Float("java.lang.Float","Basic:java.lang.Float"),
		Boolean("java.lang.Boolean","Basic:java.lang.Boolean");
		
		private String classForName;
		private String nameInXML;
		
		private TYPE(String classForName, String nameInXML){
			this.classForName = classForName;
			this.nameInXML = nameInXML;
		}
		
		
		public String getClassForName() {
			return classForName;
		}

		public String getNameInXML() {
			return nameInXML;
		}

		public static String getClassForNameByXML(String nameInXML){
			for (TYPE t : TYPE.values()) {
                if (t.getNameInXML() == nameInXML) {
                    return t.classForName;
                }
            }
            return null;
		}
	}
	
	
	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
