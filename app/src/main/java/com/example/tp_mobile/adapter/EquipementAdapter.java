package com.example.tp_mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp_mobile.R;
import com.example.tp_mobile.model.Country;
import com.example.tp_mobile.model.Equipement;

import java.util.List;

public class EquipementAdapter extends ArrayAdapter<Equipement> {
    Activity activity;
    int itemResId;
    List<Equipement> items;

    public EquipementAdapter(Activity activity, int itemResId, List<Equipement> items) {
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
        Equipement item = items.get(position);

        ImageView eqp = layout.findViewById(R.id.equipment_image);

        eqp.setImageResource(item.getImg());
        return layout;
    }

}