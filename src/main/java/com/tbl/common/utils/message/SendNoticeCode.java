package com.tbl.common.utils.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tbl.common.config.StaticConfig;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 发送模板短信请求
 *
 * @author pz
 */
public class SendNoticeCode {

    //MOBILES       手机号，接收者号码列表，JSONArray格式，限制接收者号码个数最多为100个
    //PARAMS        短信参数列表，用于依次填充模板，JSONArray格式，每个变量长度不能超过30字,对于不包含变量的模板，不填此参数表示模板即短信全文内容
    public static void sendNoticeCode(String MOBILES, String PARAMS) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(StaticConfig.getServerURL());
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = CheckSumBuilder.getCheckSum(StaticConfig.getAppSecret(), StaticConfig.getNonce(), curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", StaticConfig.getAppKey());
        httpPost.addHeader("Nonce", StaticConfig.getNonce());
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", StaticConfig.getTemplateID()));
        nvps.add(new BasicNameValuePair("mobiles", MOBILES));
        nvps.add(new BasicNameValuePair("params", PARAMS));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }
}