package com.example.jingbin.cloudreader.ui.wan

import android.animation.ValueAnimator
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.base.BaseFragment
import com.example.jingbin.cloudreader.databinding.FragmentBookBinding
import com.example.jingbin.cloudreader.ui.wan.child.ArticleFragment
import com.example.jingbin.cloudreader.ui.wan.child.HomeFragment
import com.example.jingbin.cloudreader.ui.wan.child.JokeFragment
import com.example.jingbin.cloudreader.ui.wan.child.MzituFragment
import com.example.jingbin.cloudreader.widgets.MyFragmentPagerAdapter
import java.util.*

/**
 * @author wherevere
 * @time 2019/11/12 23:26
 * @version 1.0
 */

class WanFragment : BaseFragment<FragmentBookBinding>() {

    private val mTitleList = ArrayList<String>(3)
    private val mFragments = ArrayList<Fragment>(3)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showLoading()
        initChildFragment()
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        val myAdapter = MyFragmentPagerAdapter(childFragmentManager, mFragments, mTitleList)
        bindingView.vpBook.apply {
            adapter = myAdapter
            offscreenPageLimit = 3
        }
        myAdapter.notifyDataSetChanged()

        bindingView.tabBook.apply {
            isTabIndicatorFullWidth
            setupWithViewPager(bindingView.vpBook)
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
                    if (tab.customView == null) {
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
                    if (tab.customView == null) {
                        tab.setCustomView(R.layout.layout_tab)
                    }
                    val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                    textView.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
                    val valueAnimators = ValueAnimator.ofFloat(15f, 12f).setDuration(200)
                    valueAnimators.addUpdateListener {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, valueAnimators.animatedValue as Float)
                    }
                    valueAnimators.start()
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            showContentView()
        }
    }

    override fun setContent(): Int {
        return R.layout.fragment_book
    }

    private fun initChildFragment() {
        mTitleList.clear()
        mTitleList.add("玩安卓")
        mTitleList.add("文章")
        mTitleList.add("段子")
        mFragments.add(HomeFragment.newInstance("玩安卓"))
        mFragments.add(MzituFragment())
        mFragments.add(JokeFragment.newInstance("段子"))
        //        mFragments.add(BookListFragment.newInstance("心理学"));
    }
}
