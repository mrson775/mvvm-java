package com.dallmeier.evidencer.ui.user;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.model.AccessToken;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiServiceMap;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Inject;

public class SignInRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<JsonObject> liveCurrentPerson;
    private MutableLiveData<AccessToken> liveAccessToken;

    @Inject
    public SignInRepository(ApiService apiService, ApiServiceMap apiServiceMap, Gson gson) {
        super(apiService, gson);
    }

    public void signIn(MutableLiveData<JsonObject> liveData) {
        liveCurrentPerson = liveData;
        ApiTask.execute(mService::getCurrentPerson, ApiTaskType.CURRENT_PERSON, this);
    }

    /**
     * request accessToken
     *
     * @param liveData MutableLiveData
     */
    public void accessToken(MutableLiveData<AccessToken> liveData, String username, String password, String grantType) {
        liveAccessToken = liveData;
        ApiTask.execute(() -> mService.getAccessToken(username, password, grantType), ApiTaskType.ACCESS_TOKEN, this);
    }

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCodeSender(ApiTask task) {
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
                    case CURRENT_PERSON:
                        JsonObject jsonObject = (JsonObject) task.getResponse().body();
                        liveCurrentPerson.postValue(jsonObject);
                        break;
                    case ACCESS_TOKEN:
                        AccessToken accessToken = (AccessToken) task.getResponse().body();
                        liveAccessToken.postValue(accessToken);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case CURRENT_PERSON:
                        liveCurrentPerson.postValue(null);
                        handleErrorCodeSender(task);
                        break;
                    case ACCESS_TOKEN:
                        liveAccessToken.postValue(null);
                        handleErrorCodeSender(task);
                        break;
                }
            }
        }
        return false;
    }
}