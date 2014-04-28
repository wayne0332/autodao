package cn.cafebabe.autodao.autoQuery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cafebabe.autodao.autoQuery.AutoQuery.Property;
import cn.cafebabe.autodao.util.Assert;


public final class Bean implements Property
{
	private Object object = null;
	private Method[] allMethods = null;
	private ArrayList<String> propertyNames = null; // ������
	private ArrayList<Method> getMethods = null; // set����
	private ArrayList<Method> setMethods = null; // get����
	private Map<String, Class<?>> namesAndClazz = null;
	private boolean isContainNullObject = false;
	private boolean isContainCollection = false;
	
	public Bean(Object object)
	{
		this(object, false, false);
	}
	
	public Bean(Object object, boolean isContainNullObject,
			boolean isContainCollection)
	{
		Assert.notNull(object);
		this.object = object;
		this.allMethods = object.getClass().getMethods();
		this.isContainCollection = isContainCollection;
		this.isContainNullObject = isContainNullObject;
	}
	
	private String getPropertyName(String methodName)
	{
		return methodName.substring(3, 4).toLowerCase()
				+ methodName.substring(4);
	}
	
	public ArrayList<Method> getGetMethods()
	{
		if(getMethods != null)
		{
			return getMethods;
		}
		getMethods = new ArrayList<Method>();
		for(int i = 0; i != allMethods.length; ++ i)
		{
			if(isGetMethod(allMethods[i].getName()))
			{
				getMethods.add(allMethods[i]);
			}
		}
		return getMethods;
	}
	
	private boolean isGetMethod(String methodName)
	{
		return methodName.length() > 3
				&& "get".equals(methodName.substring(0, 3))
				&& !"getClass".equals(methodName);
	}
	
	public ArrayList<Method> getSetMethods()
	{
		if(setMethods != null)
		{
			return setMethods;
		}
		setMethods = new ArrayList<Method>();
		for(int i = 0; i != allMethods.length; ++ i)
		{
			if(isSetMethod(allMethods[i].getName())) // ��ͷΪ"set"��"get",�Ҳ���"getClass"�ĺ���ż�¼
			{
				setMethods.add(allMethods[i]);
			}
		}
		return setMethods;
	}
	
	private boolean isSetMethod(String methodName)
	{
		return methodName.length() > 3
				&& "set".equals(methodName.substring(0, 3));
	}
	
	public ArrayList<String> getPropertyNames()
	{
		if(propertyNames != null)
		{
			return propertyNames;
		}
		return propertyNames = methodsToPropertyNames(getGetMethods());
	}
	
	private ArrayList<String> methodsToPropertyNames(List<Method> methods)
	{
		ArrayList<String> names = new ArrayList<String>();
		for(Method method : methods)
		{
			names.add(getPropertyName(method.getName()));
		}
		return names;
	}
	
	@Deprecated
	public ArrayList<Object> getPropertyObjects()
	{
		ArrayList<Object> objects = new ArrayList<Object>();
		for(Method method : getGetMethods())
		{
			try
			{
				objects.add(method.invoke(object));
			}
			catch(Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return objects;
	}
	
	@Override
	public Map<String, Object> getNamesAndValues()
	{
		Map<String, Object> namesAndValues = new HashMap<String, Object>();
		for(Method method : getGetMethods())
		{
			try
			{
				Object value = method.invoke(object);
				if(checkValue(value))
				{
					namesAndValues
							.put(getPropertyName(method.getName()), value);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		return namesAndValues;
	}
	
	public Map<String, Class<?>> getNamesAndClass()
	{
		if(namesAndClazz == null)
		{
			namesAndClazz = new HashMap<String, Class<?>>();
			for(Method method : getGetMethods())
			{
				try
				{
					namesAndClazz.put(getPropertyName(method.getName()),
							method.getReturnType());
				}
				catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
			}
		}
		return namesAndClazz;
	}
	
	private boolean checkValue(Object value)
	{
		return isContainNullObject ? true : value == null ? false
				: isContainCollection ? true
						: value instanceof Collection ? false : true;
	}
}
