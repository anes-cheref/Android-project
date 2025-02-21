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

import java.util.List;

public class CountryAdapter extends ArrayAdapter<Country> {
    Activity activity;
    int itemResId;
    List<Country> items;

    public CountryAdapter(Activity activity, int itemResId, List<Country> items) {
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

        ImageView flag = layout.findViewById(R.id.flag);
        TextView name = layout.findViewById(R.id.country_name);
        Country item = items.get(position);
        name.setText(item.getName());
        flag.setImageResource(item.getFlagResId());
        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResId, parent, false);
        }

        ImageView flag = layout.findViewById(R.id.flag);
        TextView name = layout.findViewById(R.id.country_name);
        Country item = items.get(position);
        name.setText(String.format("%s (+%s)", item.getName(), item.getCode()));
        flag.setImageResource(item.getFlagResId());

        return layout;
    }

}
