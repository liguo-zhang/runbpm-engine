package org.runbpm.persistence.redis;

import org.runbpm.entity.EntityConstants.VARIABLE_TYPE;
import org.runbpm.entity.Variable_;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.RunBPMUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisVariable_ extends Variable_  {

	protected String domain;
	
	protected boolean flushRedis = true;
	
	public boolean isFlushRedis() {
		return flushRedis;
	}

	public void setFlushRedis(boolean flushRedis) {
		this.flushRedis = flushRedis;
	}

	
	public void setProcessDefinitionId(String processDefinitionId) {
		super.setProcessDefinitionId(processDefinitionId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.val_procdefid, processDefinitionId);
		}
	}

	public void setProcessInstanceId(Long processInstanceId) {
		super.setProcessInstanceId(processInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.val_procinstid, processInstanceId.toString());
		}
	}

	
	public void setName(String name) {
		super.setName(name);
		if(flushRedis){
			if(RunBPMUtils.notNull(name)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.val_name, name);
			}
		}
	}

	
	public void setDescription(String description) {
		super.setDescription(description);
		if(flushRedis){
			if(RunBPMUtils.notNull(description)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.val_desc, description);
			}
		}
	}

	
	public void setType(VARIABLE_TYPE type) {
		super.setType(type);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.val_type, type.toString());
		}
	}

	
	public void setValue(Object value) {
		super.setValue(value);
		//do nothing in jedis side
	}
	
	public void setValueString(String valueString) {
		super.setValueString(valueString);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.val_valuestr, valueString);
		}
	}
	
}
