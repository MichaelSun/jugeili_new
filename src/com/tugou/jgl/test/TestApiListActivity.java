package com.tugou.jgl.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plugin.common.utils.CustomThreadPool;
import com.plugin.common.utils.CustomThreadPool.TaskWrapper;
import com.plugin.common.utils.StringUtils;
import com.plugin.internet.InternetUtils;
import com.tugou.jgl.R;
import com.tugou.jgl.activity.BaseActivity;
import com.tugou.jgl.activity.LoginActivity;
import com.tugou.jgl.api.GetAreaListRequest;
import com.tugou.jgl.api.GetAreaListResponse;
import com.tugou.jgl.api.GetCategoryListRequest;
import com.tugou.jgl.api.GetCategoryListResponse;
import com.tugou.jgl.api.GetListGroupRequest;
import com.tugou.jgl.api.GetListGroupResponse;
import com.tugou.jgl.api.LoginRequest;
import com.tugou.jgl.api.LoginResponse;
import com.tugou.jgl.api.NearbyListRequest;
import com.tugou.jgl.api.NearbyListResponse;
import com.tugou.jgl.base.Evnironment;
import com.tugou.jgl.fragment.GroupOnFragment;
import com.tugou.jgl.fragment.SubListFragment;
import com.tugou.jgl.utils.Constant;
import com.tugou.jgl.utils.Debug;

public class TestApiListActivity extends BaseActivity {
	
	private ListView listViewTest;
	private LayoutInflater mInflater;
	private TestItemAdapter testItemAdapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list);
        mInflater = getLayoutInflater();
        
        listViewTest = (ListView)findViewById(R.id.list);
        testItemAdapter = new TestItemAdapter();
        listViewTest.setAdapter(testItemAdapter);
        
        listViewTest.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
	        	switch(position){
	        	case TestConstant.GetCategoryListRequest: 
	        		GetCategoryList();
	        		break;
	        	case TestConstant.GetListGroupRequest:
	        		getListGroup();
	        		break;
	        	case TestConstant.LoginRequest:
	        		Login("liyilu@live.cn", StringUtils.MD5Encode("123456"));
	        		break;
	        	case TestConstant.NearbyListRequest:
	        		getNearbyList(100, 0, 0, 0, 0, 0);
	        		break;
	        	case TestConstant.GetAreaListRequest:
	        		getAreaList(0, 1);	
	        		break;
	        		
	        	default:
	        		break;
	        	}
				
			}
        	
        });
    }
	
    private class TestItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {// total num
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.test_list_item, null);
                holder = new ViewHolder();
                holder.textViewReq = (TextView) convertView.findViewById(R.id.req_url);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            setApiList(holder, position);
            return convertView;
        }
        
        private void setApiList(ViewHolder holder, int position){
        	switch(position){
        	case TestConstant.GetCategoryListRequest: 
        		holder.textViewReq.setText("GetCategoryListRequest");
        		break;
        	case TestConstant.GetListGroupRequest:
        		holder.textViewReq.setText("GetListGroupRequest");
        		break;
        	case TestConstant.LoginRequest:
        		holder.textViewReq.setText("LoginRequest");
        		break;
        	case TestConstant.NearbyListRequest:
        		holder.textViewReq.setText("NearbyListRequest");
        		break;
        	case TestConstant.GetAreaListRequest:
        		holder.textViewReq.setText("GetAreaListRequest");
        		break;
        	default:
        		break;
        	}
        }
        
        private class ViewHolder {
            TextView textViewReq;
        }
    }
    
    private void ToTextView(final String response){
    	runOnUiThread(new Runnable() {
    	public void run() {
    	Intent i = new Intent(TestApiListActivity.this, TestRespActivity.class);
    	Bundle bundle = new Bundle();
    	bundle.putString("response", response);
    	i.putExtras(bundle);
        startActivity(i);
    	}
    	});
    }
    
    private void GetCategoryList() {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {
					final GetCategoryListResponse response = InternetUtils.request( 
							TestApiListActivity.this.getApplicationContext(), new GetCategoryListRequest(0, 
									1));
                    if (response != null) {
                    	ToTextView(response.toString());
                    }else{
                    	ToTextView("null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    
    private void getListGroup() {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {
					final GetListGroupResponse response = InternetUtils.request( 
							TestApiListActivity.this.getApplicationContext(), new GetListGroupRequest(Constant.CATEGORY_ALL, 
									Constant.AREA_ALL, 
									Constant.ORDER_DEFAULT, 
									Constant.CITY_SHANGHAI, 
									Constant.IS_NO_ORDER_CLOSE, 
									Constant.IS_HOLIDAY_CAN_USE_CLOSE));
                    if (response != null) {
                    	ToTextView(response.toString());
                    }else{
                    	ToTextView("null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    
	private void Login(final String email, final String pwd) {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {                	
					final LoginResponse response = InternetUtils.request( 
							TestApiListActivity.this.getApplicationContext(), new LoginRequest(pwd, email));//versionCode));
                    if (response != null) {
                    	ToTextView(response.toString());
                    }else{
                    	ToTextView("null");
                    }
                } catch (Exception e) {
                	runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        	Toast.makeText(TestApiListActivity.this.getApplicationContext(), getString(R.string.network_error_tips),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }));
    }
	
    private void getNearbyList(final float dest, final int category, final int order, final int city, final int is_no_order, final int is_holiday_can_use) {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {
					final NearbyListResponse response = InternetUtils.request( 
							TestApiListActivity.this.getApplicationContext(), new NearbyListRequest(39.92, 
									116.46, 
									dest, 
									category, 
									order, 
									city,
									is_no_order,
									is_holiday_can_use));
					if (response != null) {
                    	ToTextView(response.toString());
                    }else{
                    	ToTextView("null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    
    private void getAreaList(final int area, final int city) {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {
					final GetAreaListResponse response = InternetUtils.request( 
							TestApiListActivity.this.getApplicationContext(), new GetAreaListRequest(area, city));
					if (response != null) {
                    	ToTextView(response.toString());
                    }else{
                    	ToTextView("null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
