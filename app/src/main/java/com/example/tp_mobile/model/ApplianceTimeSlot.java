package com.example.tp_mobile.model;

import com.squareup.moshi.Json;

import java.util.Date;

public class ApplianceTimeSlot {
    @Json(name="appliance")
    private Equipement equipement;

    @Json(name="order")
    private Integer order;

    @Json(name="booked_at")
    private Date bookedAt;

    public Date getBookedAt() {
        return bookedAt;
    }

    public Integer getOrder() {
        return order;
    }

    public Equipement getEquipement() {
        return equipement;
    }
}
