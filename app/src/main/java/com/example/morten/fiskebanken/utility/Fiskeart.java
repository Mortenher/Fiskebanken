package com.example.morten.fiskebanken.utility;

/**
 * Created by Morten on 27.04.2016.
 */
public class Fiskeart {
    public String name;
    public double weight;
    public double heaviest;
    public double length;
    public double longest;
    public int antall;


    public double getMeanWeight(){
        return weight/antall;
    }

    public double getMeanLenght(){
        return length/antall;
    }
}
