package cn.cafebabe.autodao;

import java.util.Arrays;

import cn.cafebabe.autodao.autoQuery.AutoQuery;

public class Test
{
	public static void main(String[] args)
	{
		System.out.println(AutoQuery.getSearchStatement(
				new Student(1, "test", null), Arrays.asList("id desc"))
				.getStatement());
	}
	
	public static class Student
	{
		private Integer id = null;
		private String name = null;
		private String password = null;
		
		public Student(Integer id, String name, String password)
		{
			super();
			this.id = id;
			this.name = name;
			this.password = password;
		}
		
		public Integer getId()
		{
			return id;
		}
		
		public void setId(Integer id)
		{
			this.id = id;
		}
		
		public String getName()
		{
			return name;
		}
		
		public void setName(String name)
		{
			this.name = name;
		}
		
		public String getPassword()
		{
			return password;
		}
		
		public void setPassword(String password)
		{
			this.password = password;
		}
	}
}
