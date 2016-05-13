package org.runbpm.persistence;

/**
 * 用于事务绑定
 * 该类用于绑定数据库访问对象（Connection、Session）

 */
public class TransactionObjectHolder {
	/**
	 * 线程局部容器，用于保持数据库访问对象
	 */
	private static final ThreadLocal<TransactionObject> container = new ThreadLocal<TransactionObject>();
	
	
	public static void bind(TransactionObject object) {
		container.set(object);
	}
	
	public static TransactionObject unbind() {
		TransactionObject transactionObject = container.get();
		container.remove();
		return transactionObject;
	}
	
	
	public static TransactionObject get() {
		return container.get();
	}
	
	
	public static boolean isExistingTransaction() {
		return get() != null;
	}
}
