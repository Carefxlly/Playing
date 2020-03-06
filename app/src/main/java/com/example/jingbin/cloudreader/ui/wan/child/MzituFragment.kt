package com.example.jingbin.cloudreader.ui.wan.child

import android.os.Bundle
import androidx.annotation.RawRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.adapter.MzituAdapter
import com.example.jingbin.cloudreader.base.BaseFragments
import com.example.jingbin.cloudreader.tools.CommonUtils
import com.example.jingbin.cloudreader.tools.ToastUtil
import com.example.jingbin.cloudreader.ui.MzituUINavigator
import com.example.jingbin.cloudreader.viewmodel.wan.MzituViewModel
import com.example.xrecyclerview.XRecyclerView

/**
 * @author One
 * @time 2020/2/24 13:38
 * @version 1.0
 */
class MzituFragment : BaseFragments(), MzituUINavigator {

    private var mzituViewModel: MzituViewModel? = null
    var mzituAdapter: MzituAdapter? = null
    private var mzituXRecyclerView: XRecyclerView? = null
    var mzituSwipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mzituViewModel = ViewModelProvider(this).get(MzituViewModel::class.java)
        isPrepare = true
        mzituViewModel?.lifecycleOwner = this
        initializeView()
        mzituViewModel?.getMzituLiveDatas()?.observe(viewLifecycleOwner, Observer { t ->
            mzituAdapter?.loadmore(t!!)
            mzituAdapter?.notifyDataSetChanged()
        })
    }

    private fun initializeView() {
        mzituSwipeRefreshLayout = view?.findViewById(R.id.xrefreshlayout_mzitu)
        mzituXRecyclerView = view?.findViewById(R.id.xrecyclerview_mzitu)
        mzituAdapter = MzituAdapter(this.activity!!)
        mzituSwipeRefreshLayout?.apply {
            setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
            setOnRefreshListener {
                mzituViewModel?.apply {
                    lifecycleOwner = this@MzituFragment
                    mzituXRecyclerView?.reset()
                    page = 1
                    status = true
                    getMzituDatas(page)
                }
            }
        }
        mzituXRecyclerView?.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mzituAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {}

                override fun onLoadMore() {
                    mzituViewModel?.apply {
                        lifecycleOwner = this@MzituFragment
//                        var page = mzituViewModel!!.page
                        mzituViewModel!!.page = ++mzituViewModel!!.page
                        status = false
                        getMzituDatas(page)
                    }
                }
            })
        }
    }


    /**
     * @return layout id
     */
    override fun setContentLayout(): Int {
        return R.layout.fragment_mzitu
    }

    override fun onVisible() {
        if (isVisibled && isFirstBoot && isPrepare) {
            mzituViewModel?.getMzituDatas(mzituViewModel!!.page)
            isFirstBoot = false
        } else {
//            ToastUtil.showToast("此时出现三个条件还有未满足的情况")
        }
    }

    override fun onInvisible() {
//        ToastUtil.showToast("此时fragment已经pause了")
    }

    override fun onRetry() {

    }

    override fun showSuccessView() {
        showContentView()
        mzituViewModel?.status = false
    }

    override fun showFailView() {
        if (mzituViewModel?.status!!) {
            showMistakeView()
        } else {
            mzituXRecyclerView?.refreshComplete()
        }
    }

    override fun showNoMoreView() {
        mzituXRecyclerView?.noMoreLoading()
    }


}