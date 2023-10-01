package com.centarius.gwizd.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centarius.gwizd.R;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.utils.AnimalSaveService;
import com.centarius.gwizd.view.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private final AnimalSaveService animalSaveService = AnimalSaveService.getInstance();

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View addButton = view.findViewById(R.id.addButton);

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        CameraFragment cameraFragment = CameraFragment.newInstance(uri);  // Create an instance of your CameraFragment
                        this.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, cameraFragment, "CameraFragment") // Replace any existing fragment with the new one
                                .addToBackStack(null) // Add this transaction to the back stack
                                .commit();
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        // Create a list to hold AnimalSpotted objects
        List<AnimalSpotted> animalSpottedList = new ArrayList<>();

        // Create the adapter
        ListAdapter adapter = new ListAdapter(getContext(), animalSpottedList);
        animalSaveService.setAnimalListener(it -> {
            if (!animalSpottedList.contains(it)) {
                animalSpottedList.add(it);
                adapter.notifyDataSetChanged();
                ((TextView)view.findViewById(R.id.recordCountTextView)).setText(
                        ("Records: " + animalSpottedList.size())
                );
            }},
                it -> {
            animalSpottedList.remove(it);
            adapter.notifyDataSetChanged();
                    ((TextView)view.findViewById(R.id.recordCountTextView)).setText(
                            ("Records: " + animalSpottedList.size())
                    );
        });
        addButton.setOnClickListener(it -> executeForAddButton(pickMedia));

        // Set the adapter
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void executeForAddButton(ActivityResultLauncher<PickVisualMediaRequest> pickMedia) {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}