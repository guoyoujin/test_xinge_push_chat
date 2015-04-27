package com.tx.customerservices.http;

import java.io.File;
import java.io.FileNotFoundException;

import com.loopj.android.http.RequestParams;

public class Example {
	
	/**
	 * 文件上传示例
	 * @throws FileNotFoundException 
	 */
	public void test() throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("theFile", new File("path"));
		HttpUtil.post(null, "url", params, new TextListener(null) {

			@Override
			public void onSuccess(String responseString) {
				//成功逻辑
			}

			@Override
			public void onFailure(Throwable throwable) {
				//失败逻辑
			}

		});
	}

}
