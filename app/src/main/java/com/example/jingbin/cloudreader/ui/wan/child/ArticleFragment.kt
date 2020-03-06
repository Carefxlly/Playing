package com.example.jingbin.cloudreader.ui.wan.child

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.app.CloudReaderApplication
import com.example.jingbin.cloudreader.base.BaseFragments
import com.example.jingbin.cloudreader.dataclass.PixabayDataClass
import com.example.jingbin.cloudreader.tools.ToastUtil
import com.example.jingbin.cloudreader.viewmodel.wan.ArticleViewModel
import com.example.xrecyclerview.XRecyclerView
import io.wherevere.know.adapter.PixabayAdapter

/**
 * @author wherevere
 * @time 2019/11/12 23:26
 * @version 1.0
 */
class ArticleFragment : BaseFragments() {

    var articleViewModel: ArticleViewModel? = null
    var articleSwipeRefreshLayout: SwipeRefreshLayout? = null
    val pixabayLiveData: MutableLiveData<PixabayDataClass>? = null
    var pixabayObserver: Observer<PixabayDataClass>? = null
    var articleXRecyclerView: XRecyclerView? = null
    var pixabayAdapter: PixabayAdapter? = null
    var page = 1
    var isPrepared: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        articleViewModel?.lifecycleOwner = this
        initView()
        isPrepared = true
        loadArticleData(page)
        articleViewModel?.pixabayLiveData()?.observe(viewLifecycleOwner, Observer { t ->
            pixabayAdapter?.loadmore(t.hits)
            pixabayAdapter?.notifyDataSetChanged()
        })
    }

    private fun initView() {
        articleSwipeRefreshLayout = view?.findViewById(R.id.xrefreshlayout_article)
        articleXRecyclerView = view?.findViewById(R.id.xrecyclerview_article)
        pixabayAdapter = PixabayAdapter(activity!!)
        articleSwipeRefreshLayout?.apply {
            setColorSchemeColors(ContextCompat.getColor(CloudReaderApplication.getInstance(), R.color.colorTabTextCheck))
            setOnRefreshListener {
                articleViewModel?.apply {
                    articleXRecyclerView?.reset()
                    page = 1
                    isRefreshStatus = true
                    lifecycleOwner = this@ArticleFragment
                    pixabayAdapter?.pixabayList?.clear()
                    loadArticleData(page)
                }
            }
        }
        articleXRecyclerView?.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = pixabayAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {}

                override fun onLoadMore() {
                    articleViewModel?.apply {
                        lifecycleOwner = this@ArticleFragment
                        isRefreshStatus = false
                        page = ++page
                        Log.w("loadmore", "page = $page")
                        loadArticleData(page)
                    }
                }
            })
        }



    }

    override fun onVisible() {
        loadArticleData(page)
        ToastUtil.showToast("this fragment is visible")
    }

    override fun onInvisible() {
        ToastUtil.showToast("this fragment is invisible")
    }

    private fun loadArticleData(page: Int) {
        if (!isVisibled or !isPrepared) return else articleViewModel?.loadArticleData(page)
    }

    override fun onRetry() {
        articleViewModel?.loadArticleData(page)
    }

    override fun setContentLayout(): Int {
        return R.layout.fragment_article
    }

}