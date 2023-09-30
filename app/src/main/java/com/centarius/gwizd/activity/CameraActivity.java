package com.centarius.gwizd.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.centarius.gwizd.R;
import com.centarius.gwizd.ml.Model;
import com.centarius.gwizd.utils.ImageUtils;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 777;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 888;
    private static final int IMAGE_SIZE = 224;
    static final int REQUEST_IMAGE_CAPTURE_CODE = 1;
    Uri imageUri;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView locationView;
    Context context;

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

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(CameraActivity.this, "Location permission is necessary", Toast.LENGTH_SHORT).show();
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
                    getLocation();
                    recognizeAnimal(imageUri);
                }
            });

    @SuppressLint("SetTextI18n")
    private void getLocation() {
        locationView = findViewById(R.id.locationView);
        fusedLocationProviderClient = new FusedLocationProviderClient(CameraActivity.this);

        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user provide the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(CameraActivity.this, location -> {
                // we got permission
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(CameraActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            locationView.setText("Latitude: " + addresses.get(0).getLatitude() +
                                    "Longitude: " + addresses.get(0).getLongitude() +
                                    "Address: " + addresses.get(0).getAddressLine(0) +
                                    "City: " + addresses.get(0).getLocality() +
                                    "Country: " + addresses.get(0).getCountryName());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("DefaultLocale")
    private void recognizeAnimal(Uri imageUri) {
        try {
            context = getApplicationContext();
            Model model = Model.newInstance(context);

            // Load and preprocess the image
            Bitmap bitmap = ImageUtils.loadAndPreprocessImage(context, imageUri, IMAGE_SIZE);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            // Run inference on the image
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * IMAGE_SIZE * IMAGE_SIZE * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            byteBuffer.rewind();


            // Fill the input buffer with image data
            int[] intValues = new int[IMAGE_SIZE * IMAGE_SIZE];

            if (bitmap != null) {
                bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            }

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            for (int pixelValue : intValues) {
                byteBuffer.putFloat((pixelValue & 0xFF) / 255.0f);
                byteBuffer.putFloat(((pixelValue >> 8) & 0xFF) / 255.0f);
                byteBuffer.putFloat(((pixelValue >> 16) & 0xFF) / 255.0f);
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Cat", "Dog", "Fox", "Boar"};

            TextView animalType = findViewById(R.id.animalTypeView);
            animalType.setText(classes[maxPos]);

//            Part to show percents of confidence
//
//            StringBuilder s = new StringBuilder();
//            for(int i = 0; i < classes.length; i++){
//                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100));
//            }
//            confidence.setText(s.toString());

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
