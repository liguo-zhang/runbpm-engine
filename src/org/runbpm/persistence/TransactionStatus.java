package org.runbpm.persistence;

/**
* 事务状态
*/
public class TransactionStatus {
	
	private TransactionObject transactionObject;
	/**
	 * 是否为新的事务，考虑到业务层互相调用导致事务提前commit
	 */
	private final boolean newTransaction;
	
	public TransactionStatus(TransactionObject transactionObject, boolean newTransaction) {
		this.transactionObject = transactionObject;
		this.newTransaction = newTransaction;
	}

	public TransactionObject getTransactionObject() {
		return transactionObject;
	}

	public void setTransactionObject(TransactionObject transactionObject) {
		this.transactionObject = transactionObject;
	}

	/**
	 * 判断是否为新的事务对象，用于业务嵌套时commit判断是否对事务进行提交
	 */
	public boolean isNewTransaction() {
		return newTransaction;
	}
}
