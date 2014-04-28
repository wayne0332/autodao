package cn.cafebabe.autodao.factory;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public final class Hibernate3SessionFactory
{
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static Configuration configuration = new Configuration();
	private static org.hibernate.SessionFactory sessionFactory;
	static String[] configFilesPath = {"/hibernate.cfg.xml",
			"/config/hibernate.cfg.xml"};
	final static String USING_CONFIG_PATH;
	
	protected Hibernate3SessionFactory()
	{}
	
	static
	{
		USING_CONFIG_PATH = findConfigPath();
		if(USING_CONFIG_PATH != null)
		{
			buildSessionFactory();
		}
		else
		{
			throw new RuntimeException("找不到hibernate配置文件!");
		}
	}
	
	static String findConfigPath()
	{
		for(String configFilePath : configFilesPath)
		{
			if(new File(configFilePath).exists())
			{
				return configFilePath;
			}
		}
		return null;
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
			configuration.configure(USING_CONFIG_PATH);
			sessionFactory = configuration.buildSessionFactory();
		}
		catch(Exception e)
		{
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
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
