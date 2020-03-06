package com.example.jingbin.cloudreader.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.base.BaseRecyclerViewAdapter;
import com.example.jingbin.cloudreader.base.BaseRecyclerViewHolder;
import com.example.jingbin.cloudreader.dataclass.GankIoDataBean;
import com.example.jingbin.cloudreader.databinding.ItemAndroidBinding;
import com.example.jingbin.cloudreader.tools.ImgLoadUtil;
import com.example.jingbin.cloudreader.widgets.webview.WebViewActivity;

import org.jetbrains.annotations.NotNull;

public class GankAndroidAdapter extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {

    private boolean isAll = false;
    private Context context;

    public GankAndroidAdapter(Context context) {
        this.context = context;
    }

    public void setAllType(boolean isAll) {
        this.isAll = isAll;
    }

    @NotNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_android);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<GankIoDataBean.ResultBean, ItemAndroidBinding> {

        ViewHolder(ViewGroup parent, int item_android) { super(parent, item_android); }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean object, int position) {

            if (isAll && "福利".equals(object.getType())) {
                binding.ivAllWelfare.setVisibility(View.VISIBLE);
                binding.llWelfareOther.setVisibility(View.GONE);
                ImgLoadUtil.displayEspImage(object.getUrl(), binding.ivAllWelfare, 1);
            } else {
                binding.ivAllWelfare.setVisibility(View.GONE);
                binding.llWelfareOther.setVisibility(View.VISIBLE);
            }

            if (isAll) {
                binding.tvContentType.setVisibility(View.VISIBLE);
                binding.tvContentType.setText(" · " + object.getType());
            } else {
                binding.tvContentType.setVisibility(View.GONE);

            }

            // 显示gif图片会很耗内存
            if (object.getImages() != null && object.getImages().size() > 0 && !TextUtils.isEmpty(object.getImages().get(0))) {
                binding.ivAndroidPic.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayGif(object.getImages().get(0), binding.ivAndroidPic);
                //                Glide.with(context).load(object.getImages().get(0))
                //                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //                        .placeholder(R.drawable.img_one_bi_one)
                //                        .error(R.drawable.img_one_bi_one)
                //                        .into(binding.ivAndroidPic);
            } else { binding.ivAndroidPic.setVisibility(View.GONE); }

            binding.setResultsBean(object);
            binding.setCommand(GankAndroidAdapter.this);
            binding.executePendingBindings();
        }

    }

    public void openDetail(GankIoDataBean.ResultBean object) {
        WebViewActivity.loadUrl(context, object.getUrl(), object.getDesc());
    }
}
