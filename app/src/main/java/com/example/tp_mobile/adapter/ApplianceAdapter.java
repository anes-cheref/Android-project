package com.example.tp_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_mobile.R;
import com.example.tp_mobile.data.Appliances;
import com.example.tp_mobile.model.Equipement;

import java.util.List;

public class ApplianceAdapter extends RecyclerView.Adapter<ApplianceAdapter.ApplianceViewHolder> {
    private final List<Equipement> appliances;


    public ApplianceAdapter(List<Equipement> items) {
        this.appliances = items;

    }

    @NonNull
    @Override
    public ApplianceAdapter.ApplianceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment, parent, false);
        return new ApplianceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplianceAdapter.ApplianceViewHolder holder, int position) {
        Equipement eq = appliances.get(position);
        Integer imgRes = Appliances.getItemId(eq.getName());
        if (imgRes != -1) holder.applianceImage.setImageResource(imgRes);
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

    public static class ApplianceViewHolder extends RecyclerView.ViewHolder {
        public ImageView applianceImage;

        public ApplianceViewHolder(@NonNull View itemView) {
            super(itemView);
            applianceImage = itemView.findViewById(R.id.equipment_image);
        }
    }
}
