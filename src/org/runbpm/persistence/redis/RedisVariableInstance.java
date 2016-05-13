package org.runbpm.persistence.redis;

import org.runbpm.entity.VariableInstance;

public class RedisVariableInstance extends RedisVariable_ implements VariableInstance {

	public void setId(long id){
		super.setId(id);
		this.domain=RedisEntityManagerImpl.getDomainValInst(id+"");
	}	
}
