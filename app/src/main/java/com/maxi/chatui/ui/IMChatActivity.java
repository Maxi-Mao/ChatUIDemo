package com.maxi.chatui.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.maxi.chatui.adapter.BaseItemDelegate;
import com.maxi.chatui.adapter.ChatRvAdapter;
import com.maxi.chatui.animator.SlideInOutBottomItemAnimator;
import com.maxi.chatui.entity.IMMessageBean;
import com.maxi.chatui.util.KeyBoardUtils;
import com.maxi.chatui.util.SharedPreferencesUtils;
import com.maxi.chatui.widget.AudioRecordButton;
import com.maxi.chatui.widget.pulltorefresh.PullToRefreshRecyclerView;
import com.maxi.chatui.widget.pulltorefresh.WrapContentLinearLayoutManager;
import com.maxi.chatui.widget.pulltorefresh.base.PullToRefreshView;

import java.lang.ref.WeakReference;

public class IMChatActivity extends BaseActivity {
    private ChatRvAdapter tbAdapter;
    private PullToRefreshRecyclerView myList;
    private WrapContentLinearLayoutManager wcLinearLayoutManger;
    private SendMessageHandler sendMessageHandler;

    @Override
    protected void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = mEditTextContent.getText().toString();
                tblist.add(getTbub(userName, ChatRvAdapter.TO_USER_MSG, content, null, null,
                        null, null, null, 0f, IMMessageBean.SendState.COMPLETED));
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                IMChatActivity.this.content = content;
                receriveHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }).start();
    }

    /**
     * 接收文字
     */
    String content = "";

    private void receriveMsgText(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = "回复：" + content;
                IMMessageBean tbub = new IMMessageBean();
                tbub.setUserName(userName);
                String time = returnTime();
                tbub.setUserContent(message);
                tbub.setTime(time);
                tbub.setType(ChatRvAdapter.FROM_USER_MSG);
                tblist.add(tbub);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
            }
        }).start();
    }

    int i = 0;

    @Override
    protected void sendImage(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    tblist.add(getTbub(userName, ChatRvAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, IMMessageBean.SendState.SENDING_OR_RECEIVEING));
                } else if (i == 1) {
                    tblist.add(getTbub(userName, ChatRvAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, IMMessageBean.SendState.SENDERROR));
                } else if (i == 2) {
                    tblist.add(getTbub(userName, ChatRvAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, IMMessageBean.SendState.COMPLETED));
                    i = -1;
                }
                imageList.add(tblist.get(tblist.size() - 1).getImageLocal());
                imagePosition.put(tblist.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                IMChatActivity.this.filePath = filePath;
                receriveHandler.sendEmptyMessageDelayed(1, 3000);
                i++;
            }
        }).start();
    }

    /**
     * 接收图片
     */
    String filePath = "";

    private void receriveImageText(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IMMessageBean tbub = new IMMessageBean();
                tbub.setUserName(userName);
                String time = returnTime();
                tbub.setTime(time);
                tbub.setImageLocal(filePath);
                tbub.setType(ChatRvAdapter.FROM_USER_IMG);
                tblist.add(tbub);
                imageList.add(tblist.get(tblist.size() - 1).getImageLocal());
                imagePosition.put(tblist.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
            }
        }).start();
    }

    @Override
    protected void sendVoice(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tblist.add(getTbub(userName, ChatRvAdapter.TO_USER_VOICE, null, null, null, null, filePath,
                        null, seconds, IMMessageBean.SendState.COMPLETED));
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                IMChatActivity.this.seconds = seconds;
                voiceFilePath = filePath;
                receriveHandler.sendEmptyMessageDelayed(2, 3000);
            }
        }).start();
    }

    /**
     * 接收语音
     */
    float seconds = 0.0f;
    String voiceFilePath = "";

    private void receriveVoiceText(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IMMessageBean tbub = new IMMessageBean();
                tbub.setUserName(userName);
                String time = returnTime();
                tbub.setTime(time);
                tbub.setUserVoiceTime(seconds);
                tbub.setUserVoicePath(filePath);
                tbub.setUserVoiceReaded(false);
                tbub.setType(ChatRvAdapter.FROM_USER_VOICE);
                tblist.add(tbub);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
            }
        }).start();
    }

    /**
     * 为了模拟接收延迟
     */
    private Handler receriveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    receriveMsgText(content);
                    break;
                case 1:
                    receriveImageText(filePath);
                    break;
                case 2:
                    receriveVoiceText(seconds, voiceFilePath);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void loadRecords() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        super.findView();
        pullList.setSlideView(new PullToRefreshView(this).getSlideView(PullToRefreshView.RECYCLERVIEW));
        myList = (PullToRefreshRecyclerView) pullList.returnMylist();
    }

    @Override
    protected void onDestroy() {
        tblist.clear();
        tbAdapter.notifyDataSetChanged();
        myList.setAdapter(null);
        sendMessageHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void init() {
        tbAdapter = new ChatRvAdapter(this, tblist);
        wcLinearLayoutManger = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myList.setLayoutManager(wcLinearLayoutManger);
        myList.setItemAnimator(new SlideInOutBottomItemAnimator(myList));
        myList.setAdapter(tbAdapter);
        sendMessageHandler = new SendMessageHandler(this);
        tbAdapter.notifyDataSetChanged();
//        tbAdapter.setSendErrorListener(new ChatRecyclerAdapter.SendErrorListener() {
//
//            @Override
//            public void onClick(int position) {
//                // TODO Auto-generated method stub
//                ChatMessageBean tbub = tblist.get(position);
//                if (tbub.getType() == ChatRecyclerAdapter.TO_USER_VOICE) {
//                    sendVoice(tbub.getUserVoiceTime(), tbub.getUserVoicePath());
//                    tblist.remove(position);
//                } else if (tbub.getType() == ChatRecyclerAdapter.TO_USER_IMG) {
//                    sendImage(tbub.getImageLocal());
//                    tblist.remove(position);
//                }
//            }
//
//        });
//        tbAdapter.setVoiceIsReadListener(new ChatRecyclerAdapter.VoiceIsRead() {
//
//            @Override
//            public void voiceOnClick(int position) {
//                // TODO Auto-generated method stub
//                for (int i = 0; i < tbAdapter.unReadPosition.size(); i++) {
//                    if (tbAdapter.unReadPosition.get(i).equals(position + "")) {
//                        tbAdapter.unReadPosition.remove(i);
//                        break;
//                    }
//                }
//            }
//
//        });
        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath) {
                // TODO Auto-generated method stub
                sendVoice(seconds, filePath);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
//                tbAdapter.stopPlayVoice();
            }
        });
        myList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        BaseItemDelegate.mHandler.removeCallbacksAndMessages(null);
                        BaseItemDelegate.isGif = true;
