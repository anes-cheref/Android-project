package com.example.tp_mobile.model;

import com.squareup.moshi.Json;
import java.util.List;

public class Habitat {
    @Json(name = "nom")
    private String residentName;

    @Json(name = "etage")
    private int floor;

    @Json(name = "equipements")
    private List<Equipement> equipements;

    // Constructeur
    public Habitat(String nom, int etage, List<Equipement> equipements) {
        this.residentName = nom;
        this.floor = etage;
        this.equipements = equipements;
    }

    // Getters
    public String getNom() { return residentName; }
    public int getEtage() { return floor; }
    public List<Equipement> getEquipements() { return equipements; }
}
