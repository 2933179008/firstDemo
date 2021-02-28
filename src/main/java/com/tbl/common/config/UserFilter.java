package com.tbl.common.config;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFilter {

	@Autowired
	private static StaticConfig staticConfig;

//	public static User isAdmin(String name, String pwd){
//
//		SymmetricEncoder se = new SymmetricEncoder();
//		String encodeRules = "123";
//        String name_content = se.AESEncode(encodeRules, name);
//        String pwd_content = se.AESEncode(encodeRules, pwd);
//        String sys_username = "", sys_userpwd = "";
//        boolean isadmin = false;
//        User user = new User();
//        Properties prop = new Properties();
//	     try{
//			 sys_username =  staticConfig.getS_user_1();
//			 sys_userpwd =  staticConfig.getS_user_2();
//			 user.setUserId( Long.valueOf( staticConfig.getS_user_id()) );
//			 user.setName( staticConfig.getS_user_showname() );
//
//	         user.setPassword(sys_userpwd);
//	    }
//	    catch(Exception e){
//	         System.out.println(e);
//	    }
//        if(  name_content.equals(sys_username) &&   pwd_content.equals(sys_userpwd) ){
//        	isadmin =true;
//            user.setUsername(name);
//        }
//		return ( isadmin ? user : null ) ;
//	}

}
