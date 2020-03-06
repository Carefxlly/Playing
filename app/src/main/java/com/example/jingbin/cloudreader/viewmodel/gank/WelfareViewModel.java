package com.example.jingbin.cloudreader.viewmodel.gank;

import androidx.lifecycle.ViewModel;

import com.example.http.HttpUtils;
import com.example.jingbin.cloudreader.dataclass.GankIoDataBean;
import com.example.jingbin.cloudreader.repository.model.GankOtherModel;
import com.example.jingbin.cloudreader.network.RequestImpl;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelfareViewModel extends ViewModel {

    private final GankOtherModel mModel;
    private WelfareNavigator navigator;
    private int mPage = 1;

    private ArrayList<String> imgList = new ArrayList<>();
    private ArrayList<String> imageTitleList = new ArrayList<>();
    private ArrayList<ArrayList<String>> allList = new ArrayList<>();

    public WelfareViewModel() {
        mModel = new GankOtherModel();
    }

    public void loadWelfareData() {
        mModel.setData("福利", mPage, HttpUtils.per_page_more);
        mModel.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                navigator.showLoadSuccessView();
                GankIoDataBean gankIoDataBean = (GankIoDataBean) object;
                if (mPage == 1) {
                    if (gankIoDataBean == null
                            || gankIoDataBean.getResults() == null
                            || gankIoDataBean.getResults().size() <= 0) {
                        navigator.showLoadFailedView();
                        return;
                    }
                } else {
                    if (gankIoDataBean == null
                            || gankIoDataBean.getResults() == null
                            || gankIoDataBean.getResults().size() <= 0) {
                        navigator.showListNoMoreLoading();
                        return;
                    }
                }
                handleImageList(gankIoDataBean);
                navigator.showAdapterView(gankIoDataBean);
            }

            @Override
            public void loadFailed() {
                navigator.showLoadFailedView();
                if (mPage > 1) {
                    mPage--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
                navigator.addRxSubscription(subscription);
            }
        });
    }

    /**
     * 异步处理用于图片详情显示的数据
     *
     * @param gankIoDataBean 原数据
     */
    private void handleImageList(GankIoDataBean gankIoDataBean) {
        Subscription subscribe = Observable.just(gankIoDataBean)
                .map(gankIoDataBean1 -> {
                    imgList.clear();
                    imageTitleList.clear();
                    for (int i = 0; i < gankIoDataBean1.getResults().size(); i++) {
                        imgList.add(gankIoDataBean1.getResults().get(i).getUrl());
                        imageTitleList.add(gankIoDataBean1.getResults().get(i).getDesc());
                    }
                    allList.clear();
                    allList.add(imgList);
                    allList.add(imageTitleList);
                    return allList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(arrayLists -> navigator.setImageList(arrayLists));
        navigator.addRxSubscription(subscribe);
    }

    public void setNavigator(WelfareNavigator navigator) {
        this.navigator = navigator;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }

    public int getPage() {
        return mPage;
    }

    public void onDestroy() {
        imgList.clear();
        imageTitleList.clear();
        allList.clear();
        imgList = null;
        imageTitleList = null;
        allList = null;
        navigator = null;
    }
}
