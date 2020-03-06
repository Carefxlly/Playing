package com.example.jingbin.cloudreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.dataclass.Girl
import com.example.jingbin.cloudreader.tools.DensityUtil


/**
 * @author One
 * @time 2020/2/26 19:34
 * @version 1.0
 */
class MzituAdapter(var context: Context) : RecyclerView.Adapter<MzituAdapter.MzituViewHolder>() {

    val mzituList: ArrayList<Girl> = ArrayList()
    private var mzzituContexts: Context? = null

    init {
        mzzituContexts = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MzituViewHolder {
        return MzituViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pixabay, parent, false))
    }

    override fun onBindViewHolder(holder: MzituViewHolder, position: Int) {
        if (position % 2 == 0) {
            DensityUtil.setViewMargin(holder.itemView, false, 12, 6, 12, 0)
        } else {
            DensityUtil.setViewMargin(holder.itemView, false, 6, 12, 12, 0)
        }
        holder.load(mzituList[position], mzzituContexts!!)
    }

    override fun getItemCount(): Int {
        return mzituList.size
    }

    fun refresh(list: ArrayList<Girl>) {
        mzituList.clear()
        mzituList.addAll(list)
    }

    fun loadmore(list: ArrayList<Girl>) {
        mzituList.addAll(list)
    }

    class MzituViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun load(girl: Girl, mzzituContexts: Context) {
            var glideUrl = GlideUrl(girl.url, LazyHeaders.Builder().addHeader("Referer", girl.refer).build())
            Glide.with(itemView.context)
                    .load(glideUrl)
                    .placeholder(R.drawable.img_default_meizi)
                    .error(R.drawable.img_default_meizi)
                    .into(itemView.findViewById(R.id.item_pixabay))
        }
    }

}