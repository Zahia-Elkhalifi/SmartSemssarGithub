package com.example.connectfirebase;

public class AnnonceModel {
    private String prix;
    private String address;
    private int annonceimg;

    public AnnonceModel(String prix, String address, int annonceimg) {
        this.prix = prix;
        this.address = address;
        this.annonceimg = annonceimg;
    }

    public String getPrix() {
        return prix;
    }

    public String getAddress() {
        return address;
    }

    public int getAnnonceimg() {
        return annonceimg;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAnnonceimg(int annonceimg) {
        this.annonceimg = annonceimg;
    }
}
