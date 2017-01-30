package com.example.mb7.sportappbp.Objects;

import java.util.ArrayList;

/**
 * Created by Basti on 16.01.2017.
 */

public class DiaryEntry {

    private String date;
    private Boolean successful;
    private ArrayList<Exercise> exerciseList;

    /**
     * Constructor to create a diary entry
     * @param date : date of the training
     */
    public DiaryEntry(String date){
        this.date = date;
        exerciseList = new ArrayList<Exercise>();
    }

    /**
     * This method returns the date of the training
     * @return returns the date of the training as string
     */
    public String getDate(){
        return this.date;
    }

    /**
     * This method returns true if the training was successful, if not it returns false
     * @return returns if the training was successful as boolean
     */
    public Boolean getSuccessful(){
        return this.successful;
    }

    public double getTotalDuration(){
        double result = 0;
        for(Exercise i : exerciseList){
            result =+ result;
        }
        return result;
    }

    public ArrayList<Exercise> getExerciseList(){
        return this.exerciseList;
    }

    public void setDate(String date){
        this.date = date;
    }
    public void setSuccessful(boolean successful){
        this.successful = successful;
    }
    public void setExerciseList(ArrayList<Exercise> newList){
        this.exerciseList = newList;
    }



    public void addActivity(Exercise activity){
        exerciseList.add(activity);
    }

    public void addNewExercises(ArrayList<Exercise> exLst){
        for(Exercise i : exLst){
            exerciseList.add(i);
        }
    }
}
