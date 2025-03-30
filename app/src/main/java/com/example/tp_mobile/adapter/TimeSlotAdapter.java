package com.example.tp_mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp_mobile.R;
import com.example.tp_mobile.interfaces.ActionOnTimeSlotClick;
import com.example.tp_mobile.model.ApplianceTimeSlot;
import com.example.tp_mobile.model.TimeSlot;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private final List<TimeSlot> items;
    private final LayoutInflater inflater;
    private final ActionOnTimeSlotClick onClickListener;

    public TimeSlotAdapter(Context context, List<TimeSlot> items, ActionOnTimeSlotClick onClickListener) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot item = items.get(position);
        double wattage = 0;
        for (ApplianceTimeSlot applianceTimeSlot: item.getApplianceTimeSlots())
            wattage += applianceTimeSlot.getEquipement().getWattage();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = String.format("%s - %s", dateFormat.format(item.getBegin()),
                dateFormat.format(item.getEnd()));
        String wattageText = String.format("%d/%dW", (int)wattage, item.getMaxWattage());
        holder.timeSlotText.setText(timeText);
        holder.wattageText.setText(wattageText);
        double part = wattage / item.getMaxWattage();
        if (part <= 0.3)
            holder.container.setCardBackgroundColor(Color.GREEN);
        else if (part <= 0.7)
            holder.container.setCardBackgroundColor(Color.rgb(255, 127, 0));
        else
            holder.container.setCardBackgroundColor(Color.RED);
        holder.container.setOnClickListener((v) -> onClickListener.action(v, item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView timeSlotText;
        TextView wattageText;
        MaterialCardView container;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlotText = itemView.findViewById(R.id.time_slot_text);
            wattageText = itemView.findViewById(R.id.wattage_text);
            container = itemView.findViewById(R.id.item_slot_container);
        }
    }
}