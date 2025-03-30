package com.example.tp_mobile.model;

import com.squareup.moshi.Json;

public class Equipement {
    @Json(name = "id")
    private int id;

    @Json(name = "name")
    private String name;

    @Json(name = "reference")
    private String reference;

    @Json(name = "wattage")
    private int wattage;

    // Constructeur
    public Equipement(int id, String name, String reference, int wattage) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.wattage = wattage;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getReference() { return reference; }
    public int getWattage() { return wattage; }
}
