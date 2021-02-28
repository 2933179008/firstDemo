package com.tbl.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 静态方法获取配置文件信息
 *
 * @author anss
 * @date 2018-08-30
 */
@Configuration
public class StaticConfig {

    private static String static_adapteRfid;
    private static String static_dyylWebserviceURL;
    private static String static_host;
    private static String static_port;
    private static String static_user;
    private static String static_password;
    private static String static_serverURL;
    private static String static_appKey;
    private static String static_appSecret;
    private static String static_nonce;
    private static String static_templateID;

    @Value("${read.adapteRfid}")
    private String getAdapteRfid;

    //webservice接口url
    @Value("${dyyl.dyylWebserviceURL}")
    private String getDyylWebserviceURL;

    //smtp服务器地址
    @Value("${email.host}")
    private String getHost;

    //邮箱端口号
    @Value("${email.port}")
    private String getPort;

    //邮箱用户名
    @Value("${email.user}")
    private String getUser;

    //邮箱密码
    @Value("${email.password}")
    private String getPassword;

    //短信请求路径URL
    @Value("${message.SERVER_URL}")
    private String getServerURL;

    //短信APP_KEY
    @Value("${message.APP_KEY}")
    private String getAppKey;

    //短信AppSecret
    @Value("${message.APP_SECRET}")
    private String getAppSecret;

    //短信随机数
    @Value("${message.NONCE}")
    private String getNonce;

    //短信短信模板ID
    @Value("${message.TEMPLATEID}")
    private String getTemplateID;

    @PostConstruct
    public void getApiToken() {

        static_adapteRfid = this.getAdapteRfid;
        static_dyylWebserviceURL = this.getDyylWebserviceURL;
        static_host = this.getHost;
        static_port = this.getPort;
        static_user = this.getUser;
        static_password = this.getPassword;
        static_serverURL = this.getServerURL;
        static_appKey = this.getAppKey;
        static_appSecret = this.getAppSecret;
        static_nonce = this.getNonce;
        static_templateID = this.getTemplateID;
    }

    public static String getGetAdapteRfid() {
        return static_adapteRfid;
    }

    public static String getDyylWebserviceURL() {
        return static_dyylWebserviceURL;
    }

    public static String getHost() {
        return static_host;
    }

    public static String getPort() {
        return static_port;
    }

    public static String getUser() {
        return static_user;
    }

    public static String getPassword() {
        return static_password;
    }

    public static String getServerURL() {
        return static_serverURL;
    }

    public static String getAppKey() { return static_appKey; }

    public static String getAppSecret() { return static_appSecret; }

    public static String getNonce() { return static_nonce; }

    public static String getTemplateID() {
        return static_templateID;
    }
}
