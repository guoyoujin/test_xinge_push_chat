package com.tx.customerservices;


import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tx.customerservices.umservice.MyPushIntentService;
import com.tx.customerservices.util.L;


@SuppressWarnings({ "deprecation", "deprecation" })
public class MainActivity extends ActivityGroup implements OnCheckedChangeListener {
	private static final String ONE = GroupOneActivity.class.getName();
	private static final String TWO = GroupTwoActivity.class.getName();
	private static final String THREE = GroupThreeActivity.class.getName();
	private static final String FOUR = GroupFourActivity.class.getName();
	public static final String INTENT_EXTRA_USERNAME = MainActivity.class
			.getName() + ".username";// êÇ³Æ¶ÔÓ¦µÄkey
	private ActivityGroupManager manager = null;
	private FrameLayout container = null;
	private RadioGroup radioGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		
	}
	
	
	private void initView() {
		radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(this);
		manager = new ActivityGroupManager();
		container = (FrameLayout) findViewById(R.id.container);
		manager.setContainer(container);
		switchActivity(ActivityGroupManager.RECORD_ACTIVITY_VIEW, ONE, GroupOneActivity.class);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		L.e(checkedId+"");
		switch (checkedId) {		
		case R.id.record_button:
			switchActivity(ActivityGroupManager.RECORD_ACTIVITY_VIEW, ONE, GroupOneActivity.class);
			break;
		case R.id.category_button:
			switchActivity(ActivityGroupManager.CATEGORY_ACTIVITY_VIEW, TWO, GroupTwoActivity.class);
			break;
		case R.id.more_button:
			switchActivity(ActivityGroupManager.MORE_AVTIVITY_VIEW, THREE, GroupThreeActivity.class);
			break;			
		case R.id.four_button:
			switchActivity(ActivityGroupManager.FOUR_AVTIVITY_VIEW, FOUR, GroupFourActivity.class);
			break;
		default:
			break;
		}
	}
	
	private View getActivityView(String activityName, Class<?> activityClass) {
		return getLocalActivityManager().startActivity(activityName,new Intent(MainActivity.this, activityClass)).getDecorView();
	}

	private void switchActivity(int num, String activityName, Class<?> activityClass) {
		manager.showContainer(num, getActivityView(activityName, activityClass));
	}

}
