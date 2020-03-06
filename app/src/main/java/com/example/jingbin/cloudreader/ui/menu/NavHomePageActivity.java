package com.example.jingbin.cloudreader.ui.menu;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.databinding.ActivityNavHomePageBinding;
import com.example.jingbin.cloudreader.tools.ShareUtils;

public class NavHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNavHomePageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_home_page);
        // 解决7.0以上系统 滑动到顶部 标题裁减一半的问题
        setSupportActionBar(binding.detailToolbar);

        binding.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.share(v.getContext(), R.string.string_share_text);
            }
        });
    }

    public static void startHome(Context mContext) {
        Intent intent = new Intent(mContext, NavHomePageActivity.class);
        mContext.startActivity(intent);
    }
}
