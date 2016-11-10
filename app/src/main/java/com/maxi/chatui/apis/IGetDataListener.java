package com.maxi.chatui.apis;

/**
 * Created by Mao Jiqing on 2016/10/27.
 */

public interface IGetDataListener<E> {
    public void success(E str);

    public void failed(String error);
}
