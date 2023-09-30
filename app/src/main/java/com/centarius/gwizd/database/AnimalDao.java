package com.centarius.gwizd.database;

import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.time.Instant;

public class AnimalDao {
    private final DatabaseReference mDatabase;

    public AnimalDao() {
        mDatabase = FirebaseDatabase.getInstance()
                .getReference();
    }

    public void saveAnimalInDb(AnimalSpotted animalSpotted) {
        String key = mDatabase.child("users")
                                .push()
                                .getKey();
        if (key == null) {
            throw new RuntimeException("Key is null");
        }
        mDatabase.child("users")
                .child(key)
                .setValue(animalSpotted);
    }

    public void tempSetAnimal() {
        this.saveAnimalInDb(new AnimalSpotted("Koteł",
                "a",
                true,
                "asddsa",
                new Location("sdas"),
                "dsasa",
                Timestamp.from(Instant.now()).toString()));
    }
}
