package com.example.mypharmacy.models;

import com.google.firebase.database.Exclude;

public class Pharmacies_pending implements java.io.Serializable{

    @Exclude
    private String key;
    private String nom;
    private String NumPortable;
    private String numFix;
    private String adresseEmail;
    private String adressePharmacie;
    private String debutGarde;
    private String finGarde;
    private String uid;
    private String etat;

    public Pharmacies_pending() {
    }

    public Pharmacies_pending(String nom, String numPortable, String numFix, String adresseEmail, String adressePharmacie, String debutGarde, String finGarde ,String uid,String etat) {
        this.nom = nom;
        NumPortable = numPortable;
        this.numFix = numFix;
        this.adresseEmail = adresseEmail;
        this.adressePharmacie = adressePharmacie;
        this.debutGarde = debutGarde;
        this.finGarde = finGarde;
        this.uid = uid;
        this.etat=etat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumPortable() {
        return NumPortable;
    }

    public void setNumPortable(String numPortable) {
        NumPortable = numPortable;
    }

    public String getNumFix() {
        return numFix;
    }

    public void setNumFix(String numFix) {
        this.numFix = numFix;
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public String getAdressePharmacie() {
        return adressePharmacie;
    }

    public void setAdressePharmacie(String adressePharmacie) {
        this.adressePharmacie = adressePharmacie;
    }

    public String getDebutGarde() {
        return debutGarde;
    }

    public void setDebutGarde(String debutGarde) {
        this.debutGarde = debutGarde;
    }

    public String getFinGarde() {
        return finGarde;
    }

    public void setFinGarde(String finGarde) {
        this.finGarde = finGarde;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEtat() {
        return etat;
    }
    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
