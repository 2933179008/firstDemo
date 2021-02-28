package com.tbl.common.config;

import com.tbl.common.shiro.RedisShiroSessionDAO;
import com.tbl.common.shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置文件
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(RedisShiroSessionDAO redisShiroSessionDAO,
                                         @Value("${renren.redis.open}") boolean redisOpen,
                                         @Value("${renren.shiro.redis}") boolean shiroRedis){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        //如果开启redis缓存且renren.shiro.redis=true，则shiro session存到redis里
        if(redisOpen && shiroRedis){
            sessionManager.setSessionDAO(redisShiroSessionDAO);
        }
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login/login_toLogin");
        shiroFilter.setSuccessUrl("/login/main/index");
        shiroFilter.setUnauthorizedUrl("/");

        Map<String, String> filterMap = new LinkedHashMap<>();

        /*给外部提供数据接口*/
        filterMap.put("/loginInterface/**", "anon");
        filterMap.put("/InstorageInterface/**", "anon");
        filterMap.put("/OutstorageInterface/**", "anon");
        filterMap.put("/stockInterface/**", "anon");
        filterMap.put("/ErpInterface/**", "anon");


        /*平板接口*/
        filterMap.put("/instorageSlab/**", "anon");


        /**平板接口**/
        filterMap.put("/PADInterfaceController/**", "anon");

        /**UWB接口**/
        filterMap.put("/uwbMoveRecord/**", "anon");

        filterMap.put("/static/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/login/**", "anon");
        filterMap.put("/statics/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
