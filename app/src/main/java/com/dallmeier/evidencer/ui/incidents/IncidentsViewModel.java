package com.dallmeier.evidencer.ui.incidents;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.model.incident_response.StateEntity;
import com.dallmeier.evidencer.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class IncidentsViewModel extends BaseViewModel<IncidentsRepository> {
    private final LiveData<List<IncidentEntity>> entityIncident = new MutableLiveData<>();
    private MutableLiveData<List<IncidentEntity>> incidentsLive = new MutableLiveData<>();
    private final AppDatabase appDatabase;
    private final Picasso picasso;

    @Inject
    public IncidentsViewModel(IncidentsRepository incidentsRepository, AppDatabase appDatabase, Picasso picasso) {
        super(incidentsRepository);
        this.appDatabase = appDatabase;
        this.picasso = picasso;
    }

    public List<IncidentEntity> getIncidents() {
        return entityIncident.getValue();
    }

    public void getIncidents(String page, int size, String sort) {
        repository.getIncidents(getResponseIncident(), page, size, sort);
    }

    public void updateIncident(IncidentEntity incident) {
        appDatabase.incidentDao().insertOrUpdate(incident);
    }

    /**
     * get incidents by title
     *
     * @param input String
     * @return List
     */
    public List<IncidentEntity> getIncidentsByTitle(String input) {
        return appDatabase.incidentDao().getIncidentsByTitle(input);
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

    /**
     * clean state on db
     */
    public void resetState() {
        StateEntity stateEntitySelect = appDatabase.stateDao().searchTmpCheck();
        if (stateEntitySelect != null) {
            stateEntitySelect.setTmpCheck(false);
            appDatabase.stateDao().update(stateEntitySelect);
        }
    }

    /**
     * set state to db
     */
    public void setState(IncidentEntity incident) {
        StateEntity stateEntity = appDatabase.stateDao().searchById(incident.getStateId());
        if (stateEntity != null) {
            stateEntity.setTmpCheck(true);
            appDatabase.stateDao().update(stateEntity);
        }
    }

    public Drawable getDrawableIsRead(IncidentEntity incident) {
        Drawable drawable;
        try {
            if (!appDatabase.incidentDao().getIncidentById(incident.getId()).isRead()) {
                drawable = Utils.getIcon(BaseApplication.getInstance().getApplicationContext(), R.drawable.ic_unread);
            } else {
                drawable = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            drawable = null;
        }
        return drawable;
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }
    public Picasso picasso() {
        return picasso;
    }
}