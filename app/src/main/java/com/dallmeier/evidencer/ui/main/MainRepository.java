package com.dallmeier.evidencer.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.incident.IncidentTypeEntity;
import com.dallmeier.evidencer.model.incident_response.StateEntity;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<Integer> liveLogout;
    private MutableLiveData<StateEntity> stateLive;
    private final AppDatabase appDatabase;

    @Inject
    public MainRepository(ApiService apiService, Gson gson, AppDatabase appDatabase) {
        super(apiService, gson);
        this.appDatabase = appDatabase;
    }

    /**
     * request accessToken
     *
     * @param liveData MutableLiveData
     */
    public void logout(MutableLiveData<Integer> liveData) {
        liveLogout = liveData;
        ApiTask.execute(() -> mService.logout(SharedPrefUtil.getInstance().getAccessToken()), ApiTaskType.LOGOUT, this);
    }

    public void getIncidentType(String page, int size, String sort) {
        ApiTask.execute(() -> mService.getIncidentType(page, size, sort), ApiTaskType.INCIDENT_TYPE, this);
    }

    public void getState(MutableLiveData<StateEntity> liveData) {
        stateLive = liveData;
        ApiTask.execute(() -> mService.getIncidents(), ApiTaskType.STATE, this);
    }

    public void getUsers() {
        ApiTask.execute(() -> mService.getUsers(Utils.convertStringToJson(Statics.userRequest)), ApiTaskType.USERS, this);
    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                //todo handle success code
                /*   CodeRequestResult codeRequestResult = new CodeRequestResult();*/
                switch (task.getType()) {
                    case LOGOUT:
                        liveLogout.postValue(task.getResponse().code());
                        break;
                    case STATE:
                        List<StateEntity> states = (ArrayList<StateEntity>) task.getResponse().body();
                        appDatabase.stateDao().insertStates(states);
                        break;
                    case INCIDENT_TYPE:
                        handleIncidentTypeRes(task);
                        break;
                    case USERS:
                        List<AssigneeEntity> assignees = (ArrayList<AssigneeEntity>) task.getResponse().body();
                        appDatabase.assigneeDao().insertAssignees(assignees);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case LOGOUT:
                        break;
                }
            }
        }
        return false;
    }

    /**
     * handle after response
     *
     * @param task INCIDENT_TYPE
     */
    private void handleIncidentTypeRes(ApiTask task) {
        JsonObject jsonObject = (JsonObject) task.getResponse().body();
        JsonObject page = null;
        JsonObject _embedded = null;
        _embedded = jsonObject.getAsJsonObject("_embedded");
        JsonArray mResArr = _embedded.getAsJsonArray("entityList");
        if (mResArr.size() > 0)
            for (int i = 0; i <= mResArr.size() - 1; i++) {
                JsonObject actor = mResArr.get(i).getAsJsonObject();
                JsonObject content = actor.getAsJsonObject("content");
                IncidentTypeEntity incidentTypeEntity = gson.fromJson(content, IncidentTypeEntity.class);
                appDatabase.incidentTypeDao().insertOrUpdate(incidentTypeEntity);
            }
    }

}