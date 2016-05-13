package org.runbpm.persistence.redis;

import org.runbpm.entity.TaskHistory;

public class RedisTaskHistory extends RedisTask_ implements TaskHistory {

	public void setId(long id){
		super.setId(id);
		
		this.domain=RedisEntityManagerImpl.getDomainTaskHist(id+"");
	}	
}
