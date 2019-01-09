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
import com.elapse.mobileplayer.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import pl.droidsonroids.gif.GifImageView;

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
    private Utils utils;
    public Net_Audio_Adapter(Context context, List<NetAudioPagerBean.ListBean> mItems) {
        this.mItems = mItems;
        this.mContext = context;
        utils = new Utils();
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
        holder holder;
        if (convertView == null){
            holder = new holder();
            convertView = initView(itemType, holder);
            //初始化公共的视图
            initCommonView(convertView, itemType, holder);
            //设置tag
            convertView.setTag(holder);
        }else {
            holder = (holder) convertView.getTag();
        }
        NetAudioPagerBean.ListBean item = (NetAudioPagerBean.ListBean) getItem(position);
        bindData(itemType, holder, item);
        return convertView;
    }

    //根据item布局绑定数据
    private void bindData(int itemType, holder holder, NetAudioPagerBean.ListBean item) {
        switch (itemType) {
            case TYPE_VIDEO://视频
                bindData(holder, item);
                //第一个参数是视频播放地址，第二个参数是标题，第三参数是视频尺寸
                holder.jcv_videoplayer.setUp(item.getVideo().getVideo().get(0),
                        " ",
                        Jzvd.SCREEN_WINDOW_NORMAL);
                Glide.with(mContext).load(item.getVideo().getThumbnail().get(0)).into(holder.jcv_videoplayer.thumbImageView);
                String playCount = item.getVideo().getPlaycount() + "次播放";
                holder.tv_play_nums.setText(playCount);
                String videoDuration = utils.timeToString(
                        item.getVideo().getDuration() * 1000) + "";
                holder.tv_video_duration.setText(videoDuration);
                break;
            case TYPE_IMAGE://图片
                bindData(holder, item);
                holder.iv_image_icon.setImageResource(R.drawable.bg_item);
                int  height = item.getImage().getHeight()<= DensityUtil.getScreenHeight()*0.75 ?
                        item.getImage().getHeight(): (int) (DensityUtil.getScreenHeight() * 0.75);
                int width = item.getImage().getWidth();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        width,height);
                holder.iv_image_icon.setLayoutParams(params);
                if(item.getImage() != null &&  item.getImage().getBig()!= null &&
                        item.getImage().getBig().size() >0){
//                    x.image().bind(holder.iv_image_icon, item.getImage().getBig().get(0));
                    Glide.with(mContext).load(item.getImage().getBig().get(0)).
                            placeholder(R.drawable.bg_item).error(R.drawable.bg_item).
                            diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_image_icon);
                }
                break;
            case TYPE_TEXT://文字
                bindData(holder, item);
                break;
            case TYPE_GIF://gif
                bindData(holder, item);
//                System.out.println("item.getGif().getImages().get(0)" + item.getGif().getImages().get(0));
                Glide.with(mContext).load(item.getGif().getImages().get(0)).
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_image_gif);

                break;
            case TYPE_AD://软件广告
                break;
        }
        //设置文本
        holder.tv_context.setText(item.getText());
    }
//初始化公共部分
    private void initCommonView(View convertView, int itemType, holder holder) {
        switch (itemType) {
            case TYPE_VIDEO://视频
            case TYPE_IMAGE://图片
            case TYPE_TEXT://文字
            case TYPE_GIF://gif
                //加载除开广告部分的公共部分视图
                //user info
                holder.iv_headpic =  convertView.findViewById(R.id.iv_headpic);
                holder.tv_name =  convertView.findViewById(R.id.tv_name);
                holder.tv_time_refresh =  convertView.findViewById(R.id.tv_time_refresh);
                holder.iv_right_more =  convertView.findViewById(R.id.iv_right_more);
                //bottom
                holder.iv_video_kind =  convertView.findViewById(R.id.iv_video_kind);
                holder.tv_video_kind_text =  convertView.findViewById(R.id.tv_video_kind_text);
                holder.tv_shenhe_ding_number =  convertView.findViewById(R.id.tv_shenhe_ding_number);
                holder.tv_shenhe_cai_number =  convertView.findViewById(R.id.tv_shenhe_cai_number);
                holder.tv_posts_number =  convertView.findViewById(R.id.tv_posts_number);
                holder.ll_download =  convertView.findViewById(R.id.ll_download);
                break;
        }
        //中间公共部分 -所有的都有
        holder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
    }

    /**
     * 根据type初始化view
     * @param itemType 类型--》5
     * @param holder holder
     * @return
     */
    private View initView(int itemType, holder holder) {
        View convertView = null;
        switch (itemType) {
            case TYPE_VIDEO://视频
                convertView = View.inflate(mContext, R.layout.all_video_item, null);
                //在这里实例化特有的
                holder.tv_play_nums =  convertView.findViewById(R.id.tv_play_nums);
                holder.tv_video_duration =  convertView.findViewById(R.id.tv_video_duration);
                holder.iv_commant =  convertView.findViewById(R.id.iv_commant);
                holder.tv_commant_context =  convertView.findViewById(R.id.tv_commant_context);
                holder.jcv_videoplayer =  convertView.findViewById(R.id.jcv_videoplayer);
                break;
            case TYPE_IMAGE://图片
                convertView = View.inflate(mContext, R.layout.all_image_item, null);
                holder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;
            case TYPE_TEXT://文字
                convertView = View.inflate(mContext, R.layout.all_text_item, null);
                break;
            case TYPE_GIF://gif
                convertView = View.inflate(mContext, R.layout.all_gif_item, null);
                holder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);
                break;
            case TYPE_AD://软件广告
                convertView = View.inflate(mContext, R.layout.all_ad_item, null);
                holder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
                holder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;

        }
        return convertView;
    }
    private void bindData(holder holder, NetAudioPagerBean.ListBean item) {
        if(item.getU()!=null && item.getU().getHeader() != null && item.getU().getHeader().get(0)!=null){
            x.image().bind(holder.iv_headpic, item.getU().getHeader().get(0));
        }
        if(item.getU() != null&&item.getU().getName()!= null){
            holder.tv_name.setText(item.getU().getName()+"");
        }

        holder.tv_time_refresh.setText(item.getPasstime());

        //设置标签
        List<NetAudioPagerBean.ListBean.TagsBean> tagsEntities = item.getTags();
        if (tagsEntities != null && tagsEntities.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tagsEntities.size(); i++) {
                buffer.append(tagsEntities.get(i).getName() + " ");
            }
            holder.tv_video_kind_text.setText(buffer.toString());
        }

        //设置点赞，踩,转发
        holder.tv_shenhe_ding_number.setText(item.getUp());
        holder.tv_shenhe_cai_number.setText(item.getDown() + "");
        holder.tv_posts_number.setText(item.getForward()+"");

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
     * @return 5
     */
    @Override
    public int getViewTypeCount() {
        return 5;
    }

    static class holder {
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
        JzvdStd jcv_videoplayer;

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
