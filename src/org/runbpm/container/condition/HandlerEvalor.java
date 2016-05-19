package org.runbpm.container.condition;

import java.util.HashMap;
import java.util.Map;

import org.runbpm.context.ProcessContextBean;
import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.DecisionHandler;

public class HandlerEvalor {
	
	private static Map<String,DecisionHandler> specialConditionHandlerMap = new HashMap<String,DecisionHandler>();
	
	private static HandlerEvalor eval = new HandlerEvalor(); 
	
	private HandlerEvalor(){
	}
	
	public static HandlerEvalor getInstance(){
		return eval;
	}

	public Object eval(String className,ProcessContextBean processContextBean){
		Object result = null;
		
		DecisionHandler specialConditionHandler = specialConditionHandlerMap.get(className);
		if(specialConditionHandler==null){
			try {
				specialConditionHandler = (DecisionHandler)Class.forName(className).newInstance();
				specialConditionHandlerMap.put(className, specialConditionHandler);
			} catch (Exception e) {
				throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_020111_CANNT_INIT_SpecialConditionHandler_Impl,"class Name :["+className+"]",e);
			}
		}
		result = specialConditionHandler.evalConditionContext(processContextBean);
		return result;
	}
}
