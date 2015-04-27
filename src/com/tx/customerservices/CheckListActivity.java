package com.tx.customerservices;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;

import com.tx.customerservices.umservice.swactivity.SwipeBackActivity;
import com.tx.customerservices.view.JustifyTextView;
import com.tx.customerservices.xlistview.MsgListView.IXListViewListener;

public class CheckListActivity extends SwipeBackActivity implements OnTouchListener,
OnClickListener, IXListViewListener{
	private JustifyTextView jtv_check_list_number, jtv_check_list_numbers,
			jtv_check_list_patient_name, jtv_check_list_patient_names,
			jtv_check_list_patient_iphone, jtv_check_list_patient_iphones,
			jtv_check_list_hospital, jtv_check_list_hospitals,
			jtv_check_list_project, jtv_check_list_projects,
			jtv_check_list_part, jtv_check_list_parts,
			jtv_check_list_specification, jtv_check_list_specifications,
			jtv_check_list_picture;
	private GridView gridview_check_list_picture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_check_list);
		setTitle(R.string.check_list);
		hidebtn_right();
		initView();

	}

	public void initView() {
		getbtn_left().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		jtv_check_list_number = (JustifyTextView) findViewById(R.id.jtv_check_list_number);
		jtv_check_list_numbers = (JustifyTextView) findViewById(R.id.jtv_check_list_numbers);

		jtv_check_list_patient_name = (JustifyTextView) findViewById(R.id.jtv_check_list_patient_name);
		jtv_check_list_patient_names = (JustifyTextView) findViewById(R.id.jtv_check_list_patient_names);

		jtv_check_list_patient_iphone = (JustifyTextView) findViewById(R.id.jtv_check_list_patient_iphone);
		jtv_check_list_patient_iphones = (JustifyTextView) findViewById(R.id.jtv_check_list_patient_iphones);

		jtv_check_list_hospital = (JustifyTextView) findViewById(R.id.jtv_check_list_hospital);
		jtv_check_list_hospitals = (JustifyTextView) findViewById(R.id.jtv_check_list_hospitals);

		jtv_check_list_project = (JustifyTextView) findViewById(R.id.jtv_check_list_project);
		jtv_check_list_projects = (JustifyTextView) findViewById(R.id.jtv_check_list_projects);

		jtv_check_list_part = (JustifyTextView) findViewById(R.id.jtv_check_list_part);
		jtv_check_list_parts = (JustifyTextView) findViewById(R.id.jtv_check_list_parts);

		jtv_check_list_specification = (JustifyTextView) findViewById(R.id.jtv_check_list_specification);
		jtv_check_list_specifications = (JustifyTextView) findViewById(R.id.jtv_check_list_specification);

		jtv_check_list_picture = (JustifyTextView) findViewById(R.id.jtv_check_list_picture);

		gridview_check_list_picture = (GridView) findViewById(R.id.gridview_check_list_picture);
	}

	public void setJustText() {
		jtv_check_list_numbers.setText("");
		jtv_check_list_patient_names.setText("");
		jtv_check_list_patient_iphones.setText("");
		jtv_check_list_hospitals.setText("");
		jtv_check_list_projects.setText("");
		jtv_check_list_parts.setText("");
		jtv_check_list_specifications.setText("");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
