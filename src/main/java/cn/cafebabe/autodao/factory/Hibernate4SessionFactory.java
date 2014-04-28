package cn.cafebabe.autodao.factory;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public final class Hibernate4SessionFactory
{
	// private static String configFile = "/config/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static Configuration configuration = null;
	private static ServiceRegistry serviceRegistry = null;
	private static org.hibernate.SessionFactory sessionFactory = null;
	
	private Hibernate4SessionFactory()
	{}
	
	static
	{
		if(new File(Hibernate3SessionFactory.USING_CONFIG_PATH).exists())
		{
			buildSessionFactory();
		}
	}
	
	public static Session getSession() throws HibernateException
	{
		Session session = threadLocal.get();
		if(session == null || !session.isOpen())
		{
			if(sessionFactory == null)
			{
				buildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
		}
		return session;
	}
	
	public static void buildSessionFactory()
	{
		try
		{
			configuration = new Configuration()
					.configure(Hibernate3SessionFactory.USING_CONFIG_PATH);
			//TODO 这里貌似有问题,不知道能不能用,而且配置位置没弄好
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
					configuration.getProperties()).build();
			sessionFactory = configuration.configure().buildSessionFactory(
					serviceRegistry);
		}
		catch(Exception e)
		{
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
		throw new RuntimeException("配置还没写好");
	}
	
	public static void closeSession() throws HibernateException
	{
		Session session = threadLocal.get();
		threadLocal.set(null);
		if(session != null)
		{
			session.close();
		}
	}
	
	public static org.hibernate.SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
	
	public static Configuration getConfiguration()
	{
		return configuration;
	}
}
