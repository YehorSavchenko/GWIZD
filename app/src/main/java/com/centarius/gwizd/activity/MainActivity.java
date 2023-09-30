package com.centarius.gwizd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.centarius.gwizd.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.centarius.gwizd.database.AnimalDao;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    Button openCameraButton;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        new AnimalDao().saveAnimalInDb(new AnimalSpotted("a", true, "asddsa", new Location("sdas"), "dsasa"));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        openCameraButton = findViewById(R.id.openCameraBtn);

        openCameraButton.setOnClickListener(view -> {

            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });

        setBottomNavigationView();

    }

    protected void setBottomNavigationView(){
        // Setting action_camera as the default selected item
        bottomNavigationView.setSelectedItemId(R.id.action_camera);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_list) {
                    // Handle Home click
                } else if (id == R.id.action_camera) {
                    // Handle Search click
                } else if (id == R.id.action_profile) {
                    // Handle Profile click
                }
                return true;
            }
        });
    }
}