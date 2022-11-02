package com.dallmeier.evidencer.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.common.Constant;
import com.dallmeier.evidencer.databinding.ActivitySigninBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshToken;
import com.dallmeier.evidencer.model.AccessToken;
import com.dallmeier.evidencer.model.UserEntity;
import com.dallmeier.evidencer.utils.LocationManagerUtils;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@AndroidEntryPoint
public class SignInActivity extends BaseActivity<SignInViewModel, ActivitySigninBinding> implements CompoundButton.OnCheckedChangeListener {
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private ActivitySigninBinding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_signin;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewDataBinding();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        liveAccessToken();
        liveGetCurrentPerson();
        setListener();
        if (hasPermission()) {
            if (LocationManagerUtils.getInstance(this).checkLocationService(this) &&
                    SharedPrefUtil.getInstance().getRememberLogin() && Utils.isConnected(this)) {
                inputUser();
            }
        } else {
            requestPermissionsApp();
        }
    }

    private void inputUser() {
        showLoading();
        if (SharedPrefUtil.getInstance().getRememberLogin()) {
            try {
                UserEntity userEntity = mViewModel.appDatabase().userDao().getUserInfo();
                if (userEntity != null) {
                    mBinding.usernameLoginEdt.setText(userEntity.getUserName());
                    mBinding.passLoginEdt.setText("......");
                } else {
                    hideLoading();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewModel.signIn();
        }
    }

    private void setListener() {
        mBinding.setActivityCallback(this);
        mBinding.setViewModelCallback(mViewModel);
        mBinding.rememberCb.setOnCheckedChangeListener(this);
    }

    private void liveAccessToken() {
        mViewModel.getAccessToken().observe(this, this::saveAccessToken);
    }

    private void liveGetCurrentPerson() {
        mViewModel.getCurrentPerson().observe(this, this::saveCurrentPerson);
    }

    private void saveCurrentPerson(JsonObject jsonObject) {
        if (jsonObject != null) {
            int mUserId = jsonObject.get("id").getAsInt();
            UserEntity userEntity = new UserEntity();
            userEntity.setIdUser(mUserId);
            userEntity.setUserName(mBinding.usernameLoginEdt.getText().toString());
            try {
                mViewModel.appDatabase().userDao().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewModel.appDatabase().userDao().insertUser(userEntity);
            intentMainActivity();
            if (mProgressHUD != null) {
                mProgressHUD.cancel();
            }
        } else {
            mBinding.passLoginEdt.setText("");
            Toast.makeText(this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
            mProgressHUD.cancel();
        }
    }

    private void saveAccessToken(AccessToken accessToken) {
        if (accessToken != null) {
            SharedPrefUtil.getInstance().setAccessToken(accessToken.getAccessToken());
            mViewModel.signIn();
        } else {
            Toast.makeText(this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
            mProgressHUD.cancel();
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
        SharedPrefUtil.getInstance().setRememberLogin(isCheck);
    }

    /**
     * validateTextInput
     */
    public void validateTextInput() {
        if (StringUtils.isEmpty(mBinding.usernameLoginEdt.getText())) {
            mBinding.usernameLoginEdt.setError(this.getString(R.string.required));
        } else if (StringUtils.isEmpty(mBinding.passLoginEdt.getText())) {
            mBinding.passLoginEdt.setError(this.getString(R.string.required));
        } else if (StringUtils.isEmpty(mBinding.baseUrlEdt.getText())) {
            mBinding.baseUrlEdt.setError(this.getString(R.string.required));
        } else if (!Constant.DOMAIN.contains(mBinding.baseUrlEdt.getText())) {
            mBinding.baseUrlEdt.setError(this.getString(R.string.base_url_invalid));
        } else {
            showLoading();
            mViewModel.accessToken(mBinding.usernameLoginEdt.getText().toString().trim(),
                    mBinding.passLoginEdt.getText().toString().trim(), Constant.GRANT_TYPE);
        }
    }

    @Subscribe
    public void refreshTokenInvalid(RefreshToken refreshToken) {
        Log.d("CountDownTimer", "refreshToken");
        liveAccessToken();
        liveGetCurrentPerson();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}