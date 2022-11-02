package com.dallmeier.evidencer.listener.EventBus;

import android.location.Location;

public class UpdateLocation {
    private Location location;

    public UpdateLocation(Location location) {
        this.location = location;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
