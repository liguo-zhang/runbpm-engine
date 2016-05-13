package org.runbpm.persistence.redis;

import org.runbpm.entity.ProcessHistory;

public class RedisProcessHistory extends RedisProcess_ implements ProcessHistory{
	
	public void setId(long id){
		super.setId(id);
		
		this.domain=RedisEntityManagerImpl.getDomainProcHist(id+"");
	}	
	
}
