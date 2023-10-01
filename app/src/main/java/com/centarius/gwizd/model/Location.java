package com.centarius.gwizd.model;

import androidx.annotation.NonNull;

public class Location {
    public Location(String locationString) {
        this.locationString = locationString;
    }

    private String locationString;

    public Location() {
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    @NonNull
    @Override
    public String toString() {
        return locationString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return locationString.equals(location.locationString);
    }

    @Override
    public int hashCode() {
        return locationString.hashCode();
    }
}
