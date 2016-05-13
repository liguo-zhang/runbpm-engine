package org.runbpm.persistence.redis;

import java.util.Date;

import org.runbpm.entity.ProcessModel_;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.RunBPMUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisProcessModel_ extends ProcessModel_ {
	
	protected String domain;
	
	public void setName(String name) {
		super.setName(name);
		if(RunBPMUtils.notNull(name)){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.procmodel_name, name);
		}
	}
	
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		shardedJedis.hset(domain, RedisEntityManagerImpl.procmodel_crdate, createDate.getTime()+"");
	}

	public void setModifyDate(Date modifyDate) {
		super.setModifyDate(modifyDate);
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		shardedJedis.hset(domain, RedisEntityManagerImpl.procmodel_mddate, modifyDate.getTime()+"");
	}

	public void setXmlcontent(String xmlcontent) {
		super.setXmlcontent(xmlcontent);
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		shardedJedis.hset(domain, RedisEntityManagerImpl.procmodel_xmlcontent, xmlcontent);
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		super.setProcessDefinitionId(processDefinitionId);
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		shardedJedis.hset(domain, RedisEntityManagerImpl.procmodel_proddefid, processDefinitionId);
	}
	
}