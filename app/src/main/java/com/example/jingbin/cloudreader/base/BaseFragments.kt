package com.example.jingbin.cloudreader.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.jingbin.cloudreader.R

/**
 * @author wherevere
 * @time 2019/11/12 21:37
 * @version 1.0
 */
abstract class BaseFragments : Fragment() {

    var isFirstBoot = true
    var isPrepare = false
    var isVisibled = false
    var loadingView: LottieAnimationView? = null
    var mistakeView: LinearLayout? = null
    var addView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.aa_fragment_base, null)
        addView = LayoutInflater.from(context).inflate(setContentLayout(), null, false)
        addView?.layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val containers: RelativeLayout = rootView.findViewById(R.id.root_container)
        containers.addView(addView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingView = view?.findViewById(R.id.loading_view)
        mistakeView = view?.findViewById(R.id.mistake_view)
        loadingView?.apply {
            setAnimation(R.raw.loading_blue_106ms)
            repeatCount = LottieDrawable.INFINITE
            speed = 1.4f
            playAnimation()
        }

//        loadingView?.setOnClickListener {
//            showMistakeView()
//        }

        mistakeView?.setOnClickListener {
            showLoadingView()
            onRetry()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisibled = true
            onVisible()
        } else {
            isVisibled = false
            onInvisible()
        }
    }

    abstract fun onVisible()

    abstract fun onInvisible()

    abstract fun onRetry()

    abstract fun setContentLayout(): Int

    fun showLoadingView() {
        mistakeView?.apply { visibility = View.GONE }
        addView?.apply { visibility = View.GONE }
        loadingView?.apply {
            visibility = View.VISIBLE
            setAnimation(R.raw.loading_blue_106ms)
            repeatCount = LottieDrawable.INFINITE
            speed = 1.4f
            playAnimation()
        }
    }

    fun showContentView() {
        loadingView?.apply {
            visibility = View.GONE
            cancelAnimation()
        }
        mistakeView?.apply { visibility = View.GONE }
        addView?.apply { visibility = View.VISIBLE }
    }

    fun showMistakeView() {
        loadingView?.apply {
            visibility = View.GONE
            cancelAnimation()
        }
        addView?.apply { visibility = View.GONE }
        mistakeView?.apply { visibility = View.VISIBLE }
    }

}