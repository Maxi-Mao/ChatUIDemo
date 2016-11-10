package com.maxi.chatui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;

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

public class VoiceMsgFromItemDelegate extends BaseItemDelegate implements ItemViewDelegate<IMMessageBean> {

    public VoiceMsgFromItemDelegate(Context mContext, List<IMMessageBean> datas) {
        super(mContext, datas);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_voicefrom_list_item;
    }

    @Override
    public boolean isForViewType(IMMessageBean item, int position) {
        return item.getType() == ChatRvAdapter.FROM_USER_VOICE;
    }

    @Override
    public void convert(final ViewHolder holder, final IMMessageBean imMessageBean, final int position) {
        holder.getView(R.id.tb_other_user_icon).setBackgroundResource(R.mipmap.tongbao_hiv);
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
        if (imMessageBean.isUserVoiceReaded()) {
            holder.getView(R.id.receiver_voice_unread).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.receiver_voice_unread).setVisibility(View.VISIBLE);
        }
        AnimationDrawable drawable;
        holder.getView(R.id.id_receiver_recorder_anim).setId(position);
        if (position == voicePlayPosition) {
            holder.getView(R.id.id_receiver_recorder_anim)
                    .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
            holder.getView(R.id.id_receiver_recorder_anim)
                    .setBackgroundResource(R.drawable.voice_play_receiver);
            drawable = (AnimationDrawable) holder.getView(R.id.id_receiver_recorder_anim)
                    .getBackground();
            drawable.start();
        } else {
            holder.getView(R.id.id_receiver_recorder_anim)
                    .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
        }
        holder.getView(R.id.voice_group).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.getView(R.id.receiver_voice_unread) != null)
                    holder.getView(R.id.receiver_voice_unread).setVisibility(View.GONE);
                holder.getView(R.id.id_receiver_recorder_anim)
                        .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
//                stopPlayVoice();
                voicePlayPosition = holder.getView(R.id.id_receiver_recorder_anim).getId();
                AnimationDrawable drawable;
                holder.getView(R.id.id_receiver_recorder_anim)
                        .setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) holder.getView(R.id.id_receiver_recorder_anim)
                        .getBackground();
                drawable.start();
                String voicePath = imMessageBean.getUserVoicePath() == null ? "" : imMessageBean
                        .getUserVoicePath();
                imMessageBean.setUserVoiceReaded(true);
                IMAudioManager.playSound(voicePath, new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voicePlayPosition = -1;
                        holder.getView(R.id.id_receiver_recorder_anim)
                                .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                    }
                });
            }

        });
        float voiceTime = imMessageBean.getUserVoiceTime();
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.setText(R.id.voice_time, f1 + "\"");
        ViewGroup.LayoutParams lParams = holder.getView(R.id.voice_receiver_image)
                .getLayoutParams();
        lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
                * imMessageBean.getUserVoiceTime());
        holder.getView(R.id.voice_receiver_image).setLayoutParams(lParams);
    }
}