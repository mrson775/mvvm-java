package com.dallmeier.evidencer.ui.user;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.AccessToken;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.google.gson.JsonObject;

import javax.inject.Inject;


public class SignInViewModel extends BaseViewModel<SignInRepository> {
    private MutableLiveData<JsonObject> responsePerson = new MutableLiveData<JsonObject>();
    private MutableLiveData<AccessToken> accessToken = new MutableLiveData<AccessToken>();
    private AppDatabase appDatabase;

    @Inject
    public SignInViewModel(SignInRepository mRepository, AppDatabase appDatabase) {
        super(mRepository);
        this.appDatabase = appDatabase;
    }

    public void signIn() {
        repository.signIn(getCurrentPerson());
    }

    public void accessToken(String username, String password, String grantType) {
        repository.accessToken(getAccessToken(), username, password, grantType);
    }

    /**
     * Live data for add sender
     *
     * @return
     */
    public MutableLiveData<JsonObject> getCurrentPerson() {
        if (responsePerson == null) {
            responsePerson = new MutableLiveData<>();
        }
        return responsePerson;
    }

    public MutableLiveData<AccessToken> getAccessToken() {
        if (accessToken == null) {
            accessToken = new MutableLiveData<>();
        }
        return accessToken;
    }

    public boolean isChecked() {
        boolean isCheck;
        isCheck = SharedPrefUtil.getInstance().getRememberLogin();
        return isCheck;
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }
}
