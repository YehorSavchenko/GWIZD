package com.centarius.gwizd.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.centarius.gwizd.R;

import java.io.File;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 777;
    static final int REQUEST_IMAGE_CAPTURE_CODE = 1;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            getPicture();
        }
    }

    private void getPicture() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("pl", "PL"));
            String timeStamp = sdf.format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            // Create an image file name
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imageFileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

            //Create the Uri to save the image
            imageUri = getContentResolver().insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Add the Uri to the intent
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            // Continue only if the Uri was successfully created
            if (imageUri != null) {
                mGetContent.launch(photoIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPicture();
            } else {
                Toast.makeText(this, "Camera permission is necessary to take a photo.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            // The image is saved in the imageUri, do something with it, for example:
            ImageView photoView = findViewById(R.id.photoView);
            photoView.setImageURI(imageUri);
        }
    }

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The image is saved in the imageUri, do something with it, for example:
                    ImageView photoView = findViewById(R.id.photoView);
                    photoView.setImageURI(imageUri);
                }
            });
}
