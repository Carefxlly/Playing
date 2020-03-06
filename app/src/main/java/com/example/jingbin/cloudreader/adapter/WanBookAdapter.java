package com.example.jingbin.cloudreader.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.base.BaseRecyclerViewAdapter;
import com.example.jingbin.cloudreader.base.BaseRecyclerViewHolder;
import com.example.jingbin.cloudreader.dataclass.book.BooksBean;
import com.example.jingbin.cloudreader.databinding.ItemBookBinding;
import com.example.jingbin.cloudreader.ui.wan.child.BookDetailActivity;
import com.example.jingbin.cloudreader.tools.PerfectClickListener;

/**
 * Created by jingbin on 2016/11/25.
 */

public class WanBookAdapter extends BaseRecyclerViewAdapter<BooksBean> {

    private Activity activity;

    public WanBookAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_book);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<BooksBean, ItemBookBinding> {

        ViewHolder(ViewGroup context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindViewHolder(final BooksBean book, final int position) {
            if (book != null) {
                binding.setBean(book);

                binding.llItemTop.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        BookDetailActivity.start(activity,book,binding.ivTopPhoto);
                    }
                });
            }
        }
    }
}
