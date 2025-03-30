package com.example.tp_mobile.interfaces;

import android.view.View;

import com.example.tp_mobile.model.TimeSlot;

public interface ActionOnTimeSlotClick {
    // agir lorsqu'on clique sur un timeslot
    void action(View v, TimeSlot timeSlot);
}
