package com.example.mb7.sportappbp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Basti on 28.01.2017.
 */

public abstract class Exercise implements Parcelable {

    String exercise;
    double weighting;
    int timeMinutes;
    int timeHours;


    public double getWeighting() {
        return this.weighting;
    }

    public String getExercise() {
        return this.exercise;
    }

    public int getTimeMunites() {
        return this.timeMinutes;
    }

    public int getTimeHours() {
        return this.timeHours;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void setTimeMinutes(int timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    public void setTimeHours(int timeHours) {
        this.timeHours = timeHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(exercise);
        parcel.writeDouble(weighting);
        parcel.writeInt(timeMinutes);
        parcel.writeInt(timeHours);
    }

}
