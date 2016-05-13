package org.runbpm.persistence.redis;

import org.runbpm.entity.ActivityHistory;

public class RedisActivityHistory extends RedisActivity_ implements  ActivityHistory{
	
	public void setId(long id){
		super.setId(id);
		this.domain=RedisEntityManagerImpl.getDomainActHist(id+"");
	}	
	
}
