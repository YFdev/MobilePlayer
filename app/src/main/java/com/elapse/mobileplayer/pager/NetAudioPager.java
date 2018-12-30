package com.elapse.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.adapter.Net_Audio_Adapter;
import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.domain.NetAudioPagerBean;
import com.elapse.mobileplayer.util.CacheUtils;
import com.elapse.mobileplayer.util.Constants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 网络音乐
 * Created by YF_lala on 2018/12/7.
 */

public class NetAudioPager extends BasePager{

    private static final String TAG = "NetAudioPager";

    @ViewInject(R.id.lv_audio_pager)
    private ListView mListView;

    @ViewInject(R.id.tv_no_network)
    private TextView tv_no_network;

    @ViewInject(R.id.ll_loading)
    private LinearLayout ll_loading;
    //数据源
    private List<NetAudioPagerBean.ListBean> list;
    //适配器
    private Net_Audio_Adapter mAdapter;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.net_audio_pager,null);
        x.view().inject(NetAudioPager.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //缓存
        String saveJson = CacheUtils.getValue(mContext,Constants.ALL_RES_URL);
        if (!TextUtils.isEmpty(saveJson)){
            //解析缓存数据
            processData(saveJson);
        }
        //联网
        getDataFromNet();
    }

    private void getDataFromNet() {
        ll_loading.setVisibility(View.VISIBLE);
        tv_no_network.setVisibility(View.GONE);
        RequestParams params = new RequestParams(Constants.ALL_RES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ll_loading.setVisibility(View.GONE);
                CacheUtils.putString(mContext,Constants.ALL_RES_URL,result);
                Log.d(TAG, "onSuccess: "+result);
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, "onError: "+ex.getMessage().trim());
                ll_loading.setVisibility(View.GONE);
                tv_no_network.setText("出错了...");
                tv_no_network.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG, "onCancelled: ");
                ll_loading.setVisibility(View.GONE);
            }

            @Override
            public void onFinished() {
                Log.d(TAG, "onFinished: ");
                ll_loading.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 解析数据，显示数据
     * @param result
     */
    private void processData(String result) {
        NetAudioPagerBean bean = parseData(result);
        list = bean.getList();
        if (list != null && list.size() > 0){
            //有数据
            tv_no_network.setVisibility(View.GONE);
            //设置适配器
            mAdapter = new Net_Audio_Adapter(mContext,list);
            mListView.setAdapter(mAdapter);
        }else {
            //没有数据
            tv_no_network.setText("没有数据...");
            tv_no_network.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Gson解析数据
     * @param result json
     * @return
     */
    private NetAudioPagerBean parseData(String result) {
        return new Gson().fromJson(result,NetAudioPagerBean.class);
    }
}
