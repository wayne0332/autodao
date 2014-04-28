package cn.cafebabe.autodao.autoQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.cafebabe.autodao.util.Assert;

class ParametersAnalyse
{
	private final Map<String, Object> updateParams, condictionParams;
	
	/**如果没有更新的属性,则此对象所有属性为null*/
	ParametersAnalyse(Map<String, Object> namesAndValues,
			Set<String> condictionParams)
	{
		if(namesAndValues.size() - condictionParams.size() > 0)
		{
			Assert.isTrue(namesAndValues != null && condictionParams != null,
					"建立ParametersAnalyse对象时构造函数参数不能为null");
			Assert.isTrue(condictionParams.size() > 0,
					"一定要有限制条件,如果想用批量更新就自己写HQL吧");
			Set<String> names = namesAndValues.keySet();
			Assert.isTrue(names.containsAll(condictionParams),
					"限制条件一定要对象有这属性,并没有被过滤掉(不同方法可能会过滤不同条件的属性,比如过滤null,collection属性等)");
			updateParams = new HashMap<String, Object>();
			this.condictionParams = new HashMap<String, Object>();
			for(String param : namesAndValues.keySet())
			{
				if(condictionParams.contains(param))
				{
					this.condictionParams.put(param, namesAndValues.get(param));
				}
				else
				{
					updateParams.put(param, namesAndValues.get(param));
				}
			}
		}
		else
		{
			updateParams = null;
			this.condictionParams = null;
		}
	}
	
	public Map<String, Object> getUpdateParams()
	{
		return updateParams;
	}
	
	public Map<String, Object> getCondictionParams()
	{
		return condictionParams;
	}
}
