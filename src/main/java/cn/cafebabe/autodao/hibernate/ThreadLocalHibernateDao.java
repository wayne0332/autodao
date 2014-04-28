package cn.cafebabe.autodao.hibernate;

import org.hibernate.Session;

import cn.cafebabe.autodao.dao.BaseDao;

@Deprecated
public class ThreadLocalHibernateDao extends HibernateDao implements BaseDao
{
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	
	@Override
	public Session getSession()
	{
		Session session = threadLocal.get();
		if(session == null || !session.isOpen())
		{
			session = super.sessionFactory.openSession();
			threadLocal.set(session);
		}
		return session;
	}
}
