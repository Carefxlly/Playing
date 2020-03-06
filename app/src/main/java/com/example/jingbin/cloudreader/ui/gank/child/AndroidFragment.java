package com.example.jingbin.cloudreader.ui.gank.child;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jingbin.cloudreader.ui.MainActivity;
import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.adapter.GankAndroidAdapter;
import com.example.jingbin.cloudreader.base.BaseFragment;
import com.example.jingbin.cloudreader.dataclass.GankIoDataBean;
import com.example.jingbin.cloudreader.databinding.FragmentAndroidBinding;
import com.example.jingbin.cloudreader.viewmodel.gank.BigAndroidNavigator;
import com.example.jingbin.cloudreader.viewmodel.gank.BigAndroidViewModel;
import com.example.xrecyclerview.XRecyclerView;

import rx.Subscription;

/**
 * 大安卓 fragment
 */
public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> implements BigAndroidNavigator {

    private static final String TAG = "AndroidFragment";
    private static final String TYPE = "mType";
    private String mType = "Android";
    private boolean mIsFirst = true;
    private GankAndroidAdapter mGankAndroidAdapter;
    private MainActivity activity;
    private BigAndroidViewModel viewModel;


    public static AndroidFragment newInstance(String type) {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_android;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(BigAndroidViewModel.class);
        viewModel.setType(mType);
        viewModel.setBigAndroidNavigator(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mGankAndroidAdapter = new GankAndroidAdapter(activity);
        bindingView.xrvAndroid.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvAndroid.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                viewModel.setPage(1);
                viewModel.loadAndroidData();
            }

            @Override
            public void onLoadMore() {
                int page = viewModel.getPage();
                page++;
                viewModel.setPage(page);
                viewModel.loadAndroidData();
            }
        });
        bindingView.xrvAndroid.setAdapter(mGankAndroidAdapter);
    }

    @Override
    protected void loadData() {
        if (!mIsVisible || !mIsFirst) {
            return;
        }
        viewModel.loadAndroidData();
    }

    /**
     * 加载失败后点击后的操作
     */
    @Override
    protected void onRefresh() {
        viewModel.loadAndroidData();
    }

    @Override
    public void showLoadSuccessView() {
        showContentView();
    }

    @Override
    public void showLoadFailedView() {
        bindingView.xrvAndroid.refreshComplete();
        showError();
        //        注意：这里不能写成 mPage == 1，否则会一直显示错误页面
        //        if (mGankAndroidAdapter.getItemCount() == 0) {
        //        }
    }

    @Override
    public void showListNoMoreLoading() {
        bindingView.xrvAndroid.noMoreLoading();
    }

    @Override
    public void showAdapterView(GankIoDataBean gankIoDataBean) {
        if (viewModel.getPage() == 1) {
            mGankAndroidAdapter.clear();
        }
        mGankAndroidAdapter.addAll(gankIoDataBean.getResults());
        mGankAndroidAdapter.notifyDataSetChanged();
        bindingView.xrvAndroid.refreshComplete();

        if (mIsFirst) {
            mIsFirst = false;
        }
    }

    @Override
    public void addRxSubscription(Subscription subscription) {
        addSubscription(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
