/**
 * RRMenu.java
 */
package com.tugou.jgl.popupmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * @author Di Zhang Sep 11, 20129:37:17 AM
 */
class RRMenu {

    private Context mContext;
    private int mLayoutRes;
    private List<RRMenuItem> mMenuItemList;
    private int[] mOptionalItemIds;
    private int[] mVisibleItemIds;

    public RRMenu(Context context) {
        mContext = context;
        mMenuItemList = new ArrayList<RRMenuItem>();
    }

    public void addItem(int id, int icon, String title) {
        RRMenuItem item = new RRMenuItem(mContext, id, title);
        item.setIcon(icon);
        mMenuItemList.add(item);
    }

    public void addItem(int id, int icon, int titleRes) {
        RRMenuItem item = new RRMenuItem(mContext, id, titleRes);
        item.setIcon(icon);
        mMenuItemList.add(item);
    }

    public void addItem(RRMenuItem item) {
        mMenuItemList.add(item);
    }

    public void addMenuItems(List<RRMenuItem> list) {
        if (list != null && !list.isEmpty()) {
            mMenuItemList.addAll(list);
        }
    }

    public void setMenuRes(int layoutRes) {
        mLayoutRes = layoutRes;
    }

    public void setItemIds(int[] ids) {
        mOptionalItemIds = ids;
        mVisibleItemIds = ids;
    }

    public void setVisibleItemId(int[] ids) {
        mVisibleItemIds = ids;
    }

    public int getMenuRes() {
        return mLayoutRes;
    }

    public int[] getItemIds() {
        return mOptionalItemIds;
    }

    public int[] getVisibleItemIds() {
        return mVisibleItemIds;
    }

    public List<RRMenuItem> getMenuItemList() {
        return mMenuItemList;
    }
    
    public boolean hasItem(int id) {
        if (mMenuItemList.size() > 0) {
            for (RRMenuItem item : mMenuItemList) {
                if (item.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeItem(int id) {
        if (mMenuItemList.size() > 0) {
            for (RRMenuItem item : mMenuItemList) {
                if (item.getId() == id) {
                    mMenuItemList.remove(item);
                }
            }
        }
    }

    public void clear() {
        mLayoutRes = 0;
        mMenuItemList.clear();
    }
}
