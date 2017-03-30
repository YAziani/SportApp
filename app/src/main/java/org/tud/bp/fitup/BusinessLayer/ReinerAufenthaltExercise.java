package com.tud.bp.fitup.BusinessLayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Basti on 29.01.2017.
 */

public class ReinerAufenthaltExercise extends Exercise {

    public ReinerAufenthaltExercise() {
        this.weighting = 2.0;
        this.category = "ReinerAufenthalt";
    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            Exercise reinerAufenthalt = new ReinerAufenthaltExercise() {
            };
            reinerAufenthalt.name = parcel.readString();
            reinerAufenthalt.weighting = parcel.readDouble();
            reinerAufenthalt.timeMinutes = parcel.readInt();
            reinerAufenthalt.timeHours = parcel.readInt();
            reinerAufenthalt.category = parcel.readString();

            return reinerAufenthalt;
        }

        @Override
        public Exercise[] newArray(int i) {
            return new Exercise[i];
        }
    };

}
