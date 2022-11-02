package com.dallmeier.evidencer.ui.incidents.create;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.AssigneeDto;
import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.Criteria;
import com.dallmeier.evidencer.model.incident.IncidentTypeEntity;
import com.dallmeier.evidencer.model.incident.create.AggregatedEvent;
import com.dallmeier.evidencer.model.incident.create.AggregatedEventDTO;
import com.dallmeier.evidencer.model.incident.create.Incident;
import com.dallmeier.evidencer.model.incident.create.IncidentDto;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.place.Places;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IncidentCreateViewModel extends BaseViewModel<IncidentCreateRepository> {
    private MutableLiveData<List<Places>> placesLive = new MutableLiveData<>();
    private MutableLiveData<Incident> incidentParentLiveData = new MutableLiveData<>();
    private final Handler mHandler;
    private Runnable mRunnable;
    private IncidentDto incidentDto;
    private Incident incident;
    private AggregatedEventDTO aggregatedEventDTO;
    private AggregatedEvent aggregatedEvent;
    private final AppDatabase appDatabase;

    @Inject
    public IncidentCreateViewModel(Handler mHandler, IncidentCreateRepository mRepository, AppDatabase appDatabase) {
        super(mRepository);
        this.mHandler = mHandler;
        this.appDatabase = appDatabase;
    }

    public List<IncidentTypeEntity> incidentTypes() {
        return appDatabase.incidentTypeDao().getEvidentTypes();
    }

    public void getPlaces(String textInput) {
        repository.getAddress(textInput, getPlacesLive());
    }

    public void createNewIncident(IncidentDto incidentDto) {
        repository.createNewIncident(incidentDto, incidentNewLive());
    }

    public MutableLiveData<List<Places>> getPlacesLive() {
        if (placesLive == null) {
            placesLive = new MutableLiveData<>();
        }
        return placesLive;
    }

    public MutableLiveData<Incident> incidentNewLive() {
        if (incidentParentLiveData == null) {
            incidentParentLiveData = new MutableLiveData<>();
        }
        return incidentParentLiveData;
    }

    public List<AssigneeEntity> assignees() {
        return appDatabase.assigneeDao().getStates();
    }

    /**
     * count down to load Places
     */
    public void loadPlacesCountDown(String textInput) {
        mRunnable = () -> {
            getPlaces(textInput);
            Log.d("CountDownTimer", textInput);
        };
        mHandler.postDelayed(mRunnable, 500);

    }

    public void cancelCountdown() {
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }

    /**
     * //todo custom userDto request
     *
     * @return UserDto
     */
    private AssigneeDto initUserDto() {
        AssigneeDto userDto = new AssigneeDto();
        userDto.setCombine("AND");
        List<Criteria> criteriaList = new ArrayList<>();
        List<String> value1 = new ArrayList<>();
        value1.add("AIMS_INCIDENT_READ");
        value1.add("AIMS_INCIDENT");
        criteriaList.add(new Criteria("authGroups.permissions.authority", "IN", value1));
        List<String> value2 = new ArrayList<>();
        value2.add("true");
        criteriaList.add(new Criteria("userEnabled", "=", value2));
        userDto.setCriterias(criteriaList);
        return userDto;
    }

    public Incident getIncident() {
        return incident;
    }

    public IncidentDto getIncidentDto() {
        return incidentDto;
    }

    public AggregatedEvent getAggregatedEvent() {
        return aggregatedEvent;
    }

    public AggregatedEventDTO getAggregatedEventDTO() {
        return aggregatedEventDTO;
    }

    public void initIncidentDto() {
        incidentDto = new IncidentDto();
        incident = new Incident("", 0, 0, "", null, null,
                null, "", "", "", 0, null,
                null, null, null, null, null, false, null);
        aggregatedEventDTO = new AggregatedEventDTO();
        aggregatedEvent = new AggregatedEvent();
        aggregatedEvent.setTimeStampMillis(System.currentTimeMillis());
        incident.setTimeStampCreated(System.currentTimeMillis());
    }

    public void inputIncidentDto(double longitudeEdt, double latitudeEdt, String address) {
        incident.setClassType("com.dallmeier.asa.aims.domain.Incident");
        aggregatedEventDTO.setAggregatedEvent(aggregatedEvent);
        incidentDto.setAggregatedEventDTO(aggregatedEventDTO);
        incident.setTimeStampFirstObserved(0);
        incidentDto.setIncident(incident);
        Location location = new Location();
        List<Double> coordinates = new ArrayList<>(2);
        coordinates.add(0, longitudeEdt);
        coordinates.add(1, latitudeEdt);

        location.setCoordinates(coordinates);
        location.setType("Point");
        incident.setLocation(location);
        incident.setAddress(address);
        createNewIncident(incidentDto);
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }
}
