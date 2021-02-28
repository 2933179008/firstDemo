package com.tbl.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * http调用共同类
 *
 * @author anss
 * @date 2018-12-13
 */
public class HttpUtil {

    //返回数据类型（1：字符串；2：字节数组）
    private static final Integer RETURN_TYPE_STRING = 1;

    /**
     * http post 方法调用(content-type: application/json)
     * @param url
     * @param params
     * @return 响应数据
     */
    public static String postJSON(String url, String params, Map<String, Object> headers) {
        return (String) postJSONWithResponseHeaders(url, params, headers).get("data");
    }

    /**
     * 调用指定url返回数据（method: post, content-type: application/json）
     * @param url
     * @param parameters
     * @param headers
     * @return
     */
    public static Map<String, Object> postJSONWithResponseHeaders(String url, String parameters, Map<String, Object> headers) {
        HttpPost httppost = new HttpPost(url);

//        String parameters = JSON.toJSONString(params);

        if (StringUtils.isEmpty(parameters)) {
            return httpInternelExecute(httppost, headers, RETURN_TYPE_STRING);
        }

        StringEntity entity = new StringEntity(parameters, ContentType.create("application/json", Consts.UTF_8));
        entity.setChunked(true);
        httppost.setEntity(entity);

        return httpInternelExecute(httppost, headers, RETURN_TYPE_STRING);
    }

    /**
     * 执行http请求并返回结果
     * @param httpUriRequest
     * @param reqHeaders
     * @param type 返回数据类型（1：字符串；2：字节数组）
     * @return {
     *     responseHeaders：响应头Map
     *     data: 返回结果（String or byte array）
     * }
     */
    private static Map<String, Object> httpInternelExecute(HttpUriRequest httpUriRequest, Map<String, Object> reqHeaders, Integer type) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        BufferedReader reader = null;

        try {
            if (reqHeaders != null) {
                for (String key : reqHeaders.keySet()) {
                    httpUriRequest.setHeader(key, (String) reqHeaders.get(key));
                }
            }

            response = httpclient.execute(httpUriRequest);

            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }

            if (entity == null) {
                throw new ClientProtocolException("Response contains no content");
            }

            InputStream inputStream = entity.getContent();
            Object data = null;

            //判断是要返回字符串
            if (type.equals(RETURN_TYPE_STRING)) {
                reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));

                StringBuilder sb = new StringBuilder(1024);

                String item;
                while ((item = reader.readLine()) != null) {
                    sb.append(item + "\n");
                }

                String s = sb.toString();
                if (StringUtils.isNotEmpty(s)) {
                    data = s.substring(0, s.length() - 1);
                }

            } else { //返回字节数组
                new IllegalStateException();
            }

            Header[] headers = response.getAllHeaders();
            Map<String, Object> responseHeaders = new HashMap<String, Object>();

            for (int i = 0; i < headers.length; i++) {
                responseHeaders.put(headers[i].getName(), headers[i].getValue());
            }
            Map<String, Object> result = new HashMap<String, Object>(2);
            result.put("responseHeaders", responseHeaders);
            result.put("data", data);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
