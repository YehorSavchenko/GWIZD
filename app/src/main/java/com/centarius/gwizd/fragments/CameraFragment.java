package com.centarius.gwizd.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.centarius.gwizd.R;
import com.centarius.gwizd.activity.MainActivity;
import com.centarius.gwizd.ml.Model;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.centarius.gwizd.model.UploadException;
import com.centarius.gwizd.utils.AnimalSaveService;
import com.centarius.gwizd.utils.ImageUtils;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

public class CameraFragment extends Fragment {

    private final AnimalSaveService animalSaveService = AnimalSaveService.getInstance();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 888;
    private static final int IMAGE_SIZE = 224;
    private Uri imageUri;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView locationView;
    private Context context;

    public static CameraFragment newInstance(Uri imageUri) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putParcelable("imageUri", imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUri = getArguments().getParcelable("imageUri");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        // Find the close button and attach a click listener
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            // Close this fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide the bottom navigation
        ((MainActivity) requireActivity()).hideBottomNavigation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).enableCameraButton();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Show the bottom navigation
        ((MainActivity) requireActivity()).showBottomNavigation();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Set the image URI to ImageView to show the picture
        ImageView photoView = requireView().findViewById(R.id.photoView);
        photoView.setImageURI(imageUri);

        // Get the location and recognize the animal
        Location location = getLocation(new Location(""));
        String animalType = recognizeAnimal(imageUri);

        // Get animal status
        RadioGroup animalStatus = requireView().findViewById(R.id.animalStatusRg);

        // save animal to gcp
        Button submitBtn = requireView().findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(view1 -> {

            AnimalSpotted.AnimalStatus status = AnimalSpotted.AnimalStatus.NO_STATUS;
            int id = animalStatus.getCheckedRadioButtonId();
            if (id == R.id.domesticRb) {
                status = AnimalSpotted.AnimalStatus.ANIMAL_OWNED;
            } else if (id == R.id.wildRb) {
                status = AnimalSpotted.AnimalStatus.ANIMAL_WILD;
            } else if (id == R.id.deadRb) {
                status = AnimalSpotted.AnimalStatus.ANIMAL_DEAD;
            }
            AnimalSpotted.AnimalStatus finalStatus = status;

            AnimalSpotted animalSpotted = new AnimalSpotted(
                    animalType, finalStatus, false,
                    "userId" + Timestamp.from(Instant.now()).toString(),
                    location, "userId", Timestamp.from(Instant.now()).toString());
            try {
                animalSaveService.saveAnimal(animalSpotted, imageUri);
                requireActivity().getSupportFragmentManager().popBackStack();
            } catch (UploadException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private Location getLocation(Location input) {
        locationView = requireView().findViewById(R.id.locationView);
        fusedLocationProviderClient = new FusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            locationView.setText(addresses.get(0).getAddressLine(0));
                            input.setLocationString(addresses.get(0).getAddressLine(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        return input;
    }

    @SuppressLint("DefaultLocale")
    private String recognizeAnimal(Uri imageUri) {
        try {
//            context = getApplicationContext();
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
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Cat", "Dog", "Fox", "Boar"};

            TextView animalType = requireView().findViewById(R.id.animalTypeView);
            animalType.setText(classes[maxPos]);

            model.close();
            return classes[maxPos];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
