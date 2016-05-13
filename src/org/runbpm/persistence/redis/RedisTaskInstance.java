package org.runbpm.persistence.redis;

import org.runbpm.entity.TaskInstance;

public class RedisTaskInstance extends RedisTask_ implements TaskInstance {

	public void setId(long id){
		super.setId(id);

		this.domain=RedisEntityManagerImpl.getDomainTaskInst(id+"");
	}
	
}
