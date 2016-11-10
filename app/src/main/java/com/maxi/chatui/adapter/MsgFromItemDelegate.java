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

public class MsgFromItemDelegate extends BaseItemDelegate implements ItemViewDelegate<IMMessageBean> {

    public MsgFromItemDelegate(Context mContext, List<IMMessageBean> datas) {
        super(mContext, datas);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_msgfrom_list_item;
    }

    @Override
    public boolean isForViewType(IMMessageBean item, int position) {
        return item.getType() == ChatRvAdapter.FROM_USER_MSG;
    }

    @Override
    public void convert(ViewHolder holder, IMMessageBean imMessageBean, int position) {
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
        ((GifTextView) (holder.getView(R.id.content))).setSpanText(mHandler, imMessageBean.getUserContent(), isGif);
    }
}