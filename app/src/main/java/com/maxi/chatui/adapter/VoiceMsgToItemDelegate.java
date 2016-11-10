package com.maxi.chatui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.maxi.chatui.R;
import com.maxi.chatui.entity.IMMessageBean;
import com.maxi.chatui.util.IMAudioManager;
import com.maxi.chatui.util.Utils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Mao Jiqing on 2016/11/3.
 */

public class VoiceMsgToItemDelegate extends BaseItemDelegate implements ItemViewDelegate<IMMessageBean> {

    public VoiceMsgToItemDelegate(Context mContext, List<IMMessageBean> datas) {
        super(mContext, datas);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_voiceto_list_item;
    }

    @Override
    public boolean isForViewType(IMMessageBean item, int position) {
        return item.getType() == ChatRvAdapter.TO_USER_VOICE;
    }

    @Override
    public void convert(final ViewHolder holder, final IMMessageBean imMessageBean, final int position) {
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
        AnimationDrawable drawable;
        holder.getView(R.id.id_recorder_anim).setId(position);
        if (position == voicePlayPosition) {
            holder.getView(R.id.id_recorder_anim)
                    .setBackgroundResource(R.mipmap.adj);
            holder.getView(R.id.id_recorder_anim)
                    .setBackgroundResource(R.drawable.voice_play_send);
            drawable = (AnimationDrawable) holder.getView(R.id.id_recorder_anim)
                    .getBackground();
            drawable.start();
        } else {
            holder.getView(R.id.id_recorder_anim)
                    .setBackgroundResource(R.mipmap.adj);
        }
        holder.getView(R.id.voice_group).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                holder.getView(R.id.id_recorder_anim).setBackgroundResource(R.mipmap.adj);
//                stopPlayVoice();
                voicePlayPosition = holder.getView(R.id.id_recorder_anim).getId();
                AnimationDrawable drawable;
                holder.getView(R.id.id_recorder_anim)
                        .setBackgroundResource(R.drawable.voice_play_send);
                drawable = (AnimationDrawable) holder.getView(R.id.id_recorder_anim)
                        .getBackground();
                drawable.start();
                String voicePath = imMessageBean.getUserVoicePath() == null ? imMessageBean.getUserVoiceUrl()
                        : imMessageBean.getUserVoicePath();
                IMAudioManager.playSound(voicePath, new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voicePlayPosition = -1;
                        holder.getView(R.id.id_recorder_anim)
                                .setBackgroundResource(R.mipmap.adj);
                    }
                });
            }

        });
        float voiceTime = imMessageBean.getUserVoiceTime();
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.setText(R.id.voice_time, f1 + "\"");
        ViewGroup.LayoutParams lParams = holder.getView(R.id.voice_image)
                .getLayoutParams();
        lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
                * imMessageBean.getUserVoiceTime());
        holder.getView(R.id.voice_image).setLayoutParams(lParams);
    }
}
