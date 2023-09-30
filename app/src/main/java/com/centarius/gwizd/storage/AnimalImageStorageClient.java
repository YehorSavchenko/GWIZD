package com.centarius.gwizd.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;

public class AnimalImageStorageClient {
    private final StorageReference storage;
    private final String imagesPath = "images/";

    public AnimalImageStorageClient() {
        this.storage = FirebaseStorage.getInstance().getReference();
    }

    public void uploadImage(Uri imageUri, String imageName) {
        StorageReference imageRef = storage.child(imagesPath +
                imageName);
        imageRef.putFile(imageUri);
    }
}
