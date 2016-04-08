package com.example.morten.fiskebanken.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Morten on 06.04.2016.
 */
public class Location implements Parcelable {

    Location Loc;

    public Location(Location loc){
        Loc = loc;
    }

    public Location(Parcel in){
        String[] data = new String[1];
        in.readStringArray(data);
      //  this.Loc = data[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
