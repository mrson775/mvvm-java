package com.dallmeier.evidencer.ui.map;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;

import org.osmdroid.views.MapView;

import java.util.List;

import javax.inject.Inject;


public class MapViewModel extends BaseViewModel<MapRepository> {
    private MutableLiveData<List<IncidentEntity>> incidentsLive = new MutableLiveData<List<IncidentEntity>>();
    private CountDownTimer countDownTimer = null;
    private final Handler mHandler;
    private final AppDatabase appDatabase;

    @Inject
    public MapViewModel(Handler mHandler, MapRepository mapRepository, AppDatabase appDatabase) {
        super(mapRepository);
        this.mHandler = mHandler;
        this.appDatabase = appDatabase;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void getIncidentsNear(double latitude, double longitude, double maxDistanceKm) {
        repository.getIncidentNearV1(getResponseIncident(), latitude, longitude, maxDistanceKm);
    }

    /**
     * @return List<Incident>
     */
    public MutableLiveData<List<IncidentEntity>> getResponseIncident() {
        if (incidentsLive == null) {
            incidentsLive = new MutableLiveData<>();
        }
        return incidentsLive;
    }

    public void cancelCountdown() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    /**
     * count down to refresh token
     */
    public void loadIncidentsCountDown(MapView mMapView) {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d("CountDownTimer", millisUntilFinished / 1000 + "");
                }

                public void onFinish() {
                    getIncidentsNear(mMapView.getMapCenter().getLatitude(), mMapView.getMapCenter().getLongitude(), 1);
                }
            };
        }
        countDownTimer.start();
    }
    /*
     */

    /**
     * get incidents by title
     *
     * @param input
     * @return
     */
    public List<IncidentEntity> getIncidentsByTitle(String input) {
        return appDatabase.incidentDao().getIncidentsByTitle(input);
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }
}