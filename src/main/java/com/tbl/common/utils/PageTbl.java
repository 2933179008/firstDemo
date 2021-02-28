package com.tbl.common.utils;


public class PageTbl {

	// 页码，默认为1
	private int pageno = 1;
	// 页面显示的数据量
	private int pagesize = 20;
	// 数据总量
	private int totalRows = 0;
	// 总页数
	private int totalPages = 0;

	/**
	 * 单次返回数据条数
	 */
	private int currentTotal = 0;

	// 数据库游标位置
	private int startIndex = 0;

	private int endIndex = 0;

	// 显示第 from - to 条数据
	private int from = 0;

	private int to = 0;
	// 排序字段名称
	private String sortname;
	// 排序顺序
	private String sortorder;

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public void setTo(int to) {
		this.to = to;
	}

	private PageData pd = new PageData();


   public PageTbl(){}

	public PageTbl(int pageSize, int totalRows )
	{
		this.pagesize = pageSize;
		this.totalRows = totalRows;
	}

	public PageTbl(String pageNo, String pageSize )
	{
		if( !StringUtils.isEmptyString( pageNo ) )
		{
			this.pageno = Integer.valueOf( pageNo );
		}
		if( !StringUtils.isEmptyString( pageSize ) )
		{
			this.pagesize = Integer.valueOf( pageSize );
		}
	}

	public PageTbl(String pageNo, String pageSize, int totalRows )
	{
		if( !StringUtils.isEmptyString( pageNo ) )
		{
			this.pageno = Integer.valueOf( pageNo );
		}
		if( !StringUtils.isEmptyString( pageSize ) )
		{
			this.pagesize = Integer.valueOf( pageSize );
		}

		this.totalRows = totalRows;
	}

	public PageTbl(int pageNo, int pageSize, int totalRows )
	{
		this.pageno = pageNo;
		this.pagesize = pageSize;
		this.totalRows = totalRows;
	}

	public PageTbl(int pageNo, int pageSize, int totalRows, String sortname, String sortorder )
	{
		this.pageno = pageNo;
		this.pagesize = pageSize;
		this.totalRows = totalRows;
		this.sortname = sortname;
		this.sortorder = sortorder;
	}

	/**
	 * 获取页码
	 * 
	 * @return 页码
	 */
	public int getPageno()
	{
		// 如果没有数据，则返回0
//		if( this.getTotalRows() <= 0 )
//		{
//			return 1;
//		}
		
		return this.pageno;
	}

	/**
	 * 获取数据总量
	 * 
	 * @return 数据总量
	 */
	public int getTotalRows()
	{
		return this.totalRows;
	}

	/**
	 * 设置数据总量
	 * 
	 * @param totalRows 数据总量
	 */
	public void setTotalRows( int totalRows )
	{
		this.totalRows = totalRows;
		//分页信息处理
		//获取总页数：根据总记录数和每页条数计算出总页数
		int pageCount = totalRows/this.pagesize;   //分页数量
		if((totalRows%this.pagesize)>0){ //余数不为0
			pageCount++;
		}
		//当用户输入的页码大于总页数，则将页码修改为最大的页数
		if(this.pageno>pageCount){
			this.pageno = pageCount;
		}else if(this.pageno<=0){
			this.pageno = 1;
		}
	}

	/**
	 * 获取总的分页数量
	 * 
	 * @return 总的分页数量
	 */
	public int getTotalPages()
	{
		if( getTotalRows() <= 0 || getPagesize() <= 0 )
		{
			return 0;
		}
		
		this.totalPages = (getTotalRows() % getPagesize()) == 0
					 ?(getTotalRows() / getPagesize())
					 :(getTotalRows() / getPagesize())+1;
		
		return this.totalPages;
	}

	/**
	 * 获取当前页码下显示数据的开始值
	 * 
	 * @return 当前页码下显示数据的开始值
	 */
	public int getFrom()
	{
		this.from = (getPageno() - 1) * getPagesize() + 1;
		
		if( this.from <= 0 )
		{
			this.from = 0;
		}
		
		return this.from;
	}

	/**
	 * 获取当前页码下显示数据的结束值
	 * 
	 * @return 当前页码下显示数据的结束值
	 */
	public int getTo()
	{
		this.to = getPageno() * getPagesize();
		if( this.to > getTotalRows() )
		{
			this.to = getTotalRows();
		}
		
		if( this.to <= 0 )
		{
			this.to = 0;
		}
		
		return this.to;
	}

	/**
	 * 获取当前页码下游标的开始位置
	 * 
	 * @return 当前页码下游标的开始位置
	 */
	public int getStartIndex()
	{
		this.startIndex = (getPageno() -1) * getPagesize();
		
		if( this.startIndex <= 0 )
		{
			this.startIndex = 0;
		}
		
		return this.startIndex;
	}

	/**
	 * 获取当前页码下游标的结束位置
	 * 
	 * @return 当前页码下游标的结束位置
	 */
	public int getEndIndex()
	{
		this.endIndex = getPageno() * getPagesize() - 1;
		
		if( this.endIndex <= 0 )
		{
			this.endIndex = 0;
		}
		
		if( this.endIndex >= getTotalRows() )
		{
			this.endIndex = getTotalRows() - 1;
		}
		
		return this.endIndex;
	}

	/**
	 * 获取排序字段名称
	 * 
	 * @return 排序字段名称
	 */
	public String getSortname()
	{
		return this.sortname;
	}

	/**
	 * 设置排序字段名称
	 * 
	 * @param sortname 排序字段名称
	 */
	public void setSortname( String sortname )
	{
		this.sortname = sortname;
	}

	/**
	 * 获取排序顺序，例如： ASC -- 升序， DESC -- 降序
	 * 
	 * @return 排序顺序
	 */
	public String getSortorder()
	{
		return this.sortorder;
	}

	/**
	 * 设置排序顺序
	 * 
	 * @param sortorder 排序顺序
	 */
	public void setSortorder( String sortorder )
	{
		this.sortorder = sortorder;
	}

	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		this.pd = pd;
	}
	
	public int getCurrentTotal() {
		return currentTotal;
	}

	public void setCurrentTotal(int currentTotal) {
		this.currentTotal = currentTotal;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("pageno:" + pageno );
		sb.append("pagesize:" + pagesize );
		sb.append("totalRows:" + totalRows );
		sb.append("totalPages:" + totalPages );
		sb.append("sortname:" + sortname );
		sb.append("sortorder:" + sortorder );
		sb.append("currentTotal:" + currentTotal);
		return sb.toString();
	}
}
