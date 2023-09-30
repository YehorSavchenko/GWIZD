package com.centarius.gwizd.database;

import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.google.firebase.FirebaseApp;

import junit.framework.TestCase;

public class AnimalDaoTest extends TestCase {

    public void testSaveAnimalInDb() {
        new AnimalDao().saveAnimalInDb(new AnimalSpotted("a", true, "asddsa", new Location("sdas"), "dsasa"));
    }
}