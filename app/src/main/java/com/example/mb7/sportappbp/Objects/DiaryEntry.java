package com.example.mb7.sportappbp.Objects;

/**
 * Created by Basti on 16.01.2017.
 */

public class DiaryEntry {

    private String date;
    private Boolean successful;

    /**
     * Constructor to create a diary entry
     * @param date : date of the training
     * @param successful : was the training successful?
     */
    public DiaryEntry(String date, Boolean successful){
        this.date = date;
        this.successful = successful;
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
}
