package org.runbpm.persistence.redis;

import org.runbpm.entity.ProcessModel;

public class RedisProcessModel extends RedisProcessModel_ implements ProcessModel {
	
	public void setId(long id){
		super.setId(id);
		
		this.domain=RedisEntityManagerImpl.getDomainProcModel(id+"");
	}		
}