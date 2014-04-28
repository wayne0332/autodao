package cn.cafebabe.autodao.dao;

import java.io.Serializable;

public interface Session
{
	public abstract void setFactory(Object factory);
	
	public abstract void beginTransaction() throws RuntimeException;
	
	public abstract void commitTransation();
	
	public abstract void rollbackTransation();
	
	public abstract boolean isUsingTransation();
	
	public abstract void close() throws RuntimeException;
	
	public abstract Query createQuery(String hql) throws RuntimeException;
	
	public abstract void delete(Object entity) throws RuntimeException;
	
	public abstract void flush() throws RuntimeException;
	
	public abstract <T> T get(Class<T> clazz, Serializable identity)
			throws RuntimeException;
	
	public abstract <T> T merge(T entity) throws RuntimeException;
	
	public abstract void persist(Object entity) throws RuntimeException;
	
	public abstract void refresh(Object entity) throws RuntimeException;
	
	public abstract void clear() throws RuntimeException;
}
