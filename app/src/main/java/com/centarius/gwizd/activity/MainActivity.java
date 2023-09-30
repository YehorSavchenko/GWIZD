package com.centarius.gwizd.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.centarius.gwizd.R;
import com.centarius.gwizd.fragments.CameraFragment;
import com.centarius.gwizd.fragments.ListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    Button openCameraButton;
    BottomNavigationView bottomNavigationView;
    Stack<Integer> fragmentStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        openCameraButton = findViewById(R.id.openCameraBtn);

        openCameraButton.setOnClickListener(view -> {
            // Open CameraFragment
            Fragment cameraFragment = new CameraFragment();  // Create an instance of your CameraFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, cameraFragment, "CameraFragment") // Replace any existing fragment with the new one
                    .addToBackStack(null) // Add this transaction to the back stack
                    .commit();
        });

        setBottomNavigationView();
    }

    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    protected void setBottomNavigationView() {
        // Setting action_camera as the default selected item
        bottomNavigationView.setSelectedItemId(R.id.action_camera);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            fragmentStack.push(id);
            Fragment selectedFragment = null;
            String tag = null;

            if (id == R.id.action_list) {
                selectedFragment = new ListFragment();
                tag = "ListFragment";
            } else if (id == R.id.action_camera) {
                // Pop the entire fragment stack to return to initial state
                getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                return true;
            } else if (id == R.id.action_profile) {
                // Initialize your profile fragment here
                // selectedFragment = new ProfileFragment();
                // tag = "ProfileFragment";
            }

            if (selectedFragment != null) {
                Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (existingFragment == null) { // Check if the fragment is already in the back stack
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment, tag)
                            .addToBackStack(tag)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, existingFragment, tag)
                            .commit();
                }
            }

            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // If the stack is empty, behave as usual
            super.onBackPressed();
        } else {
            // Pop the top fragment off the stack
            getSupportFragmentManager().popBackStack();

            // Remove current fragment from the stack
            if (!fragmentStack.isEmpty()) {
                fragmentStack.pop();
            }

            // Update the BottomNavigationView to the last item in the stack
            if (!fragmentStack.isEmpty()) {
                bottomNavigationView.setSelectedItemId(fragmentStack.peek());
            }
        }
    }

}