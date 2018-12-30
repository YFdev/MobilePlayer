package com.elapse.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.domain.NetAudioPagerBean;
import com.elapse.mobileplayer.domain.SearchBean;

import java.util.List;

/**
 *网络音乐适配器
 * Created by YF_lala on 2018/12/27.
 */

public class Net_Audio_Adapter extends BaseAdapter {

    private static final int TYPE_VIDEO = 0;//视频
    private static final int TYPE_IMAGE = 1;//图片
    private static final int TYPE_TEXT = 2;//文本
    private static final int TYPE_GIF = 3;//GIF
    private static final int TYPE_AD = 4;//广告

    private List<NetAudioPagerBean.ListBean> mItems;
    private Context mContext;

    public Net_Audio_Adapter(Context context, List<NetAudioPagerBean.ListBean> mItems) {
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
        int itemType = getItemViewType(position);
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            switch (itemType){
                case TYPE_VIDEO:

                    break;
                case TYPE_IMAGE:

                    break;
                case TYPE_TEXT:

                    break;
                case TYPE_GIF:

                    break;
                case TYPE_AD:

                    break;
            }
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    /**
     * 根据位置获取item类型
     * @param position 位置
     * @return 类型
     */
    @Override
    public int getItemViewType(int position) {
        NetAudioPagerBean.ListBean bean = mItems.get(position);
        String type = bean.getType();//video/text/image/gif/ad
        int itemType = -1;
        if ("video".equals(type)){
            itemType = TYPE_VIDEO;
        }else if ("image".equals(type)){
            itemType = TYPE_IMAGE;
        }else if ("text".equals(type)){
            itemType = TYPE_TEXT;
        }else if ("gif".equals(type)){
            itemType = TYPE_GIF;
        }else if ("ad".equals(type)){
            itemType = TYPE_AD;
        }
        return itemType;
    }

    /**
     * 获取item类型数量
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 5;
    }

    static class ViewHolder {
        //user_info
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;
        //bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        //中间公共部分 -所有的都有
        TextView tv_context;


        //Video
//        TextView tv_context;
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayer jcv_videoplayer;

        //Image
        ImageView iv_image_icon;
//        TextView tv_context;

        //Text
//        TextView tv_context;

        //Gif
        GifImageView iv_image_gif;
//        TextView tv_context;

        //软件推广
        Button btn_install;
//        TextView iv_image_icon;
        //TextView tv_context;


    }
}
