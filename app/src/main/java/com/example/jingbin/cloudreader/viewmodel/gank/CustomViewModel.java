package com.example.jingbin.cloudreader.viewmodel.gank;

import androidx.lifecycle.ViewModel;

import com.example.http.HttpUtils;
import com.example.jingbin.cloudreader.dataclass.GankIoDataBean;
import com.example.jingbin.cloudreader.repository.model.GankOtherModel;
import com.example.jingbin.cloudreader.network.RequestImpl;

import rx.Subscription;

public class CustomViewModel extends ViewModel {

    private String mType;
    private int mPage = 1;
    private CustomNavigator navigator;
    private final GankOtherModel mModel;

    public CustomViewModel() {
        mModel = new GankOtherModel();
    }

    public void loadCustomData() {
        mModel.setData(mType, mPage, HttpUtils.per_page_more);
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

    public void setNavigator(CustomNavigator navigator) {
        this.navigator = navigator;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }

    public int getPage() {
        return mPage;
    }

    public void onDestroy() {
        navigator = null;
    }
}
