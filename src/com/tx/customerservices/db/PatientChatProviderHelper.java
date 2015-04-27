package com.tx.customerservices.db;

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
import com.tx.customerservices.db.PatientChatProvider.PatientChatConstants;
import com.tx.customerservices.umservice.MyApplication;
import com.tx.customerservices.util.L;

public class PatientChatProviderHelper  {
	private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	public static Uri sendOfflineMessage(ContentResolver cr, 
			String toJID,
			String message,
			String whatName,
			int question_id,
			int patient_id,
			int kuaiZheng_id,
			int type_message) {
		L.e("sendOfflineMessage===========PatientChatProviderHelper=====");
		ContentValues values = new ContentValues();
		values.put(PatientChatConstants.DIRECTION, PatientChatConstants.INCOMING);
		values.put(PatientChatConstants.JID, toJID);
		values.put(PatientChatConstants.MESSAGE, message);
		values.put(PatientChatConstants.DELIVERY_STATUS, PatientChatConstants.DS_NEW);
		values.put(PatientChatConstants.DATE, System.currentTimeMillis());
		values.put(PatientChatConstants.WHAT_NAME, whatName);
		values.put(PatientChatConstants.QUESTION_ID, question_id);
		values.put(PatientChatConstants.PATIENT_ID, patient_id);
		values.put(PatientChatConstants.KUAIZHENG_ID, kuaiZheng_id);
		values.put(PatientChatConstants.TYPE_MESSAGE, type_message);
		Uri uri=cr.insert(PatientChatProvider.CONTENT_URI, values);
		L.e("uri================"+uri);
		return uri;
	}
	
	public static Uri sendNolineMessage(ContentResolver cr, 
			String toJID,
			String message,
			String whatName,
			int question_id,
			int patient_id,
			int kuaiZheng_id,
			int type_message) {
		L.e("sendOfflineMessage===========PatientChatProviderHelper=====");
		ContentValues values = new ContentValues();
		values.put(PatientChatConstants.DIRECTION, PatientChatConstants.OUTGOING);
		values.put(PatientChatConstants.JID, toJID);
		values.put(PatientChatConstants.MESSAGE, message);
		values.put(PatientChatConstants.DELIVERY_STATUS, PatientChatConstants.DS_NEW);
		values.put(PatientChatConstants.DATE, System.currentTimeMillis());
		values.put(PatientChatConstants.WHAT_NAME, whatName);
		values.put(PatientChatConstants.QUESTION_ID, question_id);
		values.put(PatientChatConstants.PATIENT_ID, patient_id);
		values.put(PatientChatConstants.KUAIZHENG_ID, kuaiZheng_id);
		values.put(PatientChatConstants.TYPE_MESSAGE, type_message);
		Uri uri=cr.insert(PatientChatProvider.CONTENT_URI, values);
		L.e("uri================"+uri);
		return uri;
	}
	/**
	 * 处理字符串中的表情
	 * 
	 * @param context
	 * @param message
	 *            传入的需要处理的String
	 * @param small
	 *            是否需要小图片
	 * @return
	 */
	public static CharSequence convertNormalStringToSpannableString(
			Context context, String message, boolean small) {
		L.e("传入的需要处理的String==="+message);
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
