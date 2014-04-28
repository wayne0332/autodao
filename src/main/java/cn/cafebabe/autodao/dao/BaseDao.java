package cn.cafebabe.autodao.dao;

import java.io.Serializable;
import java.util.List;

import cn.cafebabe.autodao.pojo.Page;

/** 以后加个专门用户count()函数的execute方法,得弄个排序的方法 */
public interface BaseDao
{
	public abstract <T> T persist(T object);
	
	public abstract <T> T merge(T object);
	
	public abstract void delete(Object object);
	
	public abstract <T> T refresh(T object);
	
	public abstract void flush();
	
	public abstract void clear();
	
	public abstract <T> T findById(Class<T> objectClass, Serializable id);
	
	public abstract Page getPageByExample(Object object);
	
	public abstract Page getPageByExample(Object object, String... orders);
	
	public abstract Page getPageByExample(Object object, int eachPageNumber);
	
	public abstract Page getPageByExample(Object object, int eachPageNumber,
			String... orders);
	
	public abstract <T> List<T> findByExample(T object);
	
	public abstract <T> List<T> findByExample(T object, String... orders);
	
	public abstract <T> List<T> findByExample(T object, Page pageInfo);
	
	public abstract <T> List<T> findByExample(T object, Page pageInfo,
			String... orders);
	
	@Deprecated
	public abstract <T> List<T> findByProperty(String propertyName,
			Object value, Class<T> objectClass);
	
	public abstract Page findAllPage(Class<?> objectClass);
	
	public abstract Page findAllPage(Class<?> objectClass, String... orders);
	
	public abstract Page findAllPage(Class<?> objectClass, int eachPageNumber);
	
	public abstract Page findAllPage(Class<?> objectClass, int eachPageNumber,
			String... orders);
	
	public abstract <T> List<T> findAll(Class<T> objectClass);
	
	public abstract <T> List<T> findAll(Class<T> objectClass,
			String... orders);
	
	public abstract <T> List<T> findAll(Class<T> objectClass, Page pageInfo);
	
	public abstract <T> List<T> findAll(Class<T> objectClass, Page pageInfo,
			String... orders);
	
	public abstract List<?> executeHql(String hql, Object... params);
	
	public abstract List<?> executeHql(Page pageInfo, String hql,
			Object... params);
	
	public abstract Page getPageByHql(String hql, Object... params);
	
	public abstract Page getPageByHql(int eachPageNumber, String hql,
			Object... params);
	
	public abstract Integer executeUpdateHql(String hql, Object... params);
	
	/**
	 * 根据对象属性构造更新HQL,null和collection属性自动忽略,第二个参数是作为判断条件的属性名字的集合,判断条件和更新的属性都至少有一项
	 * 返回受影响行数,-1为没有执行更新 (原因见链接)
	 * 
	 * @see cn.cafebabe.autodao.autoQuery.AutoQuery#getUpdateStatement(Object,
	 *      java.util.Collection)
	 */
	public abstract Integer update(Object object, String... conditions);
	
	public void beginTransation();
	
	public void commitTransation();
	
	public void rollbackTransation();
}
