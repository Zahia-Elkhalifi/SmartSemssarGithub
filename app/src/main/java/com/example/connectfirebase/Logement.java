package com.example.connectfirebase;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Logement {
    private String entourage,rue,residenceinfo,userId,type,prix,titre,description;


    Uri imageUri;
    private @ServerTimestamp Date timeStamp;


    public Logement() {
    }

    public Logement(String type,String entourage, String rue,String residenceinfo,Date timeStamp,String userId,String prix,String titre,String description,Uri imageUri) {
        this.type=type;
        this.entourage = entourage;
        this.rue = rue;
        this.residenceinfo = residenceinfo;
        this.timeStamp=timeStamp;
        this.userId=userId;
        this.prix=prix;
        this.titre = titre;
        this.description = description;
        this.imageUri = imageUri;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntourage() {
        return entourage;
    }

    public void setEntourage(String entourage) {
        this.entourage = entourage;
    }
    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getResidenceinfo() {
        return residenceinfo;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setResidenceinfo(String residenceinfo) {
        this.residenceinfo = residenceinfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
