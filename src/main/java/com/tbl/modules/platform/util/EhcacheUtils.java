package com.tbl.modules.platform.util;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;

/**
 * 缓存工具类
 * Created by 汪长军 on 2017/7/6.
 */
@SuppressWarnings("rawtypes")
public class EhcacheUtils {

    public static CacheManager cacheManager = null;

    /**
     * 永久有效的缓存
     */
    public static Cache simpleCache = null;
    /**
     * 在指定时间内有效的缓存
     */
    public static Cache fixCache = null;
    /**
     * 在指定时间内有效的缓存
     */
    public static Cache columnCache = null;
    /**
     * 缓存配置文件存放路径
     */
    public static final URL URL_PATH = EhcacheUtils.class.getResource("/ehcache.xml");

    static {
    	
        initCacheManager();
        intitCache();
    }

    /**
     * 加载缓存配置文件
     */
    public static void initCacheManager() {
        if (cacheManager == null) {
            Configuration configuration = new XmlConfiguration(URL_PATH);
            cacheManager = CacheManagerBuilder.newCacheManager(configuration);
            cacheManager.init();
        }
    }

    /**
     * 初始化缓存
     */
    public static void intitCache() {
        if (simpleCache == null) {
            simpleCache = cacheManager.getCache("simpleCache", String.class, String.class);
        }
        if(fixCache==null){
            fixCache = cacheManager.getCache("fixCache", String.class, String.class);
        }if(columnCache==null){
            columnCache = cacheManager.getCache("columnsCache", String.class, String.class);
        }
    }

    /**
     * 缓存键值
     *
     * @param key   键
     * @param value 缓存对象
     */
    @SuppressWarnings("unchecked")
	public static void putValue(String key, String value) {
        simpleCache.put(key, value);
    }

    /**
     * 根据键查询对应的缓存对象
     *
     * @param key 键
     */
    @SuppressWarnings("unchecked")
	public static Object getValue(String key) {
        return simpleCache.get(key) == null ? "" : simpleCache.get(key);
    }

    /**
     * 删除对应的key缓存
     *
     * @param key 键
     */
    @SuppressWarnings("unchecked")
	public static void removeElement(String key) {
        simpleCache.remove(key);
    }

    /**
     * 删除全部缓存
     */
    public static void removeAll() {

        simpleCache.clear();
    }

    /**
     * 删除部分缓存
     *
     * @param keySet 缓存集合
     */
    @SuppressWarnings("unchecked")
	public static void removeElements(Set<String> keySet) {
        simpleCache.removeAll(keySet);
    }

    /**
     * 查找所有的Key-Value
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Iterator<Entry<String,Object>> getAllKeyValue() {
        return simpleCache.iterator();
    }
}