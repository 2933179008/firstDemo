package com.tbl.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;

/**
 * log共通类
 *
 * @author anss
 * @date 2018-08-30
 */
public class LogUtil {

    private final static Log log = LogFactory.getLog(LogUtil.class);

    /**
     * @param ipStr 客户端ip地址
     * @return 根据客户端的ip地址去获取对应的mac
     */
    public static String getRealMacInfo(String ipStr){
        String serverIP ="";
        try{
            InetAddress inet = InetAddress.getLocalHost();
            serverIP = inet.getHostAddress();
        }catch (Exception e){
            log.debug("获取mac地址信息失败！");
        }

        String returnStr = "";
        String strTemp = System.getProperty("file.separator");
        String winTemp = "\\";
        String linuxTemp = "/";
        if(serverIP.equals(ipStr)){
            returnStr = getWindowMacInfo();
        }else if(winTemp.equals(strTemp)){
            returnStr = getWindowsMacInfoByIP(ipStr);
        }else if(linuxTemp.equals(strTemp)){
            returnStr = getLinuxMacInfoByIP(ipStr);
        }
        return returnStr.toUpperCase();
    }

    /**
     *
     * @return	取得本地mac地址信息
     */
    public static String getWindowMacInfo(){
        String returnStr = "";
        try {
            Process process = Runtime.getRuntime().exec("ipconfig /all");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null){
                if (line.indexOf("Physical Address") > 0) {
                    returnStr = line.substring(line.indexOf("-") - 2);
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("IOException " + e.getMessage());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return returnStr;
    }

    /**
     * @param ipStr 客户端IP
     * @return 服务器端在linux下通过IP来获取mac地址
     */
    public static String getLinuxMacInfoByIP(String ipStr){
        String returnStr = "";
        try{
            Process process = Runtime.getRuntime().exec("arp -n");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null){
                String[] aaStr = line.split(" ");
                if(ipStr.equals(aaStr[0])){
                    for(int i = 1 ; i < aaStr.length; i ++){
                        if(aaStr[i].split(":").length >= 2 && (aaStr[i] != null  || !"".equals(aaStr[i]))){
                            returnStr = aaStr[i];
                        }
                    }
                    break;
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("IOException " + e.getMessage());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return returnStr;
    }

    /**
     * @param ipStr 客户端IP
     * @return 服务器端在windows下通过IP来获取mac地址
     */
    public static String getWindowsMacInfoByIP(String ipStr){
        String returnStr = "";
        try{
            Process process = Runtime.getRuntime().exec("arp -a");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null){
                line = line.trim();
                String[] aaStr = line.split(" ");
                if(ipStr.equals(aaStr[0])){
                    for(int i = 1 ; i < aaStr.length; i ++){
                        if(aaStr[i].split("-").length >= 2 && (aaStr[i] != null  || !"".equals(aaStr[i]))){
                            returnStr = aaStr[i];
                        }
                    }
                    break;
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("IOException " + e.getMessage());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return returnStr;
    }
}
