package cn.cafebabe.autodao.autoQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.cafebabe.autodao.pojo.StatementAndValues;
import cn.cafebabe.autodao.pojo.StatementAndValues.QueryBuidler;
import cn.cafebabe.autodao.util.Assert;

public final class AutoQuery
{
	public static StatementAndValues getSearchStatementWithNullObject(
			Object object)
	{
		return getSearchStatementWithNullObject(object, null);
	}
	
	public static StatementAndValues getSearchStatementWithNullObject(
			Object object, List<String> orders)
	{
		return StatementAndValues.createStatementAndValues(
				getSearchRoot(object),
				getCondiction(getPropertyWithNullObject(object)
						.getNamesAndValues()), getOrder(orders));
	}
	
	public static StatementAndValues getPagingStatementWithNullObject(
			Object object)
	{
		return getPagingStatementWithNullObject(object, null);
	}
	
	public static StatementAndValues getPagingStatementWithNullObject(
			Object object, List<String> orders)
	{
		return StatementAndValues.createStatementAndValues(
				getPagingRoot(object),
				getCondiction(getPropertyWithNullObject(object)
						.getNamesAndValues()), getOrder(orders));
	}
	
	public static StatementAndValues getSearchStatement(Object object)
	{
		return getSearchStatement(object, null);
	}
	
	public static StatementAndValues getSearchStatement(Object object,
			List<String> orders)
	{
		return StatementAndValues.createStatementAndValues(
				getSearchRoot(object),
				getCondiction(getPropertyWithoutNullObject(object)
						.getNamesAndValues()), getOrder(orders));
	}
	
	public static StatementAndValues getPagingStatement(Object object)
	{
		return getPagingStatement(object, null);
	}
	
	public static StatementAndValues getPagingStatement(Object object,
			List<String> orders)
	{
		return StatementAndValues.createStatementAndValues(
				getPagingRoot(object),
				getCondiction(getPropertyWithoutNullObject(object)
						.getNamesAndValues()), getOrder(orders));
	}
	
	/** 如果没有更新的属性,则返回null */
	public static StatementAndValues getUpdateStatement(Object object,
			Set<String> condictionParams)
	{
		ParametersAnalyse analyse = new ParametersAnalyse(
				getPropertyWithoutNullObject(object).getNamesAndValues(),
				condictionParams);
		if(analyse.getUpdateParams() == null)
		{
			return null;
		}
		return StatementAndValues.createStatementAndValues(
				getUpdateRoot(object, analyse.getUpdateParams()),
				getCondiction(analyse.getCondictionParams()));
	}
	
	public interface Property
	{
		Map<String, Object> getNamesAndValues();
	}
	
	public static QueryBuidler getSearchRoot(final Object object)
	{
		return new QueryBuidler()
		{
			@Override
			public StringBuilder getStatement()
			{
				Assert.notNull(object);
				return new StringBuilder("from ").append(object.getClass()
						.getSimpleName());
			}
			
			@Override
			public List<Object> getValues()
			{
				return null;
			}
		};
	}
	
	public static QueryBuidler getUpdateRoot(final Object object,
			final Map<String, Object> updateParams)
	{
		return new QueryBuidler()
		{
			@Override
			public StringBuilder getStatement()
			{
				Assert.notNull(object);
				Set<String> names = updateParams.keySet();
				Assert.isTrue(names.size() > 0, "必须有更新的属性");
				StringBuilder statement = new StringBuilder("update ").append(
						object.getClass().getSimpleName()).append(" set ");
				for(String propertyName : names)
				{
					statement.append(propertyName).append("=?, ");
				}
				return statement.delete(statement.length() - 2,
						statement.length() - 1);
			}
			
			@Override
			public List<Object> getValues()
			{
				List<Object> values = new ArrayList<Object>();
				for(String name : updateParams.keySet())
				{
					values.add(updateParams.get(name));
				}
				return values;
			}
		};
	}
	
	public static QueryBuidler getPagingRoot(final Object object)
	{
		return new QueryBuidler()
		{
			@Override
			public StringBuilder getStatement()
			{
				Assert.notNull(object);
				return new StringBuilder("select count(*) from ").append(object
						.getClass().getSimpleName());
			}
			
			@Override
			public List<Object> getValues()
			{
				return null;
			}
		};
	}
	
	public static QueryBuidler getCondiction(
			final Map<String, Object> condictions)
	{
		return new QueryBuidler()
		{
			@Override
			public StringBuilder getStatement()
			{
				Set<String> names = condictions.keySet();
				StringBuilder statement = new StringBuilder();
				if(names.size() < 1)
				{
					return statement;
				}
				else
				{
					statement.append(" where ");
				}
				for(String propertyName : names)
				{
					statement.append(propertyName).append("=? and ");
				}
				return statement.delete(statement.length() - 5,
						statement.length());
			}
			
			@Override
			public List<Object> getValues()
			{
				List<Object> values = new ArrayList<Object>();
				for(String name : condictions.keySet())
				{
					values.add(condictions.get(name));
				}
				return values;
			}
		};
	}
	
	public static QueryBuidler getOrder(final List<String> orders)
	{
		return new QueryBuidler()
		{
			@Override
			public StringBuilder getStatement()
			{
				StringBuilder orderStatement = new StringBuilder();
				if(orders == null || orders.size() < 1)
				{
					return orderStatement;
				}
				orderStatement.append(" order by ");
				for(String string : orders)
				{
					orderStatement.append(string).append(", ");
				}
				return orderStatement.delete(orderStatement.length() - 2,
						orderStatement.length());
			}
			
			@Override
			public List<Object> getValues()
			{
				return null;
			}
		};
	}
	
	public static Property getPropertyWithNullObject(Object object)
	{
		return new Bean(object, true, false);
	}
	
	public static Property getPropertyWithoutNullObject(final Object object)
	{
		return new Bean(object);
	}
}
