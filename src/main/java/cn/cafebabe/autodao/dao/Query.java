package cn.cafebabe.autodao.dao;

import java.util.Iterator;
import java.util.List;

public interface Query
{
	public abstract Iterator<?> iterate() throws RuntimeException;
	
	public abstract List<?> list() throws RuntimeException;
	
	public abstract int executeUpdate() throws RuntimeException;
	
	public abstract Query setParameter(int position, Object val)
			throws RuntimeException;
	
	public abstract Query setParameter(String name, Object val)
			throws RuntimeException;
	
	public abstract Query setFirstResult(int firstResult);
	public abstract Query setMaxResults(int maxResults);
}
