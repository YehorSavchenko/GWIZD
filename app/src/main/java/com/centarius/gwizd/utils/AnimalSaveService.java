package com.centarius.gwizd.utils;

import android.net.Uri;

import com.centarius.gwizd.database.AnimalDao;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.UploadException;
import com.centarius.gwizd.storage.AnimalImageStorageClient;

import java.util.function.Consumer;

public class AnimalSaveService {
    private final AnimalImageStorageClient animalImageStorageClient;
    private final AnimalDao animalDao;
    private static final AnimalSaveService INSTANCE = new AnimalSaveService();

    public AnimalSaveService() {
        this.animalDao = new AnimalDao();
        this.animalImageStorageClient = new AnimalImageStorageClient();
    }

    public void saveAnimal(AnimalSpotted animalSpotted, Uri animalImage) throws UploadException {
        try {
            animalDao.saveAnimalInDb(animalSpotted);
            animalImageStorageClient.uploadImage(animalImage, animalSpotted.getImageName());
        } catch (Exception e) {
            throw new UploadException("Exception encountered when saving data on remote", e);
        }
    }

    public void setAnimalListener(Consumer<AnimalSpotted> animalProcessor,
                                  Consumer<AnimalSpotted> processAnimalDeleted) {
        this.animalDao.attachListener(animalProcessor, processAnimalDeleted);
    }

    public static AnimalSaveService getInstance() {
        return INSTANCE;
    }
}
