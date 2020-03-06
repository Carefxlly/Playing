package com.example.jingbin.cloudreader.viewmodel.wan

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jingbin.cloudreader.app.PIXABAY_URL
import com.example.jingbin.cloudreader.dataclass.PixabayDataClass
import com.example.jingbin.cloudreader.network.ApiService
import com.example.jingbin.cloudreader.ui.wan.child.ArticleFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author wherevere
 * @time 2019/11/12 23:30
 * @version 1.0
 */
class ArticleViewModel : ViewModel() {

    private val pixabayLiveData: MutableLiveData<PixabayDataClass>? = MutableLiveData()
    private var pixabayDataClass: PixabayDataClass? = null
    var lifecycleOwner: LifecycleOwner? = null
    var isRefreshStatus: Boolean = false

    fun loadArticleData(page: Int) {
        val retrofit = Retrofit.Builder()
                .baseUrl(PIXABAY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        viewModelScope.launch(Dispatchers.IO) {
            pixabayDataClass = retrofit.create(ApiService::class.java)
                    .remotePixabayDrawableData("14855755-cd097acbe9e51663e4f97aca4", "iphone", page, 10, "photo")
            withContext(Dispatchers.Main) {
                if (page == 1 && pixabayDataClass?.total == 0){
                    (lifecycleOwner as ArticleFragment).articleXRecyclerView?.refreshComplete()
                    (lifecycleOwner as ArticleFragment).showMistakeView()
                }else {
                    (lifecycleOwner as ArticleFragment).showContentView()
                    (lifecycleOwner as ArticleFragment).articleSwipeRefreshLayout?.isRefreshing = false
                    pixabayLiveData?.postValue(pixabayDataClass)
                }

//                if (pixabayDataClass?.errorCode != 0) {
//                    (lifecycleOwner as ArticleFragment).articleXRecyclerView?.refreshComplete()
//                    (lifecycleOwner as ArticleFragment).showMistakeView()
//                } else {
//                    if (isRefreshStatus and (pixabayDataClass?.data?.size == 0)) {
//                        (lifecycleOwner as ArticleFragment).articleXRecyclerView?.refreshComplete()
//                        (lifecycleOwner as ArticleFragment).showMistakeView()
//                    } else if (!isRefreshStatus and (pixabayDataClass?.data?.size == 0)) {
//                        (lifecycleOwner as ArticleFragment).articleXRecyclerView?.noMoreLoading()
//                    } else {
//                        pixabayLiveData?.postValue(pixabayDataClass)
//                    }
//                }
            }
        }
    }

    fun pixabayLiveData(): MutableLiveData<PixabayDataClass> {
        return pixabayLiveData!!
    }

}