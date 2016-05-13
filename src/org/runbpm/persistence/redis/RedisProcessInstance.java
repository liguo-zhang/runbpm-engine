package org.runbpm.persistence.redis;

import org.runbpm.entity.ProcessInstance;

public class RedisProcessInstance extends RedisProcess_ implements ProcessInstance{
	
	public void setId(long id){
		super.setId(id);
		
		this.domain=RedisEntityManagerImpl.getDomainProcInst(id+"");
	}
	
}
