package com.centarius.gwizd.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.centarius.gwizd.R;
import com.centarius.gwizd.fragments.CameraFragment;
import com.centarius.gwizd.fragments.ListFragment;
import com.centarius.gwizd.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    ImageButton openCameraButton;
    BottomNavigationView bottomNavigationView;
    Stack<Integer> fragmentStack = new Stack<>();
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    openCameraFragment();
                } else {
                    if (imageUri != null) {
                        getContentResolver().delete(imageUri, null, null);
                        imageUri = null;
                    }
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    callCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.top_color));
        fragmentStack.push(R.id.action_camera);  // Add the default fragment ID to the stack
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        openCameraButton = findViewById(R.id.openCameraBtn);
        openCameraButton.setOnClickListener(view -> callCamera());
        setBottomNavigationView();
    }

    private Uri createImageUri() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    private void callCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            startCamera();
        } else {
            // Request the camera permission
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            // Create a Uri where the camera app should save the photo
            imageUri = createImageUri();
            if (imageUri != null) {
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                mGetContent.launch(photoIntent);
            }
        }
    }

    public void disableCameraButton() {
        openCameraButton.setEnabled(false);
    }

    public void enableCameraButton() {
        openCameraButton.setEnabled(true);
    }

    private void openCameraFragment() {
        disableCameraButton();  // Disable camera button

        CameraFragment cameraFragment = CameraFragment.newInstance(imageUri);  // Create an instance of your CameraFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cameraFragment, "CameraFragment") // Replace any existing fragment with the new one
                .addToBackStack(null) // Add this transaction to the back stack
                .commit();
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
            if (fragmentStack.isEmpty() || id != fragmentStack.peek()) {
                fragmentStack.push(id);
            }
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
                selectedFragment = new ProfileFragment();
                tag = "ProfileFragment";
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