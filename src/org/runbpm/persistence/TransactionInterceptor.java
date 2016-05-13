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
	        	//开始调用
	        	status = getTransaction();
	            
	    		//真正发起调用
	            Object result = call.proceed();
	            
	            //如果不是嵌套调用，则提交
	            if(status.isNewTransaction()) {
					commit(status);
				}
	            return result;
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        	//不嵌套时候回滚
	        	if(status!=null&&status.isNewTransaction()) {
	        		rollback(status);
	        	}
	    		throw new RunBPMException(RunBPMException.EXCEPTION_MESSAGE.Code_999999_NO_RunBPM_Rollback,"RunBPM Error",e);
	    	}
	}
	
	/**
	 * 初始化事务拦截器，设置访问对象
	 * @param accessObject
	 */
	public abstract void initialize(Object accessObject);
	
	/**
	 * 返回当前事务状态
	 * @return
	 */
	protected abstract TransactionStatus getTransaction();
	
	/**
	 * 提交当前事务状态
	 * @param status
	 */
	protected abstract void commit(TransactionStatus status);
	
	/**
	 * 回滚当前事务状态
	 * @param status
	 */
	protected abstract void rollback(TransactionStatus status);

}
