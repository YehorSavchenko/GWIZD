package com.centarius.gwizd.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.centarius.gwizd.model.UploadException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public void saveAnimalInDb(AnimalSpotted animalSpotted) throws UploadException {
        String key = mDatabase.child("users")
                                .push()
                                .getKey();
        if (key == null) {
            throw new UploadException("Key is null");
        }
        mDatabase.child(animalsList)
                .child(key)
                .setValue(animalSpotted);
    }

    public void attachListener(Consumer<AnimalSpotted> processAnimalAdded,
                               Consumer<AnimalSpotted> processAnimalDeleted) {
        ChildEventListener animalListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Get Post object and use the values to update the UI
                AnimalSpotted animalSpotted = dataSnapshot.getValue(AnimalSpotted.class);
                if (animalSpotted != null) {
                    Log.w(TAG, animalSpotted.toString());
                    processAnimalAdded.accept(animalSpotted);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                AnimalSpotted animalSpotted = snapshot.getValue(AnimalSpotted.class);
                if (animalSpotted != null) {
                    Log.w(TAG, animalSpotted.toString());
                    processAnimalDeleted.accept(animalSpotted);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loading animal cancelled", databaseError.toException());
            }

        };
        mDatabase.child(animalsList).addChildEventListener(animalListener);
    }

    public void tempSetAnimal() throws UploadException {
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
