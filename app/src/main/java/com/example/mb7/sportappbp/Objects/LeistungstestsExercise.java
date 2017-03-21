package com.example.mb7.sportappbp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Basti on 29.01.2017.
 */

public class LeistungstestsExercise extends Exercise {

    public LeistungstestsExercise(){
        this.weighting = 3.0;
        this.category = "Leistungstests";
    }



    public static final Parcelable.Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            Exercise leistungstests = new LeistungstestsExercise();
            leistungstests.name = parcel.readString();
            leistungstests.weighting = parcel.readDouble();
            leistungstests.timeMinutes = parcel.readInt();
            leistungstests.timeHours = parcel.readInt();
            leistungstests.category = parcel.readString();
            return leistungstests;
        }

        @Override
        public Exercise[] newArray(int i) {
            return new Exercise[i];
        }
    };


}
