package com.example.mb7.sportappbp.Objects;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Basti on 16.01.2017.
 */

public class DiaryEntry {

    private String id;
    private String date;
    private String time;
    private ArrayList<Exercise> exerciseList;

    /**
     * Constructor to create a diary entry
     * @param date : date of the training
     */
    //String id, String date, String time
    public DiaryEntry(){
        this.id = id;
        this.date = date;
        this.time = time;
        exerciseList = new ArrayList<Exercise>();
    }


    public String getID(){
        return this.id;
    }

    /**
     * This method returns the date of the training
     * @return returns the date of the training as string
     */
    public String getDate(){
        return this.date;
    }


    public String getTime(){
        return this.time;
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

    public String[] getTotalTimePointsAsArrayLeistungstests(){

        String[] result = new String[2];
        int resultMin = 0;
        int resultHours = 0;

        for(Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if(i instanceof LeistungstestsExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if(resultMin >= 60){
                    resultHours ++;
                    resultMin = resultMin % 60;
                }
            }

        }
        //Calculate the points with factor 3
        int points = (int) Math.round(((resultHours * 60) + resultMin) * 3);

        //transform the time and points to a string and get it as an array back
        return result = totalTimePointsToStringArray(resultHours, resultMin, points);
    }


    public String[] getTotalTimePointsAsArrayTraining(){

        String[] result = new String[2];
        int resultMin = 0;
        int resultHours = 0;

        for(Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if(i instanceof TrainingExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if(resultMin >= 60){
                    resultHours ++;
                    resultMin = resultMin % 60;
                }
            }

        }
        //Calculate the points with factor 2
        int points = (int) Math.round(((resultHours * 60) + resultMin) * 2);

        //transform the time and points to a string and get it as an array back
        return result = totalTimePointsToStringArray(resultHours, resultMin, points);
    }


    public String[] getTotalTimePointsAsArrayWellness(){

        String[] result = new String[2];
        int resultMin = 0;
        int resultHours = 0;

        for(Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if(i instanceof WellnessExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if(resultMin >= 60){
                    resultHours ++;
                    resultMin = resultMin % 60;
                }
            }

        }
        //Calculate the points with factor 1
        int points = (int) Math.round(((resultHours * 60) + resultMin) * 1);

        //transform the time and points to a string and get it as an array back
        return result = totalTimePointsToStringArray(resultHours, resultMin, points);
    }


    public String[] getTotalTimePointsAsArrayReinerAufenthalt(){

        String[] result = new String[2];
        int resultMin = 0;
        int resultHours = 0;

        for(Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if(i instanceof ReinerAufenthaltExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if(resultMin >= 60){
                    resultHours ++;
                    resultMin = resultMin % 60;
                }
            }

        }
        //Calculate the points with factor 0.5
        int points = (int) Math.round(((resultHours * 60) + resultMin) * 0.5);

        //transform the time and points to a string and get it as an array back
        return result = totalTimePointsToStringArray(resultHours, resultMin, points);
    }


    public ArrayList<Exercise> getExerciseList(){
        return this.exerciseList;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setTime(String time){
        this.time = time;
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

    private String[] totalTimePointsToStringArray(int hours, int min, int points){

        String[] result = new String[2];

        StringBuilder sbTime = new StringBuilder();
        StringBuilder sbPoints = new StringBuilder();
        DecimalFormat df = new DecimalFormat("00");
        sbTime.append(df.format(hours)).append(":").append(df.format(min)).append(" h");
        result[0] = sbTime.toString();

        sbPoints.append(points).append(" Pkt.");
        result[1] = sbPoints.toString();

        return result;
    }

}
