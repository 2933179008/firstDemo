package com.tbl.modules.platform.constant;

import org.springframework.context.ApplicationContext;

/**
 * 常用常量
 *
*/
public class Const {
	public static final String SESSION_USER = "sessionUser";
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";			//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";		//全部菜单
	public static final String SESSION_quickmenuList = "quickmenuList";		//快捷菜单
	public static final String SESSION_QX = "QX";
	public static final String SESSION_USERROL = "USERROL";				//用户对象
	public static final String SESSION_USERNAME = "USERNAME";			//用户名
	public static final String LOGIN = "/login/login";				//登录地址
	public static final String SYSNAME = "config/system/SYSNAME.txt";	//系统名称路径
	public static final String PAGE	= "config/system/PAGE.txt";			//分页条数配置路径
	public static final String EMAIL = "config/system/EMAIL.txt";		//邮箱服务器配置路径
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";	//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";		//文件上传路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(common)|(static)|(web)|(uploadFiles)|(services)|(main)|(websocket)).*";	//不对匹配该值的访问路径拦截（正则）
	
	
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
}
