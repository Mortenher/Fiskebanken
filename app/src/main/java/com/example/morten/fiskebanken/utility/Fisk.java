package com.example.morten.fiskebanken.utility;

/**
 * Created by Morten on 23.02.2016.
 */
public class Fisk {

    int id;
    String type;
    double vekt;
    double lengde;
    String bilde;
    double lat;
    double lng;

    public int getId() {return id; }

    public String getType() {
        return type;
    }

    public double getVekt() {
        return vekt;
    }

    public double getLengde() {
        return lengde;
    }

    public String getBilde(){return bilde;}

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {this.id = id;}

    public void setVekt(double vekt) {
        this.vekt = vekt;
    }

    public void setLengde(double lengde) {
        this.lengde = lengde;
    }

    public void setBilde(String bilde){this.bilde = bilde;}

    public double getLat(){return lat;}
    public double getLng(){return lng;}
    public void setLat(double lat){this.lat = lat;}
    public void setLng(double lng){this.lng = lng;}

}