package com.tx.customerservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.tx.customerservices.umservice.swactivity.SwipeBackActivity;
import com.tx.customerservices.util.L;
import com.tx.customerservices.util.T;
import com.tx.customerservices.view.FButton;
import com.tx.customerservices.view.JustifyTextView;

public class OrderInfoActivity extends SwipeBackActivity implements OnClickListener {
	private ListView order_info_list;
	private FButton fbtn_check_list, fbtn_order_treated, fbtn_order_cancel;
	private JustifyTextView jtv_order_number_order_info,
			jtv_order_numbers_order_info, jtv_doctor_name_order_info,
			jtv_doctor_names_order_info, jtv_order_time_order_info,
			jtv_order_times_order_info, jtv_order_hospital_order_info,
			jtv_order_hospitals_order_info,
			jtv_order_inspection_item_order_info,
			jtv_order_inspection_items_order_info,
			jtv_order_patient_order_info, jtv_order_patients_order_info,
			jtv_order_patient_iphone_order_info,
			jtv_order_patient_iphones_order_info, jtv_order_price_order_info,
			jtv_order_prices_order_info, jtv_order_state_order_info,
			jtv_order_states_order_info, jtv_order_dispose_people;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_order_info);
		setTitle(R.string.order_info);
		L.e("==============="+getIntent().getExtras().getString("gyj"));
		hidebtn_right();
		initView();
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	public void initView() {
		getbtn_left().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		order_info_list = (ListView) findViewById(R.id.order_info_list);

		fbtn_check_list = (FButton) findViewById(R.id.fbtn_check_list);
		fbtn_check_list.setCornerRadius(10);
		fbtn_check_list.setShadowEnabled(false);
		fbtn_check_list.setButtonColor(getResources().getColor(
				R.color.action_sheet_curr_point));
		fbtn_check_list.setShadowColor(getResources().getColor(
				R.color.action_sheet_point));
		fbtn_check_list.setOnClickListener(this);
		fbtn_order_treated = (FButton) findViewById(R.id.fbtn_order_treated);
		fbtn_order_treated.setCornerRadius(10);
		fbtn_order_treated.setShadowEnabled(false);
		fbtn_order_treated.setButtonColor(getResources().getColor(
				R.color.action_sheet_curr_point));
		fbtn_order_treated.setShadowColor(getResources().getColor(
				R.color.action_sheet_point));
		fbtn_check_list.setOnClickListener(this);
		fbtn_order_cancel = (FButton) findViewById(R.id.fbtn_order_cancel);
		fbtn_order_cancel.setCornerRadius(10);
		fbtn_order_cancel.setShadowEnabled(false);
		fbtn_order_cancel.setButtonColor(getResources().getColor(
				R.color.action_sheet_curr_point));
		fbtn_order_cancel.setShadowColor(getResources().getColor(
				R.color.action_sheet_point));
		fbtn_order_cancel.setOnClickListener(this);
		jtv_order_number_order_info = (JustifyTextView) findViewById(R.id.jtv_order_number_order_info);
		jtv_order_numbers_order_info = (JustifyTextView) findViewById(R.id.jtv_order_numbers_order_info);

		jtv_doctor_name_order_info = (JustifyTextView) findViewById(R.id.jtv_doctor_name_order_info);
		jtv_doctor_names_order_info = (JustifyTextView) findViewById(R.id.jtv_doctor_names_order_info);

		jtv_order_time_order_info = (JustifyTextView) findViewById(R.id.jtv_order_time_order_info);
		jtv_order_times_order_info = (JustifyTextView) findViewById(R.id.jtv_order_times_order_info);

		jtv_order_hospital_order_info = (JustifyTextView) findViewById(R.id.jtv_order_hospital_order_info);
		jtv_order_hospitals_order_info = (JustifyTextView) findViewById(R.id.jtv_order_hospitals_order_info);

		jtv_order_inspection_item_order_info = (JustifyTextView) findViewById(R.id.jtv_order_inspection_item_order_info);
		jtv_order_inspection_items_order_info = (JustifyTextView) findViewById(R.id.jtv_order_inspection_items_order_info);

		jtv_order_patient_order_info = (JustifyTextView) findViewById(R.id.jtv_order_patient_order_info);
		jtv_order_patients_order_info = (JustifyTextView) findViewById(R.id.jtv_order_patients_order_info);

		jtv_order_patient_iphone_order_info = (JustifyTextView) findViewById(R.id.jtv_order_patient_iphone_order_info);
		jtv_order_patient_iphones_order_info = (JustifyTextView) findViewById(R.id.jtv_order_patient_iphones_order_info);

		jtv_order_price_order_info = (JustifyTextView) findViewById(R.id.jtv_order_price_order_info);
		jtv_order_prices_order_info = (JustifyTextView) findViewById(R.id.jtv_order_prices_order_info);

		jtv_order_state_order_info = (JustifyTextView) findViewById(R.id.jtv_order_state_order_info);
		jtv_order_states_order_info = (JustifyTextView) findViewById(R.id.jtv_order_states_order_info);

		jtv_order_dispose_people = (JustifyTextView) findViewById(R.id.jtv_order_dispose_people);

	}

	public void setJustText() {
		jtv_order_number_order_info.setText("");
		jtv_doctor_name_order_info.setText("");
		jtv_order_time_order_info.setText("");
		jtv_order_hospital_order_info.setText("");
		jtv_order_inspection_item_order_info.setText("");
		jtv_order_patient_order_info.setText("");
		jtv_order_patient_iphone_order_info.setText("");
		jtv_order_price_order_info.setText("");
		jtv_order_state_order_info.setText("");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.fbtn_check_list:
				Intent intent=new Intent(getApplicationContext(),CheckListActivity.class);
				startActivity(intent);
			break;
		case R.id.fbtn_order_treated:

			break;
		case R.id.fbtn_order_cancel:

			break;

		default:
			break;
		}

	}

}
