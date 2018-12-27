package com.elapse.mobileplayer.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.activity.SearchActivity;

/**
 *
 * Created by YF_lala on 2018/12/8.
 */

public class TitleBar extends LinearLayout {

    private Context mContext;
    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.title_bar, this);
        TextView tv_search = view.findViewById(R.id.tv_search);
        TextView rl_game = view.findViewById(R.id.iv_game);
        ImageView iv_history = view.findViewById(R.id.iv_history);
        tv_search.setOnClickListener(new MyOnClickListener());
        rl_game.setOnClickListener(new MyOnClickListener());
        iv_history.setOnClickListener(new MyOnClickListener());
        mContext = context;
    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        tv_search = getChildAt(1);
//        rl_game = getChildAt(2);
//        iv_history = getChildAt(3);
//        tv_search.setOnClickListener(new MyOnClickListener());
//        rl_game.setOnClickListener(new MyOnClickListener());
//        iv_history.setOnClickListener(new MyOnClickListener());
//    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_search:
//                    Toast.makeText(mContext,"search",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext,SearchActivity.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.rl_game:
                    Toast.makeText(mContext,"game",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_history:
                    Toast.makeText(mContext,"history",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
