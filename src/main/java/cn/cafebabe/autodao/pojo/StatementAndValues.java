package cn.cafebabe.autodao.pojo;

import java.util.ArrayList;
import java.util.List;

import cn.cafebabe.autodao.util.Assert;

public class StatementAndValues
{
	private final StringBuilder statement = new StringBuilder();
	private final List<Object> values = new ArrayList<Object>();
	
	private StatementAndValues()
	{}
	
	public static StatementAndValues createStatementAndValues(
			QueryBuidler... buidlers)
	{
		Assert.isTrue(buidlers != null && buidlers.length > 0);
		StatementAndValues statementAndValues = new StatementAndValues();
		for(QueryBuidler buidler : buidlers)
		{
			statementAndValues.append(buidler.getStatement(), buidler.getValues());
		}
		return statementAndValues;
	}
	
	public StatementAndValues appendStatement(String text)
	{
		statement.append(text);
		return this;
	}
	
	public StatementAndValues appendStatement(StringBuilder text)
	{
		statement.append(text);
		return this;
	}
	
	public StatementAndValues appendValue(Object value)
	{
		if(value != null)
		{
			values.add(value);
		}
		return this;
	}
	
	public StatementAndValues appendValues(List<Object> values)
	{
		if(values != null)
		{
			this.values.addAll(values);
		}
		return this;
	}
	
	public StatementAndValues append(String text, List<Object> values)
	{
		appendStatement(text);
		appendValues(values);
		return this;
	}
	
	public StatementAndValues append(StringBuilder text, List<Object> values)
	{
		appendStatement(text);
		appendValues(values);
		return this;
	}
	
	public String getStatement()
	{
		return statement.toString();
	}
	
	public List<Object> getValues()
	{
		return values;
	}
	
	public interface QueryBuidler
	{
		StringBuilder getStatement();
		
		List<Object> getValues();
	}
}
