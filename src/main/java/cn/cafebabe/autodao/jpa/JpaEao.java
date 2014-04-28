package cn.cafebabe.autodao.jpa;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import cn.cafebabe.autodao.autoQuery.AutoQuery;
import cn.cafebabe.autodao.exception.AutodaoException;
import cn.cafebabe.autodao.pojo.Page;
import cn.cafebabe.autodao.pojo.StatementAndValues;
import cn.cafebabe.autodao.util.Assert;

@Deprecated
@Stateless(mappedName = "jpaEao")
public class JpaEao implements BaseEao
{
	@PersistenceUnit(unitName = "persistentUnit")
	@Resource
	private EntityManagerFactory entityManagerFactory = null;
	private EntityTransaction transaction = null;
	
	@Override
	public <T> T persist(T object)
	{
		getEntityManager().persist(object);
		return object;
	}
	
	@Override
	public <T> T merge(T object)
	{
		return getEntityManager().merge(object);
	}
	
	@Override
	public void delete(Object object)
	{
		getEntityManager().remove(object);
	}
	
	@Override
	public <T> T refresh(T object)
	{
		getEntityManager().refresh(object);
		return object;
	}
	
	@Override
	public void flush()
	{
		getEntityManager().flush();
	}
	
	@Override
	public void clear()
	{
		getEntityManager().clear();
	}
	
	@Override
	public <T> T findById(Class<T> objectClass, Serializable id)
	{
		return getEntityManager().find(objectClass, id);
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
	@SuppressWarnings("unchecked")
	public <T> List<T> findByExample(T object)
	{
		return getQueryByExample(AutoQuery.getSearchStatement(object))
				.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findByExample(final T object, final Page pageInfo)
	{
		return paging(getQueryByExample(AutoQuery.getSearchStatement(object)),
				pageInfo).getResultList();
	}
	
	private Query getQueryByExample(final StatementAndValues statementAndValues)
	{
		Query query = getEntityManager().createQuery(
				statementAndValues.getStatement());
		int size = statementAndValues.getValues().size();
		for(int index = 0; index != size; ++ index)
		{
			query.setParameter(index + 1, statementAndValues.getValues().get(0));
		}
		return query;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findByProperty(String propertyName, Object value,
			Class<T> objectClass)
	{
		return getQuery(
				"from " + objectClass.getSimpleName()
						+ " as model where model." + propertyName + "= ?")
				.setParameter(0, value).getResultList();
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
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> objectClass)
	{
		return getFindAllQuery(objectClass).getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final Class<T> objectClass, final Page pageInfo)
	{
		return paging(getFindAllQuery(objectClass), pageInfo).getResultList();
	}
	
	private Query getFindAllQuery(final Class<?> objectClass)
	{
		return getQuery("from " + objectClass.getSimpleName());
	}
	
	@Override
	public List<?> executeHql(final String hql, final Object... params)
	{
		return getQueryByHql(hql, params).getResultList();
	}
	
	@Override
	public List<?> executeHql(final Page pageInfo, final String hql,
			final Object... params)
	{
		return paging(getQueryByHql(hql, params), pageInfo).getResultList();
	}
	
	@Override
	public Integer executeUpdateHql(final String hql, final Object... params)
	{
		return getQueryByHql(hql, params).executeUpdate();
	}
	
	@Override
	public Page getPageByHql(int eachPageNumber, final String hql,
			final Object... params)
	{
		return getPageByHql(Page.getDefaultPageSize(), hql, params);
	}
	
	@Override
	public Page getPageByHql(final String hql, final Object... params)
	{
		Assert.isTrue(hql.indexOf("count(") != -1, "只能对用count函数的hql进行查询!");
		return new Page((Long) getQueryByHql(hql, params).getResultList()
				.get(0));
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
		return getEntityManager().createQuery(hql);
	}
	
	private Query paging(Query query, Page pageInfo)
	{
		return query.setFirstResult(pageInfo.getFirstIndex()).setMaxResults(
				pageInfo.getPageSize());
	}
	
	private long getCountByPageQuery(Query query)
	{
		return (Integer) query.getResultList().get(0);
	}
	
	private EntityManager getEntityManager()
	{
		return entityManagerFactory.createEntityManager();
	}
	
	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory)
	{
		this.entityManagerFactory = entityManagerFactory;
	}
	
	@Override
	public void beginTransation()
	{
		transaction = getEntityManager().getTransaction();
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
