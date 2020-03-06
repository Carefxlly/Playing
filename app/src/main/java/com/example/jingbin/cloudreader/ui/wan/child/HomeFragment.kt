package com.example.jingbin.cloudreader.ui.wan.child

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.jingbin.cloudreader.ui.MainActivity
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.adapter.WanAndroidAdapter
import com.example.jingbin.cloudreader.base.BaseFragment
import com.example.jingbin.cloudreader.dataclass.wanandroid.HomeListBean
import com.example.jingbin.cloudreader.dataclass.wanandroid.WanAndroidBannerBean
import com.example.jingbin.cloudreader.databinding.FragmentWanAndroidBinding
import com.example.jingbin.cloudreader.databinding.HeaderWanAndroidBinding
import com.example.jingbin.cloudreader.tools.CommonUtils
import com.example.jingbin.cloudreader.tools.GlideImageLoader
import com.example.jingbin.cloudreader.widgets.webview.WebViewActivity
import com.example.jingbin.cloudreader.viewmodel.wan.HomeViewModel
import com.example.jingbin.cloudreader.viewmodel.wan.WanNavigator
import com.example.xrecyclerview.XRecyclerView
import rx.Subscription
import java.util.*

/**
 * @author jingbin
 * Updated by jingbin on 18/02/07.
 */
class HomeFragment :
        BaseFragment<FragmentWanAndroidBinding>(),
        WanNavigator.BannerNavigator,
        WanNavigator.ArticleListNavigator {

    private var mType: String? = "综合"
    private var mIsPrepared: Boolean = false
    private var mIsFirst = true
    private var activity: MainActivity? = null
    private var mAdapter: WanAndroidAdapter? = null
    private var androidBinding: HeaderWanAndroidBinding? = null
    private var viewModel: HomeViewModel? = null

    override fun setContent(): Int {
        return R.layout.fragment_wan_android
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = context as MainActivity?
        if (arguments != null) {
            mType = arguments!!.getString(TYPE)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showContentView()
        viewModel = HomeViewModel()
        viewModel!!.setNavigator(this)
        viewModel!!.setArticleListNavigator(this)
        initRefreshView()

        // 准备就绪
        mIsPrepared = true
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData()
    }

    private fun initRefreshView() {
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        bindingView.srlBook.setOnRefreshListener {
            bindingView.srlBook.postDelayed({
                viewModel!!.page = 0
                loadCustomData()
            }, 1000)
        }
        bindingView.xrvBook.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
        bindingView.xrvBook.setPullRefreshEnabled(false)
        bindingView.xrvBook.clearHeader()
        mAdapter = WanAndroidAdapter(getActivity())
        bindingView.xrvBook.adapter = mAdapter
        androidBinding = DataBindingUtil.inflate(layoutInflater, R.layout.header_wan_android, null, false)
        viewModel!!.getWanAndroidBanner()
        bindingView.xrvBook.addHeaderView(androidBinding!!.root)

        bindingView.xrvBook.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {}

            override fun onLoadMore() {
                var page = viewModel!!.page
                viewModel!!.page = ++page
                loadCustomData()
            }
        })
    }

    /**
     * 设置banner图
     */
    override fun showBannerView(bannerImages: ArrayList<String>,
                                mBannerTitle: ArrayList<String>, result: List<WanAndroidBannerBean.DataBean>) {
        androidBinding!!.banner.visibility = View.VISIBLE
        androidBinding!!.banner.setBannerTitles(mBannerTitle)
        androidBinding!!.banner.setImages(bannerImages).setImageLoader(GlideImageLoader()).start()
        androidBinding!!.banner.setOnBannerListener { position ->
            if (result[position] != null && !TextUtils.isEmpty(result[position].url)) {
                WebViewActivity.loadUrl(context, result[position].url, result[position].title)
            }
        }
    }

    override fun loadBannerFailure() {
        androidBinding!!.banner.visibility = View.GONE
    }

    override fun addRxSubscription(subscription: Subscription) {
        addSubscription(subscription)
    }

    override fun loadHomeListFailure() {
        showContentView()
        if (bindingView.srlBook.isRefreshing) {
            bindingView.srlBook.isRefreshing = false
        }
        if (viewModel!!.page == 0) {
            showError()
        } else {
            bindingView.xrvBook.refreshComplete()
        }
    }

    override fun showAdapterView(bean: HomeListBean) {
        if (viewModel!!.page == 0) {
            mAdapter!!.clear()
        }
        mAdapter!!.addAll(bean.data.datas)
        mAdapter!!.notifyDataSetChanged()
        bindingView.xrvBook.refreshComplete()

        if (viewModel!!.page == 0) {
            mIsFirst = false
        }
    }

    override fun showListNoMoreLoading() {
        bindingView.xrvBook.noMoreLoading()
    }

    override fun showLoadSuccessView() {
        showContentView()
        bindingView.srlBook.isRefreshing = false
    }

    override fun loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst)
            return
        bindingView.srlBook.isRefreshing = true
        bindingView.srlBook.postDelayed({ this.loadCustomData() }, 500)
    }

    override fun onInvisible() {
        // 不可见时轮播图停止滚动
        if (androidBinding != null && androidBinding!!.banner != null) {
            androidBinding!!.banner.stopAutoPlay()
        }
    }

    private fun loadCustomData() {
        viewModel!!.getHomeList(null)
    }

    override fun onRefresh() {
        bindingView.srlBook.isRefreshing = true
        loadCustomData()
    }

    companion object {

        private val TYPE = "param1"

        fun newInstance(param1: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(TYPE, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
