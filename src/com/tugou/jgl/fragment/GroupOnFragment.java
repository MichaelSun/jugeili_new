package com.tugou.jgl.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.plugin.common.utils.CustomThreadPool;
import com.plugin.common.utils.CustomThreadPool.TaskWrapper;
import com.plugin.internet.InternetUtils;
import com.tugou.jgl.R;
import com.tugou.jgl.activity.GroupDetailActivity;
import com.tugou.jgl.activity.LoginActivity;
import com.tugou.jgl.adapter.GrouponListAdapter;
import com.tugou.jgl.adapter.SubListAdater;
import com.tugou.jgl.api.GetCategoryListRequest;
import com.tugou.jgl.api.GetCategoryListResponse;
import com.tugou.jgl.api.GetListGroupRequest;
import com.tugou.jgl.api.GetListGroupResponse;
import com.tugou.jgl.api.GetListGroupResponse.GroupInfo;
import com.tugou.jgl.popupmenu.RRMenuItem;
import com.tugou.jgl.popupmenu.RRPopupMenu;
import com.tugou.jgl.popupmenu.RRPopupMenu.OnRRMenuItemClickListener;
import com.tugou.jgl.utils.Constant;
import com.tugou.jgl.utils.Debug;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GroupOnFragment extends Fragment {
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mlistView;
    private RRPopupMenu mPopupMenuCategory;
    private RRPopupMenu mPopupMenuLocation;
    private RRPopupMenu mPopupMenuOrder;
    private GroupInfo[] groupInfo;

    private static final int SET_LIST = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_LIST:
                    mlistView.setAdapter(new GrouponListAdapter(getActivity().getLayoutInflater(), groupInfo));
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groupon_list, null);

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.fragment_list);
        mlistView = mPullToRefreshListView.getRefreshableView();

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setShowIndicator(false);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        //mHandler.sendEmptyMessage(SET_LIST);
        getListGroup();
        initPopupMenuOrder();
        initPopupMenuCategory();
        initPopupMenuLocation();
        initUIView(view);
        
//        mPullToRefreshListView.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(null, LoginActivity.class);
//				startActivity(intent);
//				
//			}
//        	
//        });

        return view;
    }
    
    private void initPopupMenuOrder() {
    	mPopupMenuOrder = new RRPopupMenu(getActivity().getApplicationContext(), RRPopupMenu.RRMenuType.DROPDOWN);
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1000, getString(R.string.order_default), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1001, getString(R.string.order_near), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1002, getString(R.string.order_comment), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1003, getString(R.string.order_new), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1004, getString(R.string.order_popu), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1005, getString(R.string.order_low_price), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuOrder.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1006, getString(R.string.order_high_price), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    }
    
    private void initPopupMenuCategory() {
    	mPopupMenuCategory = new RRPopupMenu(getActivity().getApplicationContext(), RRPopupMenu.RRMenuType.DROPDOWN);
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1000, getString(R.string.order_default), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1001, getString(R.string.order_near), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1002, getString(R.string.order_comment), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1003, getString(R.string.order_new), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1004, getString(R.string.order_popu), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1005, getString(R.string.order_low_price), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1006, getString(R.string.order_high_price), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    }
    
    private void initPopupMenuLocation() {
    	mPopupMenuLocation = new RRPopupMenu(getActivity().getApplicationContext(), RRPopupMenu.RRMenuType.DROPDOWN);
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1000, getString(R.string.order_default), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1001, getString(R.string.order_near), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1002, getString(R.string.order_comment), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1003, getString(R.string.order_new), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1004, getString(R.string.order_popu), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1005, getString(R.string.order_low_price), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	mPopupMenuCategory.addItem(new RRMenuItem(getActivity().getApplicationContext(), 1006, getString(R.string.order_high_price), RRMenuItem.RRMenuItemStyle.STYLE_NORMAL));
    	
    	mPopupMenuCategory.setOnRRMenuItemClickListener(new OnRRMenuItemClickListener(){

			@Override
			public void onItemClick(RRMenuItem menuItem) {
				// TODO Auto-generated method stub
				
				
			}
    		
    	});
    }
    
    private void initUIView(View root) {
        final View cate = root.findViewById(R.id.category);
        cate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	mPopupMenuCategory.show(cate);
            }
        });
        
        final View dest = root.findViewById(R.id.dest);
        dest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	mPopupMenuOrder.show(dest);
            }
        });
        
        final View mydest = root.findViewById(R.id.my_dest);
        mydest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	mPopupMenuOrder.show(mydest);
            }
        }); 
        
        mlistView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(GroupOnFragment.this.getActivity(), GroupDetailActivity.class);
                startActivity(i);
			}
        	
        });
    }
    
    private void getListGroup() {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {
					final GetListGroupResponse response = InternetUtils.request( 
							GroupOnFragment.this.getActivity().getApplicationContext(), new GetListGroupRequest(Constant.CATEGORY_ALL, 
									Constant.AREA_ALL, 
									Constant.ORDER_DEFAULT, 
									Constant.CITY_SHANGHAI, 
									Constant.IS_NO_ORDER_CLOSE, 
									Constant.IS_HOLIDAY_CAN_USE_CLOSE));
                    if (response != null) {
//                    	if(response.result.groupInfo.length != 0){
//                    		groupInfo = response.result.groupInfo
//                    	}
                    	groupInfo = response.groupInfo;
                    	//GetCategoryList();
                    	mHandler.sendEmptyMessage(SET_LIST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    
    private void GetCategoryList() {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {
					final GetCategoryListResponse response = InternetUtils.request( 
							GroupOnFragment.this.getActivity().getApplicationContext(), new GetCategoryListRequest(0, 
									1));
                    if (response != null) {
//                    	groupInfo = response.groupInfo;
//                    	mHandler.sendEmptyMessage(SET_LIST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    
}
