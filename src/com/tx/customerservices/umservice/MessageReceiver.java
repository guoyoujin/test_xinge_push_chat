package com.tx.customerservices.umservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tx.customerservices.db.ChatProviderHelper;
import com.tx.customerservices.db.PatientChatProviderHelper;
import com.tx.customerservices.db.SendMessage;
import com.tx.customerservices.util.L;
import com.tx.customerservices.util.PreferenceConstants;
import com.tx.customerservices.util.PreferenceUtils;

public class MessageReceiver extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	// ֪ͨչʾ
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		XGNotification notific = new XGNotification();
		notific.setMsg_id(notifiShowedRlt.getMsgId());
		notific.setTitle(notifiShowedRlt.getTitle());
		notific.setContent(notifiShowedRlt.getContent());
		// notificationActionType==1ΪActivity��2Ϊurl��3Ϊintent
		notific.setNotificationActionType(notifiShowedRlt.getNotificationActionType());
		// Activity,url,intent������ͨ��getActivity()���
		notific.setActivity(notifiShowedRlt.getActivity());
		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
		context.sendBroadcast(intent);
		L.e( "����1������Ϣ, " + "֪ͨ��չʾ �� " + notifiShowedRlt.toString());
	 	SendMessage sendMessage=new SendMessage();
	 	JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(notifiShowedRlt.getCustomContent().toString());
			if(!jsonObject.isNull("patient_id")&&!jsonObject.isNull("kuaizheng_id")){
				if(!jsonObject.isNull("patient_id")){
			    	sendMessage.setPatient_id(jsonObject.optInt("patient_id", 0));
			    }if(!jsonObject.isNull("name")){
			    	sendMessage.setName(jsonObject.optString("name", ""));
			    }if(!jsonObject.isNull("question_id")){		    	
			    	sendMessage.setQuestion_id(jsonObject.optInt("question_id",1));
			    }if(!jsonObject.isNull("type_message")){
			    	sendMessage.setType_message(jsonObject.optInt("type_message",1));
			    }if(!jsonObject.isNull("kuaizheng_id")){
			    	sendMessage.setKuaizheng_id(jsonObject.optInt("kuaizheng_id",1));
			    }
			    sendMessage.setMessage(notifiShowedRlt.getContent());
			    L.e("sendMessage================"+sendMessage.toString());
			    L.e("Tag", PreferenceUtils.getPrefString(context, PreferenceConstants.ACCOUNT_ID, PreferenceConstants.DEFAULT_USER_ID));
			    Uri uri= PatientChatProviderHelper.sendOfflineMessage(context.getContentResolver(), PreferenceUtils.getPrefString(context, PreferenceConstants.ACCOUNT_ID, PreferenceConstants.DEFAULT_USER_ID),
			    		sendMessage.getMessage(),
				    	sendMessage.getName(), 
				    	sendMessage.getQuestion_id(), 
				    	sendMessage.getPatient_id(), 
				    	sendMessage.getKuaizheng_id(), 
				    	sendMessage.getType_message());
			}
			if(!jsonObject.isNull("doctor_id")&&!jsonObject.isNull("kuaizheng_id")){
				if(!jsonObject.isNull("doctor_id")){
			    	sendMessage.setDoctor_id(jsonObject.optInt("doctor_id", 1));
			    }if(!jsonObject.isNull("name")){
			    	sendMessage.setName(jsonObject.optString("name", ""));
			    }if(!jsonObject.isNull("question_id")){		    	
			    	sendMessage.setQuestion_id(jsonObject.optInt("question_id",1));
			    }if(!jsonObject.isNull("type_message")){
			    	sendMessage.setType_message(jsonObject.optInt("type_message",1));
			    }if(!jsonObject.isNull("kuaizheng_id")){
			    	sendMessage.setKuaizheng_id(jsonObject.optInt("kuaizheng_id",1));
			    }
			    sendMessage.setMessage(notifiShowedRlt.getContent());
			    L.e("sendMessage================"+sendMessage.toString());
			    L.e("Tag", PreferenceUtils.getPrefString(context, PreferenceConstants.ACCOUNT_ID, PreferenceConstants.DEFAULT_USER_ID));
			    Uri uri= ChatProviderHelper.sendOfflineMessage(context.getContentResolver(), PreferenceUtils.getPrefString(context, PreferenceConstants.ACCOUNT_ID, PreferenceConstants.DEFAULT_USER_ID),
			    		sendMessage.getMessage(),
				    	sendMessage.getName(), 
				    	sendMessage.getQuestion_id(), 
				    	sendMessage.getPatient_id(), 
				    	sendMessage.getKuaizheng_id(), 
				    	sendMessage.getType_message());
			}
			if(!jsonObject.isNull("doctor_id")&&!jsonObject.isNull("patient_id")){
				if(!jsonObject.isNull("doctor_id")){
			    	sendMessage.setDoctor_id(jsonObject.optInt("doctor_id", 1));
			    }if(!jsonObject.isNull("name")){
			    	sendMessage.setName(jsonObject.optString("name", ""));
			    }if(!jsonObject.isNull("patient_id")){
			    	sendMessage.setPatient_id(jsonObject.optInt("patient_id"));
			    }if(!jsonObject.isNull("question_id")){		    	
			    	sendMessage.setQuestion_id(jsonObject.optInt("question_id",1));
			    }if(!jsonObject.isNull("type_message")){
			    	sendMessage.setType_message(jsonObject.optInt("type_message",1));
			    }if(!jsonObject.isNull("kuaizheng_id")){
			    	sendMessage.setKuaizheng_id(jsonObject.optInt("kuaizheng_id",1));
			    }if(!jsonObject.isNull("order_id")){
			    	sendMessage.setKuaizheng_id(jsonObject.optInt("order_id",1));
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}	   
		show(context, "����1������Ϣ, " + "֪ͨ��չʾ �� " + notifiShowedRlt.toString());
	}
	
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "��ע��ɹ�";
		} else {
			text = "��ע��ʧ��" + errorCode;
		}
		L.e(LogTag, text);
		show(context, text);
	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"���óɹ�";
		} else {
			text = "\"" + tagName + "\"����ʧ��,�����룺" + errorCode;
		}
		Log.e(LogTag, text);
		show(context, text);
	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"ɾ���ɹ�";
		} else {
			text = "\"" + tagName + "\"ɾ��ʧ��,�����룺" + errorCode;
		}
		L.e(LogTag, text);
		show(context, text);
	}

	// ֪ͨ����ص� actionType=1Ϊ����Ϣ�������actionType=0Ϊ����Ϣ�����
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// ֪ͨ��֪ͨ�������������������
			// APP�Լ�����������ض���
			// �������������activity��onResumeҲ�ܼ������뿴��3���������
			text = "֪ͨ���� :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// ֪ͨ���������������
			// APP�Լ�����֪ͨ����������ض���
			text = "֪ͨ����� :" + message;
		}
		Toast.makeText(context, "�㲥���յ�֪ͨ�����:" + message.toString(),
				Toast.LENGTH_SHORT).show();
		// ��ȡ�Զ���key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1Ϊǰ̨���õ�key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.e(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP��������Ĺ��̡�����
		Log.e(LogTag, text);
		show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "ע��ɹ�";
			// ��������token
			String token = message.getToken();
		} else {
			text = message + "ע��ʧ�ܣ������룺" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);
	}

	// ��Ϣ͸��
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "�յ���Ϣ:" + message.toString();		
		// ��ȡ�Զ���key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1Ϊǰ̨���õ�key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					L.e(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP����������Ϣ�Ĺ���...
		L.e(LogTag, text);
		show(context, text);
	}
}
