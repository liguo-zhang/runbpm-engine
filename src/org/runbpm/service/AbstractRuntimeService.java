package org.runbpm.service;

import org.runbpm.persistence.EntityManager;

public abstract class AbstractRuntimeService implements RuntimeService{
	
	protected EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
