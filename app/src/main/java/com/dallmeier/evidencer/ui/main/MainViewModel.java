package com.dallmeier.evidencer.ui.main;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.incident_response.StateEntity;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel<MainRepository> {
    private MutableLiveData<Integer> liveLogout = new MutableLiveData<Integer>();
    private MutableLiveData<StateEntity> liveState = new MutableLiveData<StateEntity>();
    private final Handler mHandler;
    private final String sort = "timeStampCreated,asc";
    private final AppDatabase appDatabase;

    @Inject
    public MainViewModel(Handler mHandler, MainRepository repository, AppDatabase appDatabase) {
        super(repository);
        this.mHandler = mHandler;
        this.appDatabase = appDatabase;
    }

    public void logout() {
        repository.logout(liveLogout());
    }

    public void getStates() {
        repository.getState(liveState);
    }

    public void getUser() {
        repository.getUsers();
    }

    public void getIncidentType() {
        repository.getIncidentType(String.valueOf(0), 1000, sort);
    }

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Live data for add sender
     *
     * @return
     */
    public MutableLiveData<Integer> liveLogout() {
        if (liveLogout == null) {
            liveLogout = new MutableLiveData<>();
        }
        return liveLogout;
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }
}
