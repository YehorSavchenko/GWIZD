package com.centarius.gwizd.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class AnimalImageStorageClient {
    private final StorageReference storage;
    private final String imagesPath = "images/";

    public AnimalImageStorageClient() {
        this.storage = FirebaseStorage.getInstance().getReference();
    }

    public void uploadImage(File imageFile) {
        StorageReference imageRef = storage.child(imagesPath +
                imageFile.getName());
        imageRef.putFile(Uri.fromFile(imageFile));
    }
}
