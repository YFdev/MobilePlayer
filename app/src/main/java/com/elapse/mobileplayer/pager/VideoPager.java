package com.elapse.mobileplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.domain.MediaItem;

import java.util.ArrayList;

/**
 * Created by YF_lala on 2018/12/7.
 * local video
 */

public class VideoPager extends BasePager {

    private ListView lv_video_pager;
    private TextView tv_noInfo;
    private LinearLayout ll_loading;
    private ArrayList<MediaItem> mMediaItems ;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mMediaItems != null && mMediaItems.size()>0){
                tv_noInfo.setVisibility(View.GONE);
                ll_loading.setVisibility(View.GONE);
                //set adapter
                lv_video_pager.setAdapter(new VideoPagerAdapter());
            }else {
                tv_noInfo.setVisibility(View.VISIBLE);
                ll_loading.setVisibility(View.GONE);
            }
            return true;
        }
    });
    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.video_pager,null);
        lv_video_pager = view.findViewById(R.id.lv_video_pager);
        tv_noInfo = view.findViewById(R.id.tv_noInfo);
        ll_loading = view.findViewById(R.id.ll_loading);
        mMediaItems = new ArrayList<>();
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getData();
    }

    class VideoPagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMediaItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mContext,R.layout.item_video_pager,null);
                holder = new ViewHolder();
//                holder.img = convertView.findViewById(R.id.img);
                holder.tv_size = convertView.findViewById(R.id.tv_video_size);
                holder.tv_name = convertView.findViewById(R.id.tv_video_name);
                holder.tv_duration = convertView.findViewById(R.id.tv_video_duration);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.img.setImageResource();
            holder.tv_duration.setText(String.valueOf(mMediaItems.get(position).getDuration()));
            holder.tv_name.setText(mMediaItems.get(position).getName());
            holder.tv_size.setText(String.valueOf(mMediaItems.get(position).getSize()));
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView img;
        TextView tv_name;
        TextView tv_size;
        TextView tv_duration;
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = mContext.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objects = {MediaStore.Video.Media.DISPLAY_NAME,//name in SDCard
                        MediaStore.Video.Media.DURATION,//the duration of video
                        MediaStore.Video.Media.SIZE,//
                        MediaStore.Video.Media.DATA};// the abs path of video
                Cursor cursor = contentResolver.query(uri, objects, null, null, null);
                if (cursor != null){
                    while (cursor.moveToNext()){
                        MediaItem mediaItem = new MediaItem();
                        String name = cursor.getString(0);
                        long duration = cursor.getLong(1);
                        long size = cursor.getLong(2);
                        String data = cursor.getString(3);
                        mediaItem.setName(name);
                        mediaItem.setData(data);
                        mediaItem.setDuration(duration);
                        mediaItem.setSize(size);
                        mMediaItems.add(mediaItem);
                    }
                    cursor.close();
                }
               mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}
