package cn.cafebabe.autodao.hibernate;

import java.io.Serializable;

import org.hibernate.Transaction;

import cn.cafebabe.autodao.dao.Query;
import cn.cafebabe.autodao.dao.Session;
import cn.cafebabe.autodao.exception.AutodaoException;

public class HibernateSession implements Session
{
	private org.hibernate.SessionFactory factory = null;
	private Transaction transaction = null;
	
	@Override
	public void setFactory(Object factory)
	{
		if(factory instanceof org.hibernate.SessionFactory)
		{
			this.factory = (org.hibernate.SessionFactory) factory;
		}
		else
		{
			throw new AutodaoException(factory.getClass().getSimpleName()
					+ "不能转换为" + this.factory.getClass().getSimpleName());
		}
	}
	
	@Override
	public void beginTransaction() throws RuntimeException
	{
		transaction = getSession().getTransaction();
	}
	
	@Override
	public void close() throws RuntimeException
	{
		getSession().close();
	}
	
	@Override
	public Query createQuery(String hql) throws RuntimeException
	{
		return new HibernateQuery(getSession().createQuery(hql));
	}
	
	@Override
	public void delete(Object entity) throws RuntimeException
	{
		getSession().delete(entity);
	}
	
	@Override
	public void flush() throws RuntimeException
	{
		getSession().flush();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz, Serializable identity)
			throws RuntimeException
	{
		return (T) getSession().get(clazz, identity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T merge(T entity) throws RuntimeException
	{
		return (T) getSession().merge(entity);
	}
	
	@Override
	public void persist(Object entity) throws RuntimeException
	{
		getSession().persist(entity);
	}
	
	@Override
	public void refresh(Object entity) throws RuntimeException
	{
		getSession().refresh(entity);
	}
	
	@Override
	public void clear() throws RuntimeException
	{
		getSession().clear();
	}
	
	private org.hibernate.Session getSession()
	{
		return factory.getCurrentSession();
	}
	
	@Override
	public void commitTransation()
	{
		transaction.commit();
	}
	
	@Override
	public void rollbackTransation()
	{
		transaction.rollback();
	}
	
	@Override
	public boolean isUsingTransation()
	{
		return transaction != null && transaction.isActive();
	}
}
