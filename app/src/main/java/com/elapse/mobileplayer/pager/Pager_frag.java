package com.elapse.mobileplayer.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elapse.mobileplayer.base.BasePager;

/**
 * Created by YF_lala on 2018/12/7.
 */

public class Pager_frag extends Fragment {

    private BasePager mBasePager;
    public Pager_frag() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBasePager != null){
            return mBasePager.rootView;
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setBasePager(BasePager basePager){
        mBasePager = basePager;
    }
}
