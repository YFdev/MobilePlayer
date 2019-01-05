package com.elapse.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.domain.SearchBean;

import java.util.List;

/**
 *
 * Created by YF_lala on 2018/12/27.
 */

public class Search_Adapter extends BaseAdapter {

    private List<SearchBean.ResultBean> mItems;
    private Context mContext;

    public Search_Adapter( Context context,List<SearchBean.ResultBean> mItems) {
        this.mItems = mItems;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.net_item_audio_pager,null);
            holder = new ViewHolder();
            holder.tv_desc = convertView.findViewById(R.id.tv_description);
            holder.tv_name = convertView.findViewById(R.id.tv_video_name);
            holder.img = convertView.findViewById(R.id.img_video_desc);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchBean.ResultBean resultBean = mItems.get(position);
        holder.tv_name.setText(resultBean.getTitle());
        holder.tv_desc.setText(resultBean.getContent());
        //使用xUtils加载缩略图
//            x.image().bind(holder.img,mediaItem.getImgUrl());
        //使用Glide
        Glide.with(mContext).load(resultBean.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.nohistorydata)
                .error(R.drawable.nohistorydata)
                .into(holder.img);
        return convertView;
    }
    class ViewHolder {
        ImageView img;
        TextView tv_name;
        TextView tv_desc;
    }
}
