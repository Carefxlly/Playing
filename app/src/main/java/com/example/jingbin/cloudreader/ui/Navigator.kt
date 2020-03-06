package com.example.jingbin.cloudreader.ui

import com.example.jingbin.cloudreader.dataclass.Girl

/**
 * @author One
 * @time 2020/2/24 15:21
 * @version 1.0
 */

interface MzituRepositoryNavigator{
    fun loadSuccess(list: ArrayList<Girl>?)
    fun loadFail()
}

interface MzituUINavigator{
    fun showSuccessView()
    fun showFailView()
    fun showNoMoreView()
}