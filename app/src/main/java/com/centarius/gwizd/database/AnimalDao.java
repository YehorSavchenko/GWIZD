package com.centarius.gwizd.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Consumer;

public class AnimalDao {
    private final DatabaseReference mDatabase;
    private final String animalsList = "animals";

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
        mDatabase.child(animalsList)
                .child(key)
                .setValue(animalSpotted);
    }

    public void attachListener(Consumer<AnimalSpotted> processAnimal) {
        ValueEventListener animalListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                AnimalSpotted animalSpotted = dataSnapshot.getValue(AnimalSpotted.class);
                processAnimal.accept(animalSpotted);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };
        mDatabase.child(animalsList).addValueEventListener(animalListener);
    }

    public void tempSetAnimal() {
        this.saveAnimalInDb(new AnimalSpotted("Ja po siedzeniu do późna",
                AnimalSpotted.AnimalStatus.ANIMAL_DEAD,
                "a",
                true,
                "asddsa",
                new Location("Na HackYeah"),
                "dsasa",
                Timestamp.from(Instant.now()).toString()));
    }
}
