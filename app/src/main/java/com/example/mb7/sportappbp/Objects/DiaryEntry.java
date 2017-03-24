package com.example.mb7.sportappbp.Objects;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * When you add a new attribute and you want to save it to the database, you have to add these
 * in class DAL_USER in the method InsertDiaryEntry()
 * Created by Basti on 16.01.2017.
 */

public class DiaryEntry {

    private String id;
    private String date;
    private String time;
    private int totalpoints;
    private ArrayList<Exercise> exerciseList;





    /**
     * Constructor to create a diary entry
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
    public int getTotalpoints() {
        return totalpoints;
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

    public int[] getTotalTimePointsAsArrayLeistungstests(){

        int[] result = new int[3];
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

        //set results
        result[0] = resultHours;
        result[1] = resultMin;
        result[2] = points;

        return result;
    }


    public int[] getTotalTimePointsAsArrayTraining(){

        int[] result = new int[3];
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

        //set results
        result[0] = resultHours;
        result[1] = resultMin;
        result[2] = points;

        return result;
    }


    public int[] getTotalTimePointsAsArrayWellness(){

        int[] result = new int[3];
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

        //set results
        result[0] = resultHours;
        result[1] = resultMin;
        result[2] = points;

        return result;
    }


    public int[] getTotalTimePointsAsArrayReinerAufenthalt(){

        int[] result = new int[3];
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

        //set results
        result[0] = resultHours;
        result[1] = resultMin;
        result[2] = points;

        return result;
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
    public void setTotalpoints(int totalpoints) {
        this.totalpoints = totalpoints;
    }



    public void addExercise(Exercise activity){
        exerciseList.add(activity);
    }

    public void addNewExercises(ArrayList<Exercise> exLst){
        for(Exercise i : exLst){
            exerciseList.add(i);
        }
    }

    /*
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
    */

}
