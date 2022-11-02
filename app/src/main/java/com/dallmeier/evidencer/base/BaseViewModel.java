package com.dallmeier.evidencer.base;

import androidx.lifecycle.ViewModel;

import com.dallmeier.evidencer.common.TabSelected;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.listener.EventBus.RefreshNumberTb;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;

import org.greenrobot.eventbus.EventBus;

public class BaseViewModel<T> extends ViewModel implements IUpdateEvidents {
    public T repository;

    public BaseViewModel(T repository) {
        this.repository = repository;
    }

    /**
     * update
     * number of Evidents of the incident
     *
     * @param incidentId int
     */
    @Override
    public void updateEvidents(AppDatabase appDatabase, long incidentId) {
        IncidentEntity incidentEntity = appDatabase.incidentDao().getIncidentById(incidentId);
        try {
            incidentEntity.setNumberAllEvidences(incidentEntity.getNumberAllEvidences() + 1);
            appDatabase.incidentDao().insertOrUpdate(incidentEntity);
            EventBus.getDefault().post(new RefreshNumberTb(TabSelected.EVIDENTS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * update count of Comments of the incident
     * @param appDatabase AppDatabase
     * @param incidentId long
     */
    @Override
    public void updateComments(AppDatabase appDatabase, long incidentId) {
        IncidentEntity incidentEntity = appDatabase.incidentDao().getIncidentById(incidentId);
        try {
            incidentEntity.setNumberComment(incidentEntity.getNumberComment() + 1);
            appDatabase.incidentDao().insertOrUpdate(incidentEntity);
            EventBus.getDefault().post(new RefreshNumberTb(TabSelected.COMMENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
