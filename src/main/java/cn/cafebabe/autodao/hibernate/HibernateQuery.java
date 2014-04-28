package cn.cafebabe.autodao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import cn.cafebabe.autodao.dao.Query;

public class HibernateQuery implements Query
{
	private org.hibernate.Query query = null;
	
	public HibernateQuery(org.hibernate.Query query)
	{
		super();
		this.query = query;
	}
	
	@Override
	public Iterator<?> iterate() throws HibernateException
	{
		return query.iterate();
	}
	
	@Override
	public List<?> list() throws HibernateException
	{
		return query.list();
	}
	
	@Override
	public int executeUpdate() throws HibernateException
	{
		return query.executeUpdate();
	}
	
	@Override
	public Query setParameter(int position, Object val)
			throws HibernateException
	{
		query.setParameter(position, val);
		return this;
	}
	
	@Override
	public Query setParameter(String name, Object val)
			throws HibernateException
	{
		query.setParameter(name, val);
		return this;
	}
	
	@Override
	public Query setFirstResult(int firstResult)
	{
		query.setFirstResult(firstResult);
		return this;
	}
	
	@Override
	public Query setMaxResults(int maxResults)
	{
		query.setMaxResults(maxResults);
		return this;
	}
}
