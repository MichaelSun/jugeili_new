package com.tugou.jgl.activity;

import android.app.*;
import android.os.Bundle;
import android.view.MenuItem;
import com.tugou.jgl.R;

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 13-8-15
 * Time: PM3:22
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity {

    protected ActionBar mActionbar;

    protected Fragment mPageFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setBackgroundDrawableResource(R.drawable.transparent);

        init();
    }

    protected void init() {
        mActionbar = getActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setDisplayShowTitleEnabled(true);
        mActionbar.setDisplayShowHomeEnabled(true);
    }

    protected void fragmentUpdate(int id, Fragment fragment) {
        if (fragment != null) {
            FragmentManager manager = this.getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(id, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}