package com.example.jingbin.cloudreader.viewmodel.wan

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jingbin.cloudreader.dataclass.Girl
import com.example.jingbin.cloudreader.ui.MzituRepositoryNavigator
import com.example.jingbin.cloudreader.ui.wan.child.MzituFragment

/**
 * @author One
 * @time 2020/2/24 15:39
 * @version 1.0
 */
class MzituViewModel : ViewModel() {

    var page: Int = 1
    var status: Boolean = true
    var lifecycleOwner: LifecycleOwner? = null
    private var mzituRepository: MzituRepository? = null
    private var mzituLiveData: LiveData<ArrayList<Girl>>? = null

    init {
        mzituRepository = MzituRepository()
        mzituLiveData = MutableLiveData()
    }

    fun getMzituLiveDatas(): LiveData<ArrayList<Girl>> {
        return mzituLiveData!!
    }

    fun getMzituDatas(page: Int) {
        mzituRepository?.remoteHomepageData(mzituRepositoryNavigator, page)
    }

    private var mzituRepositoryNavigator: MzituRepositoryNavigator = object : MzituRepositoryNavigator {
        override fun loadSuccess(list: ArrayList<Girl>?) {
            if (status) {
                //下拉刷新状态下List若为空则展示错误视图,否则清空adapter的List并发送数据至Fragment
                if (list == null || list.isEmpty()) {
                    (lifecycleOwner as MzituFragment).showMistakeView()
                } else {
                    (lifecycleOwner as MzituFragment).showContentView()
                    (lifecycleOwner as MzituFragment).mzituSwipeRefreshLayout?.isRefreshing =false
                    (lifecycleOwner as MzituFragment).mzituAdapter?.mzituList?.clear()
                    (mzituLiveData as MutableLiveData).postValue(list)
                }
            } else {
                //上拉加载状态下List若为空则展示无更多视图,否则发送数据至Fragment
                if (list == null || list.isEmpty()) {
                    (lifecycleOwner as MzituFragment).showNoMoreView()
                } else {
                    (mzituLiveData as MutableLiveData).postValue(list)
                }
            }
        }

        override fun loadFail() {
            (lifecycleOwner as MzituFragment).showMistakeView()
        }
    }
}
//1.上拉加载状态需要判断是否为未联网状态，若是则显示footView网络已断开的文字；若处于联网状态，则显示没有更多内容的文字
//2.下拉刷新状态下若返回的集合处于正常状况则需先把adapter的数据源清空，然后再赋值并notify