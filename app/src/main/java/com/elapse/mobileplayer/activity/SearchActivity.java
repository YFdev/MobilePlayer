package com.elapse.mobileplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elapse.mobileplayer.R;

/**
 * APPID:5c1e2954
 * Created by YF_lala on 2018/12/22.
 */

public class SearchActivity extends Activity implements View.OnClickListener{

    private EditText tv_search_input;//搜索框
    private ImageView iv_microphone;//麦克风
    private TextView btn_search;//搜索按钮
    private ProgressBar progressBar;//进度提示
    private ListView listView;//结果列表
    private TextView noData;//未返回信息
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        tv_search_input = findViewById(R.id.tv_search_input);
        iv_microphone = findViewById(R.id.iv_microphone);
        btn_search = findViewById(R.id.btn_search);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.list_view);
        noData = findViewById(R.id.tv_nodata);
//        tv_search_input.setOnClickListener(this);//不需要点击事件
        iv_microphone.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_microphone://语音输入

                break;
            case R.id.btn_search://搜索

                break;
        }
    }
}
