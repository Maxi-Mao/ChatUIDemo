package com.maxi.chatui.adapter;

import android.content.Context;

import com.maxi.chatui.R;
import com.maxi.chatui.entity.IMMessageBean;
import com.maxi.chatui.util.Utils;
import com.maxi.chatui.widget.GifTextView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Mao Jiqing on 2016/11/3.
 */

public class MsgToItemDelegate extends BaseItemDelegate implements ItemViewDelegate<IMMessageBean> {

    public MsgToItemDelegate(Context mContext, List<IMMessageBean> datas) {
        super(mContext, datas);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_msgto_list_item;
    }

    @Override
    public boolean isForViewType(IMMessageBean item, int position) {
        return item.getType() == ChatRvAdapter.TO_USER_MSG;
    }

    @Override
    public void convert(final ViewHolder holder, IMMessageBean imMessageBean, final int position) {
        holder.getView(R.id.tb_my_user_icon).setBackgroundResource(R.mipmap.grzx_tx_s);
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
        ((GifTextView) (holder.getView(R.id.mycontent))).setSpanText(mHandler, imMessageBean.getUserContent(), isGif);
    }
}