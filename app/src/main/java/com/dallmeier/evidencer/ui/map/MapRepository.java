package com.dallmeier.evidencer.ui.map;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MapRepository extends BaseRepository implements ApiResponseCallback {
    MutableLiveData<List<IncidentEntity>> liveIncidents;

    @Inject
    public MapRepository(ApiService apiService, Gson gson) {
        super(apiService, gson);
    }

    public void getIncidentNearV1(MutableLiveData<List<IncidentEntity>> liveData, double latitude, double longitude, double maxDistanceKm) {
        liveIncidents = liveData;
        ApiTask.execute(() -> mService.getIncidentNearV1(latitude, longitude, maxDistanceKm), ApiTaskType.INCIDENT_NEAR, this);
    }

    /**
     * handleSuccessIncidentNear
     *
     * @param task ApiTask
     */
    private void handleSuccessIncidentNearV1(ApiTask task) {
        List<IncidentEntity> incidents = (ArrayList<IncidentEntity>) task.getResponse().body();
        liveIncidents.postValue(incidents);
    }

    /* *//**
     * handleSuccessIncidentNear with the new api
     *
     * @param gson Gson
     * @param task ApiTask
     *//*
    private void handleSuccessIncidentNear(Gson gson, ApiTask task) {
        JsonObject jsonObject = (JsonObject) task.getResponse().body();
        JsonObject _embedded;
        _embedded = jsonObject.getAsJsonObject("_embedded");
        JsonObject page = null;
        if (_embedded != null) {
            mResArr = _embedded.getAsJsonArray("entityList");
            page = jsonObject.getAsJsonObject("page");
            PageCurrentIncidentEntity pageEntity = gson.fromJson(page, PageCurrentIncidentEntity.class);
         *//*   if(pageEntity.getTotalElements()==0){
                BaseApplication.getInstance().dbRoom.pageCurrentDao().delete();
            }*//*
            Type type = new TypeToken<List<Parentincident>>() {
            }.getType();
            List<Parentincident> list = gson.fromJson(mResArr, type);
            for (int i = 0; i < mResArr.size(); i++) {
                JsonObject actor = mResArr.get(i).getAsJsonObject();
                JsonObject content = actor.getAsJsonObject("content");
                if (content != null) {
                    *//*   BaseApplication.getInstance().dbRoom.pageCurrentDao().insertPage(pageEntity);*//*
                    IncidentParent incidentParent = gson.fromJson(content, IncidentParent.class);
                    String incidentType = gson.toJson(incidentParent.getIncidentTypeEntity());
                    String defaultWorkflow = gson.toJson(incidentParent.getDefaultWorkflow());
                    //Create Incident with currentID
                    IncidentAllEntity incidentEntity = new IncidentAllEntity(incidentParent.getId(), incidentParent.getAssigneeId(), incidentParent.getTimeStampCreated(),
                            incidentParent.getIncidentNbo(), incidentType, defaultWorkflow, gson.toJson(incidentParent.getAggregatedEvent()));
                    incidentEntity.setLatitude(incidentParent.getLocation().getCoordinates().get(1));
                    incidentEntity.setLongitude(incidentParent.getLocation().getCoordinates().get(0));
                    //save incidentEntity to DB
                    BaseApplication.getInstance().getRoom().incidentAllDao().insertSingle(incidentEntity);
                }

            }
        }
    }*/

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCode(ApiTask task) {
       /* String errorBody = null;
        try {
            errorBody = task.getResponse().errorBody().string();
            JSONObject jsonError = new JSONObject(errorBody);
            CodeRequestResult codeRequestResult = new CodeRequestResult();
            codeRequestResult.setApiTaskType(task.getType());
            codeRequestResult.setErrCode(jsonError.optString(ApiErrorCode.ERROR_CODE));
            passStringErr(codeRequestResult);
            addSenderCode.postValue(codeRequestResult);
        } catch (IOException | JSONException e) {
            CodeRequestResult codeRequestResult = new CodeRequestResult();
            codeRequestResult.setDescription(context.getString(R.string.errCode_err020));
            addSenderCode.postValue(codeRequestResult);
            e.printStackTrace();
        }
*/
    }

    private void passStringErr(CodeRequestResult codeRequestResult) {
        switch (codeRequestResult.getErrCode()) {
            /*case SENDER_WAS_ASSIGNED_BEFORE:
                codeRequestResult.setDescription(context.getString(R.string.errCode_err003));
                break;
            case INVALID_SERIAL_NUMBER:
                codeRequestResult.setDescription(context.getString(R.string.errCode_err002));
                break;
            case USER_IS_EXISTED:
                codeRequestResult.setDescription(context.getString(R.string.errCode_err045));
                break;
            case ASSIGN_SENDER_ERROR:
                codeRequestResult.setDescription(context.getString(R.string.errCode_err020));
            case EXCEED_MAX_SENDER:
                codeRequestResult.setDescription(context.getString(R.string.errCode_err048));
                break;*/
        }

    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                //todo handle success code
                /*   CodeRequestResult codeRequestResult = new CodeRequestResult();*/
                switch (task.getType()) {
                    case INCIDENT_NEAR:
                        handleSuccessIncidentNearV1(task);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case INCIDENT_NEAR:
                        handleErrorCode(task);
                        break;
                }
            }
        }
        return false;
    }
}