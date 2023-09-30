package com.centarius.gwizd.fragments;

import android.annotation.SuppressLint;
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

        // Create a list to hold AnimalSpotted objects
        List<AnimalSpotted> animalSpottedList = new ArrayList<>();

        // Create the adapter
        ListAdapter adapter = new ListAdapter(getContext(), animalSpottedList);
        animalSaveService.setAnimalListener(it -> {
            if (!animalSpottedList.contains(it)) {
                animalSpottedList.add(it);
                adapter.notifyDataSetChanged();
            }}, animalSpottedList::remove);

        // Set the adapter
        recyclerView.setAdapter(adapter);

        return view;
    }
}