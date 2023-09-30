package com.centarius.gwizd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centarius.gwizd.R;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.centarius.gwizd.utils.AnimalSaveService;
import com.centarius.gwizd.view.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimalSaveService animalSaveService = AnimalSaveService.getInstance();

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create a list to hold AnimalSpotted objects
        List<AnimalSpotted> animalSpottedList = new ArrayList<>();
        Location location = new Location("whatever");
        AnimalSpotted animal1 = new AnimalSpotted(
                "Dog",
                AnimalSpotted.AnimalStatus.ANIMAL_OWNED,
                "This is a pet",
                false,
                "http://example.com/image1.jpg",
                location,
                "user1",
                "2023-09-19T10:00:00"
        );

        AnimalSpotted animal2 = new AnimalSpotted(
                "Eagle",
                AnimalSpotted.AnimalStatus.ANIMAL_WILD,
                "It's a wild bird",
                false,
                "http://example.com/image2.jpg",
                location,
                "user2",
                "2023-09-18T15:30:00"
        );

        AnimalSpotted animal3 = new AnimalSpotted(
                "Cat",
                AnimalSpotted.AnimalStatus.ANIMAL_DEAD,
                "Found dead near road",
                true,
                "http://example.com/image3.jpg",
                location,
                "user3",
                "2023-09-17T12:45:00"
        );

        animalSpottedList.add(animal1);
        animalSpottedList.add(animal2);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);
        animalSpottedList.add(animal3);

        // Create the adapter
        ListAdapter adapter = new ListAdapter(getContext(), animalSpottedList);

        // Set the adapter
        recyclerView.setAdapter(adapter);

        return view;
    }
}