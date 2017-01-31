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

    public int getTotalTimeMinutes() {
        int result = 0;
        for (Exercise i : exerciseList) {

            result = result + i.getTimeMunites();

            if (result > 59) {
                result = result - 60;
                //i.setTimeHours(i.getTimeHours() + 1 );
            }
        }
        return result;
    }

    public int getTotalTimeHours(){
        int result = 0;
        for(Exercise i : exerciseList) {
            result = result + i.getTimeHours();
        }
        return result;
    }

    public int getTotalTimeMinutesLeistungstests(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof LeistungstestsExercise)
                result = result + i.getTimeMunites();
        }
        return result;
    }

    public int getTotalTimeHoursLeistungstests(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof LeistungstestsExercise)
                result = result + i.getTimeHours();
        }
        return result;
    }

    public int getTotalTimeMinutesTraining(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof TrainingExercise)
                result = result + i.getTimeMunites();
        }
        return result;
    }

    public int getTotalTimeHoursTraining(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof TrainingExercise)
                result = result + i.getTimeHours();
        }
        return result;
    }

    public int getTotalTimeMinutesWellness(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof WellnessExercise)
                result = result + i.getTimeMunites();
        }
        return result;
    }

    public int getTotalTimeHoursWellness(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof WellnessExercise)
                result = result + i.getTimeHours();
        }
        return result;
    }
    public int getTotalTimeMinutesReinerAufenthalt(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof ReinerAufenthaltExercise)
                result = result + i.getTimeMunites();
        }
        return result;
    }

    public int getTotalTimeHoursReinerAufenthalt(){
        int result = 0;
        for(Exercise i : exerciseList) {

            if(i instanceof ReinerAufenthaltExercise)
                result = result + i.getTimeHours();
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
