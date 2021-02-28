package com.tbl.common.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class PageData extends HashMap<Object, Object> implements Map<Object, Object>{
	
	private static final long serialVersionUID = 1L;
	
	Map<String,Object> map = null;
	HttpServletRequest request;
	
	public PageData() 
	{
		map = new HashMap<String,Object>();
	}
	
	public PageData(HttpServletRequest request)
	{
		this.request = request;
		Map<String, String[]> properties = request.getParameterMap();
		Map<String,Object> returnMap = new HashMap<String,Object>(); 
		Iterator<Entry<String, String[]>> entries = properties.entrySet().iterator();
		Entry<String, String[]> entry;
		String name = "";
		while (entries.hasNext())
		{
			String value = "";
			entry = (Entry<String, String[]>) entries.next();
			name = (String) entry.getKey(); 
			Object valueObj = entry.getValue(); 
			if(null == valueObj)
			{ 
				value = ""; 
			}
			else if(valueObj instanceof String[])
			{ 
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++)
				{ 
					value += (StringUtils.isEmptyString(value) ?  "" : ",") + values[i];
				}
			}
			else
			{
				value = valueObj.toString(); 
			}
			returnMap.put(name, value); 
		}
		map = returnMap;
	}
	
	@Override
	public Object get(Object key) 
	{
		Object obj = null;
		if(map.get(key) instanceof Object[]) 
		{
			Object[] arr = (Object[])map.get(key);
			obj = request == null ? arr:(request.getParameter((String)key) == null ? arr:arr[0]);
		} 
		else 
		{
			obj = map.get(key);
		}
		return obj;
	}
	
	public String getString(Object key) 
	{
		return (String)get(key);
	}
	
	public long getLong(Object key){
		return (long)get(key);
	}
	
	@Override
	public Object put(Object key, Object value) {
		return map.put((String)key, value);
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map t) {
		map.putAll(t);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}
	
}
