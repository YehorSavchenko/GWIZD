package com.centarius.gwizd.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.centarius.gwizd.R;
import com.centarius.gwizd.view.ListAdapter;

import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> data = Arrays.asList("Item 1", "Item 2", "Item 3");
        ListAdapter adapter = new ListAdapter(this, data);
        recyclerView.setAdapter(adapter);

    }
}