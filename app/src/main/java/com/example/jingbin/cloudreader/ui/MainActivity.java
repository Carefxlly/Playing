package com.example.jingbin.cloudreader.ui;

import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.tools.LogUtil;
import com.example.jingbin.cloudreader.tools.StatusbarColorUtils;
import com.example.jingbin.cloudreader.tools.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jingbin.cloudreader.app.ConstantsImageUrl;
import com.example.jingbin.cloudreader.repository.UserUtil;
import com.example.jingbin.cloudreader.databinding.ActivityMainBinding;
import com.example.jingbin.cloudreader.databinding.NavHeaderMainBinding;
import com.example.jingbin.cloudreader.network.rx.RxBus;
import com.example.jingbin.cloudreader.network.rx.RxBusBaseMessage;
import com.example.jingbin.cloudreader.network.rx.RxCodeConstants;
import com.example.jingbin.cloudreader.ui.gank.GankFragment;
import com.example.jingbin.cloudreader.ui.menu.LoginActivity;
import com.example.jingbin.cloudreader.ui.menu.NavAboutActivity;
import com.example.jingbin.cloudreader.ui.menu.NavDeedBackActivity;
import com.example.jingbin.cloudreader.ui.menu.NavDownloadActivity;
import com.example.jingbin.cloudreader.ui.menu.NavHomePageActivity;
import com.example.jingbin.cloudreader.ui.movie.OneFragment;
import com.example.jingbin.cloudreader.ui.wan.WanFragment;
import com.example.jingbin.cloudreader.ui.wan.child.ArticleListActivity;
import com.example.jingbin.cloudreader.tools.CommonUtils;
import com.example.jingbin.cloudreader.tools.DialogBuild;
import com.example.jingbin.cloudreader.tools.ImgLoadUtil;
import com.example.jingbin.cloudreader.tools.PerfectClickListener;
import com.example.jingbin.cloudreader.tools.SPUtils;
import com.example.jingbin.cloudreader.widgets.MyFragmentPagerAdapter;
import com.example.jingbin.cloudreader.widgets.OnLoginListener;
import com.example.jingbin.cloudreader.widgets.statusbar.StatusBarUtil;
import com.example.jingbin.cloudreader.widgets.webview.WebViewActivity;

import java.util.ArrayList;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jingbin on 16/11/21.
 * Link to:https://github.com/youlookwhat/CloudReader
 * Contact me:http://www.jianshu.com/u/e43c6e979831
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Toolbar toolbar;
    private ImageView ivTitleTwo;
    private ImageView ivTitleOne;
    private ImageView ivTitleThree;
    private ViewPager vpContent;
    private FrameLayout llTitleMenu;
    private FloatingActionButton fab;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private ActivityMainBinding mBinding;
    private NavHeaderMainBinding bind;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusbarColorUtils.setStatusBarDarkIcon(this, true);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initStatusView();
        initContentId();
        initContentFragment();
        initDrawerLayout();
        initListener();
        initRxBus();
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(
                MainActivity.this,
                drawerLayout,
                CommonUtils.getColor(R.color.colorWhite)
        );


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取Heap Size阈值
        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        // 返回值是以Mb为单位
        int memoryOfClass = am.getMemoryClass();
