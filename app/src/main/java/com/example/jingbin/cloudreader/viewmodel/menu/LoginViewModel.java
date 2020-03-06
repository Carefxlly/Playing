package com.example.jingbin.cloudreader.viewmodel.menu;

import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableField;

import android.text.TextUtils;

import com.example.jingbin.cloudreader.dataclass.wanandroid.LoginBean;
import com.example.jingbin.cloudreader.repository.UserUtil;
import com.example.jingbin.cloudreader.repository.room.Injection;
import com.example.jingbin.cloudreader.network.HttpClient;
import com.example.jingbin.cloudreader.tools.ToastUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author jingbin
 * @data 2018/5/7
 * @Description
 */

public class LoginViewModel extends ViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    private LoginNavigator navigator;

    public void setNavigator(LoginNavigator navigator) {
        this.navigator = navigator;
    }

    public void register() {
        if (!verifyData())
            return;
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer()
                .register(username.get(), password.get(), password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onNext(LoginBean bean) {
                        if (bean != null && bean.getData() != null) {
                            Injection.get().addData(bean.getData());
                            UserUtil.handleLoginSuccess();
                            navigator.loadSuccess();
                        } else {
                            if (bean != null) {
                                ToastUtil.showToastLong(bean.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        navigator.addRxSubscription(subscribe);
    }

    public void login() {
        if (!verifyData())
            return;
        Subscription subscribe = HttpClient.Builder.getWanAndroidServer()
                .login(username.get(), password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onNext(LoginBean bean) {
                        if (bean != null && bean.getData() != null) {
                            Injection.get().addData(bean.getData());
                            UserUtil.handleLoginSuccess();
                            navigator.loadSuccess();
                        } else {
                            if (bean != null) {
                                ToastUtil.showToastLong(bean.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        navigator.addRxSubscription(subscribe);
    }

    private boolean verifyData() {
        if (TextUtils.isEmpty(username.get())) {
            ToastUtil.showToast("请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtil.showToast("请输入密码");
            return false;
        }
        return true;
    }

    public void onDestroy() {
        navigator = null;
    }
}
