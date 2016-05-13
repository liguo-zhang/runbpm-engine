package org.runbpm.persistence;

/**
 * ���������
 * �������ڰ����ݿ���ʶ���Connection��Session��

 */
public class TransactionObjectHolder {
	/**
	 * �ֲ߳̾����������ڱ������ݿ���ʶ���
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
