package com.example.tp_mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tp_mobile.R;
import com.example.tp_mobile.data.Appliances;
import com.example.tp_mobile.interfaces.ActionOnEquipmentClick;
import com.example.tp_mobile.model.Equipement;

import java.util.List;

public class DetailedEquipmentsAdapter extends ArrayAdapter<Equipement> {
    Activity activity;
    int itemResId;
    List<Equipement> items;
    private final ActionOnEquipmentClick onEquipmentClick;
    public DetailedEquipmentsAdapter(Activity activity, int itemResId, List<Equipement> items,
                                     ActionOnEquipmentClick onEquipmentClick) {
        super(activity, itemResId, items);
        this.activity = activity;
        this.itemResId = itemResId;
        this.items = items;
        this.onEquipmentClick = onEquipmentClick;
    }

    @NonNull
    @Override
    public View getView(int position, View converView, @NonNull ViewGroup parent) {
        View layout = converView;
        if (converView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResId, parent, false);
        }
        Equipement item = items.get(position);
        TextView name = layout.findViewById(R.id.equipment_name);
        TextView ref = layout.findViewById(R.id.reference);
        TextView conso = layout.findViewById(R.id.equipment_conso);
        ImageView img = layout.findViewById(R.id.equipment_image);

        name.setText(item.getName());
        ref.setText(item.getReference());
        conso.setText(String.format(activity.getString(R.string.conso_info), item.getWattage()));
        Integer imgRes = Appliances.getItemId(item.getName());
        if (imgRes != -1) img.setImageResource(imgRes);
        layout.setOnClickListener((v) -> onEquipmentClick.action(v, item));

        return layout;
    }
}
