package com.tugou.jgl.activity;

import android.os.Bundle;

import com.tugou.jgl.R;
import com.tugou.jgl.fragment.SubListFragment;
import com.tugou.jgl.utils.Debug;

public class GroupDetailActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.groupon_detail);

        mActionbar.setTitle(R.string.movie_title);
        Debug.LOGD("!!!!!!!!!!!!!!lon = " + JuGeiLiActivity.locationData.longitude);
        Debug.LOGD("!!!!!!!!!!!!!!lat = " + JuGeiLiActivity.locationData.latitude);
        //fragmentUpdate(R.id.sub_fl, new SubListFragment());
    }
}
