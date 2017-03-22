package com.example.mb7.sportappbp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Basti on 28.01.2017.
 */

public abstract class Exercise implements Parcelable {

    String name;
    double weighting;
    int timeMinutes;
    int timeHours;
    String category;


    public double getWeighting() {
        return this.weighting;
    }

    public String getName() {
        return this.name;
    }

    public int getTimeMunites() {
        return this.timeMinutes;
    }

    public int getTimeHours() {
        return this.timeHours;
    }

    public String getCategory(){
        return this.category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeMinutes(int timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    public void setTimeHours(int timeHours) {
        this.timeHours = timeHours;
    }

    public void setCategory(String category){this.category = category; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(weighting);
        parcel.writeInt(timeMinutes);
        parcel.writeInt(timeHours);
        parcel.writeString(category);
    }

}
