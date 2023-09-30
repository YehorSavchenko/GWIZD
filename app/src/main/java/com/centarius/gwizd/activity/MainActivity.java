package com.centarius.gwizd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.centarius.gwizd.R;
import com.centarius.gwizd.database.AnimalDao;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    Button openCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        new AnimalDao().saveAnimalInDb(new AnimalSpotted("a", true, "asddsa", new Location("sdas"), "dsasa"));

        openCameraButton = findViewById(R.id.openCameraBtn);

        openCameraButton.setOnClickListener(view -> {

            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });
    }
}