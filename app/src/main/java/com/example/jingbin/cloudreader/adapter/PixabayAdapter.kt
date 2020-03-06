package io.wherevere.know.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jingbin.cloudreader.R
import com.example.jingbin.cloudreader.dataclass.Hit
import com.example.jingbin.cloudreader.tools.DensityUtil

/**
 * @author One
 * @time 2019/11/25 19:42
 * @version 1.0
 */
class PixabayAdapter(var context: Context) : RecyclerView.Adapter<PixabayAdapter.PixabayViewHolder>() {

    val pixabayList: ArrayList<Hit> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixabayViewHolder {
        return PixabayViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pixabay, parent, false))
    }

    override fun onBindViewHolder(holder: PixabayViewHolder, position: Int) {
        if (position % 2 == 0) {
            DensityUtil.setViewMargin(holder.itemView, false, 12, 6, 12, 0)
        } else {
            DensityUtil.setViewMargin(holder.itemView, false, 6, 12, 12, 0)
        }
        holder.load(pixabayList[position])
    }

    override fun getItemCount(): Int {
        return pixabayList.size
    }

    fun refresh(list: ArrayList<Hit>) {
        pixabayList.clear()
        pixabayList.addAll(list)
    }

    fun loadmore(list: ArrayList<Hit>) {
        pixabayList.addAll(list)
    }

    class PixabayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun load(hit: Hit) {
            Glide.with(itemView.context)
                    .load(hit.webformatURL)
                    .into(itemView.findViewById(R.id.item_pixabay))
        }
    }

}