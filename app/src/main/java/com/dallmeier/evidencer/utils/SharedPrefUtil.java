package com.dallmeier.evidencer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SharedPrefUtil {
    private static final String PREFS_NAME = "life";
    private static SharedPrefUtil mInstance = null;
    private SharedPreferences mPref;
    private static final String USER = "user";
    private static final String INCIDENT = "incident";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String BEARER_TOKEN = "BEARER_TOKEN";
    private static final String REMEMBER_LOGIN = "remember_login";
    private static final String THEME = "theme";

    public static void init(@NonNull Context context) {
        if (mInstance == null) {
            synchronized (SharedPrefUtil.class) {
                if (mInstance == null) {
                    mInstance = new SharedPrefUtil(context);
                }
            }
        }
    }

    public static SharedPrefUtil getInstance() {
        if (mInstance == null) {
            throw new NullPointerException("SharedPrefUtil has not been initialized properly. Call SharedPrefUtil.init(Context) in your Application.onCreate() method.");
        }
        return mInstance;
    }

    private SharedPrefUtil(Context context) {
        this.mPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserLogin(String userInfo) {
        SharedPreferences.Editor et = this.mPref.edit();
        et.putString(USER, userInfo);
        et.apply();
    }

    public void setIncident(IncidentEntity incident) {
        SharedPreferences.Editor et = this.mPref.edit();
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String json = gson.toJson(incident);
        et.putString(INCIDENT, json);
        et.apply();
    }

    public IncidentEntity getIncident() {
        String incidentString = this.mPref.getString(INCIDENT, "");
        Gson gson = new Gson();
        IncidentEntity incident = gson.fromJson(incidentString, IncidentEntity.class);
        return incident;
    }

    public void setRememberLogin(boolean checked) {
        SharedPreferences.Editor et = this.mPref.edit();
        et.putBoolean(REMEMBER_LOGIN, checked);
        et.apply();
    }

    public boolean getRememberLogin() {
        boolean accessToken = this.mPref.getBoolean(REMEMBER_LOGIN, false);
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor et = this.mPref.edit();
        et.putString(ACCESS_TOKEN, accessToken);
        et.apply();
    }

    public String getAccessToken() {
        String accessToken = this.mPref.getString(ACCESS_TOKEN, "");
        return "Bearer " + accessToken;
    }

    public void setTheme(String theme) {
        SharedPreferences.Editor et = this.mPref.edit();
        et.putString(THEME, theme);
        et.apply();
    }

    public String getTheme() {
        String theme = this.mPref.getString(THEME, Statics.DARK_THEME);
        return theme;
    }
}
