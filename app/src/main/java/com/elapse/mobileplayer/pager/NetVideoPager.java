package com.elapse.mobileplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.activity.SystemVideoPlayer;
import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.util.CacheUtils;
import com.elapse.mobileplayer.util.Constants;
import com.elapse.mobileplayer.util.Utils;
import com.elapse.mobileplayer.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by YF_lala on 2018/12/7.
 * local video
 */

public class NetVideoPager extends BasePager {
    private static final String TAG = "NetVideoPager";

    @ViewInject(R.id.lv_video_pager)
    private XListView lv_video_pager;

    @ViewInject(R.id.tv_no_network)
    private TextView tv_no_network;

    @ViewInject(R.id.ll_loading)
    private LinearLayout ll_loading;

    @ViewInject(R.id.pb_loading)
    private ProgressBar pb_loading;

//    @ViewInject(R.id.store_house_ptr_frame)
//    private PtrFrameLayout mPtrFrame;
    //数据集合
    private ArrayList<MediaItem> mMediaItems;

    //适配器
    private NetVideoPagerAdapter mAdapter;
    //是否加载更多
    private boolean isLoadMore;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.net_video_pager,null);
        mAdapter = new NetVideoPagerAdapter();
//        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mContext);
//        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
//        mPtrFrame.setHeaderView(header);
//        mPtrFrame.addPtrUIHandler(header);
//        mPtrFrame.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return true;
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                mPtrFrame.
//            }
//        });
        //初始化
        lv_video_pager.setPullLoadEnable(true);
        lv_video_pager.setXListViewListener(new MyXListViewListener());
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
                //position - 1,因为下拉刷新的头也算一个item
                intent.putExtra("position",position - 1);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class MyXListViewListener implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            getMoreFromNet();
        }
    }

    private void getMoreFromNet() {

        RequestParams params = new RequestParams(Constants.URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: ");
                isLoadMore = true;
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isLoadMore = false;
                Log.d(TAG, "onError: "+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                isLoadMore = false;
                Log.d(TAG, "onCancelled: "+cex.getMessage());
            }

            @Override
            public void onFinished() {
                isLoadMore = false;
                Log.d(TAG, "onFinished: ");
            }
        });
    }

    private void onLoad() {
        lv_video_pager.stopRefresh();
        lv_video_pager.stopLoadMore();
        lv_video_pager.setRefreshTime("更新时间"+Utils.getSystemTime());
    }

    @Override
    public void initData() {
        super.initData();
        String savedJson = CacheUtils.getValue(mContext,Constants.URL);
        if (!TextUtils.isEmpty(savedJson)){
            processData(savedJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: ");
                isLoadMore = true;
                CacheUtils.putString(mContext,Constants.URL,result);
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isLoadMore = false;
//                tv_no_network.setVisibility(View.VISIBLE);
//                ll_loading.setVisibility(View.GONE);
                showData();
                Log.d(TAG, "onError: "+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                isLoadMore = false;
                Log.d(TAG, "onCancelled: "+cex.getMessage());
            }

            @Override
            public void onFinished() {
                isLoadMore = false;
                Log.d(TAG, "onFinished: ");
            }
        });
    }

    private void processData(String result) {
       if (!isLoadMore){
           mMediaItems = parsedJson(result);
           showData();
       }else {
           //加载更多
           //要将得到的数据添加到原来的集合中
           mMediaItems.addAll(parsedJson(result));
           //刷新适配器
           mAdapter.notifyDataSetChanged();
           onLoad();
           isLoadMore = false;
       }

    }

    private void showData() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            if (mMediaItems != null && mMediaItems.size()>0){
                tv_no_network.setVisibility(View.GONE);
                //set adapter
                lv_video_pager.setAdapter(mAdapter);
                onLoad();
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
            //使用xUtils加载缩略图
//            x.image().bind(holder.img,mediaItem.getImgUrl());
            //使用Glide
            Glide.with(mContext).load(mediaItem.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.nohistorydata)
                    .error(R.drawable.nohistorydata)
                    .into(holder.img);
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView img;
        TextView tv_name;
        TextView tv_desc;
    }
}
