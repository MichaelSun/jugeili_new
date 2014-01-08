/**
 * RRPopupMenuHelper.java
 */
package com.tugou.jgl.popupmenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import com.tugou.jgl.R;

import java.util.List;

/**
 * @author Di Zhang Sep 11, 20129:34:31 AM
 */
class RRPopupMenuHelper implements OnDismissListener {

    private Context mContext;
    private RRMenu mMenu;
    private View mAnchorView;
    private RRPopupMenu.RRMenuType mMenuType;
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private RRMenuAdapter mAdapter;
    private LayoutInflater mInflater;
    private ListView mListView;
    private int mPopupMaxWidth;
    private Animation mPushInAnimation;
    private Animation mPushOutAnimation;
    private Animation mFadeInAnimation;
    private Animation mFadeOutAnimation;
    private RelativeLayout mBgView;
    private RelativeLayout mContentView;
    private Handler mHandler = new Handler();
    private static final int DISMISS_DELAY = 300;

    private boolean mIsDismissing = false;

    private RRPopupMenu.OnRRMenuItemClickListener mOnItemClickListener;
    private RRPopupMenu.OnRRMenuDismissListener mOnMenuDismissListener;

    public RRPopupMenuHelper(Context context, RRMenu menu) {
        this(context, menu, null);
    }

    public RRPopupMenuHelper(Context context, RRMenu menu, View anchorView) {
        this(context, menu, anchorView, RRPopupMenu.RRMenuType.DROPDOWN);
    }

    public RRPopupMenuHelper(Context context, RRMenu menu, View anchorView, RRPopupMenu.RRMenuType type) {
        mContext = context;
        mMenu = menu;
        mAnchorView = anchorView;
        mMenuType = type;
        mInflater = LayoutInflater.from(mContext);
        mPopupMaxWidth = mContext.getResources().getDisplayMetrics().widthPixels / 2;

        mPushInAnimation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
        mPushOutAnimation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
        mFadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        mFadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
    }

    public void setAnchorView(View view) {
        mAnchorView = view;
    }

    public void setMenuType(RRPopupMenu.RRMenuType type) {
        mMenuType = type;
    }

