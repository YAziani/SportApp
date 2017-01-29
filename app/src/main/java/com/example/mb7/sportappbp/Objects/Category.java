package com.example.mb7.sportappbp.Objects;

/**
 * Created by Basti on 28.01.2017.
 */

public abstract class Category {

    String activity;
    float weighting;
    float duration;


    public float getWeighting(){
        return weighting;
    }

    public String getActivity(){
        return activity;
    }

    public void setDuration(float duration){
        this.duration = duration;
    }

}
