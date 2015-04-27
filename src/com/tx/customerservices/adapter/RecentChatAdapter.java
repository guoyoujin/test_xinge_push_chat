package com.tx.customerservices.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.tx.customerservices.R;
import com.tx.customerservices.db.ChatProvider;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.db.ChatProviderHelper;
import com.tx.customerservices.util.L;
import com.tx.customerservices.util.StringUtils;
import com.tx.customerservices.util.TimeUtil;
import com.tx.customerservices.view.CustomDialog;

public class RecentChatAdapter extends SimpleCursorAdapter {
	private static final String SELECT = ChatConstants.DATE
			+ " in (select max(" + ChatConstants.DATE + ") from "
			+ ChatProvider.TABLE_NAME + " group by " + ChatConstants.QUESTION_ID
			+ " having count(*)>0)";// 查询合并重复jid字段的所有聊天对象
	private static final String[] FROM = new String[] {
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
					ChatProvider.ChatConstants.DELIVERY_STATUS };// 查询字段
	private static final String SORT_ORDER = ChatConstants.DATE + " DESC";
	private ContentResolver mContentResolver;
	private LayoutInflater mLayoutInflater;
	private Activity mContext;

	public RecentChatAdapter(Activity context) {
		super(context, 0, null, FROM, null);
		mContext = context;
		mContentResolver = context.getContentResolver();
		mLayoutInflater = LayoutInflater.from(context);
	}

	public void requery() {
		Cursor cursor = mContentResolver.query(ChatProvider.CONTENT_URI, FROM,SELECT, null, SORT_ORDER);
		Cursor oldCursor = getCursor();
		changeCursor(cursor);
		mContext.stopManagingCursor(oldCursor);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cursor cursor = this.getCursor();
		cursor.moveToPosition(position);
		long dateMilliseconds = cursor.getLong(cursor.getColumnIndex(ChatProvider.ChatConstants.DATE));
		String date = TimeUtil.getChatTime(dateMilliseconds);
		String message = cursor.getString(cursor.getColumnIndex(ChatProvider.ChatConstants.MESSAGE));
		String jid = cursor.getString(cursor.getColumnIndex(ChatProvider.ChatConstants.JID));
		String question_id=cursor.getInt(cursor.getColumnIndex(ChatProvider.ChatConstants.QUESTION_ID))+"";
		//String selection =ChatConstants.DELIVERY_STATUS + " = " + ChatConstants.DS_NEW;// 新消息数量字段
		String selection = ChatConstants.QUESTION_ID + " = '" + question_id + "' AND "
				+ ChatConstants.DIRECTION + " = " + ChatConstants.INCOMING
				+ " AND " + ChatConstants.DELIVERY_STATUS + " = "
				+ ChatConstants.DS_NEW;// 新消息数量字段
		Cursor msgcursor = mContentResolver.query(
						ChatProvider.CONTENT_URI,
						new String[] { 
						ChatConstants.DATE, ChatConstants.MESSAGE }, 
						selection,
						null, 
						SORT_ORDER);
		msgcursor.moveToFirst();
		int count = msgcursor.getCount();
		L.e("=======msgcursor========"+count);
		ViewHolder viewHolder;
		if (convertView == null|| convertView.getTag(R.drawable.ic_launcher+ (int) dateMilliseconds) == null) {
			convertView = mLayoutInflater.inflate(R.layout.recent_listview_item, parent, false);
			viewHolder = buildHolder(convertView, question_id);
			convertView.setTag(R.drawable.ic_launcher + (int) dateMilliseconds,viewHolder);
			convertView.setTag(R.string.app_name, R.drawable.ic_launcher+ (int) dateMilliseconds);
		} else {
			viewHolder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher+ (int) dateMilliseconds);
		}
		viewHolder.jidView.setText(question_id);
		if(!StringUtils.isBlank(message)){
			viewHolder.msgView.setText(ChatProviderHelper.convertNormalStringToSpannableString(mContext, message, false));
		}else{
			viewHolder.msgView.setText("null");
		}
		viewHolder.dataView.setText(date);
		if (msgcursor.getCount() > 0) {
			viewHolder.msgView.setText(msgcursor.getString(msgcursor.getColumnIndex(ChatConstants.MESSAGE)));
			viewHolder.dataView.setText(TimeUtil.getChatTime(msgcursor.getLong(msgcursor.getColumnIndex(ChatConstants.DATE))));
			viewHolder.unReadView.setText(msgcursor.getCount()+"");
		}
		viewHolder.unReadView.setVisibility(count > 0 ? View.VISIBLE: View.GONE);
		viewHolder.unReadView.bringToFront();
		msgcursor.close();

		return convertView;
	}

	private ViewHolder buildHolder(View convertView, final String question_id) {
		ViewHolder holder = new ViewHolder();
		holder.jidView = (TextView) convertView
				.findViewById(R.id.recent_list_item_name);
		holder.dataView = (TextView) convertView
				.findViewById(R.id.recent_list_item_time);
		holder.msgView = (TextView) convertView
				.findViewById(R.id.recent_list_item_msg);
		holder.unReadView = (TextView) convertView.findViewById(R.id.unreadmsg);
		holder.deleteBtn = (Button) convertView
				.findViewById(R.id.recent_del_btn);
		holder.deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				removeChatHistoryDialog(question_id, question_id);
			}
		});
		return holder;
	}

	private static class ViewHolder {
		TextView jidView;
		TextView dataView;
		TextView msgView;
		TextView unReadView;
		Button deleteBtn;
	}

	void removeChatHistory(final String JID) {
		mContentResolver.delete(ChatProvider.CONTENT_URI,ChatProvider.ChatConstants.QUESTION_ID + " = ?", new String[] { JID });
	}

	void removeChatHistoryDialog(final String JID, final String userName) {
		new CustomDialog.Builder(mContext)
				.setTitle(R.string.deleteChatHistory_title)
				.setMessage(
						mContext.getString(R.string.deleteChatHistory_text,
								userName, JID))
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								removeChatHistory(JID);
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create().show();
	}
}
