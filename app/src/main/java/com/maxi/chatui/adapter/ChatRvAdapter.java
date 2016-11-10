package com.maxi.chatui.adapter;

import android.content.Context;

import com.maxi.chatui.entity.IMMessageBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by Mao Jiqing on 2016/11/4.
 */

public class ChatRvAdapter extends MultiItemTypeAdapter<IMMessageBean> {
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int FROM_USER_VOICE = 4;//接收消息类型
    public static final int TO_USER_VOICE = 5;//发送消息类型

    public ChatRvAdapter(Context context, List<IMMessageBean> datas) {
        super(context, datas);
        addItemViewDelegate(FROM_USER_MSG, new MsgFromItemDelegate(context,datas));
        addItemViewDelegate(TO_USER_MSG, new MsgToItemDelegate(context,datas));
        addItemViewDelegate(FROM_USER_IMG, new ImgMsgFromItemDelegate(context,datas));
        addItemViewDelegate(TO_USER_IMG, new ImgMsgToItemDelegate(context,datas));
        addItemViewDelegate(FROM_USER_VOICE, new VoiceMsgFromItemDelegate(context,datas));
        addItemViewDelegate(TO_USER_VOICE, new VoiceMsgToItemDelegate(context,datas));
    }
}

