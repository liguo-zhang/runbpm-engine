package org.runbpm.persistence.redis;

import org.runbpm.entity.ActivityInstance;

public class RedisActivityInstance extends RedisActivity_ implements  ActivityInstance{
	
	public void setId(long id){
		super.setId(id);
		this.domain=RedisEntityManagerImpl.getDomainActInst(id+"");
	}	
	
}
