package com.dallmeier.evidencer.ui.incidents.create;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.incident.create.Incident;
import com.dallmeier.evidencer.model.incident.create.IncidentDto;
import com.dallmeier.evidencer.model.place.Places;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiServiceMap;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IncidentCreateRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<List<Places>> liveData;
    private MutableLiveData<Incident> liveDataNewIncident;
    private MutableLiveData<List<AssigneeEntity>> liveAssignee;
    private ApiServiceMap mServiceMap;

    @Inject
    public IncidentCreateRepository(ApiService apiService, ApiServiceMap apiServiceMap, Gson gson) {
        super(apiService, gson);
        this.mServiceMap = apiServiceMap;
    }

    public void getAddress(String textInput, MutableLiveData<List<Places>> liveData) {
        this.liveData = liveData;
        ApiTask.execute(() -> mServiceMap.filterAddress(textInput), ApiTaskType.FILTER_ADDRESS, this);
    }

    public void createNewIncident(IncidentDto incidentDto, MutableLiveData<Incident> liveData) {
        liveDataNewIncident = liveData;
        ApiTask.execute(() -> mService.createNewIncident(incidentDto), ApiTaskType.CREATE_INCIDENT, this);
    }

    private void handleCreateSuccess(ApiTask task) {
        Incident incidentParent = (Incident) task.getResponse().body();
        this.liveDataNewIncident.postValue(incidentParent);
    }

    private void handlePlacesSuccess(ApiTask task) {
        ArrayList<Places> list = (ArrayList<Places>) task.getResponse().body();
        this.liveData.postValue(list);
    }

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCodeSender(ApiTask task) {

    }

    private void passStringErr(CodeRequestResult codeRequestResult) {
        switch (codeRequestResult.getErrCode()) {
        }

    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                //todo handle success code
                /*   CodeRequestResult codeRequestResult = new CodeRequestResult();*/
                switch (task.getType()) {
                    case FILTER_ADDRESS:
                        handlePlacesSuccess(task);
                        break;
                    case CREATE_INCIDENT:
                        handleCreateSuccess(task);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case INCIDENT_TYPE:
                        handleErrorCodeSender(task);
                        break;
                }
            }
        }

        return false;
    }
}