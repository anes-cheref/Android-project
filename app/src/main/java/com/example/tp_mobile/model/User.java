package com.example.tp_mobile.model;

public class User {
    private String nom,prenom,email,mdp,numero;

    public User(String nom, String email, String prenom, String mdp, String numero) {
        this.nom = nom;
        this.email = email;
        this.prenom = prenom;
        this.mdp = mdp;
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public String getNumero() {
        return numero;
    }
}
