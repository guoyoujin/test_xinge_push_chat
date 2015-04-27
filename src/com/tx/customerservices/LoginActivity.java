package com.tx.customerservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.tx.customerservices.http.DialogTextListener;
import com.tx.customerservices.http.HttpUtil;
import com.tx.customerservices.util.PreferenceConstants;
import com.tx.customerservices.util.PreferenceUtils;
import com.tx.customerservices.util.StringUtils;
import com.tx.customerservices.util.T;

public class LoginActivity extends Activity {
	
	private EditText edt_account_input,edt_password;
	private CheckBox chb_auto_save_password,chb_silence_login;
	private boolean isLogin=false;
	private boolean  isSilenceLogin=false;
	private String mAccount;
	private String mPassword;
	private boolean isAutoSavePassword = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				saveAccount();
				intentActivity();
				break;
			case 1:
				
				break;
			case 2:
				
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 初始化控件
	 */
	public void initView(){
		edt_account_input=(EditText)findViewById(R.id.edt_account_input);
		edt_password=(EditText)findViewById(R.id.edt_password);
		chb_auto_save_password=(CheckBox)findViewById(R.id.chb_auto_save_password);
		chb_silence_login=(CheckBox) findViewById(R.id.chb_silence_login);
	}
	
	/**
	 * 登录成功跳转到相应的页面
	 */
	public void intentActivity(){
		Intent intent=new Intent(getApplicationContext(),MainActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 点击登录
	 * @param view
	 */
	public void onLoginClick(View view){		
		text_is_null();
	}
	
	/**
	 * 判断输入是否合法
	 * 
	 */
	public void text_is_null(){
		mAccount=edt_account_input.getText().toString().trim();
		mPassword=edt_password.getText().toString().trim();
		if (StringUtils.isBlank(mAccount)){
			T.showLong(this, R.string.user_name_is_null);
			return;
		}
		if(StringUtils.isBlank(mPassword)){
			T.showShort(this, R.string.password_input_prompt);
			return;
		}
		onLogin();
	}
	
	/**
	 * 
	 * 登录请求，向handler发送请求结果
	 */
	public void onLogin(){
		Map<String,String>map=new HashMap<String, String>();
		map.put("username", mAccount);
		map.put("password", mPassword);
		HttpUtil.get(this, HttpUtil.LOGIN_IN, map, new DialogTextListener(this) {
			public void onSuccess(String responseString) {
				super.onSuccess(responseString);
				Message msg=new Message();
				msg.what=1;
				msg.obj=responseString;
				handler.sendMessage(msg);
			}
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBytes, Throwable throwable) {
				super.onFailure(statusCode, headers, responseBytes, throwable);
				Message msg=new Message();
				msg.what=0;
				msg.obj=statusCode;
				handler.sendMessage(msg);
				
			}
		});	
	}
	
	/**
	 * 
	 * 登录成功保存用户的账户信息
	 */
	public void saveAccount(){
		isAutoSavePassword=chb_auto_save_password.isChecked();
		isSilenceLogin=chb_silence_login.isChecked();
		PreferenceUtils.setPrefBoolean(this, PreferenceConstants.SCLIENTNOTIFY,
				isSilenceLogin);
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
				mAccount);// 帐号是一直保存的
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT_ID,
				PreferenceConstants.DEFAULT_USER_ID);// 帐号是一直保存的
		if(isLogin&&isAutoSavePassword){
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
					mPassword);
		}else{
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
					"");
		}
	}

}
