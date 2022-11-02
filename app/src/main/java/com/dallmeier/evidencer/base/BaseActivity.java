package com.dallmeier.evidencer.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.listener.EventBus.RefreshToken;
import com.dallmeier.evidencer.ui.incidents.IncidentsFragment;
import com.dallmeier.evidencer.ui.main.MainActivity;
import com.dallmeier.evidencer.ui.map.MapFragment;
import com.dallmeier.evidencer.utils.ProgressHUD;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.dallmeier.evidencer.utils.network.ConnectivityReceiver;
import com.dallmeier.evidencer.utils.network.INetwork;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import static android.Manifest.permission.RECORD_AUDIO;

/**
 * BaseActivity
 */

public abstract class BaseActivity<M extends BaseViewModel, T extends ViewDataBinding> extends AppCompatActivity implements INetwork {
    /* IncidentsFragment incidentsFragment = IncidentsFragment.newInstance();
     MapFragment mapFragment = MapFragment.newInstance();*/
    protected ProgressHUD mProgressHUD;
    protected static final int PERMISSIONS_REQUEST = 1;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter filter;
    @Inject
    protected M mViewModel;
    protected T mBinding;

    protected abstract
    @LayoutRes
    int getLayoutResId();

    public abstract int getBindingViewModel();

    public T getViewDataBinding() {
        return mBinding;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getTheme(SharedPrefUtil.getInstance().getTheme()));
        mBinding = DataBindingUtil.setContentView(this, getLayoutResId());
        mBinding.setVariable(getBindingViewModel(), mViewModel);
        mBinding.executePendingBindings();
        connectivityReceiver = new ConnectivityReceiver(this);
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public void intentMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        this.startActivity(mainIntent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected boolean hasPermission() {
        return Utils.hasPermissions(this, Statics.permission);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermissionSingle(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Statics.permission, PERMISSIONS_REQUEST);
        }
    }

    protected void requestPermissionAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{RECORD_AUDIO}, PERMISSIONS_REQUEST);
        }
    }

    /**
     * count down to refresh token
     */
    public void refreshTokenCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(200000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("CountDownTimer", millisUntilFinished / 1000 + "");
            }

            public void onFinish() {
                EventBus.getDefault().post(new RefreshToken());
                refreshTokenCountDown();
            }
        };
        countDownTimer.start();
    }

    protected void replaceFragmentIncident() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.container, new IncidentsFragment(), "INCIDENT");
        /*if (incidentsFragment.isAdded()) { // if the fragment is already in container
            ft.show(incidentsFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container, incidentsFragment, "INCIDENT");
        }
        // Hide fragment B
        if (mapFragment.isAdded()) {
            ft.hide(mapFragment);
        }*/
        ft.commit();
    }

    protected void replaceFragmentMap() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.container, new MapFragment(), "MAP");
        /*if (mapFragment.isAdded()) { // if the fragment is already in container
            ft.show(mapFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container, mapFragment, "INCIDENT");
        }
        // Hide fragment B
        if (incidentsFragment.isAdded()) {
            ft.hide(incidentsFragment);
        }*/
        ft.commit();
    }

    public void showLoading() {
        if (mProgressHUD == null) {
            mProgressHUD = ProgressHUD.show(this, getString(R.string.loading), true, true, null);
        } else {
            if (!mProgressHUD.isShowing())
                mProgressHUD.show();
        }
    }

    public void hideLoading() {
        if (mProgressHUD != null) {
            mProgressHUD.cancel();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //todo handle network
        Log.d("BaseActivity", "isConnected" + isConnected);
    }
}
