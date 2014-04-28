package cn.cafebabe.autodao.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.hibernate.SessionFactory;

import cn.cafebabe.autodao.dao.BaseDao;
import cn.cafebabe.autodao.hibernate.HibernateDao;
import cn.cafebabe.autodao.hibernate.ThreadLocalHibernateDao;

@Deprecated
public class DaoFactory
{
	public static BaseDao getHibernateAutoTransactionDao(
			SessionFactory sessionFactory)
	{
		HibernateDao dao = new HibernateDao();
		dao.setSessionFactory(sessionFactory);
		return weaveAutoTransactionDao(dao);
	}
	
	public static BaseDao weaveAutoTransactionDao(BaseDao dao)
	{
		try
		{
			return new AutoTransactionDaoJavaProxy().getDao(dao);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static class AutoTransactionDaoJavaProxy implements
			InvocationHandler
	{
		private BaseDao dao = null;
		
		public BaseDao getDao(BaseDao dao)
		{
			this.dao = dao;
			return (BaseDao) Proxy.newProxyInstance(dao.getClass()
					.getClassLoader(), dao.getClass().getInterfaces(), this);
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable
		{
			Object result = null;
			dao.beginTransation();
			try
			{
				result = method.invoke(dao, args);
				dao.commitTransation();
			}
			catch(Exception e)
			{
				dao.rollbackTransation();
				result = null;
			}
			finally
			{
				if(dao instanceof ThreadLocalHibernateDao)
				{
					((HibernateDao) dao).getSession().close();
				}
			}
			return result;
		}
	}
}
