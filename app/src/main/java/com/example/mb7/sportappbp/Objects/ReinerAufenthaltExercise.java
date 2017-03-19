package com.example.mb7.sportappbp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Basti on 29.01.2017.
 */

public class ReinerAufenthaltExercise extends Exercise {

    public ReinerAufenthaltExercise(){
        this.weighting = 2.0;
    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            Exercise ReinerAufenthalt = new ReinerAufenthaltExercise() {
            };
            ReinerAufenthalt.exercise = parcel.readString();
            ReinerAufenthalt.weighting = parcel.readDouble();
            ReinerAufenthalt.timeMinutes = parcel.readInt();
            ReinerAufenthalt.timeHours = parcel.readInt();
            return ReinerAufenthalt;
        }

        @Override
        public Exercise[] newArray(int i) {
            return new Exercise[i];
        }
    };

}
