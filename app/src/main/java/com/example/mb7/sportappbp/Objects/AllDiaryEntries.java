package com.example.mb7.sportappbp.Objects;

import java.util.ArrayList;

/**
 * Created by Basti on 16.01.2017.
 */

public class AllDiaryEntries {

    //Create an object of SingleObject
    private static AllDiaryEntries allDiaryEntries;
    private ArrayList<DiaryEntry> diaryList;

    //set constructor private cause of singleton
    private AllDiaryEntries(){
        diaryList = new ArrayList<DiaryEntry>();
    };

    /**
     * This method returns the singleton object of AllDiaryEntires
     * @return the only one object
     */
    public static AllDiaryEntries getInstance(){
        if(allDiaryEntries == null)
            allDiaryEntries = new AllDiaryEntries();

        return allDiaryEntries;
    }

    public void add(DiaryEntry diaryEntry){
        allDiaryEntries.add(diaryEntry);
    }

    /**
     * This method returns the list with all diary entries.
     * @return ArrayList with all diary entries
     */
    public ArrayList<DiaryEntry> getDiaryList(){
        return diaryList;
    }

    /**
     * This method adds the all dates of the list as a string to a new list
     * @return a arraylist with all dates as strings
     */
    public ArrayList<String> getAllDates(){
        ArrayList<String> dateList = new ArrayList<String>();

        for(DiaryEntry p : diaryList){
            dateList.add(p.getDate());
        }

        return dateList;
    }



}
