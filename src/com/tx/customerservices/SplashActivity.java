package com.tx.customerservices;

import com.tx.customerservices.util.PreferenceConstants;
import com.tx.customerservices.util.PreferenceUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

public class SplashActivity extends Activity {
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mHandler = new Handler();
		String password = PreferenceUtils.getPrefString(this,
				PreferenceConstants.DEFAULT_USER_PASSWORD, "");
		if (!TextUtils.isEmpty(password)) {
			mHandler.postDelayed(gotoMainAct, 2000);
		} else {
			mHandler.postDelayed(gotoLoginAct, 2000);
		}
	}

	Runnable gotoLoginAct = new Runnable() {

		@Override
		public void run() {
			startActivity(new Intent(SplashActivity.this, LoginActivity.class));
			finish();
		}
	};

	Runnable gotoMainAct = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	};

}
