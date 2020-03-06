package com.example.jingbin.cloudreader.viewmodel.wan;

import androidx.lifecycle.ViewModel;

import com.example.jingbin.cloudreader.dataclass.wanandroid.DuanZiBean;
import com.example.jingbin.cloudreader.repository.model.JokeModel;

import java.util.List;

import rx.Subscription;

/**
 * @author jingbin
 * @data 2018/2/8
 * @Description 玩安卓ViewModel
 */

public class JokeViewModel extends ViewModel {

    private int mPage = 1;
    private boolean isRefreshBK = false;
    private final JokeModel mModel;
    private WanNavigator.JokeNavigator jokeNavigator;

    public JokeViewModel() {
        mModel = new JokeModel();
    }

    public void setNavigator(WanNavigator.JokeNavigator navigator) {
        this.jokeNavigator = navigator;
    }

    private WanNavigator.JokeModelNavigator navigator = new WanNavigator.JokeModelNavigator() {
        @Override
        public void loadSuccess(List<DuanZiBean> lists) {
            jokeNavigator.showLoadSuccessView();
            if (isRefreshBK) {
                if (lists == null || lists.size() <= 0) {
                    jokeNavigator.loadListFailure();
                    return;
                }
            } else {
                if (lists == null || lists.size() <= 0) {
                    jokeNavigator.showListNoMoreLoading();
                    return;
                }
            }
            jokeNavigator.showAdapterView(lists);
        }

        @Override
        public void loadFail() {
            jokeNavigator.loadListFailure();
        }

        @Override
        public void addSubscription(Subscription subscription) {
            jokeNavigator.addRxSubscription(subscription);
        }
    };

    public void showQSBKList() {
        mModel.showQSBKList(navigator, mPage);
    }

    public void onDestroy() {
        navigator = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // set and get
    ////////////////////////////////////////////////////////////////////////////

    public void setRefreshBK(boolean refreshBK) {
        isRefreshBK = refreshBK;
    }

    public boolean isRefreshBK() {
        return isRefreshBK;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }

    public int getPage() {
        return mPage;
    }
}
