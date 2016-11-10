package com.maxi.chatui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

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

public class ImgMsgToItemDelegate extends BaseItemDelegate implements ItemViewDelegate<IMMessageBean> {

    public ImgMsgToItemDelegate(Context mContext, List<IMMessageBean> datas) {
        super(mContext, datas);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_imageto_list_item;
    }

    @Override
    public boolean isForViewType(IMMessageBean item, int position) {
        return item.getType() == ChatRvAdapter.TO_USER_IMG;
    }

    @Override
    public void convert(final ViewHolder holder, IMMessageBean imMessageBean, final int position) {
        holder.getView(R.id.tb_my_user_icon).setBackgroundResource(R.mipmap.grzx_tx_s);
        switch (imMessageBean.getSendState()) {
            case SENDING_OR_RECEIVEING:
                an = AnimationUtils.loadAnimation(mContext,
                        R.anim.update_loading_progressbar_anim);
                LinearInterpolator lin = new LinearInterpolator();
                an.setInterpolator(lin);
                an.setRepeatCount(-1);
                holder.setBackgroundRes(R.id.mysend_fail_img, R.mipmap.xsearch_loading);
                holder.getView(R.id.mysend_fail_img).startAnimation(an);
                an.startNow();
                holder.setVisible(R.id.mysend_fail_img, true);
                break;

            case COMPLETED:
                holder.getView(R.id.mysend_fail_img).clearAnimation();
                holder.getView(R.id.mysend_fail_img).setVisibility(View.GONE);
                break;

            case SENDERROR:
                holder.getView(R.id.mysend_fail_img).clearAnimation();
                holder.getView(R.id.mysend_fail_img)
                        .setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
                holder.getView(R.id.mysend_fail_img).setVisibility(View.VISIBLE);
                holder.getView(R.id.mysend_fail_img).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        if (sendErrorListener != null) {
                            sendErrorListener.onClick(position);
                        }
                    }

                });
                break;
            default:
                break;
        }
        /* time */
        if (position != 0) {
            String showTime = Utils.getTime(imMessageBean.getTime(), datas.get(position - 1).getTime());
            if (showTime != null) {
                holder.setVisible(R.id.mychat_time, true);
                holder.setText(R.id.mychat_time, showTime);
            } else {
                holder.setVisible(R.id.mychat_time, false);
            }
        } else {
            String showTime = Utils.getTime(imMessageBean.getTime(), null);
            holder.setVisible(R.id.mychat_time, true);
            holder.setText(R.id.mychat_time, showTime);
        }
        final String imageSrc = imMessageBean.getImageLocal() == null ? "" : imMessageBean
                .getImageLocal();
        final String imageUrlSrc = imMessageBean.getImageUrl() == null ? "" : imMessageBean
                .getImageUrl();
        final String imageIconUrl = imMessageBean.getImageIconUrl() == null ? ""
                : imMessageBean.getImageIconUrl();
        final int res;
        res = R.drawable.chatto_bg_focused;
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