/**
 * RRMenuItem.java
 */
package com.tugou.jgl.popupmenu;

import android.content.Context;

/**
 * @author Di Zhang Sep 11, 20129:37:25 AM
 */
public class RRMenuItem {

    private Context mContext;
    private int mId;
    private int mIconRes;
    private String mTitle;
    private RRMenuItemStyle mStyle;

    public enum RRMenuItemStyle {
        STYLE_NORMAL, STYLE_CANCEL
    }

    public RRMenuItem(Context context) {
        this(context, -1, 0);
    }

    public RRMenuItem(Context context, int id, int titleRes) {
        this(context, id, titleRes, RRMenuItemStyle.STYLE_NORMAL);
    }

    public RRMenuItem(Context context, int id, String title) {
        this(context, id, title, RRMenuItemStyle.STYLE_NORMAL);
    }

    public RRMenuItem(Context context, int id, int titleRes, RRMenuItemStyle style) {
        this(context, id, context.getString(titleRes), style);
    }

    public RRMenuItem(Context context, int id, String title, RRMenuItemStyle style) {
        this(context, id, 0, title, style);
    }

    public RRMenuItem(Context context, int id, int iconRes, String title, RRMenuItemStyle style) {
        this.mContext = context;
        this.mId = id;
        this.mIconRes = iconRes;
        this.mTitle = title;
        this.mStyle = style;
    }

    public RRMenuItem(Context context, int id, int iconRes, int titleRes, RRMenuItemStyle style) {
        this(context, id, iconRes, context.getString(titleRes), style);
    }

    public void setId(int id) {
        mId = id;
    }

    public void setIcon(int iconRes) {

    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTitle(int titleRes) {
        mTitle = mContext.getResources().getString(titleRes);
    }

    public void setMenuItemStyle(RRMenuItemStyle style) {
        mStyle = style;
    }

    public int getId() {
        return this.mId;
    }

    public int getIcon() {
        return mIconRes;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public RRMenuItemStyle getMenuItemStyle() {
        return mStyle;
    }

}
