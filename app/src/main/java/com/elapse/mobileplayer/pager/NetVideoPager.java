package com.elapse.mobileplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.activity.SystemVideoPlayer;
import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by YF_lala on 2018/12/7.
 * local video
 */

public class NetVideoPager extends BasePager {
    private static final String TAG = "NetVideoPager";

    @ViewInject(R.id.lv_video_pager)
    private ListView lv_video_pager;

    @ViewInject(R.id.tv_no_network)
    private TextView tv_no_network;

    @ViewInject(R.id.ll_loading)
    private LinearLayout ll_loading;

    @ViewInject(R.id.pb_loading)
    private ProgressBar pb_loading;
    //数据集合
    private ArrayList<MediaItem> mMediaItems;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.net_video_pager,null);
        //初始化
        x.view().inject(this,view);
        lv_video_pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MediaItem mediaItem = mMediaItems.get(position);
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
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        RequestParams params = new RequestParams(Constants.URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: ");
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, "onError: "+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG, "onCancelled: "+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.d(TAG, "onFinished: ");
            }
        });
    }

    private void processData(String result) {
        mMediaItems = parsedJson(result);
        if (mMediaItems != null && mMediaItems.size() > 0){
            if (mMediaItems != null && mMediaItems.size()>0){
                tv_no_network.setVisibility(View.GONE);
                //set adapter
                lv_video_pager.setAdapter(new NetVideoPagerAdapter());
            }else {
                tv_no_network.setVisibility(View.VISIBLE);
//                ll_loading.setVisibility(View.GONE);
            }
            ll_loading.setVisibility(View.GONE);
        }
    }

    /**
     * 解析json
     * 1、用系统接口
     * 2、第三方解析工具：gson,fastJson,jackson
     * @param result json
     * @return
     */
    private ArrayList<MediaItem> parsedJson(String result) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
//            JSONArray array = jsonObject.getJSONArray("trailers");//如果trailers字段不存在，会崩溃
            JSONArray array = jsonObject.optJSONArray("trailers");//不会崩溃
            if (array != null && array.length() >0){
                for (int i = 0; i < array.length(); i++){
                    JSONObject item = (JSONObject) array.get(i);
                    if (item != null){
                        MediaItem mediaItem = new MediaItem();
                        String movieName = item.optString("movieName");
                        String videoTitle = item.optString("videoTitle");
                        String imgUrl = item.optString("coverImg");
                        String videoUrl = item.optString("url");
                        //高清地址
//                        String highQUrl = item.optString("hightUrl");
                        mediaItem.setName(movieName);
                        mediaItem.setDesc(videoTitle);
                        mediaItem.setImgUrl(imgUrl);
                        mediaItem.setData(videoUrl);
                        mMediaItems.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }

    class NetVideoPagerAdapter extends BaseAdapter {

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
                convertView = View.inflate(mContext,R.layout.net_item_video_pager,null);
                holder = new ViewHolder();
                holder.tv_desc = convertView.findViewById(R.id.tv_description);
                holder.tv_name = convertView.findViewById(R.id.tv_video_name);
                holder.img = convertView.findViewById(R.id.img_video_desc);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            MediaItem mediaItem = mMediaItems.get(position);
            holder.tv_name.setText(mediaItem.getName());
            holder.tv_desc.setText(mediaItem.getDesc());
            x.image().bind(holder.img,mediaItem.getImgUrl());
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView img;
        TextView tv_name;
        TextView tv_desc;
    }
}
