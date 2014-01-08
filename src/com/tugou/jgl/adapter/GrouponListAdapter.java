package com.tugou.jgl.adapter;

import com.plugin.common.view.WebImageView;
import com.tugou.jgl.R;
import com.tugou.jgl.api.GetListGroupResponse.GroupInfo;
import com.tugou.jgl.utils.Constant;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GrouponListAdapter extends ImageListTypeBaseAdapter {

    private LayoutInflater mLayoutInflater;
    private GroupInfo[] groupInfo;

    public GrouponListAdapter(LayoutInflater inflater, GroupInfo[] groupInfo) {
        mLayoutInflater = inflater;
        this.groupInfo = groupInfo;
        
    }

    @Override
    void onHide() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return groupInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = convertView;
        ViewHolder holder = null;
        if (ret == null) {
            ret = mLayoutInflater.inflate(R.layout.sublist_item, null);
            holder = new ViewHolder();
            holder.coverImageView = (WebImageView) ret.findViewById(R.id.cover);
            holder.tv_title = (TextView)ret.findViewById(R.id.title);
            holder.tv_description = (TextView)ret.findViewById(R.id.desc);
            holder.tv_price = (TextView)ret.findViewById(R.id.count);
            holder.tv_regular_price = (TextView)ret.findViewById(R.id.origin_count);
            holder.tv_num_bought = (TextView)ret.findViewById(R.id.dest);
            holder.free_tips = (ImageView)ret.findViewById(R.id.free_tips);
            holder.new_icon = (ImageView)ret.findViewById(R.id.new_icon);
             
            ret.setTag(holder);
        } else {
            holder = (ViewHolder) ret.getTag();
        }

        
//        holder.coverImageView.setImageURI(new Uri.Builder()
//                      .path("http://ww4.sinaimg.cn/bmiddle/6aadc52dgw1e827avbgzyj21kw11x129.jpg").build());
        holder.coverImageView.setImageURI(new Uri.Builder()
        .path(groupInfo[position].cover).build());
        holder.tv_title.setText(groupInfo[position].name);
        holder.tv_description.setText(groupInfo[position].description);
        holder.tv_price.setText(groupInfo[position].price + mLayoutInflater.getContext().getString(R.string.tuangou_yuan));
        holder.tv_regular_price.setText(groupInfo[position].regular_price + mLayoutInflater.getContext().getString(R.string.tuangou_yuan));
        holder.tv_num_bought.setText(groupInfo[position].num_bought + mLayoutInflater.getContext().getString(R.string.tuangou_ren));
        
        if(Integer.valueOf(groupInfo[position].is_new) == Constant.IS_NEW){
        	holder.new_icon.setVisibility(View.VISIBLE);
        }        

        return ret;
    }

    private static class ViewHolder {
        public WebImageView coverImageView;
        TextView tv_title;
        TextView tv_description;
        TextView tv_price;
        TextView tv_regular_price;
        TextView tv_num_bought;
        ImageView free_tips;
        ImageView new_icon;
        
    }

}
