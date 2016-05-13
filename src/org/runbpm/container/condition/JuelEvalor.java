package org.runbpm.container.condition;

import java.util.Iterator;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.runbpm.context.Execution;
import org.runbpm.entity.VariableInstance;

import de.odysseus.el.ExpressionFactoryImpl;

public class JuelEvalor {
	
	private static ExpressionFactory expressionFactory = new ExpressionFactoryImpl();

	public Object eval(String condition, Execution handlerContext){
		 Map<String, VariableInstance> variableMap = handlerContext.getVariableMap();
		Object result = false;
		// de.odysseus.el.util provides包提供即时可用的子类ELContext
		de.odysseus.el.util.SimpleContext context = new de.odysseus.el.util.SimpleContext();

		Iterator<String> dataFieldInstanceMapIt = variableMap.keySet().iterator();
		while (dataFieldInstanceMapIt.hasNext()) {
			String expressName = dataFieldInstanceMapIt.next();
			Object dataFieldInstanceValue = variableMap.get(expressName).getValue();

			context.setVariable(expressName, expressionFactory.createValueExpression(
							dataFieldInstanceValue,
							Object.class));
		}
		ValueExpression valueExpression = expressionFactory
				.createValueExpression(context, condition,Object.class);
		result = valueExpression.getValue(context);
		
		return result;
	}
}
