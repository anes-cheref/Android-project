package com.example.tp_mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tp_mobile.R;
import com.example.tp_mobile.model.Equipement;
import com.example.tp_mobile.model.Resident;
import com.example.tp_mobile.ui.login.RegisterActivity;

import java.util.List;

public class ResidentAdapter extends ArrayAdapter<Resident> {
    Activity activity;
    int itemResId;
    List<Resident> items;

    public ResidentAdapter(Activity activity, int itemResId, List<Resident> items) {
        super(activity, itemResId, items);
        this.activity = activity;
        this.itemResId = itemResId;
        this.items = items;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View layout = converView;
        if (converView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResId, parent, false);
        }
        Resident item = items.get(position);

        TextView name = layout.findViewById(R.id.resident_name);
        name.setText(item.getResidentName());

        TextView floorVal = layout.findViewById(R.id.floor);
        floorVal.setText(String.format("%s", item.getFloor()));

        TextView equipments_nb = layout.findViewById(R.id.equipments_nb);
        equipments_nb.setText(String.format("%s equipements", item.getEquipements().size()));

        EquipementAdapter adapter = new EquipementAdapter(activity,
                R.layout.item_equipment, item.getEquipements());

        ListView equipments = layout.findViewById(R.id.equipments_images);
        equipments.setAdapter(adapter);

        return layout;
    }

}