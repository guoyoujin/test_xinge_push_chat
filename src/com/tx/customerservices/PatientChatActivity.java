package com.tx.customerservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ActionBar.LayoutParams;
import android.content.AsyncQueryHandler;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.tx.customerservices.adapter.FaceAdapter;
import com.tx.customerservices.adapter.FacePageAdeapter;
import com.tx.customerservices.adapter.PatientChatAdapter;
import com.tx.customerservices.db.ChatProvider;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.db.ChatProviderHelper;
import com.tx.customerservices.db.PatientChatProvider;
import com.tx.customerservices.db.PatientChatProvider.PatientChatConstants;
import com.tx.customerservices.db.PatientChatProviderHelper;
import com.tx.customerservices.umservice.MyApplication;
import com.tx.customerservices.umservice.swactivity.SwipeBackActivity;
import com.tx.customerservices.util.L;
import com.tx.customerservices.util.PreferenceConstants;
import com.tx.customerservices.util.PreferenceUtils;
import com.tx.customerservices.view.CirclePageIndicator;
import com.tx.customerservices.xlistview.MsgListView;
import com.tx.customerservices.xlistview.MsgListView.IXListViewListener;


public class PatientChatActivity extends SwipeBackActivity implements OnTouchListener,
OnClickListener, IXListViewListener{
	public static final String INTENT_EXTRA_USERNAME = PatientChatActivity.class
			.getName() + ".username";// 昵称对应的key
	private MsgListView mMsgListView_patient_chat;// 对话ListView
	private ViewPager mFaceViewPager_patient_chat;// 表情选择ViewPager
	private int mCurrentPage = 0;// 当前表情页
	private boolean mIsFaceShow = false;// 是否显示表情
	private Button mSendMsgBtn_patient_chat;// 发送消息button
	private ImageButton mFaceSwitchBtn_patient_chat;// 切换键盘和表情的button
	private TextView mTitleNameView_patient_chat;// 标题栏
	private ImageView mTitleStatusView_patient_chat;
	private EditText mChatEditText_patient_chat;// 消息输入框
	private LinearLayout mFaceRoot_patient_chat;// 表情父容器
	private WindowManager.LayoutParams mWindowNanagerParams;
	private InputMethodManager mInputMethodManager;
	private List<String> mFaceMapKeys;// 表情对应的字符串数组
	private String mWithJabberID = null;// 当前聊天用户的ID
	private ContentObserver mContactObserver = new ContactObserver();// 联系人数据监听，主要是监听对方在线状态

	public static final String INTENT_QUESTION_ID="question_id";
	public static final String INTENT_PATIENT_ID="patient_id";
	public static final String INTENT_KUAIZHENG_ID="kuaiZheng_id";
	public static final String INTENT_PATIENT_NAME="patient_name";
			
	private int intent_patient_id=1;
	private int intent_kuaiZheng_id=1;
	private String intent_patient_name="";
	private int intent_question_id=1;
	
	private static final String[] PROJECTION_FROM = new String[] {
		PatientChatProvider.PatientChatConstants._ID, 
		PatientChatProvider.PatientChatConstants.DATE,
		PatientChatProvider.PatientChatConstants.DIRECTION,
		PatientChatProvider.PatientChatConstants.JID, 
		PatientChatProvider.PatientChatConstants.WHAT_NAME,
		PatientChatProvider.PatientChatConstants.MESSAGE, 
		PatientChatProvider.PatientChatConstants.PATIENT_ID,
		PatientChatProvider.PatientChatConstants.TYPE_MESSAGE,
		PatientChatProvider.PatientChatConstants.QUESTION_ID,
		PatientChatProvider.PatientChatConstants.KUAIZHENG_ID,
		PatientChatProvider.PatientChatConstants.DELIVERY_STATUS };// 查询字段
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_chat);
		setContentLayout(R.layout.activity_patient_chat);
		getIntentData();
		initData();// 初始化数据
		initView();// 初始化view
		initFacePage();// 初始化表情
		setChatWindowAdapter();// 初始化对话数据
		hidebtn_right();
		getContentResolver().registerContentObserver(
				PatientChatProvider.CONTENT_URI, true, mContactObserver);// 开始监听联系人数据库
		getbtn_left().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void getIntentData(){
		intent_question_id=getIntent().getExtras().getInt(INTENT_QUESTION_ID,0);
		intent_patient_id=getIntent().getExtras().getInt(INTENT_PATIENT_ID,0);
		intent_kuaiZheng_id=getIntent().getExtras().getInt(INTENT_KUAIZHENG_ID,1);
		intent_patient_name=getIntent().getExtras().getString(INTENT_PATIENT_NAME,"");
		L.e("intent_question_id====="+intent_question_id+"===intent_patient_id==="+intent_patient_id+"====intent_kuaiZheng_id==="+intent_kuaiZheng_id+"===intent_doctor_name==="+intent_patient_name);
	}

	private void initData() {
		//mWithJabberID = getIntent().getDataString().toLowerCase();// 获取聊天对象的id
		// 将表情map的key保存在数组中
		Set<String> keySet = MyApplication.getInstance().getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);
	}
	private void initView() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mWindowNanagerParams = getWindow().getAttributes();

		mMsgListView_patient_chat = (MsgListView) findViewById(R.id.msg_listView_patient_chat);
		// 触摸ListView隐藏表情       输入法
		mMsgListView_patient_chat.setOnTouchListener(this);
		mMsgListView_patient_chat.setPullLoadEnable(false);
		mMsgListView_patient_chat.setXListViewListener(this);
		mSendMsgBtn_patient_chat = (Button) findViewById(R.id.send_patient_chat);
		mFaceSwitchBtn_patient_chat = (ImageButton) findViewById(R.id.face_switch_btn_patient_chat);
		mChatEditText_patient_chat = (EditText) findViewById(R.id.input_patient_chat);
		mFaceRoot_patient_chat = (LinearLayout) findViewById(R.id.face_ll_patient_chat);
		mFaceViewPager_patient_chat = (ViewPager) findViewById(R.id.face_pager_patient_chat);
		mChatEditText_patient_chat.setOnTouchListener(this);
		mChatEditText_patient_chat.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mWindowNanagerParams.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| mIsFaceShow) {
						mFaceRoot_patient_chat.setVisibility(View.GONE);
						mIsFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});
		mChatEditText_patient_chat.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					mSendMsgBtn_patient_chat.setEnabled(true);
				} else {
					mSendMsgBtn_patient_chat.setEnabled(false);
				}
			}
		});
		mFaceSwitchBtn_patient_chat.setOnClickListener(this);
		mSendMsgBtn_patient_chat.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.face_switch_btn_patient_chat:
			if (!mIsFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						mChatEditText_patient_chat.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mFaceRoot_patient_chat.setVisibility(View.VISIBLE);
				mFaceSwitchBtn_patient_chat.setImageResource(R.drawable.aio_keyboard);
				mIsFaceShow = true;
			} else {
				mFaceRoot_patient_chat.setVisibility(View.GONE);
				mInputMethodManager.showSoftInput(mChatEditText_patient_chat, 0);
				mFaceSwitchBtn_patient_chat
						.setImageResource(R.drawable.qzone_edit_face_drawable);
				mIsFaceShow = false;
			}
			break;
		case R.id.send_patient_chat:// 发送消息
			sendMessageIfNotNull();
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.msg_listView_patient_chat:
			mInputMethodManager.hideSoftInputFromWindow(
					mChatEditText_patient_chat.getWindowToken(), 0);
			mFaceSwitchBtn_patient_chat
					.setImageResource(R.drawable.qzone_edit_face_drawable);
			mFaceRoot_patient_chat.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;
		case R.id.input_patient_chat:
			mInputMethodManager.showSoftInput(mChatEditText_patient_chat, 0);
			mFaceSwitchBtn_patient_chat
					.setImageResource(R.drawable.qzone_edit_face_drawable);
			mFaceRoot_patient_chat.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;

		default:
			break;
		}
		return false;
	}
	
	private void initFacePage() {
		// TODO Auto-generated method stub
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < MyApplication.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv);
		mFaceViewPager_patient_chat.setAdapter(adapter);
		mFaceViewPager_patient_chat.setCurrentItem(mCurrentPage);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator_patient_chat);
		indicator.setViewPager(mFaceViewPager_patient_chat);
		adapter.notifyDataSetChanged();
		mFaceRoot_patient_chat.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurrentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	private GridView getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == MyApplication.NUM) {// 删除键的位置
					int selection = mChatEditText_patient_chat.getSelectionStart();
					String text = mChatEditText_patient_chat.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mChatEditText_patient_chat.getText().delete(start, end);
							return;
						}
						mChatEditText_patient_chat.getText()
								.delete(selection - 1, selection);
					}
				} else {
					int count = mCurrentPage * MyApplication.NUM + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) MyApplication.getInstance()
									.getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(PatientChatActivity.this,
								newBitmap);
						String emojiStr = mFaceMapKeys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						mChatEditText_patient_chat.append(spannableString);
					} else {
						String ori = mChatEditText_patient_chat.getText().toString();
						int index = mChatEditText_patient_chat.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, mFaceMapKeys.get(count));
						mChatEditText_patient_chat.setText(stringBuilder.toString());
						mChatEditText_patient_chat.setSelection(index
								+ mFaceMapKeys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 联系人数据库变化监听
	 * 
	 */
	private class ContactObserver extends ContentObserver {
		public ContactObserver() {
			super(new Handler());
		}

		public void onChange(boolean selfChange) {
			L.e("ContactObserver.onChange: " + selfChange);
			updateContactStatus();// 联系人状态变化时，刷新界面
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mContactObserver);
	}
	// 查询联系人数据库字段
	private static final String[] STATUS_QUERY = new String[] {
				ChatProvider.ChatConstants.WHAT_NAME,
				ChatProvider.ChatConstants.MESSAGE, 
				ChatProvider.ChatConstants.DATE,
				ChatProvider.ChatConstants.PACKET_ID,
				ChatProvider.ChatConstants.TYPE_MESSAGE,
				ChatProvider.ChatConstants.QUESTION_ID,
				ChatProvider.ChatConstants.KUAIZHENG_ID};
	private void updateContactStatus() {
		Cursor cursor = getContentResolver().query(ChatProvider.CONTENT_URI,
				STATUS_QUERY, null,null, null);
		int MODE_IDX = cursor
				.getColumnIndex(ChatProvider.ChatConstants.MESSAGE);
		int MSG_IDX = cursor
				.getColumnIndex(ChatProvider.ChatConstants.DATE);

		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			int status_mode = cursor.getInt(MODE_IDX);
			String status_message = cursor.getString(MSG_IDX);
			L.e("contact status changed: " + status_mode + " " + status_message);
		}
		cursor.close();
	}
	/**
	 * 设置聊天的Adapter
	 */
	private void setChatWindowAdapter() {
		
		String selection = PatientChatConstants.QUESTION_ID + "='" + intent_question_id + "'";
		// 异步查询数据库
		new AsyncQueryHandler(getContentResolver()) {

			@Override
			protected void onQueryComplete(int token, Object cookie,
					Cursor cursor) {
				// ListAdapter adapter = new ChatWindowAdapter(cursor,
				// PROJECTION_FROM, PROJECTION_TO, mWithJabberID);
				ListAdapter adapter = new PatientChatAdapter(PatientChatActivity.this,
						cursor, PROJECTION_FROM);
				mMsgListView_patient_chat.setAdapter(adapter);
				mMsgListView_patient_chat.setSelection(adapter.getCount() - 1);
			}

		}.startQuery(0, null, PatientChatProvider.CONTENT_URI, PROJECTION_FROM,
				selection, null, null);
	}
	
	private void sendMessageIfNotNull() {
		if (mChatEditText_patient_chat.getText().length() >= 1) {
			L.e("intent_kuaiZheng_id========="+intent_kuaiZheng_id);
			Uri uri= PatientChatProviderHelper.sendNolineMessage(getContentResolver(),
					PreferenceUtils.getPrefString(context,PreferenceConstants.ACCOUNT_ID, PreferenceConstants.DEFAULT_USER_ID), 
					mChatEditText_patient_chat.getText().toString(),
					"gyj",
					intent_question_id,
					intent_patient_id,
					intent_kuaiZheng_id,
					ChatConstants.TYPE_MESSAGE_1);
			mChatEditText_patient_chat.setText(null);
			mSendMsgBtn_patient_chat.setEnabled(false);
		}
	}

}
