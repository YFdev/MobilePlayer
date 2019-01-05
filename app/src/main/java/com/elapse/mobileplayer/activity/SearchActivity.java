package com.elapse.mobileplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.adapter.Search_Adapter;
import com.elapse.mobileplayer.domain.SearchBean;
import com.elapse.mobileplayer.util.Constants;
import com.elapse.mobileplayer.util.JsonParser;
import com.google.gson.Gson;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * APPID:5c1e2954
 * Created by YF_lala on 2018/12/22.
 */

public class SearchActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "SearchActivity";
    private EditText tv_search_input;//搜索框
    private ImageView iv_microphone;//麦克风
    private TextView btn_search;//搜索按钮
    private ProgressBar progressBar;//进度提示
    private ListView listView;//结果列表
    private TextView noData;//未返回信息
    private SpeechRecognizer mSpeechRecognizer;

    private  RecognizerDialog dialog;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private String url;
    private List<SearchBean.ResultBean> items;
    private Search_Adapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        initDialog();
        initView();
    }

//    private void initDialog() {
//        MyInitListener listener = new MyInitListener();
//        mSpeechRecognizer = SpeechRecognizer.createRecognizer(this, listener);
////        //2、设置参数
//        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE,"zh_cn");//普通话
//        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT,"mandarin");
//        //1、创建RecognizerDialog
//        dialog = new RecognizerDialog(this,listener);
////        //3、设置回调接口
//        dialog.setListener(new MyRecognizerDialogListener());
//    }

    private void initView() {
        tv_search_input = findViewById(R.id.tv_search_input);
        iv_microphone = findViewById(R.id.iv_microphone);
        btn_search = findViewById(R.id.btn_search);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.list_view);
        noData = findViewById(R.id.tv_nodata);
        iv_microphone.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        items = new ArrayList<>();
        mAdapter = new Search_Adapter(this,items);
        //设置适配器
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_microphone://语音输入
                if (reqPermissionSucceed()){
                    showDialog();
                }else {
                    Toast.makeText(SearchActivity.this,"无法访问麦克风",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_search://搜索
                //网络搜索
                searchNewsFromNet();
                break;
        }
    }

    private boolean reqPermissionSucceed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},2);
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showDialog();
                }else {
                    Toast.makeText(this,"无法访问麦克风",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 从新闻接口获取数据
     */
    private void searchNewsFromNet() {
        String text = tv_search_input.getText().toString().trim();
        if (!TextUtils.isEmpty(text)){
            try {
//                text = URLEncoder.encode(text,"utf-8");
                url = Constants.URL_SEARCH + text;
                //获取缓存
//                String result = CacheUtils.getValue(this,url);
//                if (! TextUtils.isEmpty(result)){
//                    processData(result);
//                    mAdapter.notifyDataSetChanged();
//                }
                getDataFromNet(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getDataFromNet(String url) {
//         final String key = url;
        //显示进度
        noData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //网络请求
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: "+result);
                //缓存
//                CacheUtils.putString(SearchActivity.this,key,result);
                SearchBean bean = processData(result);
                progressBar.setVisibility(View.GONE);
                items.clear();//发起请求前清空
                items = bean.getResult();
                if (items != null && items.size() > 0){
                    noData.setVisibility(View.GONE);
                }else {
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("没有您要搜索的内容...");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, "onError: "+ex.getMessage());
                noData.setText("出错了....");
                noData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinished() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private SearchBean processData(String result) {
        return new Gson().fromJson(result,SearchBean.class);
    }

    private void showDialog() {
        MyInitListener listener = new MyInitListener();
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(this, listener);
//        //2、设置参数 特朗普
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE,"zh_cn");//普通话
        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT,"mandarin");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,"0");
        //1、创建RecognizerDialog
        dialog = new RecognizerDialog(this,listener);
//        //3、设置回调接口
        dialog.setListener(new MyRecognizerDialogListener());
        //2、设置参数
//        dialog.setParameter(SpeechConstant.LANGUAGE,"zh_cn");//普通话
//        dialog.setParameter(SpeechConstant.ACCENT,"mandarin");
        //3、设置回调接口
//        dialog.setListener(new MyRecognizerDialogListener());
        dialog.show();// Attempt to invoke virtual method 'boolean com.iflytek.cloud.SpeechRecognizer.setParameter(java.lang.String, java.lang.String)' on a null object reference
    }

    class  MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS){
                Log.d(TAG, "onInit: failed");
            }
        }
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener{

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String resultString = recognizerResult.getResultString();
            Log.d(TAG, "onResult: "+resultString);
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(TAG, "onError: "+speechError.getErrorDescription());
            Toast.makeText(SearchActivity.this,"出错了...",Toast.LENGTH_SHORT).show();
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        tv_search_input.setText(resultBuffer.toString());
        tv_search_input.setSelection(tv_search_input.length());
    }
}