    public void show() {
        if (isShowing()) {
            mPopupWindow.dismiss();
        }
        if (!tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public boolean tryShow() {
        if (mAnchorView == null) {
            return false;
        }

        final List<RRMenuItem> menuList = mMenu.getMenuItemList();
        int menuRes = mMenu.getMenuRes();
        if (menuList != null && !menuList.isEmpty()) {
            if (mMenuType == RRPopupMenu.RRMenuType.LOCATION) {
                mPopupView = mInflater.inflate(R.layout.popup_menu_location_def, null);

                mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.setOnDismissListener(this);

                mBgView = (RelativeLayout) mPopupView.findViewById(R.id.popup_menu_location_def_rl);
                mBgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                mContentView = (RelativeLayout) mPopupView.findViewById(R.id.popup_menu_location_def_content_rl);

                mListView = (ListView) mPopupView.findViewById(R.id.popup_menu_location_def_listview);
                mAdapter = new RRMenuAdapter(mContext, mMenu, mMenuType);
                mListView.setAdapter(mAdapter);

            } else if (mMenuType == RRPopupMenu.RRMenuType.DROPDOWN) {
                mPopupView = mInflater.inflate(R.layout.popup_menu_dropdown_def, null);

                mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setOnDismissListener(this);

                mAdapter = new RRMenuAdapter(mContext, mMenu, mMenuType);
                mPopupWindow.setWidth(Math.min(mPopupMaxWidth, measureContentWidth(mAdapter)));

                mContentView = (RelativeLayout) mPopupView.findViewById(R.id.popup_menu_dropdown_def_rl);

                mListView = (ListView) mPopupView.findViewById(R.id.popup_menu_dropdown_def_listview);
                mListView.setAdapter(mAdapter);
            }
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0 && position < menuList.size()) {
                        RRMenuItem menuItem = menuList.get(position);
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(menuItem);
                        }
                        dismiss();
                    }
                }
            });

        } else if (menuRes > 0) {
            mPopupView = mInflater.inflate(menuRes, null);
            if (mMenuType == RRPopupMenu.RRMenuType.LOCATION) {
                mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setOnDismissListener(this);
            } else if (mMenuType == RRPopupMenu.RRMenuType.DROPDOWN) {
                mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setOnDismissListener(this);
            }
            int[] itemIds = mMenu.getItemIds();
            int[] visibleItemIds = mMenu.getVisibleItemIds();
            for (int id : itemIds) {
                mPopupView.findViewById(id).setVisibility(View.GONE);
            }
            for (int visibleId : visibleItemIds) {
                mPopupView.findViewById(visibleId).setVisibility(View.VISIBLE);
            }
            for (final int itemId : itemIds) {
                mPopupView.findViewById(itemId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RRMenuItem item = new RRMenuItem(mContext);
                        item.setId(itemId);
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClick(item);
                        dismiss();
                    }
                });
            }
        }

        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.update();
        if (mMenuType == RRPopupMenu.RRMenuType.LOCATION) {
            mPopupWindow.showAtLocation(mAnchorView, Gravity.BOTTOM, 0, 0);
            mBgView.startAnimation(mFadeInAnimation);
        } else {
            mPopupWindow.showAsDropDown(mAnchorView);
        }
        mContentView.startAnimation(mPushInAnimation);

        return true;
    }

    public void setOnItemClickListener(RRPopupMenu.OnRRMenuItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnMenuDismissListener(RRPopupMenu.OnRRMenuDismissListener listener) {
        mOnMenuDismissListener = listener;
    }

    @Override
    public void onDismiss() {
        mPopupWindow = null;
        if (mOnMenuDismissListener != null) {
            mOnMenuDismissListener.onDismisss();
        }
    }

    public void dismiss() {
        if (isShowing() && !mIsDismissing) {
            mIsDismissing = true;
            if (mMenuType == RRPopupMenu.RRMenuType.LOCATION) {
                mBgView.startAnimation(mFadeOutAnimation);
            }
            mContentView.startAnimation(mPushOutAnimation);

            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isShowing()) {
                        mPopupWindow.dismiss();
                        mIsDismissing = false;
                    }
                }
            }, DISMISS_DELAY);
        }
    }

    public void dismissNow() {
        if (isShowing()) {
            mHandler.removeCallbacksAndMessages(null);
            mPopupWindow.dismiss();
        }
    }

    public void clearDismissEvent() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    private int measureContentWidth(ListAdapter adapter) {
        // Menus don't tend to be long, so this is more sane than it looks.
        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            // if (mMeasureParent == null) {
            // mMeasureParent = new FrameLayout(mContext);
            // }
            itemView = adapter.getView(i, itemView, (ViewGroup) mPopupView);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        return width;
    }

    private static class RRMenuAdapter extends BaseAdapter {

        private Context mContext;
        private RRMenu mMenu;
        private RRPopupMenu.RRMenuType mMenuType;

        public RRMenuAdapter(Context context, RRMenu menu, RRPopupMenu.RRMenuType menuType) {
            mContext = context;
            mMenu = menu;
            mMenuType = menuType;
        }

        @Override
        public int getCount() {
            return mMenu.getMenuItemList().size();
        }

        @Override
        public Object getItem(int position) {
            return mMenu.getMenuItemList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final RRMenuItem menuItem = mMenu.getMenuItemList().get(position);
            final int icon = menuItem.getIcon();
            final String title = menuItem.getTitle();
            final RRMenuItem.RRMenuItemStyle style = menuItem.getMenuItemStyle();

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                if (mMenuType == RRPopupMenu.RRMenuType.LOCATION) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_menu_location_def_item, null);
                    holder.mItemIcon = (ImageView) convertView.findViewById(R.id.popup_menu_location_def_item_icon_iv);
                    holder.mItemTv = (TextView) convertView.findViewById(R.id.popup_menu_location_def_item_tv);

                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_menu_dropdown_def_item, null);
                    holder.mItemTv = (TextView) convertView.findViewById(R.id.popup_menu_dropdown_def_item_tv);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!TextUtils.isEmpty(title)) {
                holder.mItemTv.setText(title);
            } else {
                holder.mItemTv.setText("");
            }
            if (mMenuType == RRPopupMenu.RRMenuType.LOCATION) {
                if (icon > 0) {
                    holder.mItemIcon.setVisibility(View.VISIBLE);
                    holder.mItemIcon.setImageResource(icon);
                } else {
                    holder.mItemIcon.setVisibility(View.GONE);
                }

                if (style == RRMenuItem.RRMenuItemStyle.STYLE_CANCEL) {
                    XmlResourceParser xpp = mContext.getResources().getXml(
                            R.color.popup_menu_location_cancel_text_color);
                    try {
                        ColorStateList csl = ColorStateList.createFromXml(mContext.getResources(), xpp);
                        holder.mItemTv.setTextColor(csl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    XmlResourceParser xpp = mContext.getResources().getXml(R.color.popup_menu_location_text_color);
                    try {
                        ColorStateList csl = ColorStateList.createFromXml(mContext.getResources(), xpp);
                        holder.mItemTv.setTextColor(csl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return convertView;
        }
    }

    private final static class ViewHolder {
        ImageView mItemIcon;
        TextView mItemTv;
    }

}