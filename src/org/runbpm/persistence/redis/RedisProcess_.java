package org.runbpm.persistence.redis;

import java.util.Date;

import org.runbpm.entity.EntityConstants.PROCESS_STATE;
import org.runbpm.entity.Process_;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.RunBPMUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisProcess_ extends Process_ {
	
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
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_name, name);
			}
		}
	}
	
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
		shardedJedis.hset(domain, RedisEntityManagerImpl.proc_crdate, createDate.getTime()+"");
	}

	public void setModifyDate(Date modifyDate) {
		super.setModifyDate(modifyDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.proc_mddate, modifyDate.getTime()+"");
		}
	}
	
	public void setState(PROCESS_STATE state) {
		super.setState(state);
		if(flushRedis){
			
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.proc_state, state.toString());
			
		}
	}
	

	public void setDescription(String description) {
		super.setDescription(description);
		if(flushRedis){
			if(RunBPMUtils.notNull(description)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_state, description);
			}
		}
	}
	

	public void setProcessDefinitionId(String processDefinitionId) {
		super.setProcessDefinitionId(processDefinitionId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.proc_procdefid, processDefinitionId);
		}
	}
	

	public void setProcessModelId(long processModelId) {
		super.setProcessModelId(processModelId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.proc_modelid, processModelId+"");
		}
	}
	

	public void setParentActivityInstanceId(long parentActivityInstanceId) {
		super.setParentActivityInstanceId(parentActivityInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.proc_pareactinstid, parentActivityInstanceId+"");
		}
	}
	
	public void setCreator(String creator) {
		super.setCreator(creator);
		if(flushRedis){
			if(RunBPMUtils.notNull(creator)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_creator, creator);
			}
		}
	}
	

	public void setCompleteDate(Date completeDate) {
		super.setCompleteDate(completeDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.proc_cldate, completeDate.getTime()+"");
		}
	}
	

	public void setStateBeforeSuspend(PROCESS_STATE stateBeforeSuspend) {
		super.setStateBeforeSuspend(stateBeforeSuspend);
		if(flushRedis){
			if(stateBeforeSuspend!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_stbfsp, stateBeforeSuspend.toString());
			}
		}
	}
	

	public void setKeyA(String keyA) {
		super.setKeyA(keyA);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keya, keyA.toString());
			}
		}
	}


	public void setKeyB(String keyB) {
		super.setKeyB(keyB);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keyb, keyB.toString());
			}
		}
	}

	public void setKeyC(String keyC) {
		super.setKeyC(keyC);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keyc, keyC.toString());
			}
		}
	}	

	public void setKeyD(String keyD) {
		super.setKeyD(keyD);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keyd, keyD.toString());
			}
		}
	}

	public void setKeyE(String keyE) {
		super.setKeyE(keyE);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keye, keyE.toString());
			}
		}
	}

	public void setKeyF(String keyF) {
		super.setKeyF(keyF);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keyf, keyF.toString());
			}
		}
	}

	public void setKeyG(String keyG) {
		super.setKeyG(keyG);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keyg, keyG.toString());
			}
		}
	}

	public void setKeyH(String keyH) {
		super.setKeyH(keyH);
		if(flushRedis){
			if(keyH!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.proc_keyh, keyH.toString());
			}
		}
	}	
	
}
	