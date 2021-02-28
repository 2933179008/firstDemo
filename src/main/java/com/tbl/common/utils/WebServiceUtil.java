package com.tbl.common.utils;

import com.tbl.common.config.StaticConfig;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * webservice工具类
 *
 * @author anss
 * @date 2019-01-02
 */
public class WebServiceUtil {

    private static Client client;
    private static Object lock = new Object();

    /**
     * 调取webService服务
     * @author anss
     * @data 2019-1-2
     * @return
     */
    public static Client getClient(){
        if(client == null){
            synchronized (lock) {
                if(client == null){
                    DynamicClientFactory dcf = DynamicClientFactory.newInstance();
                    client = dcf.createClient(StaticConfig.getDyylWebserviceURL());
                    HTTPConduit http = (HTTPConduit) client.getConduit();
                    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
                    httpClientPolicy.setConnectionTimeout(1000 * 60 * 10);
                    httpClientPolicy.setAllowChunking(false);
                    httpClientPolicy.setReceiveTimeout(1000 * 60 * 100);
                    http.setClient(httpClientPolicy);
                }
            }
        }
        return client;
    }


}
