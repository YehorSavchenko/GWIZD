package com.centarius.gwizd.model;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Location {
    private String whatever;

    public String getWhatever() {
        return whatever;
    }

    public void setWhatever(String whatever) {
        this.whatever = whatever;
    }

    public Location(String whatever) {
        this.whatever = whatever;
    }

    public Location() {
    }

    @NonNull
    @Override
    public String toString() {
        return whatever;
    }
}
