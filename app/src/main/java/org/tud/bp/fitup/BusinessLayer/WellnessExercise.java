package com.tud.bp.fitup.BusinessLayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Basti on 29.01.2017.
 */

public class WellnessExercise extends Exercise {

    public WellnessExercise() {
        this.weighting = 1.0;
        this.category = "Wellness";
    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            Exercise wellness = new WellnessExercise() {
            };
            wellness.name = parcel.readString();
            wellness.weighting = parcel.readDouble();
            wellness.timeMinutes = parcel.readInt();
            wellness.timeHours = parcel.readInt();
            wellness.category = parcel.readString();
            return wellness;
        }

        @Override
        public Exercise[] newArray(int i) {
            return new Exercise[i];
        }
    };
}
