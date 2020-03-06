package com.example.jingbin.cloudreader.viewmodel.wan

import android.annotation.SuppressLint
import com.example.jingbin.cloudreader.app.MZITU
import com.example.jingbin.cloudreader.dataclass.Girl
import com.example.jingbin.cloudreader.tools.LogUtil
import com.example.jingbin.cloudreader.ui.MzituRepositoryNavigator
import com.example.jingbin.cloudreader.ui.wan.child.MzituFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

/**
 * @author One
 * @time 2020/2/25 17:41
 * @version 1.0
 */
class MzituRepository {

    @SuppressLint("CheckResult")
    fun remoteHomepageData(mzituRepositoryNavigator: MzituRepositoryNavigator, page: Int) {
        Observable.just("$MZITU/page/$page/")
                .subscribeOn(Schedulers.io())
                .map(object : Function<String, ArrayList<Girl>> {
                    override fun apply(t: String): ArrayList<Girl> {
                        val girls: ArrayList<Girl> = ArrayList<Girl>()
                        try {
                            val doc = Jsoup.connect("$MZITU/page/$page/").timeout(10000).get()
                            val total = doc.select("div.postlist").first()
                            val items = total.select("li")
                            for (element in items) {
                                val girl = Girl(element.select("img").first().attr("data-original"))
                                girl.link = element.select("a[href]").attr("href")
                                girl.refer = "$MZITU/"
                                girls.add(girl)
                            }
                            LogUtil.e("==============================================================================")
                            LogUtil.e(doc.text())
                            LogUtil.e("==============================================================================")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return girls
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: ArrayList<Girl>? ->
                    mzituRepositoryNavigator.loadSuccess(t)
                    LogUtil.e("==============================================================================")
                    LogUtil.e(t.toString())
                    LogUtil.e("==============================================================================")

                }
    }

    fun localHomepageData() {
    }

}