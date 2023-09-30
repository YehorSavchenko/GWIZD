package com.centarius.gwizd.database;

import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimalDao {
    private DatabaseReference mDatabase;

    public AnimalDao() {
        mDatabase = FirebaseDatabase.getInstance()
                .getReference();
    }

    public void saveAnimalInDb(AnimalSpotted animalSpotted) {
        String key = mDatabase.child("users")
                                .push()
                                .getKey();
        mDatabase.child("users")
                .child(key)
                .setValue(animalSpotted);
    }

    public void tempSetAnimal() {
        this.saveAnimalInDb(new AnimalSpotted("a", true, "asddsa", new Location("sdas"), "dsasa"));
    }
}
