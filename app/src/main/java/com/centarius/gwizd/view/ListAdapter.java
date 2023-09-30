package com.centarius.gwizd.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.centarius.gwizd.R;
import com.centarius.gwizd.model.AnimalSpotted;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private final List<AnimalSpotted> dataList;
    private final LayoutInflater layoutInflater;

    public ListAdapter(Context context, List<AnimalSpotted> dataList) {
        this.dataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_animal_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        AnimalSpotted animalSpotted = dataList.get(position);

        holder.tvAnimalTypeAndStatus.setText(animalSpotted.getAnimalType() + " (" + animalSpotted.getAnimalStatus().toString() + ")");
        holder.tvLocation.setText("Location: " + animalSpotted.getLocation());  // Assuming Location has a suitable toString method
        holder.tvTimestamp.setText("Timestamp: " + animalSpotted.getTimestamp());

        // Assuming you will set up image loading for the ImageView
        // For example, if using Glide: Glide.with(holder.ivArrow.getContext()).load(animalSpotted.getImageUrl()).into(holder.ivArrow);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAnimalTypeAndStatus;
        final TextView tvLocation;
        final TextView tvTimestamp;
        final ImageView ivArrow; // For the clickable arrow

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnimalTypeAndStatus = itemView.findViewById(R.id.tvAnimalTypeAndStatus);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivArrow = itemView.findViewById(R.id.ivArrow);
        }
    }
}
