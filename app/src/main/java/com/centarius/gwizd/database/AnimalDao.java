package com.centarius.gwizd.database;

import com.centarius.gwizd.model.AnimalSpotted;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimalDao {
    private DatabaseReference mDatabase;

    public AnimalDao() {
        mDatabase = FirebaseDatabase.getInstance()
                .getReference();
    }

    public void saveAnimalInDb(AnimalSpotted animalSpotted) {
        mDatabase.child("users")
                .child("1")
                .setValue(animalSpotted);
    }
}
