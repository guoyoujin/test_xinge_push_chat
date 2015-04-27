package com.tx.customerservices;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tx.customerservices.bean.Order;
import com.tx.customerservices.util.L;
import com.tx.customerservices.util.SimpleBaseAdapter;
import com.tx.customerservices.view.FButton;
import com.tx.customerservices.view.JustifyTextView;
import com.tx.customerservices.xlistview.MsgListView;


public class GroupOneActivity extends BaseActivity {
	public static final String INTENT_EXTRA_USERNAME = GroupOneActivity.class
			.getName() + ".username";// �ǳƶ�Ӧ��key
	private LinearLayout ll_title_group;
	private PopupWindow pwMyPopWindow;
	private TextView title_group;
	ImageView iv_group;
	private ListView lvPopupList;
	private List<String> order_gropu=new ArrayList<String>();
	
	private List<Order> order_list=new ArrayList<Order>();
	
	private MsgListView group_one_order_list;
	
	private  OrderList orderAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_group_one);
		setTitle(R.string.title_order);
		// ����logcat���������debug������ʱ��ر�
		XGPushConfig.enableDebug(this, true);
		// �����Ҫ֪��ע���Ƿ�ɹ�����ʹ��registerPush(getApplicationContext(), XGIOperateCallback)��callback�汾
		// �����Ҫ���˺ţ���ʹ��registerPush(getApplicationContext(),account)�汾
		// ����ɲο���ϸ�Ŀ���ָ��
		// ���ݵĲ���ΪApplicationContext		
		XGPushManager.registerPush(getApplicationContext(),"gtj", new XGIOperateCallback() {			
			@Override
			public void onSuccess(Object data, int arg1) {
				// TODO Auto-generated method stub
				L.e(Constants.LogTag,"+++ register push sucess. token:" + data.toString());
			}			
			@Override
			public void onFail(Object data, int arg1, String arg2) {
				// TODO Auto-generated method stub
				L.e(Constants.LogTag,"+++ register push sucess. token:" + data.toString());
				L.e(Constants.LogTag,"+++ register push arg1. token:" + arg1);
				L.e(Constants.LogTag,"+++ register push arg2. token:" + arg2.toString());
			}
		});
		iv_group=get_title_Image();
		iv_group.setVisibility(View.VISIBLE);
		ll_title_group=get_title_linearLayout();
		title_group=getTitle_tv();
		iniPopupWindow();
		hidebtn_left();
		hidebtn_right();
		initView();	
	}

	public void initView(){
		Order order=new Order();
		order.setOrder_number("15001");
		order.setOrder_state("δ����");
		order_list.add(order);
		
		Order order2=new Order();
		order2.setOrder_number("15002");
		order2.setOrder_state("������");
		order_list.add(order2);
		
		Order order3=new Order();
		order3.setOrder_number("15003");
		order3.setOrder_state("δ����");
		order_list.add(order3);
		
		Order order4=new Order();
		order4.setOrder_number("15004");
		order4.setOrder_state("ȡ��");
		order_list.add(order4);
		
		Order order5=new Order();
		order5.setOrder_number("15005");
		order5.setOrder_state("δ����");
		order_list.add(order5);
		ll_title_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPupupWindow();
			}
		});
		
		group_one_order_list=(MsgListView)findViewById(R.id.group_one_order_list);
		group_one_order_list.setPullLoadEnable(false);// ��ֹ�������ظ���
		group_one_order_list.setPullRefreshEnable(false);
		orderAdapter=new OrderList(this, order_list);
		group_one_order_list.setAdapter(orderAdapter);		
	}
	
	// ��ʾPopupWindow
	private void showPupupWindow() {
		if (pwMyPopWindow != null) {
			if (pwMyPopWindow.isShowing()) {
				pwMyPopWindow.dismiss();// �ر�
				iv_group.setBackgroundResource(R.drawable.patient_group_down);
			} else {
				pwMyPopWindow.showAsDropDown(ll_title_group);// ��ʾ
				iv_group.setBackgroundResource(R.drawable.patient_group_up);
			}
		}
	}

	private void iniPopupWindow() {
		order_gropu.add("������");
		order_gropu.add("�����");
		order_gropu.add("δ����");
		order_gropu.add("ȡ��");
		order_gropu.add("ȫ������");
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.order_group,
				null);
		lvPopupList = (ListView) layout.findViewById(R.id.setting_lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// �������popupwindow�е�ListView�ſ��Խ��յ���¼�
		OrderGropuAdapter adapter = new OrderGropuAdapter(this, order_gropu);
		lvPopupList.setAdapter(adapter);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// �ر�
					title_group.setText(order_gropu.get(position));
					iv_group.setBackgroundResource(R.drawable.patient_group_down);				
				}
			}
		});
		// ����popupwindow�Ŀ�Ⱥ͸߶�����Ӧ
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		L.e(lvPopupList.getMeasuredWidth()+"");
		L.e(lvPopupList.getMeasuredHeight()+"");
		pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
		pwMyPopWindow.setHeight(lvPopupList.getMeasuredHeight()*5);
		ViewGroup.LayoutParams params = lvPopupList.getLayoutParams();
		params.height = lvPopupList.getMeasuredHeight()*5;
		lvPopupList.setLayoutParams(params);
		// ����popupwindow�����Ļ�����ط���ʧ
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.patientgroup_selected));// ���ñ���ͼƬ�������ڲ��������ã�Ҫͨ������������
		pwMyPopWindow.setOutsideTouchable(true);// ����popupwindow�ⲿ��popupwindow��ʧ�����Ҫ�����popupwindowҪ�б���ͼƬ�ſ��Գɹ�������
		pwMyPopWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				iv_group.setBackgroundResource(R.drawable.patient_group_down);
			}
		});
	}
	
	class OrderList extends SimpleBaseAdapter<Order>{

		public OrderList(Context context, List<Order> data) {
			super(context, data);
		}

		public int getItemResource() {
			return R.layout.group_one_order_list_item;
		}

		@SuppressLint("ResourceAsColor")
		public View getItemView(final int position, View convertView,ViewHolder holder) {
			FButton fbtn_order_state=holder.getView(R.id.fbtn_order_state);
			fbtn_order_state.setCornerRadius(0);
			fbtn_order_state.setShadowEnabled(false);
			if(order_list.get(position).getOrder_state().equals(getString(R.string.order_cancel))){
				
				fbtn_order_state.setText(getString(R.string.order_cancel));
				fbtn_order_state.setButtonColor(R.color.actionbar_bgcolor);
			}else if(order_list.get(position).getOrder_state().equals(getString(R.string.order_stocks))){				
				fbtn_order_state.setText(getString(R.string.order_stocks));
				fbtn_order_state.setButtonColor(R.color.actionbar_bgcolor);
			}else if(order_list.get(position).getOrder_state().equals(getString(R.string.order_account_paid))){				
				fbtn_order_state.setText(getString(R.string.order_account_paid));
				fbtn_order_state.setButtonColor(R.color.actionbar_bgcolor);
			}else if(order_list.get(position).getOrder_state().equals(getString(R.string.order_treated))){
				fbtn_order_state.setText(getString(R.string.order_treated));
				fbtn_order_state.setButtonColor(R.color.actionbar_bgcolor);
			}else{
				fbtn_order_state.setText(getString(R.string.order_all));
				fbtn_order_state.setButtonColor(R.color.actionbar_bgcolor);
			}
			FButton fbtn_order_info=holder.getView(R.id.fbtn_order_info);
			fbtn_order_info.setCornerRadius(10);
			fbtn_order_info.setShadowEnabled(false);
			fbtn_order_info.setButtonColor(getResources().getColor(R.color.action_sheet_curr_point));
			fbtn_order_info.setShadowColor(getResources().getColor(R.color.action_sheet_point));
			fbtn_order_info.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent(getApplicationContext(),OrderInfoActivity.class);
					intent.putExtra("gyj", "gyj");
					L.e("gyj======="+intent.getExtras().getString("gyj"));
					startActivity(intent);
				}
			});
			FButton fbtn_doctor_patient_communicate=holder.getView(R.id.fbtn_doctor_patient_communicate);
			fbtn_doctor_patient_communicate.setCornerRadius(10);
			fbtn_doctor_patient_communicate.setShadowEnabled(false);
			fbtn_doctor_patient_communicate.setButtonColor(getResources().getColor(R.color.action_sheet_curr_point));
			fbtn_doctor_patient_communicate.setShadowColor(getResources().getColor(R.color.action_sheet_point));
			fbtn_doctor_patient_communicate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent(getApplicationContext(),DoctorPatientChatActivity.class);
					intent.putExtra(DoctorPatientChatActivity.INTENT_ORDER_ID, order_list.get(position).getOrder_number());
					startActivity(intent);					
				}
			});
			JustifyTextView jtv_order_number=holder.getView(R.id.jtv_order_number);
			jtv_order_number.setText(order_list.get(position).getOrder_number());
			return convertView;
		}		
	}
	
	protected void onPause() {
		
		super.onPause();
	}
	
	protected void onResume() {

		super.onResume();
	}
	
	
	
	
	/**
	 * order_group������
	 * @author gyj
	 *
	 */
	class OrderGropuAdapter extends SimpleBaseAdapter<String> {
		public OrderGropuAdapter(Context context, List<String> data) {
			super(context, data);
		}
		@Override
		public int getItemResource() {
			return R.layout.order_group_list_item;
		}
		public View getItemView(int position, View convertView,
				ViewHolder holder) {
			TextView text = holder.getView(R.id.tv_list_item);
			L.e("Tag", order_gropu.get(position));
			text.setText(order_gropu.get(position));
			return convertView;
		}
	}
}
