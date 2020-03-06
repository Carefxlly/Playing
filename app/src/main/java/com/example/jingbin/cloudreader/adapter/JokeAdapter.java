package com.example.jingbin.cloudreader.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;

import android.view.ViewGroup;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.base.BaseRecyclerViewAdapter;
import com.example.jingbin.cloudreader.base.BaseRecyclerViewHolder;
import com.example.jingbin.cloudreader.dataclass.wanandroid.DuanZiBean;
import com.example.jingbin.cloudreader.databinding.ItemJokeBinding;
import com.example.jingbin.cloudreader.tools.DialogBuild;
import com.example.jingbin.cloudreader.tools.TimeUtil;

/**
 * Created by jingbin on 2016/11/25.
 */

public class JokeAdapter extends BaseRecyclerViewAdapter<DuanZiBean> {

    private Activity activity;

    public JokeAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_joke);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<DuanZiBean, ItemJokeBinding> {

        ViewHolder(ViewGroup context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindViewHolder(final DuanZiBean bean, final int position) {
            if (bean != null) {
                binding.setBean(bean);
                binding.executePendingBindings();
                binding.setTime(TimeUtil.formatDataTime(Long.valueOf(bean.getCreateTime() + "000")));
                binding.llItemTop.setOnLongClickListener(v -> {
                    DialogBuild.showItems(v, bean.getContent());
                    return false;
                });
            }
        }
    }
}