//                        tbAdapter.isPicRefresh = false;
                        tbAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        BaseItemDelegate.mHandler.removeCallbacksAndMessages(null);
                        BaseItemDelegate.isGif = false;
//                        tbAdapter.isPicRefresh = true;
                        reset();
                        KeyBoardUtils.hideKeyBoard(IMChatActivity.this,
                                mEditTextContent);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        controlKeyboardLayout(activityRootView, pullList);
        super.init();
    }

    /**
     * @param root             最外层布局
     * @param needToScrollView 要滚动的布局,就是说在键盘弹出的时候,你需要试图滚动上去的View,在键盘隐藏的时候,他又会滚动到原来的位置的布局
     */
    private void controlKeyboardLayout(final View root, final View needToScrollView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                //获取当前界面可视部分
                IMChatActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = IMChatActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                int recyclerHeight = 0;
                if (wcLinearLayoutManger != null) {
                    recyclerHeight = wcLinearLayoutManger.getRecyclerHeight();
                }
                if (heightDifference == bottomStatusHeight) {
                    needToScrollView.scrollTo(0, 0);
                } else {
                    if ((int) SharedPreferencesUtils.getParam(IMChatActivity.this, SharedPreferencesUtils.KEY_BROAD_HEIGHT, 0) == 0) {
                        keyBroadHeight = heightDifference - bottomStatusHeight;
                        SharedPreferencesUtils.setParam(IMChatActivity.this, SharedPreferencesUtils.KEY_BROAD_HEIGHT, heightDifference - bottomStatusHeight);
                    }
                    if (heightDifference < recyclerHeight) {
                        int contentHeight = wcLinearLayoutManger == null ? 0 : wcLinearLayoutManger.getHeight();
                        if (recyclerHeight < contentHeight) {
                            listSlideHeight = heightDifference - (contentHeight - recyclerHeight);
                            needToScrollView.scrollTo(0, listSlideHeight);
                        } else {
                            listSlideHeight = heightDifference;
                            needToScrollView.scrollTo(0, listSlideHeight);
                        }
                    } else {
                        listSlideHeight = 0;
                    }
                }
            }
        });
    }

    static class SendMessageHandler extends Handler {
        WeakReference<IMChatActivity> mActivity;

        SendMessageHandler(IMChatActivity activity) {
            mActivity = new WeakReference<IMChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            IMChatActivity theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
                    case REFRESH:
                        theActivity.tbAdapter.notifyDataSetChanged();
                        int position = theActivity.tbAdapter.getItemCount() - 1 < 0 ? 0 : theActivity.tbAdapter.getItemCount() - 1;
                        theActivity.myList.smoothScrollToPosition(position);
                        break;
                    case SEND_OK:
                        theActivity.mEditTextContent.setText("");
                        theActivity.tbAdapter.notifyItemInserted(theActivity.tblist
                                .size() - 1);
                        theActivity.myList.smoothScrollToPosition(theActivity.tbAdapter.getItemCount() - 1);
                        break;
                    case RECERIVE_OK:
                        theActivity.tbAdapter.notifyItemInserted(theActivity.tblist
                                .size() - 1);
                        theActivity.myList.smoothScrollToPosition(theActivity.tbAdapter.getItemCount() - 1);
                        break;
                    case PULL_TO_REFRESH_DOWN:
                        theActivity.pullList.refreshComplete();
                        theActivity.tbAdapter.notifyDataSetChanged();
                        theActivity.myList.smoothScrollToPosition(theActivity.position - 1);
                        theActivity.isDown = false;
                        break;
                    default:
                        break;
                }
            }
        }

    }

}