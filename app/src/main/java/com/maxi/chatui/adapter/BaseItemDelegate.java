package com.maxi.chatui.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.maxi.chatui.entity.IMMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao Jiqing on 2016/11/3.
 */

public class BaseItemDelegate {
    public Context mContext;
    public Animation an;
    public SendErrorListener sendErrorListener;
    public static Handler mHandler = new Handler();
    public int voicePlayPosition = -1;
    public int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    public int mMaxItemWith;
    public static boolean isGif = true;
    public List<IMMessageBean> datas = new ArrayList<>();

    public interface SendErrorListener {
        public void onClick(int position);
    }

    public BaseItemDelegate(Context mContext, List<IMMessageBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
        // 获取系统宽度
        WindowManager wManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }

//    public void stopPlayVoice() {
//        if (voicePlayPosition != -1) {
//            View voicePlay = (View) ((Activity) mContext)
//                    .findViewById(voicePlayPosition);
//            if (voicePlay != null) {
//                if (getItemViewType(voicePlayPosition) == FROM_USER_VOICE) {
//                    voicePlay.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
//                } else {
//                    voicePlay.setBackgroundResource(R.mipmap.adj);
//                }
//            }
//            IMAudioManager.pause();
//            voicePlayPosition = -1;
//        }
//    }
}
