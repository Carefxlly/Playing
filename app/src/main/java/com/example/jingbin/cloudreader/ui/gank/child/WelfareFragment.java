package com.example.jingbin.cloudreader.ui.gank.child;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.adapter.WelfareAdapter;
import com.example.jingbin.cloudreader.base.BaseFragment;
import com.example.jingbin.cloudreader.dataclass.GankIoDataBean;
import com.example.jingbin.cloudreader.databinding.FragmentWelfareBinding;
import com.example.jingbin.cloudreader.widgets.viewbigimage.ViewBigImageActivity;
import com.example.jingbin.cloudreader.viewmodel.gank.WelfareNavigator;
import com.example.jingbin.cloudreader.viewmodel.gank.WelfareViewModel;
import com.example.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import rx.Subscription;

/**
 * @author One
 * @version 1.0
 * @time 2020/2/26 20:44
 */

public class WelfareFragment extends BaseFragment<FragmentWelfareBinding> implements WelfareNavigator {

    private WelfareAdapter mWelfareAdapter;
    private WelfareViewModel viewModel;
    private boolean isPrepared = false;
    private boolean isFirst = true;
    private ArrayList<String> imgList = new ArrayList<>();
    private ArrayList<String> imgTitleList = new ArrayList<>();
    private static final String TAG = "WelfareFragment";

    @Override
    public int setContent() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(WelfareViewModel.class);    //取得ViewModel实例
        viewModel.setNavigator(this);    //在ViewModel放置Fragment实例
        initRecycleView();
        isPrepared = true;  //代码执行至此Fragment一开始被使用
    }

    private void initRecycleView() {
        bindingView.xrvWelfare.setPullRefreshEnabled(false);    //关闭上拉刷新
        bindingView.xrvWelfare.clearHeader();   //配合setPullRefreshEnabled()方法一起使用
        mWelfareAdapter = new WelfareAdapter(); //实例化RecyclerView适配器
        //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
        bindingView.xrvWelfare.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        bindingView.xrvWelfare.setAdapter(mWelfareAdapter);
        bindingView.xrvWelfare.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                int page = viewModel.getPage();
                page++;
                viewModel.setPage(page);
                viewModel.loadWelfareData();
            }
        });
        mWelfareAdapter.setOnItemClickListener((resultsBean, position) ->
                ViewBigImageActivity.startImageList(WelfareFragment.this.getContext(), position, imgList, imgTitleList));
    }

    @Override   //加载失败点击重新加载
    protected void onRefresh() {
        viewModel.loadWelfareData();
    }

    @Override   //当Fragment第一次或者被销毁时加载数据
    protected void loadData() {
        if (!mIsVisible || !isPrepared || !isFirst) {
            return;
        }
        viewModel.loadWelfareData();
    }

    @Override
    public void showAdapterView(GankIoDataBean gankIoDataBean) {
        if (viewModel.getPage() == 1) {
            mWelfareAdapter.clear();
        }  //若请求页数为则清空集合数据
        mWelfareAdapter.addAll(gankIoDataBean.getResults());        //将请求的数据添加到集合当中
        mWelfareAdapter.notifyDataSetChanged();     //通知Adapter更新数据
        bindingView.xrvWelfare.refreshComplete();   //结束加载
        if (isFirst) {
            isFirst = false;
        }           //显示成功后就不是第一次了，不再刷新
    }

    @Override   //隐藏加载失败View
    public void showLoadSuccessView() {
        showContentView();
    }

    @Override
    public void showLoadFailedView() {
        bindingView.xrvWelfare.refreshComplete();   //结束加载
        if (mWelfareAdapter.getItemCount() == 0) {
            showError();
        }   //若item数量为空则直接显示加载失败
    }

    @Override
    public void showListNoMoreLoading() {
        bindingView.xrvWelfare.noMoreLoading();
    }

    @Override
    public void setImageList(ArrayList<ArrayList<String>> arrayLists) {
        if (arrayLists != null && arrayLists.size() == 2) {
            imgList.addAll(arrayLists.get(0));
            imgTitleList.addAll(arrayLists.get(1));
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
