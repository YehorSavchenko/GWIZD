package com.centarius.gwizd.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centarius.gwizd.R;
import com.centarius.gwizd.model.AnimalSpotted;
import com.centarius.gwizd.model.Location;
import com.centarius.gwizd.view.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        ListAdapter adapter = new ListAdapter(this, animalSpottedList);
        recyclerView.setAdapter(adapter);

    }
}