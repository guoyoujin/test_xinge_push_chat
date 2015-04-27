package com.tx.customerservices;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.tx.customerservices.adapter.RecentChatAdapter;
import com.tx.customerservices.adapter.RecentPatientChatAdapter;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.db.PatientChatProvider;
import com.tx.customerservices.swipelistview.BaseSwipeListViewListener;
import com.tx.customerservices.swipelistview.SwipeListView;
import com.tx.customerservices.util.L;

public class GroupThreeActivity extends BaseActivity implements OnClickListener {
	private Handler mainHandler = new Handler();
	private ContentObserver mChatObserver = new ChatObserver();
	private ContentResolver mContentResolver;
	private SwipeListView mSwipeListView;
	private RecentPatientChatAdapter mRecentChatAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_group_three);
		setTitle(R.string.title_patient);
		mContentResolver = getApplication().getContentResolver();
		mRecentChatAdapter = new RecentPatientChatAdapter(GroupThreeActivity.this);
		initView();
		hidebtn_left();
		hidebtn_right();
		showTitleView();

	}
	
	private class ChatObserver extends ContentObserver {
		public ChatObserver() {
			super(mainHandler);
		}

		public void onChange(boolean selfChange) {
			updateRoster();
			L.e("liweiping", "selfChange" + selfChange);
		}
	}

	
	//	intent_question_id=getIntent().getExtras().getInt("question_id",0);
	//	intent_doctor_id=getIntent().getExtras().getInt("doctor_id",0);
	//	intent_kuaiZheng_id=getIntent().getExtras().getInt("kuaiZheng_id",0);
	//	intent_doctor_name=getIntent().getExtras().getString("doctor_name","");
	BaseSwipeListViewListener mSwipeListViewListener = new BaseSwipeListViewListener() {
		public void onClickFrontView(int position) {
			Cursor clickCursor = mRecentChatAdapter.getCursor();
			clickCursor.moveToPosition(position);			
			int question_id = clickCursor.getInt(clickCursor.getColumnIndex(PatientChatProvider.PatientChatConstants.QUESTION_ID));
			int patient_id=clickCursor.getInt(clickCursor.getColumnIndex(PatientChatProvider.PatientChatConstants.PATIENT_ID));
			int kuai_zheng=clickCursor.getInt(clickCursor.getColumnIndex(PatientChatProvider.PatientChatConstants.KUAIZHENG_ID));
			String patient_name=clickCursor.getString(clickCursor.getColumnIndex(PatientChatProvider.PatientChatConstants.WHAT_NAME));	
			if(kuai_zheng==0){
				kuai_zheng=1;
			}
		
			Intent toChatIntent = new Intent(getApplicationContext(), PatientChatActivity.class);
			L.e("question_id======"+question_id+"=====patient_id===="+patient_id+"======kuai_zheng======="+kuai_zheng+"====patient_name===="+patient_name);
			toChatIntent.putExtra(PatientChatActivity.INTENT_QUESTION_ID,question_id);
			toChatIntent.putExtra(PatientChatActivity.INTENT_PATIENT_ID,patient_id);
			toChatIntent.putExtra(PatientChatActivity.INTENT_KUAIZHENG_ID,kuai_zheng);
			toChatIntent.putExtra(PatientChatActivity.INTENT_PATIENT_NAME,patient_name);
			L.e("INTENT_QUESTION_ID======"+toChatIntent.getIntExtra(PatientChatActivity.INTENT_QUESTION_ID, 0));
			L.e("INTENT_PATIENT_ID======"+toChatIntent.getIntExtra(PatientChatActivity.INTENT_PATIENT_ID, 0));
			L.e("INTENT_KUAIZHENG_ID======"+toChatIntent.getIntExtra(PatientChatActivity.INTENT_KUAIZHENG_ID, 0));
			L.e("INTENT_PATIENT_NAME======"+toChatIntent.getIntExtra(PatientChatActivity.INTENT_QUESTION_ID, 0));
			startActivity(toChatIntent);
		}
		public void onClickBackView(int position) {
			mSwipeListView.closeOpenedItems();// 关闭打开的项
		}
	};
	public void updateRoster() {
		mRecentChatAdapter.requery();
	}
	private void initView() {
		mSwipeListView = (SwipeListView) this
				.findViewById(R.id.recent_listview_chat_patient);
		mSwipeListView.setEmptyView(this.findViewById(R.id.recent_listview_chat_patient));
		mSwipeListView.setAdapter(mRecentChatAdapter);
		mSwipeListView.setSwipeListViewListener(mSwipeListViewListener);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnRightImage:
		default:
			break;
		}
	}
	
	public void onResume() {
		super.onResume();
		mRecentChatAdapter.requery();
		mContentResolver.registerContentObserver(PatientChatProvider.CONTENT_URI,true, mChatObserver);
	}

	public void onPause() {
		super.onPause();
		mContentResolver.unregisterContentObserver(mChatObserver);
	}

}
