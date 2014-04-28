package cn.cafebabe.autodao.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cn.cafebabe.autodao.autoQuery.AutoQuery;
import cn.cafebabe.autodao.exception.AutodaoException;
import cn.cafebabe.autodao.pojo.Page;
import cn.cafebabe.autodao.pojo.StatementAndValues;
import cn.cafebabe.autodao.util.Assert;

@SuppressWarnings("unchecked")
public class CommonDao implements BaseDao
{
	private Session session = null;
	
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
		return (List<T>) getQueryByExample(AutoQuery.getSearchStatement(object))
				.list();
	}
	
	@Override
	public <T> List<T> findByExample(final T object, final Page pageInfo)
	{
		return (List<T>) paging(
				getQueryByExample(AutoQuery.getSearchStatement(object)),
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
		return (List<T>) getQuery(
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
		return (List<T>) getFindAllQuery(objectClass).list();
	}
	
	@Override
	public <T> List<T> findAll(final Class<T> objectClass, final Page pageInfo)
	{
		return (List<T>) paging(getFindAllQuery(objectClass), pageInfo).list();
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
		Assert.isTrue(hql.indexOf("count(") == -1
				&& hql.indexOf("select") != -1, "只能对用count函数的hql进行查询!");
		return new Page(eachPageNumber, (Long) getQueryByHql(
				checkHqlWidthCount(hql), params).list().get(0));
	}
	
	private String checkHqlWidthCount(String hql)
	{
		if(hql.indexOf("count(") == -1 && hql.indexOf("select") == -1)
		{
			hql = "select count(*) " + hql;
		}
		return hql;
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
	
	private Session getSession()
	{
		return session;
	}
	
	@Override
	public void beginTransation()
	{
		getSession().beginTransaction();
	}
	
	@Override
	public void commitTransation()
	{
		checkTransaction();
		getSession().commitTransation();
	}
	
	@Override
	public void rollbackTransation()
	{
		checkTransaction();
		getSession().rollbackTransation();
	}
	
	private void checkTransaction()
	{
		if(getSession().isUsingTransation())
		{
			throw new AutodaoException("事务不存在或已关闭!");
		}
	}
	
	public void setSession(Session session)
	{
		this.session = session;
	}
	
	@Override
	public Page getPageByExample(Object object, String... orders)
	{
		return new Page(
				getCountByPageQuery(getQueryByExample(AutoQuery
						.getPagingStatement(object, Arrays.asList(orders)))));
	}
	
	@Override
	public Page getPageByExample(Object object, int eachPageNumber,
			String... orders)
	{
		return new Page(eachPageNumber,
				getCountByPageQuery(getQueryByExample(AutoQuery
						.getPagingStatement(object, Arrays.asList(orders)))));
	}
	
	@Override
	public <T> List<T> findByExample(T object, String... orders)
	{
		return (List<T>) getQueryByExample(
				AutoQuery.getSearchStatement(object, Arrays.asList(orders)))
				.list();
	}
	
	@Override
	public <T> List<T> findByExample(T object, Page pageInfo, String... orders)
	{
		return (List<T>) paging(
				getQueryByExample(AutoQuery.getSearchStatement(object,
						Arrays.asList(orders))), pageInfo).list();
	}
	
	@Override
	public Page findAllPage(Class<?> objectClass, String... orders)
	{
		try
		{
			return new Page(
					getCountByPageQuery(getQueryByExample(AutoQuery
							.getPagingStatement(objectClass.newInstance(),
									Arrays.asList(orders)))));
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return new Page(getCountByPageQuery(getFindAllPageQuery(objectClass)));
	}
	
	@Override
	public Page findAllPage(Class<?> objectClass, int eachPageNumber,
			String... orders)
	{
		try
		{
			return new Page(eachPageNumber,
					getCountByPageQuery(getQueryByExample(AutoQuery
							.getPagingStatement(objectClass.newInstance(),
									Arrays.asList(orders)))));
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return new Page(eachPageNumber,
				getCountByPageQuery(getFindAllPageQuery(objectClass)));
	}
	
	@Override
	public <T> List<T> findAll(Class<T> objectClass, String... orders)
	{
		try
		{
			return (List<T>) getQueryByExample(
					AutoQuery.getSearchStatement(objectClass.newInstance(),
							Arrays.asList(orders))).list();
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return (List<T>) getFindAllQuery(objectClass).list();
	}
	
	@Override
	public <T> List<T> findAll(Class<T> objectClass, Page pageInfo,
			String... orders)
	{
		try
		{
			return (List<T>) paging(
					getQueryByExample(AutoQuery.getSearchStatement(
							objectClass.newInstance(), Arrays.asList(orders))),
					pageInfo).list();
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return (List<T>) paging(getFindAllQuery(objectClass), pageInfo).list();
	}
}
