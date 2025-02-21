package com.example.tp_mobile.model;

import java.util.List;

public class Resident {
    private Integer id;
    private String residentName;
    private Integer floor;
    private Double area;
    private List<Equipement> equipements;

    public Resident(Integer id, String residentName, Integer floor, Double area,
                    List<Equipement> equipements) {
        this.id = id;
        this.residentName = residentName;
        this.floor = floor;
        this.area = area;
        this.equipements = equipements;
    }

    public Resident(String residentName, int floor, List<Equipement> equipements) {
        this(null, residentName, floor, null, equipements);
    }

    public String getResidentName() {
        return residentName;
    }

    public int getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public double getArea() {
        return area;
    }

    public List<Equipement> getEquipements() {
        return equipements;
    }
}
