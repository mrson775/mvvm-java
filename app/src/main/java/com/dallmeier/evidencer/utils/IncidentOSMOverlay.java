package com.dallmeier.evidencer.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.dallmeier.evidencer.model.incident_response.IncidentEntity;

import org.osmdroid.views.MapView;

import java.util.HashSet;
import java.util.Set;

public class IncidentOSMOverlay extends EvidentOSMOverlay {
    private Set<IncidentEntity> mTappedIncidents;

    public IncidentOSMOverlay(Drawable defaultMarker, Context context,
                              MapView mapView) {
        super(defaultMarker);
        mTappedIncidents = new HashSet<>();
    }

    public void addToTappedSet(IncidentEntity incident) {
        mTappedIncidents.add(incident);
    }

    public boolean inTappedIncidentSet(IncidentEntity incident) {
        return mTappedIncidents.contains(incident);
    }

}
