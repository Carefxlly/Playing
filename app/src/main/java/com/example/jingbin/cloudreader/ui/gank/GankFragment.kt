package com.example.jingbin.cloudreader.ui.gank

import android.animation.ValueAnimator
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.base.BaseFragment
import com.example.jingbin.cloudreader.databinding.FragmentGankBinding
import com.example.jingbin.cloudreader.network.rx.RxBus
import com.example.jingbin.cloudreader.network.rx.RxCodeConstants
import com.example.jingbin.cloudreader.ui.gank.child.AndroidFragment
import com.example.jingbin.cloudreader.ui.gank.child.CustomFragment
import com.example.jingbin.cloudreader.ui.gank.child.EverydayFragment
import com.example.jingbin.cloudreader.ui.gank.child.WelfareFragment
import com.example.jingbin.cloudreader.widgets.MyFragmentPagerAdapter

import java.util.ArrayList

import rx.Subscription

class GankFragment : BaseFragment<FragmentGankBinding>() {

    private val mTitleList = ArrayList<String>(4)
    private val mFragments = ArrayList<Fragment>(4)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showLoading()
        initFragmentList()

        val myAdapter = MyFragmentPagerAdapter(childFragmentManager, mFragments, mTitleList)
        bindingView.vpGank.adapter = myAdapter
        bindingView.vpGank.offscreenPageLimit = 3
        myAdapter.notifyDataSetChanged()
        bindingView.tabGank.apply {
            isTabIndicatorFullWidth
            setupWithViewPager(bindingView.vpGank)
            if (getTabAt(0)?.customView == null) {
                getTabAt(0)?.setCustomView(R.layout.layout_tab)
            }
            val text0 = getTabAt(0)?.customView!!.findViewById<TextView>(android.R.id.text1)
            text0.setTextColor(ContextCompat.getColor(activity!!, R.color.colorTabTextCheck))
            val valueAnimator = ValueAnimator.ofFloat(12f, 15f).setDuration(200)
            valueAnimator.addUpdateListener {
                text0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, valueAnimator.animatedValue as Float)
            }
            valueAnimator.start()
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (null == tab.customView) {
                        tab.setCustomView(R.layout.layout_tab)
                    }
                    val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                    textView.setTextColor(ContextCompat.getColor(activity!!, R.color.colorTabTextCheck))
                    val valueAnimators = ValueAnimator.ofFloat(12f, 15f).setDuration(200)
                    valueAnimators.addUpdateListener {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, valueAnimators.animatedValue as Float)
                    }
                    valueAnimators.start()
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    if (null == tab.customView) {
                        tab.setCustomView(R.layout.layout_tab)
                    }
                    val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                    textView.setTextColor(resources.getColor(R.color.black))
                    val valueAnimators = ValueAnimator.ofFloat(15f, 12f).setDuration(200)
                    valueAnimators.addUpdateListener {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, valueAnimators.animatedValue as Float)
                    }
                    valueAnimators.start()
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        showContentView()
        // item点击跳转
        initRxBus()
    }

    override fun setContent(): Int {
        return R.layout.fragment_gank
    }

    private fun initFragmentList() {
        mTitleList.clear()
        mTitleList.add("每日推荐")
        mTitleList.add("Gank妹子")
        mTitleList.add("文章")
//        mTitleList.add("安卓")
        mFragments.add(EverydayFragment())
        mFragments.add(WelfareFragment())
        mFragments.add(CustomFragment())
//        mFragments.add(AndroidFragment.newInstance("Android"))
    }

    /**
     * 每日推荐点击"更多"跳转
     */
    private fun initRxBus() {
        val subscription = RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE, Int::class.java)
                .subscribe { integer ->
                    when (integer) {
                        0 -> bindingView.vpGank.currentItem = 3
                        1 -> bindingView.vpGank.currentItem = 1
                        2 -> bindingView.vpGank.currentItem = 2
                    }
                }
        addSubscription(subscription)
    }
}
