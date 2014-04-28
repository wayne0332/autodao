package cn.cafebabe.autodao.pojo;

import cn.cafebabe.autodao.util.Assert;

public final class Page
{
	private static int defaultPageSize = 7;
	private int currentPage = 1, pageSize = defaultPageSize;
	private long totalCount = 0;

	public Page()
	{
	}

	@Deprecated
	public Page(long totalCount)
	{
		this(1, defaultPageSize, totalCount);
	}

	@Deprecated
	public Page(int pageSize, long totalCount)
	{
		this(1, pageSize, totalCount);
	}

	@Deprecated
	public Page(int currentPage, int pageSize, long totalCount)
	{
		setCurrentPage(currentPage);
		setPageSize(pageSize);
		setTotalCount(totalCount);
	}

	@Deprecated
	public static Page getPage(int currentPage, int eachPageNumber,
	                           long pageNumber)
	{
		return new Page(currentPage, eachPageNumber, pageNumber
				* eachPageNumber);
	}

	public int getFirstIndex()
	{
		return (getCurrentPage() - 1) * getPageSize();
	}

	public int getCurrentPage()
	{
		return (currentPage < 1 ? 1
				: currentPage > getPageCount() ? getPageCount() : currentPage);
	}

	public int getPageSize()
	{
		Assert.isTrue(pageSize > 0, "每页条数要大于0!");
		return pageSize;
	}

	public int getPageCount()
	{
		Long pageCount = getTotalCount() / getPageSize();
		Assert.isTrue(pageCount < Integer.MAX_VALUE, "总数量太多,分页会出错!");
		if (totalCount % pageSize != 0)
		{
			pageCount += 1;
		}
		return pageCount.intValue();
	}

	public long getTotalCount()
	{
		Assert.isTrue(totalCount > -1, "数据总数不能为负数!");
		return totalCount;
	}

	public static int getDefaultPageSize()
	{
		return defaultPageSize;
	}

	public static void setDefaultPageSize(int defaultPageSize)
	{
		Assert.isTrue(defaultPageSize > 0, "每页条数不能小于1!");
		Page.defaultPageSize = defaultPageSize;
	}

	public Page setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
		return this;
	}

	public Page setPageCount(int pageCount)
	{
		setTotalCount(pageCount * getPageSize());
		return this;
	}

	public Page setTotalCount(long totalCount)
	{
		this.totalCount = totalCount;
		return this;
	}

	public Page setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
		return this;
	}
}
