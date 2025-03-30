package com.example.tp_mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_mobile.R;
import com.example.tp_mobile.model.Equipement;
import com.example.tp_mobile.model.Habitat;

import java.util.List;

public class ResidentAdapter extends ArrayAdapter<Habitat> {
    Activity activity;
    int itemResId;
    List<Habitat> items;

    public ResidentAdapter(Activity activity, int itemResId, List<Habitat> items) {
        super(activity, itemResId, items);
        this.activity = activity;
        this.itemResId = itemResId;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View converView, @NonNull ViewGroup parent) {
        View layout = converView;
        if (converView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResId, parent, false);
        }
        Habitat item = items.get(position);

        TextView name = layout.findViewById(R.id.resident_name);
        name.setText(item.getNom());

        TextView floorVal = layout.findViewById(R.id.floor);
        floorVal.setText(String.format("%s", item.getEtage()));

        TextView equipments_nb = layout.findViewById(R.id.equipments_nb);
        equipments_nb.setText(String.format("%s equipements", item.getEquipements().size()));

        TextView conso = layout.findViewById(R.id.conso);
        int totalWattage = item.getEquipements().stream()
                .mapToInt(Equipement::getWattage) // extrait l'attribut wattage
                .sum();
        conso.setText(String.format(activity.getString(R.string.total_wattage), totalWattage));

        ApplianceAdapter applianceAdapter = new ApplianceAdapter(item.getEquipements());

        RecyclerView equipments = layout.findViewById(R.id.equipments_images);
        equipments.setAdapter(applianceAdapter);

        return layout;
    }

}