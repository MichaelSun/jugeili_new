/**
 * RRPopupMenu.java
 */
package com.tugou.jgl.popupmenu;

import java.util.List;

import android.content.Context;
import android.view.View;

/**
 * @author Di Zhang Sep 11, 20129:34:03 AM
 */
public class RRPopupMenu {

    public interface OnRRMenuItemClickListener {

        public void onItemClick(RRMenuItem menuItem);
    }

    public interface OnRRMenuDismissListener {

        public void onDismisss();
    }

    public static final String TAG = "RRPopupMenu";

    public enum RRMenuType {
        DROPDOWN, LOCATION
    }

    private Context mContext;
    private RRMenuType mType;
    private RRPopupMenuHelper mHelper;
    private RRMenu mMenu;
    private OnRRMenuItemClickListener mOnRRMenuItemClickListener;
    private OnRRMenuDismissListener mOnRRMenuDismissListener;

    public RRPopupMenu(Context context) {
        this(context, RRMenuType.DROPDOWN);
    }

    /**
     * 
     * @param context
     * @param menuType
     */
    public RRPopupMenu(Context context, RRMenuType menuType) {
        this(context, null, menuType);
    }

    public RRPopupMenu(Context context, View anchorView, RRMenuType menuType) {
        mContext = context;
        mType = menuType;
        mMenu = new RRMenu(context);
        mHelper = new RRPopupMenuHelper(mContext, mMenu, anchorView, mType);
    }

    public void setAnchorView(View view) {
        mHelper.setAnchorView(view);
    }

    public void setOnRRMenuItemClickListener(OnRRMenuItemClickListener listener) {
        mOnRRMenuItemClickListener = listener;
        mHelper.setOnItemClickListener(mOnRRMenuItemClickListener);
    }

    public void setOnRRMenuDismissListener(OnRRMenuDismissListener listener) {
        mOnRRMenuDismissListener = listener;
        mHelper.setOnMenuDismissListener(mOnRRMenuDismissListener);
    }

    /**
     * 自定义布局
     * 
     * @param menuRes
     * @param itemIds
     *            可点item的id
     */
    public void inflate(int menuRes, int[] itemIds) {
        mMenu.setMenuRes(menuRes);
        mMenu.setItemIds(itemIds);
    }

    /**
     * 自定义布局中可见的item的id
     * 
     * @param ids
     */
    public void setVisibleItemIds(int[] ids) {
        mMenu.setVisibleItemId(ids);
    }

    public void addItems(List<RRMenuItem> itemList) {
        mMenu.addMenuItems(itemList);
    }

    public void addItem(RRMenuItem item) {
        mMenu.addItem(item);
    }

    public void addItem(int id, String title) {
        mMenu.addItem(id, 0, title);
    }

    public void addItem(int id, int titleRes) {
        mMenu.addItem(id, 0, titleRes);
    }

    public void addItem(int id, int icon, int titleRes) {
        mMenu.addItem(id, icon, titleRes);
    }

    public void addItem(int id, int icon, String title) {
        mMenu.addItem(id, icon, title);
    }

    public void removeItem(int id) {
        mMenu.removeItem(id);
    }
    
    public boolean hasItem(int id) {
        return mMenu.hasItem(id);
    }

    public void clear() {
        mMenu.clear();
    }

    public void show() {
        mHelper.show();
    }

    public void show(View view) {
        mHelper.setAnchorView(view);
        mHelper.show();
    }

    public boolean isShow() {
        if (mHelper != null && mHelper.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    public void dismiss() {
        mHelper.dismiss();
    }

    public void dismissNow() {
        mHelper.dismissNow();
    }

    public void clearDismiss() {
        mHelper.clearDismissEvent();
    }
}
