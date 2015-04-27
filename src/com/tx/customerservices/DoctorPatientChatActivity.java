package com.tx.customerservices;

import android.content.AsyncQueryHandler;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;

import com.tx.customerservices.adapter.ChatAdapter;
import com.tx.customerservices.adapter.DoctorPatientChatAdapter;
import com.tx.customerservices.db.ChatProvider;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.db.DoctorPatientChatProvider.DoctorPatientChatConstants;
import com.tx.customerservices.umservice.swactivity.SwipeBackActivity;
import com.tx.customerservices.util.L;
import com.tx.customerservices.xlistview.MsgListView;

public class DoctorPatientChatActivity extends SwipeBackActivity {
	private MsgListView msg_listView_doctor_patient_chat;
	private static final String[] PROJECTION_FROM = new String[] {
		DoctorPatientChatConstants._ID, 
		DoctorPatientChatConstants.DATE,
		DoctorPatientChatConstants.DIRECTION,
		DoctorPatientChatConstants.JID, 
		DoctorPatientChatConstants.WHAT_NAME,
		DoctorPatientChatConstants.MESSAGE, 
		DoctorPatientChatConstants.DOCTOR_ID,
		DoctorPatientChatConstants.TYPE_MESSAGE,
		DoctorPatientChatConstants.ORDER_ID,
		DoctorPatientChatConstants.PATIENT_ID,
		DoctorPatientChatConstants.DELIVERY_STATUS };// 查询字段
	public static final String INTENT_ORDER_ID="order_id";
	private int intent_order_id=1;		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_doctor_patient_chat);
		hidebtn_right();
		getbtn_left().setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		initView();
		getIntentData();
		setChatWindowAdapter();
	}
	private void initView() {
		msg_listView_doctor_patient_chat=(MsgListView)findViewById(R.id.msg_listView_doctor_patient_chat);
	}
	private void getIntentData(){
		intent_order_id=getIntent().getExtras().getInt(INTENT_ORDER_ID,0);
	}
	/**
	 * 设置聊天的Adapter
	 */
	private void setChatWindowAdapter() {		
		String selection = DoctorPatientChatConstants.ORDER_ID + "='" + intent_order_id + "'";
		// 异步查询数据库
		new AsyncQueryHandler(getContentResolver()) {
			protected void onQueryComplete(int token, Object cookie,
					Cursor cursor) {
				// ListAdapter adapter = new ChatWindowAdapter(cursor,
				// PROJECTION_FROM, PROJECTION_TO, mWithJabberID);
				ListAdapter adapter = new DoctorPatientChatAdapter(DoctorPatientChatActivity.this,
						cursor, PROJECTION_FROM);
				msg_listView_doctor_patient_chat.setAdapter(adapter);
				msg_listView_doctor_patient_chat.setSelection(adapter.getCount() - 1);
			}
		}.startQuery(0, null, ChatProvider.CONTENT_URI, PROJECTION_FROM,
				selection, null, null);
	}

}
