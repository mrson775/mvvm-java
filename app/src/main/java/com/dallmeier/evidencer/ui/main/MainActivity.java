package com.dallmeier.evidencer.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.databinding.ActivityMainBinding;
import com.dallmeier.evidencer.listener.EventBus.SearchInput;
import com.dallmeier.evidencer.listener.IConfirmDialog;
import com.dallmeier.evidencer.model.UserEntity;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.ui.user.SignInActivity;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> implements IConfirmDialog, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private TextView tvTitle;
    private SearchView searchView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        liveLogout();
        refreshTokenCountDown();
    }

    private void liveLogout() {
        mViewModel.liveLogout().observe(this, this::logoutResponse);
    }

    private void logoutResponse(Integer integer) {
        if (integer == ApiResponseCode.SUCCESS) {
            SharedPrefUtil.getInstance().setRememberLogin(false);
            SharedPrefUtil.getInstance().setAccessToken("");
            SignInActivity.toActivity(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.tvTitle);

        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        setSupportActionBar(toolbar);
        initProfile(toolbar);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initProfile(Toolbar toolbar) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView profile = navigationView.getHeaderView(0).findViewById(R.id.name_tv);
        ImageView logoutImg = navigationView.getHeaderView(0).findViewById(R.id.logout_img);
        logoutImg.setOnClickListener(this);
        UserEntity userInfo = mViewModel.appDatabase().userDao().getUserInfo();
        profile.setText(userInfo.getUserName());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final LinearLayout content = findViewById(R.id.app_bar_main);
        setupToolbarAndDrawer(content, toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void setupToolbarAndDrawer(LinearLayout nv, Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                nv.setTranslationX(slideOffset * drawerView.getWidth());
            }
        };
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolBar();
        }
        mViewModel.getStates();
        mViewModel.getIncidentType();
        mViewModel.getUser();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_themes:
                Utils.showThemesDialog(this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Utils.showConfirmDialogYesNo(this, R.string.app_name, R.string.confirm_exit, this, Statics.EXIT_APP);
    }

    @Override
    public void clickYes(Dialog dialog, int task) {
        switch (task) {
            case Statics.EXIT_APP:
                this.finishAffinity();
                break;
            case Statics.LOGOUT:
                mViewModel.logout();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_img:
                Utils.showConfirmDialogYesNo(this, R.string.app_name, R.string.logout, this, Statics.LOGOUT);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                tvTitle.setText(getString(R.string.menu_map));
                searchView.setQuery("", false);
                searchView.clearFocus();
                replaceFragmentMap();
                break;
            case R.id.nav_incidents:
                replaceFragmentIncident();
                tvTitle.setText(getString(R.string.menu_incidents));
                searchView.setQuery("", false);
                searchView.clearFocus();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mViewModel.getHandler().removeCallbacksAndMessages(null);
        mViewModel.getHandler().postDelayed(() -> EventBus.getDefault().post(new SearchInput(newText)), 400);
        return true;
    }
}