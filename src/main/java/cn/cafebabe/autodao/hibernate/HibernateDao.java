package cn.cafebabe.autodao.hibernate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cn.cafebabe.autodao.autoQuery.AutoQuery;
import cn.cafebabe.autodao.dao.BaseDao;
import cn.cafebabe.autodao.exception.AutodaoException;
import cn.cafebabe.autodao.pojo.Page;
import cn.cafebabe.autodao.pojo.StatementAndValues;
import cn.cafebabe.autodao.util.Assert;

@Deprecated
@SuppressWarnings("unchecked")
public class HibernateDao implements BaseDao
{
	@Resource
	protected SessionFactory sessionFactory = null;
	private Transaction transaction = null;
	
	public <T> T save(T object)
	{
		getSession().save(object);
		return object;
	}
	
	@Override
	public <T> T persist(T object)
	{
		getSession().persist(object);
		return object;
	}
	
	@Override
	public <T> T refresh(T object)
	{
		getSession().refresh(object);
		return object;
	}
	
	@Override
	public void flush()
	{
		getSession().flush();
	}
	
	@Override
	public void clear()
	{
		getSession().clear();
	}
	
	public void evict(Object object)
	{
		getSession().evict(object);
	}
	
	public <T> T update(T object)
	{
		getSession().update(object);
		return object;
	}
	
	public <T> T saveOrUpdate(T object)
	{
		getSession().saveOrUpdate(object);
		return object;
	}
	
	@Override
	public <T> T merge(T object)
	{
		return (T) getSession().merge(object);
	}
	
	@Override
	public void delete(Object object)
	{
		getSession().delete(object);
	}
	
	@Override
	public <T> T findById(Class<T> objectClass, Serializable id)
	{
		return (T) getSession().get(objectClass, id);
	}
	
	@Override
	public Page getPageByExample(Object object)
	{
		return new Page(
				getCountByPageQuery(getQueryByExample(AutoQuery
						.getPagingStatement(object))));
	}
	
	@Override
	public Page getPageByExample(Object object, int eachPageNumber)
	{
		return new Page(eachPageNumber,
				getCountByPageQuery(getQueryByExample(AutoQuery
						.getPagingStatement(object))));
	}
	
	@Override
	public <T> List<T> findByExample(T object)
	{
		return getQueryByExample(AutoQuery.getSearchStatement(object)).list();
	}
	
	@Override
	public <T> List<T> findByExample(final T object, final Page pageInfo)
	{
		return paging(getQueryByExample(AutoQuery.getSearchStatement(object)),
				pageInfo).list();
	}
	
	private Query getQueryByExample(final StatementAndValues statementAndValues)
	{
		Query query = getSession().createQuery(
				statementAndValues.getStatement());
		int size = statementAndValues.getValues().size();
		for(int index = 0; index != size; ++ index)
		{
			query.setParameter(index, statementAndValues.getValues().get(index));
		}
		return query;
	}
	
	@Override
	public <T> List<T> findByProperty(String propertyName, Object value,
			Class<T> objectClass)
	{
		return getQuery(
				"from " + objectClass.getSimpleName()
						+ " as model where model." + propertyName + "= ?")
				.setParameter(0, value).list();
	}
	
	@Override
	public Page findAllPage(Class<?> objectClass)
	{
		return new Page(getCountByPageQuery(getFindAllPageQuery(objectClass)));
	}
	
	@Override
	public Page findAllPage(Class<?> objectClass, int eachPageNumber)
	{
		return new Page(eachPageNumber,
				getCountByPageQuery(getFindAllPageQuery(objectClass)));
	}
	
	private Query getFindAllPageQuery(Class<?> objectClass)
	{
		return getQuery("select count(*) from " + objectClass.getSimpleName());
	}
	
	@Override
	public <T> List<T> findAll(Class<T> objectClass)
	{
		return getFindAllQuery(objectClass).list();
	}
	
	@Override
	public <T> List<T> findAll(final Class<T> objectClass, final Page pageInfo)
	{
		return paging(getFindAllQuery(objectClass), pageInfo).list();
	}
	
	private Query getFindAllQuery(final Class<?> objectClass)
	{
		return getQuery("from " + objectClass.getSimpleName());
	}
	
	@Override
	public List<?> executeHql(final String hql, final Object... params)
	{
		return getQueryByHql(hql, params).list();
	}
	
	@Override
	public List<?> executeHql(final Page pageInfo, final String hql,
			final Object... params)
	{
		return paging(getQueryByHql(hql, params), pageInfo).list();
	}
	
	@Override
	public Page getPageByHql(int eachPageNumber, final String hql,
			final Object... params)
	{
		Assert.isTrue(hql.indexOf("count(") != -1, "只能对用count函数的hql进行查询!");
		return new Page(eachPageNumber, (Long) getQueryByHql(hql, params)
				.list().get(0));
	}
	
	@Override
	public Page getPageByHql(final String hql, final Object... params)
	{
		return getPageByHql(Page.getDefaultPageSize(), hql, params);
	}
	
	@Override
	public Integer executeUpdateHql(final String hql, final Object... params)
	{
		return getQueryByHql(hql, params).executeUpdate();
	}
	
	@Override
	public Integer update(final Object object, final String... conditions)
	{
		StatementAndValues statement = AutoQuery.getUpdateStatement(object,
				new HashSet<String>(Arrays.asList(conditions)));
		if(statement != null)
		{
			return getQueryByHql(statement.getStatement(),
					statement.getValues().toArray()).executeUpdate();
		}
		else
		{
			return -1;
		}
	}
	
	private Query getQueryByHql(final String hql, final Object... params)
	{
		Query query = getQuery(hql);
		for(int i = 0; i != params.length; ++ i)
		{
			query.setParameter(i, params[i]);
		}
		return query;
	}
	
	private Query getQuery(final String hql)
	{
		return getSession().createQuery(hql);
	}
	
	private Query paging(Query query, Page pageInfo)
	{
		return query.setFirstResult(pageInfo.getFirstIndex()).setMaxResults(
				pageInfo.getPageSize());
	}
	
	private long getCountByPageQuery(Query query)
	{
		return (Long) query.list().get(0);
	}
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void beginTransation()
	{
		transaction = getSession().beginTransaction();
	}
	
	@Override
	public void commitTransation()
	{
		checkTransaction();
		transaction.commit();
	}
	
	@Override
	public void rollbackTransation()
	{
		checkTransaction();
		transaction.rollback();
	}
	
	private void checkTransaction()
	{
		if(transaction == null || !transaction.isActive())
		{
			throw new AutodaoException("事务不存在或已关闭!");
		}
	}
	
	@Override
	public Page getPageByExample(Object object, String... orders)
	{
		return null;
	}
	
	@Override
	public Page getPageByExample(Object object, int eachPageNumber,
			String... orders)
	{
		return null;
	}
	
	@Override
	public <T> List<T> findByExample(T object, String... orders)
	{
		return null;
	}
	
	@Override
	public <T> List<T> findByExample(T object, Page pageInfo, String... orders)
	{
		return null;
	}
	
	@Override
	public Page findAllPage(Class<?> objectClass, String... orders)
	{
		return null;
	}
	
	@Override
	public Page findAllPage(Class<?> objectClass, int eachPageNumber,
			String... orders)
	{
		return null;
	}
	
	@Override
	public <T> List<T> findAll(Class<T> objectClass, String... orders)
	{
		return null;
	}
	
	@Override
	public <T> List<T> findAll(Class<T> objectClass, Page pageInfo,
			String... orders)
	{
		return null;
	}
}
