package com.tx.customerservices.http;

import java.util.Calendar;

import org.apache.http.Header;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tx.customerservices.util.L;

public class TextListener extends TextHttpResponseHandler {
	private String TAG="TextListener";
    private Context context;
    private RequestHandle requestHandle;
    private String url;
    private boolean isCache = false;
    private String method = "GET";
    
    public void setMethod(String method) {
    	this.method = method;
    }
    
    public void setCache(boolean isCache) {
    	this.isCache = isCache;
    }
    
    public void setUrl(String url) {
    	this.url = url;
    }
    
    public TextListener(Context context) {
        this.context = context;
    }
    
    protected Context getContext() {
        return context;
    }

    public RequestHandle getRequestHandle() {
        return requestHandle;
    }
    
	public void setRequestHandle(RequestHandle requestHandle) {		
	}

	@Override
	public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		L.e("TAG","===statusCode==="+statusCode+"===responseString==="+responseString+"===throwable==="+throwable.toString());
		if (method.equals("GET")) {
			SharedPreferences sp = context.getSharedPreferences(url, Context.MODE_PRIVATE);
			onSuccess(200, null, sp.getString("text", ""));
		} else {
			
			onSuccess(200, null, "{\"success\":false}");
		}
		//onFailure(throwable);
	}

	@Override
	public final void onSuccess(int statusCode, Header[] headers, String responseString) {
		L.e("TAG","====responseString===="+responseString);
		if (!isCache) { //添加缓存
			Editor editor = context.getSharedPreferences(url, Context.MODE_PRIVATE).edit();
			editor.putLong("update_time", Calendar.getInstance().getTimeInMillis());
			editor.putString("text", responseString);
			editor.commit();
		}
		onSuccess(responseString);
	}

    public void onSuccess(String responseString) {
    }

    public void onFailure(Throwable throwable) {
    }

}
