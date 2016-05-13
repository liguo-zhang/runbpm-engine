package org.runbpm.container.condition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.runbpm.context.Execution;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.exception.ExpressionRuntimeException;

public class AviatorELEvalor {
	
	public Object eval(String condition, Execution handlerContext){
		Map<String, VariableInstance> variableMap = handlerContext.getVariableMap();
		Object result = false;
		
		condition = condition.replace("${", "");
		condition = condition.replace("}", "");
        
        Map<String, Object> env = new HashMap<String, Object>();
        Iterator<String> dataFieldInstanceMapIt = variableMap.keySet().iterator();
		while (dataFieldInstanceMapIt.hasNext()) {
			String expressName = dataFieldInstanceMapIt.next();
			Object dataFieldInstanceValue = variableMap.get(expressName).getValue();
			env.put(expressName, dataFieldInstanceValue);
        }
    	try{
    		result =  AviatorEvaluator.execute(condition, env);
    	}catch(ExpressionRuntimeException e){
    		throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020003_Runtime_Exception_From_Aviator,handlerContext.toString(),e);
    	}
		return result;
	}
}
