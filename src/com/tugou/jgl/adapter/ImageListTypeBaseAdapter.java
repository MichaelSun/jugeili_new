package com.tugou.jgl.adapter;

import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 13-8-28
 * Time: PM2:39
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageListTypeBaseAdapter extends BaseAdapter {

    protected ArrayList<ImageView> mShowImageList = new ArrayList<ImageView>();

    abstract void onHide();

}
