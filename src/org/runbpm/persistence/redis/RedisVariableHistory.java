package org.runbpm.persistence.redis;

import org.runbpm.entity.VariableHistory;

public class RedisVariableHistory extends RedisVariable_ implements VariableHistory {

	public void setId(long id){
		super.setId(id);
		this.domain=RedisEntityManagerImpl.getDomainValHist(id+"");
	}	
}
