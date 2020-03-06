package com.example.jingbin.cloudreader.ui.wan.child

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.adapter.JokeAdapter
import com.example.jingbin.cloudreader.base.BaseFragment
import com.example.jingbin.cloudreader.dataclass.wanandroid.DuanZiBean
import com.example.jingbin.cloudreader.databinding.FragmentWanAndroidBinding
import com.example.jingbin.cloudreader.tools.CommonUtils
import com.example.jingbin.cloudreader.viewmodel.wan.JokeViewModel
import com.example.jingbin.cloudreader.viewmodel.wan.WanNavigator
import com.example.xrecyclerview.XRecyclerView
import rx.Subscription
import java.util.*

/**
 * @author jingbin
 * Updated by jingbin on 18/04/19.
 */

class JokeFragment : BaseFragment<FragmentWanAndroidBinding>(), WanNavigator.JokeNavigator {

    private var mType: String? = "综合"
    private var mIsPrepared: Boolean = false
    private var mIsFirst: Boolean = true
    private var mAdapter: JokeAdapter? = null
    private var viewModel: JokeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mType = arguments?.getString(TYPE)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JokeViewModel::class.java)
        viewModel!!.setNavigator(this)
        initRefreshView()
        mIsPrepared = true
        /**
         * 因为启动时先走loadData()再走onActivityCreated,所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData()
    }

    override fun setContent(): Int {
        return R.layout.fragment_wan_android
    }

    private fun initRefreshView() {
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        bindingView.srlBook.setOnRefreshListener {
            bindingView.srlBook.postDelayed({
                bindingView.xrvBook.reset()
                viewModel!!.isRefreshBK = true
                viewModel!!.page = Random().nextInt(100)
                viewModel!!.showQSBKList()
            }, 100)
        }
        bindingView.xrvBook.layoutManager = LinearLayoutManager(activity)
        bindingView.xrvBook.setPullRefreshEnabled(false)
        bindingView.xrvBook.clearHeader()
        mAdapter = JokeAdapter(activity)
        bindingView.xrvBook.adapter = mAdapter

        bindingView.xrvBook.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {}

            override fun onLoadMore() {
                var page = viewModel!!.page
                viewModel!!.page = ++page
                viewModel!!.isRefreshBK = false
                loadCustomData()
            }
        })
    }

    override fun loadData() {
        if (!mIsVisible || !mIsPrepared || !mIsFirst) {
            return
        }
        bindingView.srlBook.isRefreshing = true
        bindingView.srlBook.postDelayed({ loadCustomData() }, 100)
    }

    private fun loadCustomData() {
        viewModel!!.showQSBKList()
    }

    override fun onRefresh() {
        bindingView.srlBook.isRefreshing = true
        loadCustomData()
    }

    override fun showAdapterView(bean: List<DuanZiBean>) {
        if (viewModel!!.isRefreshBK) {
            mAdapter!!.clear()
        }
        mAdapter!!.addAll(bean)
        mAdapter!!.notifyDataSetChanged()
        bindingView.xrvBook.refreshComplete()

        if (mIsFirst) {
            mIsFirst = false
        }
    }

    override fun showLoadSuccessView() {
        showContentView()
        bindingView.srlBook.isRefreshing = false
    }

    override fun showListNoMoreLoading() {
        bindingView.xrvBook.noMoreLoading()
    }

    override fun loadListFailure() {
        showContentView()
        if (bindingView.srlBook.isRefreshing) {
            bindingView.srlBook.isRefreshing = false
        }
        if (viewModel!!.isRefreshBK) {
            showError()
        } else {
            bindingView.xrvBook.refreshComplete()
        }
    }

    override fun addRxSubscription(subscription: Subscription) {
        addSubscription(subscription)
    }

    companion object {

        private const val TYPE = "param1"

        fun newInstance(param1: String): JokeFragment {
            val fragment = JokeFragment()
            val args = Bundle()
            args.putString(TYPE, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
