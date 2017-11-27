package org.runbpm.service;

import org.runbpm.persistence.EntityManager;

public abstract class AbstractRunBPMService implements RunBPMService{
	
	protected EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
