package com.maxi.chatui.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.maxi.chatui.R;
import com.polites.android.GestureImageView;

/**
 * Created by Mao Jiqing on 2016/11/9.
 */

public class ImageViewSingleActivity extends Activity {

    private String imageUrl;
    private ProgressBar loadBar;
    private GestureImageView imageGiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.containsKey("image")) {
                imageUrl = bundle.getString("image");
            }
        }
        setContentView(R.layout.activity_image_view);
        init();
        loadImage(imageUrl);
    }

    private void init() {
        loadBar = (ProgressBar) findViewById(R.id.imageView_loading_pb);
        imageGiv = (GestureImageView) findViewById(R.id.imageView_item_giv);
        imageGiv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
    }

    public void loadImage(String url) {
        if (url.startsWith("http://")) {
            Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource != null) {
                        imageGiv.setImageBitmap(resource);
                        loadBar.setVisibility(View.GONE);
                        imageGiv.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            imageGiv.setImageBitmap(BitmapFactory.decodeFile(url));
            loadBar.setVisibility(View.GONE);
            imageGiv.setVisibility(View.VISIBLE);
        }
    }
}
