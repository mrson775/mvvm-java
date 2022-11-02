package com.dallmeier.evidencer.network;

import android.os.Bundle;

import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.ui.user.SignInActivity;
import com.dallmeier.evidencer.utils.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dallmeier.evidencer.network.ApiResponseCode.SUCCESS_CREATE;

/**
 * Created by Mrson 6/24/2016.
 */
public class ApiTask implements Callback {

    private OnCreateCallCallback mCallback;
    private ApiTaskType mType;
    private ApiResponseCallback mListener;
    private Response mResponse;
    private Throwable mThrowable;
    private Call mCall;
    private Bundle mArguments;

    public static ApiTask create(OnCreateCallCallback callback,
                                 ApiTaskType type,
                                 ApiResponseCallback listener) {
        ApiTask task = new ApiTask();
        task.mCallback = callback;
        task.mType = type;
        task.mListener = listener;

        return task;
    }

    public static void execute(OnCreateCallCallback callback,
                               ApiTaskType type,
                               ApiResponseCallback listener) {
        ApiTask task = new ApiTask();
        task.mCallback = callback;
        task.mType = type;
        task.mListener = listener;
        task.execute();
    }

    public static void execute(OnCreateCallCallback callback,
                               ApiTaskType type,
                               ApiResponseCallback listener, Bundle bundle) {
        ApiTask task = new ApiTask();
        task.mCallback = callback;
        task.mType = type;
        task.mListener = listener;
        task.mArguments = bundle;
        task.execute();
    }

    private ApiTask() {
        // Required empty public constructor
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public void execute() {
        mCallback.onCreateCall().enqueue(this);
    }

    /**
     * Invoked for a received HTTP response.
     * <p/>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call call, Response response) {
        mCall = call;
        mResponse = response;
        mThrowable = null;
        mListener.onResponse(this);
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call call, Throwable t) {
        mCall = call;
        mResponse = null;
        mThrowable = t;
        //mListener.onResponse(this);
        SharedPrefUtil.getInstance().setAccessToken("");
        SharedPrefUtil.getInstance().setRememberLogin(false);
        SignInActivity.toActivity(BaseApplication.getInstance().getApplicationContext());
        // Crashlytics.logException(t);
    }

    public ApiTaskType getType() {
        return mType;
    }

    public Response getResponse() {
        return mResponse;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public Call getCall() {
        return mCall;
    }

    public interface OnCreateCallCallback {
        Call onCreateCall();
    }
}
