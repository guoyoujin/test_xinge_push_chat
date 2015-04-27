package com.tx.customerservices.umservice;




/**
 * Developer defined push intent service. 
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}. 
 * @author lucas
 *
 */
public class MyPushIntentService{
//	private static final String TAG = MyPushIntentService.class.getName();
//	private static final String APP_NAME = "xx";
//	private static final int MAX_TICKER_MSG_LEN = 50;
//	protected static int SERVICE_NOTIFICATION = 1;
//	private Handler mMainHandler = new Handler();
//	private NotificationManager mNotificationManager;
//	private Notification mNotification;
//	private Intent mNotificationIntent;
//	private Vibrator mVibrator;
//	protected WakeLock mWakeLock;
//	private MessageListener messageListener;// 消息监听接口对象 
//
//	private Map<String, Integer> mNotificationCount = new HashMap<String, Integer>(
//			2);
//	private Map<String, Integer> mNotificationId = new HashMap<String, Integer>(
//			2);
//	private int mLastNotificationId = 2;
//	private Handler handler=new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 0:
//				 Intent intents=new Intent();
//			     intents.putExtra("message", msg.obj.toString());
//			     L.e(msg.obj.toString());
//			     intents.setAction("com.tx.customerservices.MyReceiver");
//			     sendBroadcast(intents);
//				break;
//			case 1:
//				
//				break;
//			default:
//				break;
//			}
//		};
//	};
//	@Override
//	protected void onMessage(Context context, Intent intent) {
//		super.onMessage(context, intent);
//		try {
//			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
//			UMessage msg = new UMessage(new JSONObject(message));
//			notifyClient("123", "gyj", message, true);
//			L.e("message="+message);
//			//message={"msg_id":"us70826142978164261610","display_type":"custom","alias":"","random_min":0,"body":{"custom":"asdasdafqwer123123"},"extra":{"patient_id":"1","doctor_id":"1","name":"gyj","question_id":"1","type_people":"1","message":"agasgag"}}
//			//L.e("custom="+msg.custom);
////			messageListener.Message(message);
//			//发送广播
//			JSONObject jsonObject=new JSONObject(message).optJSONObject("extra");
//		    L.e("jsonObject="+jsonObject.toString());
//		    SendMessage sendMessage=new SendMessage();
//		    if(!jsonObject.isNull("doctor_id")){
//		    	sendMessage.setDoctor_id(jsonObject.optInt("doctor_id", 0));
//		    }if(!jsonObject.isNull("name")){
//		    	sendMessage.setName(jsonObject.optString("name", ""));
//		    }if(!jsonObject.isNull("patient_id")){
//		    	sendMessage.setPatient_id(jsonObject.optInt("patient_id"));
//		    }if(!jsonObject.isNull("question_id")){
//		    	
//		    	sendMessage.setQuestion_id(jsonObject.optInt("question_id"));
//		    }if(!jsonObject.isNull("type_people")){
//		    	sendMessage.setType_people(jsonObject.optInt("type_people"));
//		    }if(!jsonObject.isNull("message")){
//		    	sendMessage.setMessage(jsonObject.optString("message"));
//		    }
//		    L.e("sendMessage================"+sendMessage.toString());
//		    Uri uri= ChatProviderHelper.sendOfflineMessage(getContentResolver(), "", sendMessage.getMessage(),
//		    		sendMessage.getName(), sendMessage.getQuestion_id(), sendMessage.getDoctor_id(), 
//		    		sendMessage.getPatient_id(), sendMessage.getKuaizheng_id(), sendMessage.getType_people());
//		    L.e("uri================"+uri);
//		    Message msgs=new Message();
//		    msgs.what=0;
//		    msgs.obj=message;
//		    handler.sendMessage(msgs);
//			// code  to handle message here
//			// ...
//		    
//		} catch (Exception e) {
//			Log.e(TAG, e.getMessage());
//		}
//	}
//	/** 
//     * 提供给外部的消息监听方法 
//     *  
//     * @param messageListener 
//     *            消息监听接口对象 
//     */  
//    public void setMessageListener(MessageListener messageListener) {  
//        this.messageListener = messageListener;  
//    }  
//	@Override
//	public void onCreate() {
//		L.e("============MyPushIntentService========onCreate=====");
//		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//		mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
//				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_NAME);
//		addNotificationMGR();
//		super.onCreate();
//	}
//	
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return super.onBind(intent);
//	}
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		// TODO Auto-generated method stub
//		L.e("============onStartCommand==============");
//		return super.onStartCommand(intent, flags, startId);
//	}
//	private void addNotificationMGR() {
//		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		mNotificationIntent = new Intent(this, MainActivity.class);
//	}
//
//	protected void notifyClient(String fromJid, String fromUserName,
//			String message, boolean showNotification) {
//		L.e("Tag", "fromJid==="+fromJid+"======fromUserName==="+fromUserName+"====message="+message);
//
//		if (!showNotification) {
//			return;
//		}
//		mWakeLock.acquire();
//		setNotification(fromJid, fromUserName, message);
//		setLEDNotification();
//
//		int notifyId = 0;
//		if (mNotificationId.containsKey(fromJid)) {
//			notifyId = mNotificationId.get(fromJid);
//		} else {
//			mLastNotificationId++;
//			notifyId = mLastNotificationId;
//			mNotificationId.put(fromJid, Integer.valueOf(notifyId));
//		}
//
//		// If vibration is set to true, add the vibration flag to
//		// the notification and let the system decide.
//		boolean vibraNotify = PreferenceUtils.getPrefBoolean(this,
//				PreferenceConstants.VIBRATIONNOTIFY, true);
//		if (vibraNotify) {
//			mVibrator.vibrate(400);
//		}
//		mNotificationManager.notify(notifyId, mNotification);
//
//		mWakeLock.release();
//	}
//
//	private void setNotification(String fromJid, String fromUserId,
//			String message) {
//		L.e("Tag", "fromJid==="+fromJid+"======fromUserId==="+fromUserId+"====message="+message);
//		int mNotificationCounter = 0;
//		if (mNotificationCount.containsKey(fromJid)) {
//			mNotificationCounter = mNotificationCount.get(fromJid);
//		}
//		mNotificationCounter++;
//		mNotificationCount.put(fromJid, mNotificationCounter);
//		String author;
//		if (null == fromUserId || fromUserId.length() == 0) {
//			author = fromJid;
//		} else {
//			author = fromUserId;
//		}
//		String title = author;
//		String ticker;
//		boolean isTicker = PreferenceUtils.getPrefBoolean(this,
//				PreferenceConstants.TICKER, true);
//		if (isTicker) {
//			int newline = message.indexOf('\n');
//			int limit = 0;
//			String messageSummary = message;
//			if (newline >= 0)
//				limit = newline;
//			if (limit > MAX_TICKER_MSG_LEN
//					|| message.length() > MAX_TICKER_MSG_LEN)
//				limit = MAX_TICKER_MSG_LEN;
//			if (limit > 0)
//				messageSummary = message.substring(0, limit) + " [...]";
//			ticker = title + ":\n" + messageSummary;
//		} else
//			ticker = author;
//		mNotification = new Notification(R.drawable.notify_newmessage, ticker,
//				System.currentTimeMillis());
//		Uri userNameUri = Uri.parse(fromJid);
//		mNotificationIntent.setData(userNameUri);
//		mNotificationIntent.putExtra(MainActivity.INTENT_EXTRA_USERNAME,
//				fromUserId);
//		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//		// need to set flag FLAG_UPDATE_CURRENT to get extras transferred
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//				mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		mNotification.setLatestEventInfo(this, title, message, pendingIntent);
//		if (mNotificationCounter > 1)
//			mNotification.number = mNotificationCounter;
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//	}
//
//	private void setLEDNotification() {
//		boolean isLEDNotify = PreferenceUtils.getPrefBoolean(this,
//				PreferenceConstants.LEDNOTIFY, true);
//		if (isLEDNotify) {
//			mNotification.ledARGB = Color.MAGENTA;
//			mNotification.ledOnMS = 300;
//			mNotification.ledOffMS = 1000;
//			mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
//		}
//	}
//
//	public void resetNotificationCounter(String userJid) {
//		mNotificationCount.remove(userJid);
//	}
//
//	public void clearNotification(String Jid) {
//		int notifyId = 0;
//		if (mNotificationId.containsKey(Jid)) {
//			notifyId = mNotificationId.get(Jid);
//			mNotificationManager.cancel(notifyId);
//		}
//	}
//	/** 
//     * 消息监听接口 
//     *  
//     * @author way 
//     *  
//     */  
//    public interface MessageListener {  
//        public void Message(String msg);  
//    }  
//	
}
