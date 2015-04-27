package com.tx.customerservices;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GroupFourActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_group_four);
		setTitle(R.string.title_myself);
		hidebtn_left();
		hidebtn_right();
		showTitleView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_four, menu);
		return true;
	}

}
