package com.example.tp_mobile.model;

import com.squareup.moshi.Json;

import java.util.Date;
import java.util.List;

public class TimeSlot {
    @Json(name="id")
    private Integer id;

    @Json(name="begin")
    private Date begin;

    @Json(name="end")
    private Date end;

    @Json(name="max_wattage")
    private Integer maxWattage;

    @Json(name="appliances")
    private List<ApplianceTimeSlot> applianceTimeSlots;


    public Integer getMaxWattage() {
        return maxWattage;
    }

    public Date getEnd() {
        return end;
    }

    public Date getBegin() {
        return begin;
    }

    public Integer getId() {
        return id;
    }

    public List<ApplianceTimeSlot> getApplianceTimeSlots() {
        return applianceTimeSlots;
    }
}
