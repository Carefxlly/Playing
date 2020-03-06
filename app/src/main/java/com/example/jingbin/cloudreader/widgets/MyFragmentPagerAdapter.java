package com.example.jingbin.cloudreader.widgets;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by jingbin on 2016/12/6.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<?> mFragment;
    private List<String> mTitleList;

    /**
     * 普通，主页使用
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragments) {
        super(fm);
        this.mFragment = mFragments;
    }

    /**
     * 接收首页传递的标题
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragments, List<String> mTitleLists) {
        super(fm);
        this.mFragment = mFragments;
        this.mTitleList = mTitleLists;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 首页显示title，每日推荐等..
     * 若有问题，移到对应单独页面
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null && position < mTitleList.size()) {
            return mTitleList.get(position);
        } else {
            return "";
        }
    }

    public void addFragmentList(List<?> fragment) {
        this.mFragment.clear();
        this.mFragment = null;
        this.mFragment = fragment;
        notifyDataSetChanged();
    }

}
