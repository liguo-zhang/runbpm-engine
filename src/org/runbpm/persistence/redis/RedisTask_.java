package org.runbpm.persistence.redis;

import java.util.Date;

import org.runbpm.entity.EntityConstants.TASK_STATE;
import org.runbpm.entity.EntityConstants.TASK_TYPE;
import org.runbpm.entity.Task_;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.RunBPMUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisTask_ extends Task_ {

	protected String domain;
	
	protected boolean flushRedis = true;
	
	public boolean isFlushRedis() {
		return flushRedis;
	}

	public void setFlushRedis(boolean flushRedis) {
		this.flushRedis = flushRedis;
	}


	public void setName(String name) {
		super.setName(name);
		if(flushRedis){
			if(RunBPMUtils.notNull(name)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_name, name);
			}
		}
	}
	
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_crdate, createDate.getTime()+"");
		}
	}

	public void setModifyDate(Date modifyDate) {
		super.setModifyDate(modifyDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_mddate, modifyDate.getTime()+"");
		}
	}

	
	public void setActivityDefinitionId(String activityDefinitionId) {
		super.setActivityDefinitionId(activityDefinitionId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_actcdefid, activityDefinitionId);
		}
	}


	public void setProcessDefinitionId(String processDefinitionId) {
		super.setProcessDefinitionId(processDefinitionId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_procdefid, processDefinitionId);
		}
	}

	public void setProcessModelId(long processModelId) {
		super.setProcessModelId(processModelId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_modelid, processModelId+"");
		}
	}


	public void setDescription(String description) {
		super.setDescription(description);
		if(flushRedis){
			if(RunBPMUtils.notNull(description)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_desc, description);
			}
		}
	}

	public void setState(TASK_STATE state) {
		super.setState(state);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_state, state.toString());
		}
	}
	
	public void setUserId(String userId) {
		super.setUserId(userId);
		if(flushRedis){
			if(RunBPMUtils.notNull(userId)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_userid, userId);
			}
		}
	}
	

	public void setActivityInstanceId(long activityInstanceId) {
		super.setActivityInstanceId(activityInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_actinstid, activityInstanceId+"");
		}
	}

	public void setProcessInstanceId(long processInstanceId) {
		super.setProcessInstanceId(processInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_procinstid, processInstanceId+"");
		}
	}
	
	public void setCompleteDate(Date completeDate) {
		super.setCompleteDate(completeDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.task_cldate, completeDate.getTime()+"");
		}
	}


	public void setKeyA(String keyA) {
		super.setKeyA(keyA);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keya, keyA.toString());
			}
		}
	}


	public void setKeyB(String keyB) {
		super.setKeyB(keyB);
		if(flushRedis){
			if(keyB!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keyb, keyB.toString());
			}
		}
	}

	public void setKeyC(String keyC) {
		super.setKeyC(keyC);
		if(flushRedis){
			if(keyC!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keyc, keyC.toString());
			}
		}
	}	

	public void setKeyD(String keyD) {
		super.setKeyD(keyD);
		if(flushRedis){
			if(keyD!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keyd, keyD.toString());
			}
		}
	}

	public void setKeyE(String keyE) {
		super.setKeyE(keyE);
		if(flushRedis){
			if(keyE!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keye, keyE.toString());
			}
		}
	}

	public void setKeyF(String keyF) {
		super.setKeyF(keyF);
		if(flushRedis){
			if(keyF!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keyf, keyF.toString());
			}
		}
	}

	public void setKeyG(String keyG) {
		super.setKeyG(keyG);
		if(flushRedis){
			if(keyG!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keyg, keyG.toString());
			}
		}
	}

	public void setKeyH(String keyH) {
		super.setKeyH(keyH);
		if(flushRedis){
			if(keyH!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_keyh, keyH.toString());
			}
		}
	}	
	
	public void setStateBeforeSuspend(TASK_STATE stateBeforeSuspend) {
		super.setStateBeforeSuspend(stateBeforeSuspend);
		if(flushRedis){
			if(stateBeforeSuspend!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_stbfsp, stateBeforeSuspend.toString());
			}
		}
	}
	
	public void setType(TASK_TYPE type) {
		this.type = type;
		if(flushRedis){
			if(type!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.task_type, type.toString());
			}
		}
	}

	
}
