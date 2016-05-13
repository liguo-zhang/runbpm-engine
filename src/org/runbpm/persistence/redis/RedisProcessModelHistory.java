package org.runbpm.persistence.redis;

import org.runbpm.entity.ProcessModelHistory;

public class RedisProcessModelHistory extends RedisProcessModel_ implements ProcessModelHistory {
	
	public void setId(long id){
		super.setId(id);
		
		this.domain=RedisEntityManagerImpl.getDomainProcModelHist(id+"");
	}
	
}