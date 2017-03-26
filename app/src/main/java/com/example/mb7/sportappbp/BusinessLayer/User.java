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
    private String challangeName;






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

    public void setChallangeName(String challangeName){
        this.challangeName = challangeName;
    }

    public String getChallangeName(){
        return challangeName;
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
    public boolean InsertStimmung(StimmungsAngabe stimmungsAngabe)
    {
        DAL_User.InsertStimmung(this, stimmungsAngabe);
        return true;
    }
    public boolean UpdateStimmung(StimmungsAngabe stimmungsAngabe)
    {
        DAL_User.UpdateStimmung(this, stimmungsAngabe);
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

    public StimmungsAngabe GetStimmnungsabfrage(String date){
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

    public void saveRegistration(String username, String password) {
        DAL_RegisteredUsers.insertRegistration(username, password);
    }
}
