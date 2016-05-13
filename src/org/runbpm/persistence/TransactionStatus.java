package org.runbpm.persistence;

/**
* ����״̬
*/
public class TransactionStatus {
	
	private TransactionObject transactionObject;
	/**
	 * �Ƿ�Ϊ�µ����񣬿��ǵ�ҵ��㻥����õ���������ǰcommit
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
	 * �ж��Ƿ�Ϊ�µ������������ҵ��Ƕ��ʱcommit�ж��Ƿ����������ύ
	 */
	public boolean isNewTransaction() {
		return newTransaction;
	}
}
