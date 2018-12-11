package com.elapse.mobileplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.systemVideoPlayer.SystemVideoPlayer;
import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.util.Utils;

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
    private Utils mUtils;

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
        mUtils = new Utils();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.video_pager,null);
        lv_video_pager = view.findViewById(R.id.lv_video_pager);
        //set onClickEvent
        lv_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItem mediaItem = mMediaItems.get(position);
                //跳转到播放页
//                Intent intent = new Intent(mContext, SystemVideoPlayer.class);
//                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//                mContext.startActivity(intent);
//                Intent intent1 = new Intent(); //隐式意图
//                intent1.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//                mContext.startActivity(intent1);
                //传递视频列表
                Intent intent = new Intent(mContext,SystemVideoPlayer.class);
//                intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
                Bundle b = new Bundle();
                b.putSerializable("video_list",mMediaItems);
                intent.putExtras(b);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
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
            MediaItem mediaItem = mMediaItems.get(position);
            holder.tv_duration.setText(mUtils.timeToString((int) mediaItem.getDuration()));
            holder.tv_name.setText(mediaItem.getName());
            holder.tv_size.setText(Formatter.formatFileSize(mContext,mediaItem.getSize()));
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
