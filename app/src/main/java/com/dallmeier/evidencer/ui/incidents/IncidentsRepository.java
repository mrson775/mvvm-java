package com.dallmeier.evidencer.ui.incidents;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.dao.dao_v1.IncidentDao;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class IncidentsRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<List<IncidentEntity>> liveIncidents;
    private final IncidentDao incidentDao;
    private final AppDatabase appDatabase;

    @Inject
    public IncidentsRepository(IncidentDao incidentDao, ApiService apiService, Gson gson, AppDatabase appDatabase) {
        super(apiService, gson);
        this.incidentDao = incidentDao;
        this.appDatabase = appDatabase;
    }

    public void getIncidents(MutableLiveData<List<IncidentEntity>> liveData, String page, int size, String sort) {
        liveIncidents = liveData;
        ApiTask.execute(() -> mService.getIncidents(page, size, sort), ApiTaskType.INCIDENTS, this);
    }

    private void handleIncidentsSuccess_V1(ApiTask task, Gson gson) {
        JsonObject jsonObject = (JsonObject) task.getResponse().body();
        assert jsonObject != null;
        JsonArray mResArr = jsonObject.getAsJsonArray("content");
        Type type = new TypeToken<List<IncidentEntity>>() {
        }.getType();
        List<IncidentEntity> incidents = gson.fromJson(mResArr, type);
        if (incidents.isEmpty()) {
            return;
        }
        liveIncidents.postValue(incidents);
        IncidentEntity incident;
        for (int i = 0; i <= incidents.size() - 1; i++) {
            incident = appDatabase.incidentDao().getIncidentById(incidents.get(i).getId());
            if (incident == null) {
                incident = incidents.get(i);
            }
            if (incidents.get(i).isHasLocation()) {
                incident.setType(incidents.get(i).getLocation().getType());
                incident.setLatitude(incidents.get(i).getLocation().getCoordinates().get(1));
                incident.setLongitude(incidents.get(i).getLocation().getCoordinates().get(0));
                incident.setAggregatedEventId(incidents.get(i).getAggregatedEvent().getId());
                incident.setNumberAllEvidences(incidents.get(i).getNumberAllEvidences());
                incidentDao.insertOrUpdate(incident);
            }
        }

    }

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCode(ApiTask task) {
        //todo some thing
    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                //todo handle success code
                switch (task.getType()) {
                    case INCIDENTS:
                        handleIncidentsSuccess_V1(task, gson);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case INCIDENTS:
                        handleErrorCode(task);
                        break;
                }
            }
        }
        return false;
    }
}