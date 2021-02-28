
package com.tbl.modules.platform.controller;

import com.tbl.common.utils.PageData;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.entity.system.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Controller公共组件
 *
 * @author anss
 * @date 2018-8-24
 */
public abstract class AbstractController {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6357869213649815390L;

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected HttpSession session;

	protected Logger logger = LoggerFactory.getLogger(getClass());


	protected Long getUserId() {
		return getSessionUser().getUserId();
	}


	/**
	 * 得到PageData
	 */
	public PageData getPageData()
	{
		return new PageData(this.getRequest());
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 得到session对象
	 * @return
	 */
	public HttpSession getSession(){
		return this.getRequest().getSession();
	}

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}

	/**
	 * 初始化分页信息
	 */
	public PageTbl getPage()
	{
		HttpServletRequest reuqest = this.getRequest();
		String pageNo = (String)reuqest.getParameter("page");
		String pageSize = (String)reuqest.getParameter("rows");
		String sortOrder = (String)reuqest.getParameter("sord");
		String sortName = (String)reuqest.getParameter("sidx");

		PageTbl page = new PageTbl();
		if( !StringUtils.isEmptyString(pageNo) )
		{
			page.setPageno(Integer.parseInt(pageNo));
		}

		if(!StringUtils.isEmptyString(pageSize))
		{
			page.setPagesize(Integer.parseInt(pageSize));
		}

		if(!StringUtils.isEmptyString(sortName))
		{
			page.setSortname(sortName);
		}

		if(!StringUtils.isEmptyString(sortOrder))
		{
			page.setSortorder(sortOrder);
		}
		return page;
	}

	/**
	 * 转换分页对象内容，将分页信息初始化到JqGridjson map中
	 * @param pageMap
	 * @param page 分页对象
	 * @return
	 */
	public Map<String,Object> executePageMap(Map<String,Object> pageMap, PageTbl page)
	{
		if( page != null )
		{
			pageMap.put("page", page.getPageno());  //当前页
			pageMap.put("total", page.getTotalPages()); //总页数
			pageMap.put("records", page.getTotalRows());//总记录数
		}
		return pageMap;
	}

	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}

	/**
	 * 得到session对象中的user对象
	 * @return
	 */
	public User getSessionUser(){
		User sessionUser = (User) this.getSession().getAttribute("sessionUser");
		return sessionUser;
	}
}
