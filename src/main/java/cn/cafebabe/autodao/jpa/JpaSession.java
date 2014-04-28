package cn.cafebabe.autodao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import cn.cafebabe.autodao.dao.Query;
import cn.cafebabe.autodao.dao.Session;
import cn.cafebabe.autodao.exception.AutodaoException;

public class JpaSession implements Session
{
	private EntityManagerFactory factory = null;
	private EntityTransaction transaction = null;
	
	@Override
	public void setFactory(Object factory)
	{
		if(factory instanceof EntityManagerFactory)
		{
			this.factory = (EntityManagerFactory) factory;
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
		return new JpaQuery(getSession().createQuery(hql));
	}
	
	@Override
	public void delete(Object entity) throws RuntimeException
	{
		getSession().remove(entity);
	}
	
	@Override
	public void flush() throws RuntimeException
	{
		getSession().flush();
	}
	
	@Override
	public <T> T get(Class<T> clazz, Serializable identity)
			throws RuntimeException
	{
		return (T) getSession().find(clazz, identity);
	}
	
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
	
	private EntityManager getSession()
	{
		return factory.createEntityManager();
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
