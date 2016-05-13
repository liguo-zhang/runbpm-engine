package org.runbpm.persistence;

import org.aspectj.lang.ProceedingJoinPoint;
import org.runbpm.exception.RunBPMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public abstract class TransactionInterceptor {
	
	protected static final Logger logger = LoggerFactory
			.getLogger(TransactionInterceptor.class);
	
	private boolean recordTime = false;
	
	public boolean isRecordTime() {
		return recordTime;
	}

	public void setRecordTime(boolean recordTime) {
		this.recordTime = recordTime;
	}
	public Object interceptor(ProceedingJoinPoint call) throws Throwable {
			StopWatch clock = null;
			if(this.recordTime){
				clock =  new StopWatch("Profiling for ");
				clock.start(call.toShortString());
			}
	        
			TransactionStatus status = null;
	        try {
	        	//��ʼ����
	        	status = getTransaction();
	            
	    		//�����������
	            Object result = call.proceed();
	            
	            //�������Ƕ�׵��ã����ύ
	            if(status.isNewTransaction()) {
					commit(status);
				}
	            return result;
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        	//��Ƕ��ʱ��ع�
	        	if(status!=null&&status.isNewTransaction()) {
	        		rollback(status);
	        	}
	    		throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_999999_NO_RunBPM_Rollback,"RunBPM Error",e);
	    	}
	}
	
	/**
	 * ��ʼ�����������������÷��ʶ���
	 * @param accessObject
	 */
	public abstract void initialize(Object accessObject);
	
	/**
	 * ���ص�ǰ����״̬
	 * @return
	 */
	protected abstract TransactionStatus getTransaction();
	
	/**
	 * �ύ��ǰ����״̬
	 * @param status
	 */
	protected abstract void commit(TransactionStatus status);
	
	/**
	 * �ع���ǰ����״̬
	 * @param status
	 */
	protected abstract void rollback(TransactionStatus status);

}
