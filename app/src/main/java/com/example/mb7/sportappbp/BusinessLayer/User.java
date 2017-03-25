package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.DataAccessLayer.DAL_RegisteredUsers;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;

import java.util.Date;
import java.util.List;

/**
 * Created by MB7 on 31.01.2017.
 */

public class User {

    private String name;
    private int points;
    private Challenge challenge = null;
    private String email;






    public static User Create(String Name)
    {
        User user = new User(Name);
        return user;
    }
    private User (String Name)
    {
        name =Name;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getName()
    {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void GetLastTodayStimmungsabfrage(Date date)
    {
        DAL_User.GetLastTodayStimmungsabfrage(this,date);
    }
    public boolean SaveStimmung(StimmungsAngabe stimmungsAngabe, Date date)
    {
        DAL_User.InsertStimmung(this, stimmungsAngabe, date);
        return true;
    }
    public boolean SaveStimmungScore(StimmungAbfrageScore stimmungAbfrageScore, Date date)
    {
        DAL_User.InsertStimmungScore(this,stimmungAbfrageScore, date);
        return true;
    }


    public boolean SaveFitnessFragebogen(FitnessFragebogen fitnessfragebogen, Date date)
    {
        DAL_User.InsertFitnessFragebogen(this,fitnessfragebogen,date);
        return true;
    }
    public boolean SaveFragebogen(Fragebogen fragebogen, Date date)
    {
        DAL_User.InsertFragebogen(this,fragebogen,date);
        return true;
    }

    public StimmungsAngabe GetStimmnungsabfrage(Date date){
        return DAL_User.GetStimmnungsabfrage(this,date);

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
    public boolean LoadCompleteDiry(){
        DAL_User.LoadCompleteDiary(this);
        return true;
    }

    public boolean getTotalPointsFromTo(Date from, Date to){
            //todo implement the firebase method
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

    public void saveRegistration(String username, String email, String password) {
        DAL_RegisteredUsers.insertRegistration(username, email, password);
    }
}
