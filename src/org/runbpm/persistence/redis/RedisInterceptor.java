package org.runbpm.persistence.redis;

import org.runbpm.exception.RunBPMException;
import org.runbpm.persistence.TransactionInterceptor;
import org.runbpm.persistence.TransactionObject;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.persistence.TransactionStatus;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisInterceptor extends TransactionInterceptor{
	
	private ShardedJedisPool shardedJedisPool;
	
	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}
	
	@Override
	public void initialize(Object accessObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected TransactionStatus getTransaction() {
		//开始调用
    	try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if(isExistingTransaction) {
				return new TransactionStatus(TransactionObjectHolder.get(), false);
			}else{
				ShardedJedis shardedJedis = shardedJedisPool.getResource();
				
				TransactionObject to = new TransactionObject();
				to.setShardedJedis(shardedJedis);
	            
				TransactionObjectHolder.bind(to);
				return new TransactionStatus(to, true);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(TransactionObjectHolder.get() != null){
				TransactionObjectHolder.unbind();
			}
			throw new RunBPMException(e.getMessage(), e);
		}
	}

	@Override
	protected void commit(TransactionStatus status) {
		TransactionObjectHolder.unbind();
		ShardedJedis shardedJedis  = status.getTransactionObject().getShardedJedis();
		shardedJedis.close();
		
	}

	@Override
	protected void rollback(TransactionStatus status) {
		TransactionObjectHolder.unbind();
		ShardedJedis shardedJedis  = status.getTransactionObject().getShardedJedis();
		shardedJedis.close();
	}
}
