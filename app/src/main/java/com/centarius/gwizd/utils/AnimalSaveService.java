package com.centarius.gwizd.utils;

import android.net.Uri;

import com.centarius.gwizd.database.AnimalDao;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.storage.AnimalImageStorageClient;

import java.io.File;

public class AnimalSaveService {
    private final AnimalImageStorageClient animalImageStorageClient;
    private final AnimalDao animalDao;

    public AnimalSaveService() {
        this.animalDao = new AnimalDao();
        this.animalImageStorageClient = new AnimalImageStorageClient();
    }

    public void saveAnimal(AnimalSpotted animalSpotted, Uri animalImage) {
        animalDao.saveAnimalInDb(animalSpotted);
        animalImageStorageClient.uploadImage(animalImage, animalSpotted.getUserId());
    }
}
