package com.example.xrecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class XLoadingMore extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private LottieAnimationView footLoading;
    private TextView mText;
    private ImageView mIvProgress;
    private AnimationDrawable mAnimationDrawable;

    public XLoadingMore(Context context) {
        super(context);
        initView(context);
    }

    public XLoadingMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.yun_refresh_footer, this);
        mText = findViewById(R.id.msg);
        footLoading = findViewById(R.id.foot_loading);
        footLoading.setSpeed(1.5f);
        //        footLoading.useHardwareAcceleration();
        if (!footLoading.isAnimating()) {
            footLoading.playAnimation();
        }
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //        mIvProgress = findViewById(R.id.iv_progress);
        //        mAnimationDrawable = (AnimationDrawable) mIvProgress.getDrawable();
        //        if (!mAnimationDrawable.isRunning()) {
        //            mAnimationDrawable.start();
        //        }


    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                if (!footLoading.isAnimating()) {
                    footLoading.playAnimation();
                }else {
                    footLoading.resumeAnimation();
                }
                footLoading.setVisibility(View.VISIBLE);

                //                if (!mAnimationDrawable.isRunning()) { mAnimationDrawable.start(); }
                //                mIvProgress.setVisibility(View.VISIBLE);
                //                mText.setText(getContext().getText(R.string.listview_loading));
                mText.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;

            case STATE_COMPLETE:
                if (footLoading.isAnimating()) {
                    footLoading.cancelAnimation();
                }

                //                if (mAnimationDrawable.isRunning()) { mAnimationDrawable.stop(); }
                mText.setText(getContext().getText(R.string.listview_loading));
                mText.setVisibility(View.GONE);
                this.setVisibility(View.GONE);
                break;

            case STATE_NOMORE:
                if (footLoading.isAnimating()) {
                    footLoading.cancelAnimation();
                }

                //                if (mAnimationDrawable.isRunning()) { mAnimationDrawable.stop(); }
                footLoading.setVisibility(View.GONE);
                //                mIvProgress.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                mText.setText(getContext().getText(R.string.nomore_loading));
                break;
        }
    }

    public void reSet() {
        this.setVisibility(GONE);
    }
}
