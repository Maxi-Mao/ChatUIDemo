package com.maxi.chatui.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.maxi.chatui.apis.IGetDataListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Mao Jiqing on 2016/11/3.
 */

public class Utils {
    @SuppressLint("SimpleDateFormat")
    public static String getTime(String time, String before) {
        String show_time = null;
        if (before != null) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = df.parse(time);
                java.util.Date date = df.parse(before);
                long l = now.getTime() - date.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (min >= 1) {
                    show_time = time.substring(11);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            show_time = time.substring(11);
        }
        String getDay = getDay(time);
        if (show_time != null && getDay != null)
            show_time = getDay + " " + show_time;
        return show_time;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDay(String time) {
        String showDay = null;
        String nowTime = returnTime();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date now = df.parse(nowTime);
            java.util.Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            if (day >= 365) {
                showDay = time.substring(0, 10);
            } else if (day >= 1 && day < 365) {
                showDay = time.substring(5, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showDay;
    }

    @SuppressLint("SimpleDateFormat")
    public static String returnTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static void getLoacalBitmap(final String url, final IGetDataListener<Bitmap> mIGetDataListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream out;
                    FileInputStream fis = new FileInputStream(url);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    out = new ByteArrayOutputStream();
                    @SuppressWarnings("unused")
                    int hasRead = 0;
                    byte[] buffer = new byte[1024 * 2];
                    while ((hasRead = bis.read(buffer)) > 0) {
                        // 读出多少数据，向输出流中写入多少
                        out.write(buffer);
                        out.flush();
                    }
                    out.close();
                    fis.close();
                    bis.close();
                    byte[] data = out.toByteArray();
                    // 长宽减半
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 3;
                    mIGetDataListener.success(BitmapFactory.decodeByteArray(data, 0, data.length, opts));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    mIGetDataListener.failed(e.getMessage());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mIGetDataListener.failed(e.getMessage());
                }
            }

        }).start();

    }

}