//        LogUtil.w("memoryClass" + memoryOfClass);
//        ToastUtil.showToast("memoryClass" + memoryOfClass);
    }

    private void initStatusView() {
        ViewGroup.LayoutParams layoutParams = mBinding.include.viewStatus.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(this);
        mBinding.include.viewStatus.setLayoutParams(layoutParams);
    }

    @SuppressLint("RestrictedApi")
    private void initContentId() {
        drawerLayout = mBinding.drawerLayout;
        navView = mBinding.navView;
        fab = mBinding.include.fab;
        toolbar = mBinding.include.toolbar;
        llTitleMenu = mBinding.include.llTitleMenu;
        vpContent = mBinding.include.vpContent;
        ivTitleOne = mBinding.include.ivTitleOne;
        ivTitleTwo = mBinding.include.ivTitleTwo;
        ivTitleThree = mBinding.include.ivTitleThree;
        fab.setVisibility(View.GONE);
    }

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new WanFragment());
        mFragmentList.add(new GankFragment());
        mFragmentList.add(new OneFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        vpContent.setAdapter(adapter);
        vpContent.setOffscreenPageLimit(2);         // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        vpContent.addOnPageChangeListener(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        setCurrentItem(0);
    }

    /**
     * inflateHeaderView 进来的布局要宽一些
     */
    private void initDrawerLayout() {
        navView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = navView.getHeaderView(0);
        bind = DataBindingUtil.bind(headerView);
        if (bind != null) {
            bind.setListener(this);
        }
        bind.dayNightSwitch.setChecked(SPUtils.getNightMode());

        ImgLoadUtil.displayCircle(bind.ivAvatar, ConstantsImageUrl.IC_AVATAR);
        bind.llNavExit.setOnClickListener(this);
        bind.ivAvatar.setOnClickListener(this);

//        bind.llNavHomepage.setOnClickListener(listener);
        bind.llNavScanDownload.setOnClickListener(listener);
        bind.llNavDeedback.setOnClickListener(listener);
//        bind.llNavAbout.setOnClickListener(listener);
        bind.llNavLogin.setOnClickListener(listener);
        bind.llNavCollect.setOnClickListener(listener);
    }

    private void initListener() {
        llTitleMenu.setOnClickListener(this);
        mBinding.include.ivTitleOne.setOnClickListener(this);
        mBinding.include.ivTitleTwo.setOnClickListener(this);
        mBinding.include.ivTitleThree.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_title_one:
                if (vpContent.getCurrentItem() != 0) {
                    setCurrentItem(0);
                }
                break;
            case R.id.iv_title_two:
                if (vpContent.getCurrentItem() != 1) {
                    setCurrentItem(1);
                }
                break;
            case R.id.iv_title_three:
                if (vpContent.getCurrentItem() != 2) {
                    setCurrentItem(2);
                }
                break;
            case R.id.iv_avatar:
                WebViewActivity.loadUrl(v.getContext(), CommonUtils.getString(R.string.string_url_cloudreader), "CloudReader");
                break;
            case R.id.ll_nav_exit:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                break;
            case 1:
                setCurrentItem(1);
                break;
            case 2:
                setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        boolean isOne = false;
        boolean isTwo = false;
        boolean isThree = false;
        switch (position) {
            case 0:
                isOne = true;
                break;
            case 1:
                isTwo = true;
                break;
            case 2:
                isThree = true;
                break;
            default:
                isTwo = true;
                break;
        }
        vpContent.setCurrentItem(position);
        ivTitleOne.setSelected(isOne);
        ivTitleTwo.setSelected(isTwo);
        ivTitleThree.setSelected(isThree);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            mBinding.drawerLayout.postDelayed(() -> {
                switch (v.getId()) {
//                    case R.id.ll_nav_homepage:// 主页
//                        NavHomePageActivity.startHome(MainActivity.this);
//                        break;
                    case R.id.ll_nav_scan_download://扫码下载
                        NavDownloadActivity.start(MainActivity.this);
                        break;
                    case R.id.ll_nav_deedback:// 问题反馈
                        NavDeedBackActivity.start(MainActivity.this);
                        break;
//                    case R.id.ll_nav_about:// 关于云阅
//                        NavAboutActivity.start(MainActivity.this);
//                        break;
                    case R.id.ll_nav_collect:// 玩安卓收藏
                        if (UserUtil.isLogin(MainActivity.this)) {
                            ArticleListActivity.start(MainActivity.this);
                        }
                        break;
                    case R.id.ll_nav_login:// 玩安卓登录
                        DialogBuild.showItems(v, new OnLoginListener() {
                            @Override
                            public void loginWanAndroid() {
                                LoginActivity.start(MainActivity.this);
                            }

                            @Override
                            public void loginGitHub() {
                                WebViewActivity.loadUrl(v.getContext(), "https://github.com/login", "登录GitHub账号");
                            }
                        });
                        break;
                    default:
                        break;
                }
            }, 260);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 夜间模式待完善
     */
    public boolean getNightMode() {
        return SPUtils.getNightMode();
    }

    public void onNightModeClick(View view) {
        if (!SPUtils.getNightMode()) {
            //            SkinCompatManager.getInstance().loadSkin(Constants.NIGHT_SKIN);
        } else {
            // 恢复应用默认皮肤
            //            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
        SPUtils.setNightMode(!SPUtils.getNightMode());
        bind.dayNightSwitch.setChecked(SPUtils.getNightMode());
    }

    /**
     * 每日推荐点击"新电影热映榜"跳转
     */
    private void initRxBus() {
        Subscription subscribe = RxBus
                .getDefault()
                .toObservable(RxCodeConstants.JUMP_TYPE_TO_ONE, RxBusBaseMessage.class)
                .subscribe(integer -> setCurrentItem(2));
        addSubscription(subscribe);
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
