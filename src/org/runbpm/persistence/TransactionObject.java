package org.runbpm.persistence;

import java.sql.Connection;

import org.hibernate.Session;

import redis.clients.jedis.ShardedJedis;

public class TransactionObject {

	private Connection connection;
	
	private ShardedJedis shardedJedis;
	
	private Session session;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public ShardedJedis getShardedJedis() {
		return shardedJedis;
	}

	public void setShardedJedis(ShardedJedis shardedJedis) {
		this.shardedJedis = shardedJedis;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	
	
}
