package cn.cafebabe.autodao.util;

import cn.cafebabe.autodao.exception.AutodaoException;

public class Assert
{
	public static void notNull(Object object)
	{
		if(object == null)
		{
			throw new NullPointerException();
		}
	}
	
	public static void isTrue(boolean expression)
	{
		if(!expression)
		{
			throw new AutodaoException();
		}
	}
	
	public static void isTrue(boolean expression,String msg)
	{
		if(!expression)
		{
			throw new AutodaoException(msg);
		}
	}
}
