package com.tx.customerservices;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tx.customerservices.util.NetUtil;


/**
* 继承于Activity用于以后方便管理
* 
* @author coder
* 
*/
public class BaseActivity extends Activity{
		public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();
        private TextView tv_title;
        private TextView btn_left, btn_right;
        private LinearLayout linout_title_bar,centre_lin_layout;
        private LinearLayout ly_content,net_status_bar;
        private ImageView img_title_group;
        // 内容区域的布局
        private View contentView;
        public Context context;       
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.common_title);
                net_status_bar=(LinearLayout) findViewById(R.id.net_status_bar);
                net_status_bar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent;
						try {  
			                String sdkVersion = android.os.Build.VERSION.SDK;  
			                if (Integer.valueOf(sdkVersion) > 10) {  
			                    intent = new Intent(  
			                    android.provider.Settings.ACTION_SETTINGS);  
			                } else {  
			                    intent = new Intent();  
			                    ComponentName comp = new ComponentName(  
			                        "com.android.settings",  
			                        "com.android.settings.Settings");  
			                    intent.setComponent(comp);  
			                    intent.setAction("android.intent.action.VIEW");  
			                }  
			                   
			                BaseActivity.this.startActivity(intent);  
			            } catch (Exception e) {  
			                Log.v("LOG","open network settings failed, please check...");  
			            }  
					}
				});
                centre_lin_layout=(LinearLayout) findViewById(R.id.centre_lin_layout);
                img_title_group=(ImageView)findViewById(R.id.img_title_group);
                tv_title = (TextView) findViewById(R.id.tv_title);
                btn_left = (TextView) findViewById(R.id.btn_left);
                btn_right = (TextView)findViewById(R.id.btn_right);
                linout_title_bar=(LinearLayout) findViewById(R.id.linout_title_bar);
                ly_content = (LinearLayout) findViewById(R.id.ly_content);
                if(context==null){
                	context=BaseActivity.this;
                }         
        }
        
        /***
         * 设置内容区域
         * 
         * @param resId
         *            资源文件ID
         */
        @SuppressWarnings("deprecation")
		@SuppressLint({ "InlinedApi", "NewApi" })
		public void setContentLayout(int resId) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                contentView = inflater.inflate(resId, null);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT);
                contentView.setLayoutParams(layoutParams);
                contentView.setBackgroundDrawable(null);
                if (null != ly_content) {
                        ly_content.addView(contentView);
                }

        }
        @Override
        protected void onResume() {
        	onNetChange();
        	if (mListeners.size() > 0)
    			for (BackPressHandler handler : mListeners) {
    				handler.activityOnResume();
    			}
        	super.onResume();
        	
        }
        @Override
        protected void onPause() {
        	 if (mListeners.size() > 0)
     			for (BackPressHandler handler : mListeners) {
     				handler.activityOnPause();
     			}
        	 super.onPause();
        }
       
        @Override
        protected void onStart() {
        	onNetChange();
        	super.onStart();
        
        }
        
    	public void onNetChange() {
    		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
    			com.tx.customerservices.util.T.showShort(context, R.string.net_error_tip);
    			net_status_bar.setVisibility(View.VISIBLE);
    		} else {
    			net_status_bar.setVisibility(View.GONE);
    		}
    	}
        /***
         * 设置内容区域
         * 
         * @param view
         *            View对象
         */
        public void setContentLayout(View view) {
                if (null != ly_content) {
                        ly_content.addView(view);
                }
        }

        /**
         * 得到内容的View
         * 
         * @return
         */
        public View getLyContentView() {

                return contentView;
        }

        /**
         * 得到左边的按钮
         * 
         * @return
         */
        public TextView getbtn_left() {
                return btn_left;
        }

        /**
         * 得到title
         * 
         * @return
         */
        public TextView getTitle_tv() {
                return tv_title;
        }
        /**
         * 得到右边的按钮
         * 
         * @return
         */
        public TextView getbtn_right() {
                return btn_right;
        }
        
        /**
         * 得到linear
         * 
         * @return
         */
        public LinearLayout get_title_linearLayout(){
        	return centre_lin_layout;
        }
        
        /**
         * 得到Image
         * 
         * @return
         */
        public ImageView get_title_Image(){
        	return img_title_group;
        }

        /**
         * 设置标题
         * 
         * @param title
         */
        public void setTitle(String title) {

                if (null != tv_title) {
                        tv_title.setText(title);
                }

        }

        /**
         * 设置标题
         * 
         * @param resId
         */
        public void setTitle(int resId) {
                tv_title.setText(getString(resId));
        }

        /**
         * 设置左边按钮的图片资源
         * 
         * @param resId
         */
        public void setbtn_leftRes(int resId) {
                if (null != btn_left) {
                        btn_left.setBackgroundResource(resId);
                }

        }

        /**
         * 设置左边按钮的图片资源
         * 
         * @param bm
         */
        @SuppressWarnings("deprecation")
		public void setbtn_leftRes(Drawable drawable) {
                if (null != btn_left) {
                        btn_left.setBackgroundDrawable(drawable);
                }

        }

        /**
         * 设置右边按钮的图片资源
         * 
         * @param resId
         */
        public void setbtn_rightRes(int resId) {
                if (null != btn_right) {
                        btn_right.setBackgroundResource(resId);
                }
        }

        /**
         * 设置右边按钮的图片资源
         * 
         * @param drawable
         */
        @SuppressWarnings("deprecation")
		public void setbtn_rightRes(Drawable drawable) {
                if (null != btn_right) {
                        btn_right.setBackgroundDrawable(drawable);
                }
        }

        /**
         * 隐藏上方的标题栏
         */
        public void hideTitleView() {

                if (null != linout_title_bar) {
                	linout_title_bar.setVisibility(View.GONE);
                }
        }
        /**
         * 显示上方的标题栏
         */
        public void showTitleView() {

                if (null != linout_title_bar) {
                	linout_title_bar.setVisibility(View.VISIBLE);
                }
        }

        /**
         * 隐藏左边的按钮
         */
        public void hidebtn_left() {

                if (null != btn_left) {
                        btn_left.setVisibility(View.GONE);
                }

        }

        /***
         * 隐藏右边的按钮
         */
        public void hidebtn_right() {
                if (null != btn_right) {
                        btn_right.setVisibility(View.GONE);
                }

        }

        public BaseActivity() {

        }
//
//		@SuppressWarnings("unchecked")
//		public static <T extends View> T getAdapterView(View convertView, int id) {
//			SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
//            if (viewHolder == null) {
//                viewHolder = new SparseArray<View>();
//                convertView.setTag(viewHolder);
//            }
//            View childView = viewHolder.get(id);
//            if (childView == null) {
//                childView = convertView.findViewById(id);
//                viewHolder.put(id, childView);
//            }
//            return (T) childView;
//        }
//	
//        public static Bitmap getBitmapFromResources(Activity act, int resId) {
//        	Resources res = act.getResources();
//        	return BitmapFactory.decodeResource(res, resId);
//        }
//        @SuppressWarnings("deprecation")
//		public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
//        	BitmapDrawable bd = new BitmapDrawable(bitmap);
//        	// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
//        	return bd;
//        }
        public static abstract interface BackPressHandler {

    		public abstract void activityOnResume();

    		public abstract void activityOnPause();

    	}
}
