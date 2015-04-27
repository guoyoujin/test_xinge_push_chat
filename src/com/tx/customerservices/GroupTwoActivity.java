package com.tx.customerservices;



import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tx.customerservices.adapter.RecentChatAdapter;
import com.tx.customerservices.db.ChatProvider;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.swipelistview.BaseSwipeListViewListener;
import com.tx.customerservices.swipelistview.SwipeListView;
import com.tx.customerservices.util.L;

public class GroupTwoActivity extends BaseActivity implements OnClickListener {
	private Handler mainHandler = new Handler();
	private ContentObserver mChatObserver = new ChatObserver();
	private ContentResolver mContentResolver;
	private SwipeListView mSwipeListView;
	private RecentChatAdapter mRecentChatAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_group_two);
		setTitle(R.string.title_doctor);
		mContentResolver = getApplication().getContentResolver();
		mRecentChatAdapter = new RecentChatAdapter(GroupTwoActivity.this);
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
			int question_id = clickCursor.getInt(clickCursor.getColumnIndex(ChatConstants.QUESTION_ID));
			int doctor_id=clickCursor.getInt(clickCursor.getColumnIndex(ChatConstants.DOCTOR_ID));
			int kuai_zheng=clickCursor.getInt(clickCursor.getColumnIndex(ChatConstants.KUAIZHENG_ID));
			if(kuai_zheng==0){
				kuai_zheng=1;
			}
			String doctor_name=clickCursor.getString(clickCursor.getColumnIndex(ChatConstants.WHAT_NAME));
			//Uri userNameUri = Uri.parse(question_id);
			Intent toChatIntent = new Intent(getApplicationContext(), ChatActivity.class);
			//toChatIntent.setData(userNameUri);
			L.e("question_id======"+question_id+"=====doctor_id===="+doctor_id+"======kuai_zheng======="+kuai_zheng+"====doctor_name===="+doctor_name);
			toChatIntent.putExtra(ChatActivity.INTENT_QUESTION_ID,question_id);
			toChatIntent.putExtra(ChatActivity.INTENT_DOCTOR_ID,doctor_id);
			toChatIntent.putExtra(ChatActivity.INTENT_KUAIZHENG_ID,kuai_zheng);
			toChatIntent.putExtra(ChatActivity.INTENT_DOCTOR_NAME,doctor_name);
			L.e("INTENT_QUESTION_ID======"+toChatIntent.getIntExtra(ChatActivity.INTENT_QUESTION_ID, 0));
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
				.findViewById(R.id.recent_listview);
		mSwipeListView.setEmptyView(this.findViewById(R.id.recent_empty));
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
		mContentResolver.registerContentObserver(ChatProvider.CONTENT_URI,true, mChatObserver);
	}

	public void onPause() {
		super.onPause();
		mContentResolver.unregisterContentObserver(mChatObserver);
	}

}
