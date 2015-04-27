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
			.getName() + ".username";// 昵称对应的key
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
		// 开启logcat输出，方便debug，发布时请关闭
		XGPushConfig.enableDebug(this, true);
		// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
		// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
		// 具体可参考详细的开发指南
		// 传递的参数为ApplicationContext		
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
		order.setOrder_state("未处理");
		order_list.add(order);
		
		Order order2=new Order();
		order2.setOrder_number("15002");
		order2.setOrder_state("处理中");
		order_list.add(order2);
		
		Order order3=new Order();
		order3.setOrder_number("15003");
		order3.setOrder_state("未付款");
		order_list.add(order3);
		
		Order order4=new Order();
		order4.setOrder_number("15004");
		order4.setOrder_state("取消");
		order_list.add(order4);
		
		Order order5=new Order();
		order5.setOrder_number("15005");
		order5.setOrder_state("未处理");
		order_list.add(order5);
		ll_title_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPupupWindow();
			}
		});
		
		group_one_order_list=(MsgListView)findViewById(R.id.group_one_order_list);
		group_one_order_list.setPullLoadEnable(false);// 禁止下拉加载更多
		group_one_order_list.setPullRefreshEnable(false);
		orderAdapter=new OrderList(this, order_list);
		group_one_order_list.setAdapter(orderAdapter);		
	}
	
	// 显示PopupWindow
	private void showPupupWindow() {
		if (pwMyPopWindow != null) {
			if (pwMyPopWindow.isShowing()) {
				pwMyPopWindow.dismiss();// 关闭
				iv_group.setBackgroundResource(R.drawable.patient_group_down);
			} else {
				pwMyPopWindow.showAsDropDown(ll_title_group);// 显示
				iv_group.setBackgroundResource(R.drawable.patient_group_up);
			}
		}
	}

	private void iniPopupWindow() {
		order_gropu.add("处理中");
		order_gropu.add("已完成");
		order_gropu.add("未付款");
		order_gropu.add("取消");
		order_gropu.add("全部订单");
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.order_group,
				null);
		lvPopupList = (ListView) layout.findViewById(R.id.setting_lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件
		OrderGropuAdapter adapter = new OrderGropuAdapter(this, order_gropu);
		lvPopupList.setAdapter(adapter);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// 关闭
					title_group.setText(order_gropu.get(position));
					iv_group.setBackgroundResource(R.drawable.patient_group_down);				
				}
			}
		});
		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		L.e(lvPopupList.getMeasuredWidth()+"");
		L.e(lvPopupList.getMeasuredHeight()+"");
		pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
		pwMyPopWindow.setHeight(lvPopupList.getMeasuredHeight()*5);
		ViewGroup.LayoutParams params = lvPopupList.getLayoutParams();
		params.height = lvPopupList.getMeasuredHeight()*5;
		lvPopupList.setLayoutParams(params);
		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.patientgroup_selected));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
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
	 * order_group适配器
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
