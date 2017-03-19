package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;
import com.example.mb7.sportappbp.Objects.DiaryEntry;

import java.util.Date;
import java.util.List;

/**
 * Created by MB7 on 31.01.2017.
 */

public class User {

    private String name;
    private User (String Name)
    {
        name =Name;
    }

    public static User Create(String Name)
    {
        User user = new User(Name);
        return user;
    }

    public String getName()
    {
        return name;
    }

    public void GetLastTodayStimmungsabfrage(Date date)
    {
        DAL_User.GetLastTodayStimmungsabfrage(this,date);
    }
    public boolean SaveStimmung(StimmungAbfrage stimmungAbfrage, Date date)
    {
        DAL_User.InsertStimmung(this,stimmungAbfrage, date);
        return true;
    }
    public boolean SaveFitnessFragebogen(FitnessFragebogen fitnessfragebogen)
    {
        DAL_User.InsertFitnessFragebogen(this,fitnessfragebogen);
        return true;
    }
    public boolean SaveFragebogen(Fragebogen fragebogen)
    {
        DAL_User.InsertFragebogen(this,fragebogen);
        return true;
    }

    public void GetStimmnungsabfrage(Date date){
        DAL_User.GetStimmnungsabfrage(this,date);
    }
    public void GetLastTodayDiaryEntry(Date date)
    {
        DAL_User.GetLastTodayDiaryEntry(this,date);
    }

    public boolean SaveDiaryEntry(DiaryEntry diaryEntry)
    {
        DAL_User.InsertDiaryEntry(this,diaryEntry);
        return true;
    }

    /**
     * hand ratings to DAL_User to save them into database
     * @param listMethod list containing the rated methods
     * @param listRating list containing the ratings
     */
    void saveRating(List<String> listMethod, List<String> listRating){
        DAL_User.insertRating(this,listMethod,listRating);
    }

    /**
     * save updated group values for alternating group assignment
     * @param currentActiveGroup the currently active group
     * @param nextActiveGroup the next group to be active
     * @param alternGroup the set of groups currently used
     */
    void saveAlternGroupUpdate(String currentActiveGroup, String nextActiveGroup, String alternGroup) {
        DAL_User.insertAlternGroupUpdate(this,currentActiveGroup,nextActiveGroup,alternGroup);
    }
}
