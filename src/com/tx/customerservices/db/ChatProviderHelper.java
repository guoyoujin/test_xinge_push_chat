package com.tx.customerservices.db;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import com.tx.customerservices.db.ChatProvider.ChatConstants;
import com.tx.customerservices.umservice.MyApplication;
import com.tx.customerservices.util.L;




public class ChatProviderHelper {
	private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	public static Uri sendOfflineMessage(ContentResolver cr, 
			String toJID,
			String message,
			String whatName,
			int question_id,
			int doctor_id,
			int kuaiZheng_id,
			int type_message) {
		L.e("sendOfflineMessage=========ChatProviderHelper=======");
		ContentValues values = new ContentValues();
		values.put(ChatConstants.DIRECTION, ChatConstants.INCOMING);
		values.put(ChatConstants.JID, toJID);
		values.put(ChatConstants.MESSAGE, message);
		values.put(ChatConstants.DELIVERY_STATUS, ChatConstants.DS_NEW);
		values.put(ChatConstants.DATE, System.currentTimeMillis());
		values.put(ChatConstants.WHAT_NAME, whatName);
		values.put(ChatConstants.QUESTION_ID, question_id);
		values.put(ChatConstants.DOCTOR_ID, doctor_id);
		values.put(ChatConstants.KUAIZHENG_ID, kuaiZheng_id);
		values.put(ChatConstants.TYPE_MESSAGE, type_message);
		Uri uri=cr.insert(ChatProvider.CONTENT_URI, values);
		L.e("uri================"+uri);
		return uri;
	}
	
	public static Uri sendNolineMessage(ContentResolver cr, 
			String toJID,
			String message,
			String whatName,
			int question_id,
			int doctor_id,
			int kuaiZheng_id,
			int type_message) {
		L.e("sendOfflineMessage=========ChatProviderHelper=======");
		ContentValues values = new ContentValues();
		values.put(ChatConstants.DIRECTION, ChatConstants.OUTGOING);
		values.put(ChatConstants.JID, toJID);
		values.put(ChatConstants.MESSAGE, message);
		values.put(ChatConstants.DELIVERY_STATUS, ChatConstants.DS_NEW);
		values.put(ChatConstants.DATE, System.currentTimeMillis());
		values.put(ChatConstants.WHAT_NAME, whatName);
		values.put(ChatConstants.QUESTION_ID, question_id);
		values.put(ChatConstants.DOCTOR_ID, doctor_id);
		values.put(ChatConstants.KUAIZHENG_ID, kuaiZheng_id);
		values.put(ChatConstants.TYPE_MESSAGE, type_message);
		Uri uri=cr.insert(ChatProvider.CONTENT_URI, values);
		L.e("uri================"+uri);
		return uri;
	}
	/**
	 * �����ַ����еı���
	 * 
	 * @param context
	 * @param message
	 *            �������Ҫ�����String
	 * @param small
	 *            �Ƿ���ҪСͼƬ
	 * @return
	 */
	public static CharSequence convertNormalStringToSpannableString(
			Context context, String message, boolean small) {
		L.e("�������Ҫ�����String==="+message);
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (MyApplication.getInstance().getFaceMap().containsKey(str2)) {
					int face = MyApplication.getInstance().getFaceMap().get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), face);
					if (bitmap != null) {
						if (small) {
							int rawHeigh = bitmap.getHeight();
							int rawWidth = bitmap.getHeight();
							int newHeight = 30;
							int newWidth = 30;
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
							bitmap = Bitmap.createBitmap(bitmap, 0, 0,
									rawWidth, rawHeigh, matrix, true);
						}
						ImageSpan localImageSpan = new ImageSpan(context,
								bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}

}
