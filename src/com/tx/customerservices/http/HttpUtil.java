package com.tx.customerservices.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public final class HttpUtil {
	
	public static final String PER_TAG = "tx_customer_services_";
	private static AsyncHttpClient httpClient;
	public static final String versionAndSystem="";
	public static final String BASE_URL="";
	
	
	
	public static final String LOGIN_IN="";

    public static synchronized AsyncHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new AsyncHttpClient();
        }
        return httpClient;
    }

    /**
     *无图片加载的Get有缓
     */
    public static void get(String url, RequestParams params, TextListener listener) {
            RequestHandle requestHandle = getHttpClient().get(BASE_URL + url + versionAndSystem, params, listener);
            listener.setRequestHandle(requestHandle);
            listener.setUrl(PER_TAG + Md5Util.getDigest(BASE_URL + url + versionAndSystem));
            listener.setCache(false);
            
    }
    /**
     * Get有缓
     */
    public static void get(Context context, String url, TextListener listener) {
        RequestHandle requestHandle = getHttpClient().get(context,BASE_URL + url+versionAndSystem, listener);
        listener.setRequestHandle(requestHandle);
        listener.setUrl(PER_TAG + Md5Util.getDigest(BASE_URL + url + versionAndSystem));
        listener.setCache(false);
    }

    /**
     * Get有缓
     */
    public static void get(Context context, String url, RequestParams params, TextListener listener) {
            RequestHandle requestHandle = getHttpClient().get(context, BASE_URL+url+versionAndSystem, params, listener);
            listener.setRequestHandle(requestHandle);
            listener.setUrl(PER_TAG + Md5Util.getDigest(BASE_URL+url+versionAndSystem));
            listener.setCache(false);
            
    }

    /**
     * Post无缓
     */
    public static void post(Context context, String url, RequestParams params, TextListener listener) {
        RequestHandle requestHandle = getHttpClient().post(context, BASE_URL+url+versionAndSystem, params, listener);
        listener.setRequestHandle(requestHandle);
        listener.setMethod("POST");
    }
    
    /**
     * Get方法-带有缓存
     * 兼容Map<>参数
     */
    public static void get(Context context, String url, Map<String, String> map, TextListener listener) {
    	RequestParams params = new RequestParams(map);
    	get(context, url+versionAndSystem, params, listener);
    }

    /**
     * Post方法-无缓
     * 兼容Map<>参数
     */
    public static void post(Context context, String url, Map<String, Object> map,Map<String, File> mapFile, TextListener listener) { 	
    	RequestParams params = new RequestParams();
    	for (Entry<String, Object> iterable_element : map.entrySet()) {
			String conte=iterable_element.getKey();
			String value=iterable_element.getValue().toString();
			params.put(conte, value);
		}
    	if(mapFile!=null&&mapFile.size()>0){
    		for (Entry<String, File> iterable_element : mapFile.entrySet()) {
        		String conte=iterable_element.getKey();
        		File value=iterable_element.getValue();
    			try {
    				params.put(conte, value);
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			}
    		}
    	}

    	post(context, url+versionAndSystem, params, listener);
    }
    
    public static void post(Context context, String url, Map<String, Object> map, TextListener listener) { 	
    	RequestParams params = new RequestParams();
    	for (Entry<String, Object> iterable_element : map.entrySet()) {
			String conte=iterable_element.getKey();
			String value=iterable_element.getValue().toString();
			params.put(conte, value);
		}	
    	post(context, url+versionAndSystem, params, listener);
    }

    
}
