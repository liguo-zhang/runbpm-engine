package org.runbpm;

public class RunBPMTestCase {
	
	

	public String getBPMNXMLName(){
		
		String className = this.getClass().getName();
		String pkgName = this.getClass().getPackage().getName();
		String fileName = className.substring(className.indexOf(pkgName)+pkgName.length()+1,className.length())+".xml";
		return fileName;
		
	}
}
