package com.example.mb7.sportappbp.BusinessLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sebastian on 16.01.2017.
 */

public class DiaryEntry implements Serializable {

    private Date date;
    private int totalPoints;
    private ArrayList<Exercise> exerciseList;
    public String sDate;
    public String sTime;


    /**
     * Constructor to create a diary entry
     */
    //String id, String date, String time
    public DiaryEntry() {
        exerciseList = new ArrayList<Exercise>();
    }

    public ArrayList<Exercise> getExerciseList() {
        return this.exerciseList;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setExerciseList(ArrayList<Exercise> newList) {
        this.exerciseList = newList;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }


    /**
     * This method returns the date of the training
     *
     * @return returns the date of the training as string
     */
    public Date getDate() {
        return this.date;
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

    public int getTotalPoints() {
        return totalPoints;
    }


    /**
     * Calculates the total time and points of the category Leistungstest and returns them as an array
     * @return the array with hour at the first , minutes second and points third position
     */
    public int[] getTotalTimePointsAsArrayLeistungstests() {

        int[] result = new int[3];
        int resultMin = 0;
        int resultHours = 0;

        for (Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if (i instanceof LeistungstestsExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if (resultMin >= 60) {
                    resultHours++;
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


    /**
     * Calculates the total time and points of the category Training and returns them as an array
     * @return the array with hour at the first , minutes second and points third position
     */
    public int[] getTotalTimePointsAsArrayTraining() {

        int[] result = new int[3];
        int resultMin = 0;
        int resultHours = 0;

        for (Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if (i instanceof TrainingExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if (resultMin >= 60) {
                    resultHours++;
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


    /**
     * Calculates the total time and points of the category Wellness and returns them as an array
     * @return the array with hour at the first , minutes second and points third position
     */
    public int[] getTotalTimePointsAsArrayWellness() {

        int[] result = new int[3];
        int resultMin = 0;
        int resultHours = 0;

        for (Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if (i instanceof WellnessExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if (resultMin >= 60) {
                    resultHours++;
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


    /**
     * Calculates the total time and points of the category Reiner Aufenthalt  and returns them as an array
     * @return the array with hour at the first , minutes second and points third position
     */
    public int[] getTotalTimePointsAsArrayReinerAufenthalt() {

        int[] result = new int[3];
        int resultMin = 0;
        int resultHours = 0;

        for (Exercise i : exerciseList) {
            //calculate all "Reiner Aufenthalt" exercises
            if (i instanceof ReinerAufenthaltExercise) {
                //Calculate minutes and hours
                resultHours = resultHours + i.getTimeHours();
                resultMin = resultMin + i.getTimeMunites();

                //check, if the minutes are bigger as an hour
                if (resultMin >= 60) {
                    resultHours++;
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


    public void addExercise(Exercise activity) {
        exerciseList.add(activity);
    }

    public void addNewExercises(ArrayList<Exercise> exLst) {
        for (Exercise i : exLst) {
            exerciseList.add(i);
        }
    }
}
