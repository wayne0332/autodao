package cn.cafebabe.autodao.jpa;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import cn.cafebabe.autodao.dao.Query;

public class JpaQuery implements Query
{
	private javax.persistence.Query query = null;
	
	public JpaQuery(javax.persistence.Query query)
	{
		super();
		this.query = query;
	}
	
	@Override
	public Iterator<?> iterate() throws HibernateException
	{
		return list().iterator();
	}
	
	@Override
	public List<?> list() throws HibernateException
	{
		return query.getResultList();
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
