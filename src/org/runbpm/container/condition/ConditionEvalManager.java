package org.runbpm.container.condition;

import java.util.Map;

import org.runbpm.bpmn.definition.ConditionExpression;
import org.runbpm.bpmn.definition.SequenceFlow;
import org.runbpm.context.Configuration;
import org.runbpm.context.ProcessContextBean;
import org.runbpm.entity.VariableInstance;
import org.runbpm.exception.RunBPMException;
import org.runbpm.handler.DecisionHandler;
import org.runbpm.utils.RunBPMUtils;

public class ConditionEvalManager {
	

	public Object eval(ProcessContextBean processContextBean) {
		Object result = null;
		SequenceFlow transition = processContextBean.getSequenceFlow();
		Map<String, VariableInstance> variableMap = processContextBean.getVariableMap();
		String expressValue = transition.getConditionExpression().getValue();
		if (RunBPMUtils.notNull(expressValue)) {
			// 开始计算 默认为juel
			ConditionExpression.CONDITION_EXPRESSION_TYPE tType = transition.getConditionExpression().getAdvancedType();
			if (tType == null||tType.equals(ConditionExpression.CONDITION_EXPRESSION_TYPE.juel)) {
				JuelEvalor eval = new JuelEvalor();
				result = eval.eval(expressValue,processContextBean);
			}
			else if (tType.equals(ConditionExpression.CONDITION_EXPRESSION_TYPE.aviator_el)) {
				AviatorELEvalor eval = new AviatorELEvalor();
				result = eval.eval(expressValue,processContextBean);
			}
			// 类加载: handler_bean_class
			else if (tType.equals(ConditionExpression.CONDITION_EXPRESSION_TYPE.handler_bean_class)) {
				HandlerEvalor handlerEvalor = HandlerEvalor
						.getInstance();
				result = handlerEvalor.eval(expressValue,processContextBean);
			}
			// spring bean : handler_bean_id
			else if (tType.equals(ConditionExpression.CONDITION_EXPRESSION_TYPE.handler_bean_id)) {
				result = evalByBeanId(expressValue, processContextBean);
			}
			// 动态类加载 : handler_bean_class_variable
			else if (tType.equals(ConditionExpression.CONDITION_EXPRESSION_TYPE.handler_bean_class_variable)) {	
				VariableInstance v = variableMap.get(expressValue);
				if (v == null) {
					throw new RunBPMException(
							RunBPMException.EXCEPTION_MESSAGE.Code_020102_GOT_NULL_VAR_FOR_SpecialConditionHandler_Impl,
							"variable[" + expressValue + "]");
				}
				String className = v.getValue().toString();

				HandlerEvalor handlerEvalor = HandlerEvalor
						.getInstance();
				result = handlerEvalor.eval(className, processContextBean);

			}
			// 动态spring bean : handler_bean_id_variable
			else if (tType.equals(ConditionExpression.CONDITION_EXPRESSION_TYPE.handler_bean_id_variable)) {
				VariableInstance v = variableMap.get(expressValue);
				if (v == null) {
					throw new RunBPMException(
							RunBPMException.EXCEPTION_MESSAGE.Code_020102_GOT_NULL_VAR_FOR_SpecialConditionHandler_Impl,
							"variable[" + expressValue + "]");
				}
				String beanId = v.getValue().toString();

				result = evalByBeanId(beanId, processContextBean);
			}

		}
		return result;
	}

	private Object evalByBeanId(String beanId, ProcessContextBean processContextBean) {
		Object result;
		Object springBean = Configuration.getContext().getBean(beanId);
		if (springBean instanceof DecisionHandler) {
			DecisionHandler specialConditionHandler = (DecisionHandler) springBean;
			result = specialConditionHandler
					.evalConditionContext(processContextBean);
		} else {
			throw new RunBPMException(
					RunBPMException.EXCEPTION_MESSAGE.Code_020101_NO_SpecialConditionHandler_Impl,
					"bean id :[" + beanId + "]");
		}
		return result;
	}
}
