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
import com.tx.customerservices.db.DoctorPatientChatProvider.DoctorPatientChatConstants;
import com.tx.customerservices.umservice.MyApplication;
import com.tx.customerservices.util.L;

public class DoctorPatientChatProviderHelper  {
	private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	public static Uri sendOfflineMessage(ContentResolver cr, 
			String toJID,
			String message,
			String whatName,
			int order_id,
			int doctor_id,
			int patient_id,
			int type_message) {
		L.e("sendOfflineMessage===========PatientChatProviderHelper=====");
		ContentValues values = new ContentValues();
		values.put(DoctorPatientChatConstants.DIRECTION, DoctorPatientChatConstants.INCOMING);
		values.put(DoctorPatientChatConstants.JID, toJID);
		values.put(DoctorPatientChatConstants.MESSAGE, message);
		values.put(DoctorPatientChatConstants.DELIVERY_STATUS, DoctorPatientChatConstants.DS_NEW);
		values.put(DoctorPatientChatConstants.DATE, System.currentTimeMillis());
		values.put(DoctorPatientChatConstants.WHAT_NAME, whatName);
		values.put(DoctorPatientChatConstants.ORDER_ID, order_id);
		values.put(DoctorPatientChatConstants.PATIENT_ID, patient_id);
		values.put(DoctorPatientChatConstants.DOCTOR_ID, doctor_id);
		values.put(DoctorPatientChatConstants.TYPE_MESSAGE, type_message);
		Uri uri=cr.insert(DoctorPatientChatProvider.CONTENT_URI, values);
		L.e("uri================"+uri);
		return uri;
	}
	
	public static Uri sendNolineMessage(ContentResolver cr, 
			String toJID,
			String message,
			String whatName,
			int order_id,
			int patient_id,
			int doctor_id,
			int type_message) {
		L.e("sendOfflineMessage===========PatientChatProviderHelper=====");
		ContentValues values = new ContentValues();
		values.put(DoctorPatientChatConstants.DIRECTION, DoctorPatientChatConstants.OUTGOING);
		values.put(DoctorPatientChatConstants.JID, toJID);
		values.put(DoctorPatientChatConstants.MESSAGE, message);
		values.put(DoctorPatientChatConstants.DELIVERY_STATUS, DoctorPatientChatConstants.DS_NEW);
		values.put(DoctorPatientChatConstants.DATE, System.currentTimeMillis());
		values.put(DoctorPatientChatConstants.WHAT_NAME, whatName);
		values.put(DoctorPatientChatConstants.ORDER_ID, order_id);
		values.put(DoctorPatientChatConstants.PATIENT_ID, patient_id);
		values.put(DoctorPatientChatConstants.DOCTOR_ID, doctor_id);
		values.put(DoctorPatientChatConstants.TYPE_MESSAGE, type_message);
		Uri uri=cr.insert(DoctorPatientChatProvider.CONTENT_URI, values);
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

