package com.tbl.common.utils;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author anss
 * @date 2018-08-30
 */
public class ConfigUtil {

    private static URL filePath=Thread.currentThread().getContextClassLoader().getResource("");
    private static String fileName = "resources.properties";
    public ConfigUtil(){}
    private static Properties props = new Properties();
    static{
        try {
            props.load(new FileInputStream(filePath.getPath()+ File.separator+fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getValue(String key){
        return props.getProperty(key);
    }

    public static void updateProperties(String key,String value) {
        props.setProperty(key, value);
    }

    public static void writeProperties(String key,String value) {
        try {
            OutputStream fos = new FileOutputStream(filePath.getPath()+File.separator+fileName);
            props.setProperty(key, value);
            //以适合使用 load 方法加载到 Properties 表中的格式，
            //将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + key + "' value");
            fos.close();// 关闭流
        } catch (IOException e) {
            System.err.println("Visit "+filePath+" for updating "+key+" value error");
        }
    }

    public static void main(String[] args) {
        System.out.println(getValue("1"));
    }
}
