package com.centarius.gwizd.model;

import androidx.annotation.NonNull;

public class Location {
    public Location(String whatever) {
        this.whatever = whatever;
    }

    private String whatever;

    public Location() {
    }

    public String getWhatever() {
        return whatever;
    }

    public void setWhatever(String whatever) {
        this.whatever = whatever;
    }

    @NonNull
    @Override
    public String toString() {
        return whatever;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return whatever.equals(location.whatever);
    }

    @Override
    public int hashCode() {
        return whatever.hashCode();
    }
}
