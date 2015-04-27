package com.tx.customerservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import android.view.ViewGroup.LayoutParams;
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

import com.tx.customerservices.adapter.ChatAdapter;
import com.tx.customerservices.adapter.FaceAdapter;
import com.tx.customerservices.adapter.FacePageAdeapter;
import com.tx.customerservices.db.ChatProvider;
import com.tx.customerservices.db.ChatProviderHelper;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.umservice.MyApplication;
import com.tx.customerservices.umservice.swactivity.SwipeBackActivity;
import com.tx.customerservices.util.L;
import com.tx.customerservices.util.PreferenceConstants;
import com.tx.customerservices.util.PreferenceUtils;
import com.tx.customerservices.view.CirclePageIndicator;
import com.tx.customerservices.xlistview.MsgListView;
import com.tx.customerservices.xlistview.MsgListView.IXListViewListener;



public class ChatActivity extends SwipeBackActivity implements OnTouchListener,
OnClickListener, IXListViewListener{
	public static final String INTENT_EXTRA_USERNAME = ChatActivity.class
			.getName() + ".username";// �ǳƶ�Ӧ��key
	private MsgListView mMsgListView;// �Ի�ListView
	private ViewPager mFaceViewPager;// ����ѡ��ViewPager
	private int mCurrentPage = 0;// ��ǰ����ҳ
	private boolean mIsFaceShow = false;// �Ƿ���ʾ����
	private Button mSendMsgBtn;// ������Ϣbutton
	private ImageButton mFaceSwitchBtn;// �л����̺ͱ����button
	private TextView mTitleNameView;// ������
	private ImageView mTitleStatusView;
	private EditText mChatEditText;// ��Ϣ�����
	private LinearLayout mFaceRoot;// ���鸸����
	private WindowManager.LayoutParams mWindowNanagerParams;
	private InputMethodManager mInputMethodManager;
	private List<String> mFaceMapKeys;// �����Ӧ���ַ�������
	private String mWithJabberID = null;// ��ǰ�����û���ID
	private ContentObserver mContactObserver = new ContactObserver();// ��ϵ�����ݼ�������Ҫ�Ǽ����Է�����״̬
	
	public static final String INTENT_QUESTION_ID="question_id";
	public static final String INTENT_DOCTOR_ID="doctor_id";
	public static final String INTENT_KUAIZHENG_ID="kuaiZheng_id";
	public static final String INTENT_DOCTOR_NAME="doctor_name";
	private int intent_question_id=1;		
	private int intent_doctor_id=1;
	private int intent_kuaiZheng_id=1;
	private String intent_doctor_name="";
	private static final String[] PROJECTION_FROM = new String[] {
		ChatProvider.ChatConstants._ID, 
		ChatProvider.ChatConstants.DATE,
		ChatProvider.ChatConstants.DIRECTION,
		ChatProvider.ChatConstants.JID, 
		ChatProvider.ChatConstants.WHAT_NAME,
		ChatProvider.ChatConstants.MESSAGE, 
		ChatProvider.ChatConstants.DOCTOR_ID,
		ChatProvider.ChatConstants.TYPE_MESSAGE,
		ChatProvider.ChatConstants.QUESTION_ID,
		ChatProvider.ChatConstants.KUAIZHENG_ID,
		ChatProvider.ChatConstants.DELIVERY_STATUS };// ��ѯ�ֶ�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_chat);
		setContentLayout(R.layout.activity_chat);
		getIntentData();
		initData();// ��ʼ������
		initView();// ��ʼ��view
		initFacePage();// ��ʼ������
		setChatWindowAdapter();// ��ʼ���Ի�����
		hidebtn_right();
		getContentResolver().registerContentObserver(
				ChatProvider.CONTENT_URI, true, mContactObserver);// ��ʼ������ϵ�����ݿ�
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
		intent_doctor_id=getIntent().getExtras().getInt(INTENT_DOCTOR_ID,0);
		intent_kuaiZheng_id=getIntent().getExtras().getInt(INTENT_KUAIZHENG_ID,1);
		intent_doctor_name=getIntent().getExtras().getString(INTENT_DOCTOR_NAME,"");
		L.e("intent_question_id====="+intent_question_id+"===intent_doctor_id==="+intent_doctor_id+"====intent_kuaiZheng_id==="+intent_kuaiZheng_id+"===intent_doctor_name==="+intent_doctor_name);
	}

	private void initData() {
		//mWithJabberID = getIntent().getDataString().toLowerCase();// ��ȡ��������id
		// ������map��key������������
		Set<String> keySet = MyApplication.getInstance().getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);
	}
	private void initView() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mWindowNanagerParams = getWindow().getAttributes();

		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		// ����ListView���ر���       ���뷨
		mMsgListView.setOnTouchListener(this);
		mMsgListView.setPullLoadEnable(false);
		mMsgListView.setXListViewListener(this);
		mSendMsgBtn = (Button) findViewById(R.id.send);
		mFaceSwitchBtn = (ImageButton) findViewById(R.id.face_switch_btn);
		mChatEditText = (EditText) findViewById(R.id.input);
		mFaceRoot = (LinearLayout) findViewById(R.id.face_ll);
		mFaceViewPager = (ViewPager) findViewById(R.id.face_pager);
		mChatEditText.setOnTouchListener(this);
		mChatEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mWindowNanagerParams.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| mIsFaceShow) {
						mFaceRoot.setVisibility(View.GONE);
						mIsFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});
		mChatEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					mSendMsgBtn.setEnabled(true);
				} else {
					mSendMsgBtn.setEnabled(false);
				}
			}
		});
		mFaceSwitchBtn.setOnClickListener(this);
		mSendMsgBtn.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.face_switch_btn:
			if (!mIsFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						mChatEditText.getWindowToken(), 0);
				try {
					Thread.sleep(80);// �����ʱ���һ����Ļ������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mFaceRoot.setVisibility(View.VISIBLE);
				mFaceSwitchBtn.setImageResource(R.drawable.aio_keyboard);
				mIsFaceShow = true;
			} else {
				mFaceRoot.setVisibility(View.GONE);
				mInputMethodManager.showSoftInput(mChatEditText, 0);
				mFaceSwitchBtn
						.setImageResource(R.drawable.qzone_edit_face_drawable);
				mIsFaceShow = false;
			}
			break;
		case R.id.send:// ������Ϣ
			sendMessageIfNotNull();
			break;
		default:
			break;
		}
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.msg_listView:
			mInputMethodManager.hideSoftInputFromWindow(
					mChatEditText.getWindowToken(), 0);
			mFaceSwitchBtn
					.setImageResource(R.drawable.qzone_edit_face_drawable);
			mFaceRoot.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;
		case R.id.input:
			mInputMethodManager.showSoftInput(mChatEditText, 0);
			mFaceSwitchBtn
					.setImageResource(R.drawable.qzone_edit_face_drawable);
			mFaceRoot.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;

		default:
			break;
		}
		return false;
	}
	
	private void initFacePage() {
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < MyApplication.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv);
		mFaceViewPager.setAdapter(adapter);
		mFaceViewPager.setCurrentItem(mCurrentPage);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mFaceViewPager);
		adapter.notifyDataSetChanged();
		mFaceRoot.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				mCurrentPage = arg0;
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// ����GridViewĬ�ϵ��Ч��
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == MyApplication.NUM) {// ɾ������λ��
					int selection = mChatEditText.getSelectionStart();
					String text = mChatEditText.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mChatEditText.getText().delete(start, end);
							return;
						}
						mChatEditText.getText().delete(selection - 1, selection);
					}
				} else {
					int count = mCurrentPage * MyApplication.NUM + arg2;
					// ע�͵Ĳ��֣���EditText����ʾ�ַ���
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());
					// �����ⲿ�֣���EditText����ʾ����
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (Integer) MyApplication.getInstance().getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// ������������
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// �½�������
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// ����ͼƬ����ת�Ƕ�
						// matrix.postRotate(-30);
						// ����ͼƬ����б
						// matrix.postSkew(0.1f, 0.1f);
						// ��ͼƬ��Сѹ��
						// ѹ����ͼƬ�Ŀ�͸��Լ�kB��С����仯
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = mFaceMapKeys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						mChatEditText.append(spannableString);
					} else {
						String ori = mChatEditText.getText().toString();
						int index = mChatEditText.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, mFaceMapKeys.get(count));
						mChatEditText.setText(stringBuilder.toString());
						mChatEditText.setSelection(index
								+ mFaceMapKeys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	// ��ֹ��pageview�ҹ���
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

	public void onRefresh() {
		
	}

	public void onLoadMore() {
		
	}
	/**
	 * ��ϵ�����ݿ�仯����
	 * 
	 */
	private class ContactObserver extends ContentObserver {
		public ContactObserver() {
			super(new Handler());
		}

		public void onChange(boolean selfChange) {
			L.e("ContactObserver.onChange: " + selfChange);
			updateContactStatus();// ��ϵ��״̬�仯ʱ��ˢ�½���
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mContactObserver);
	}
	// ��ѯ��ϵ�����ݿ��ֶ�
	private static final String[] STATUS_QUERY = new String[] {
				ChatProvider.ChatConstants.WHAT_NAME,
				ChatProvider.ChatConstants.MESSAGE, 
				ChatProvider.ChatConstants.DATE,
				ChatProvider.ChatConstants.DOCTOR_ID,
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
	 * ���������Adapter
	 */
	private void setChatWindowAdapter() {		
		String selection = ChatConstants.QUESTION_ID + "='" + intent_question_id + "'";
		// �첽��ѯ���ݿ�
		new AsyncQueryHandler(getContentResolver()) {
			protected void onQueryComplete(int token, Object cookie,
					Cursor cursor) {
				// ListAdapter adapter = new ChatWindowAdapter(cursor,
				// PROJECTION_FROM, PROJECTION_TO, mWithJabberID);
				ListAdapter adapter = new ChatAdapter(ChatActivity.this,
						cursor, PROJECTION_FROM);
				mMsgListView.setAdapter(adapter);
				mMsgListView.setSelection(adapter.getCount() - 1);
			}
		}.startQuery(0, null, ChatProvider.CONTENT_URI, PROJECTION_FROM,
				selection, null, null);
	}
	
	private void sendMessageIfNotNull() {
		if (mChatEditText.getText().length() >= 1) {
			L.e("intent_kuaiZheng_id========="+intent_kuaiZheng_id);
			Uri uri= ChatProviderHelper.sendNolineMessage(getContentResolver(),
					PreferenceUtils.getPrefString(context,PreferenceConstants.ACCOUNT_ID, PreferenceConstants.DEFAULT_USER_ID), 
					mChatEditText.getText().toString(),
					"gyj",
					intent_question_id,
					intent_doctor_id,
					intent_kuaiZheng_id,
					ChatConstants.TYPE_MESSAGE_1);
			mChatEditText.setText(null);
			mSendMsgBtn.setEnabled(false);
		}
	}
}
