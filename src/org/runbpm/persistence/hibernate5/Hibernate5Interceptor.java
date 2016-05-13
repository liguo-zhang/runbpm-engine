package org.runbpm.persistence.hibernate5;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.TransactionInterceptor;
import org.runbpm.persistence.TransactionObject;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.persistence.TransactionStatus;
import org.runbpm.utils.AssertHelper;

public class Hibernate5Interceptor extends TransactionInterceptor{
	
	//spring注射
	public SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void initialize(Object accessObject) {
		
	}

	@Override
	protected TransactionStatus getTransaction() {
		//开始调用
    	try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if(isExistingTransaction) {
				return new TransactionStatus(TransactionObjectHolder.get(), false);
			}else{
				Session session = sessionFactory.openSession();
				session.getTransaction().begin();
				
				TransactionObject to = new TransactionObject();
				to.setSession(session);
	            
				TransactionObjectHolder.bind(to);
				return new TransactionStatus(to, true);
			}
		} catch (Exception e) {
			if(TransactionObjectHolder.get() != null){
				TransactionObjectHolder.unbind();
			}
			logger.error(e.getMessage(), e);
			throw new RunBPMException(e.getMessage(), e);
		}
	}

	@Override
	protected void commit(TransactionStatus status) {
		AssertHelper.isTrue(status.isNewTransaction());
		Session session = status.getTransactionObject().getSession();
        if (session != null) {
            try {
            	
            	logger.info("commit transaction=" + session.hashCode());
            	session.getTransaction().commit();
            	session.close();
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	TransactionObjectHolder.unbind();
            	if(session!=null&&session.isOpen()){
            		session.close();
            	}
            }
        }
	}

	@Override
	protected void rollback(TransactionStatus status) {
		Session session = status.getTransactionObject().getSession();
        if (session != null) {
            try {
            	
            	logger.info("rollback transaction=" + session.hashCode());
            	
            	if(session!=null&&session.isOpen()){
            		session.getTransaction().rollback();
            		session.close();
            	}
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	TransactionObjectHolder.unbind();
            	if(session!=null&&session.isOpen()){
            		session.close();
            	}
            }
        }
		
	}
	
}
