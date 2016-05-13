package org.runbpm.persistence.redis;

import java.util.Date;

import org.runbpm.entity.Activity_;
import org.runbpm.entity.EntityConstants.ACTIVITY_STATE;
import org.runbpm.entity.EntityConstants.ACTIVITY_TYPE;
import org.runbpm.persistence.TransactionObjectHolder;
import org.runbpm.utils.RunBPMUtils;

import redis.clients.jedis.ShardedJedis;

public class RedisActivity_ extends Activity_ {
	
	protected boolean flushRedis = true;
	
	public boolean isFlushRedis() {
		return flushRedis;
	}

	public void setFlushRedis(boolean flushRedis) {
		this.flushRedis = flushRedis;
	}

	protected String domain;
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}


	public void setName(String name) {
		super.setName(name);
		if(flushRedis){
			if(RunBPMUtils.notNull(name)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_name, name);
			}
		}
	}
	
	public void setCreateDate(Date createDate) {
		super.setCreateDate(createDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_crdate, createDate.getTime()+"");
		}
	}

	public void setModifyDate(Date modifyDate) {
		super.setModifyDate(modifyDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_mddate, modifyDate.getTime()+"");
		}
	}

	public void setType(ACTIVITY_TYPE type) {
		super.setType(type);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_type, type.toString());
		}
	}

	
	public void setProcessDefinitionId(String processDefinitionId) {
		super.setProcessDefinitionId(processDefinitionId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_procdefid, processDefinitionId);
		}
	}

	
	public void setProcessModelId(long processModelId) {
		super.setProcessModelId(processModelId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_modelid, processModelId+"");
		}
	}


	public void setDescription(String description) {
		super.setDescription(description);
		if(flushRedis){
			if(RunBPMUtils.notNull(description)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_desc, description);
			}
		}
	}

	
	public void setState(ACTIVITY_STATE state) {
		super.setState(state);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_state, state.toString());
		}
	}

	
	public void setActivityDefinitionId(String activityDefinitionId) {
		super.setActivityDefinitionId(activityDefinitionId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_actcdefid, activityDefinitionId);
		}
	}

	
	public void setProcessInstanceId(long processInstanceId) {
		super.setProcessInstanceId(processInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_procinstid, processInstanceId+"");
		}
	}
	public void setParentActivityInstanceId(long parentActivityInstanceId) {
		super.setParentActivityInstanceId(parentActivityInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_pareactinstid, parentActivityInstanceId+"");
		}
	}


	public void setSubProcessBlockId(String subProcessBlockId) {
		super.setSubProcessBlockId(subProcessBlockId);
		if(flushRedis){
			if(RunBPMUtils.notNull(subProcessBlockId)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_subprocblockId,subProcessBlockId);
			}
		}
	}
	
	

	public void setSequenceBlockId(String sequenceBlockId) {
		super.setSequenceBlockId(sequenceBlockId);
		if(flushRedis){
			if(RunBPMUtils.notNull(sequenceBlockId)){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_sqblockId,sequenceBlockId);
			}
		}
	}
	
	public void setCompleteDate(Date completeDate) {
		super.setCompleteDate(completeDate);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_cldate,completeDate.getTime()+"");
		}
	}
	
	public void setStateBeforeSuspend(ACTIVITY_STATE stateBeforeSuspend) {
		super.setStateBeforeSuspend(stateBeforeSuspend);
		if(flushRedis){
			if(stateBeforeSuspend!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_stbfsp,stateBeforeSuspend.toString());
			}
		}
	}
	
	public void setCallActivityProcessInstanceId(
			long callActivityProcessInstanceId) {
		super.setCallActivityProcessInstanceId(callActivityProcessInstanceId);
		if(flushRedis){
			ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
			shardedJedis.hset(domain, RedisEntityManagerImpl.act_callprocid,callActivityProcessInstanceId+"");
		}
	}

	public void setKeyA(String keyA) {
		super.setKeyA(keyA);
		if(flushRedis){
			if(keyA!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keya, keyA.toString());
			}
		}
	}


	public void setKeyB(String keyB) {
		super.setKeyB(keyB);
		if(flushRedis){
			if(keyB!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keyb, keyB.toString());
			}
		}
	}

	public void setKeyC(String keyC) {
		super.setKeyC(keyC);
		if(flushRedis){
			if(keyC!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keyc, keyC.toString());
			}
		}
	}	

	public void setKeyD(String keyD) {
		super.setKeyD(keyD);
		if(flushRedis){
			if(keyD!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keyd, keyD.toString());
			}
		}
	}

	public void setKeyE(String keyE) {
		super.setKeyE(keyE);
		if(flushRedis){
			if(keyE!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keye, keyE.toString());
			}
		}
	}

	public void setKeyF(String keyF) {
		super.setKeyF(keyF);
		if(flushRedis){
			if(keyF!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keyf, keyF.toString());
			}
		}
	}

	public void setKeyG(String keyG) {
		super.setKeyG(keyG);
		if(flushRedis){
			if(keyG!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keyg, keyG.toString());
			}
		}
	}

	public void setKeyH(String keyH) {
		super.setKeyH(keyH);
		if(flushRedis){
			if(keyH!=null){
				ShardedJedis shardedJedis = TransactionObjectHolder.get().getShardedJedis();
				shardedJedis.hset(domain, RedisEntityManagerImpl.act_keyh, keyH.toString());
			}
		}
	}	
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ActivityInstance Id:[").append(this.getId()).append("] ");
		stringBuffer.append("ActivityInstance Name:[").append(this.getName()).append("] ");
		stringBuffer.append("activityDefinitionId Id:[").append(this.getActivityDefinitionId()).append("] ");
		return stringBuffer.toString();
	}
	
}