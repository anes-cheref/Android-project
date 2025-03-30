package com.example.tp_mobile.ui.reservations;

import androidx.lifecycle.ViewModel;

import com.example.tp_mobile.model.Equipement;
import com.example.tp_mobile.model.Habitat;
import com.example.tp_mobile.model.TimeSlot;

public class ReservationsViewModel extends ViewModel {
    private Habitat habitat = null;
    private TimeSlot timeSlot = null;
    private Equipement equipement = null;

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }
}