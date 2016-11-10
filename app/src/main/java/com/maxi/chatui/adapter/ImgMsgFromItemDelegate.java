package com.maxi.chatui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.maxi.chatui.R;
import com.maxi.chatui.entity.IMMessageBean;
import com.maxi.chatui.ui.ImageViewSingleActivity;
import com.maxi.chatui.util.Utils;
import com.maxi.chatui.widget.BubbleImageView;
import com.maxi.chatui.widget.CustomShapeTransformation;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Mao Jiqing on 2016/11/3.
 */

public class ImgMsgFromItemDelegate extends BaseItemDelegate implements ItemViewDelegate<IMMessageBean> {

    public ImgMsgFromItemDelegate(Context mContext, List<IMMessageBean> datas) {
        super(mContext, datas);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_imagefrom_list_item;
    }

    @Override
    public boolean isForViewType(IMMessageBean item, int position) {
        return item.getType() == ChatRvAdapter.FROM_USER_IMG;
    }

    @Override
    public void convert(final ViewHolder holder, IMMessageBean imMessageBean, final int position) {
        holder.setBackgroundRes(R.id.tb_other_user_icon, R.mipmap.tongbao_hiv);
        /* time */
        if (position != 0) {
            String showTime = Utils.getTime(imMessageBean.getTime(), datas.get(position - 1).getTime());
            if (showTime != null) {
                holder.setVisible(R.id.chat_time, true);
                holder.setText(R.id.chat_time, showTime);
            } else {
                holder.setVisible(R.id.chat_time, false);
            }
        } else {
            String showTime = Utils.getTime(imMessageBean.getTime(), null);
            holder.setVisible(R.id.chat_time, true);
            holder.setText(R.id.chat_time, showTime);
        }
        final String imageSrc = imMessageBean.getImageLocal() == null ? "" : imMessageBean
                .getImageLocal();
        final String imageUrlSrc = imMessageBean.getImageUrl() == null ? "" : imMessageBean
                .getImageUrl();
        final String imageIconUrl = imMessageBean.getImageIconUrl() == null ? ""
                : imMessageBean.getImageIconUrl();
        final int res;
        res = R.drawable.chatfrom_bg_focused;
        Glide.with(mContext).load(imageSrc).transform(new CustomShapeTransformation(mContext, res)).into((BubbleImageView) holder.getView(R.id.image_message));
        ((BubbleImageView) holder.getView(R.id.image_message)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
//                stopPlayVoice();
                Intent intent = new Intent(mContext, ImageViewSingleActivity.class);
                intent.putExtra("image", imageSrc);
                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, "share").toBundle());
            }

        });
    }
}