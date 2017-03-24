package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.Comparator.UserSortPoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * When you want to add a user to the challenge, you have to use the addUser() method. Otherwise the list will not be sorted.
 * Created by Basti on 21.03.2017.
 */

public class Challenge {



    private String name;
    private ArrayList<User> userList;
    private Date startDate;
    private Date endDate;
    private Boolean active;
    final Calendar todayCalendar = Calendar.getInstance();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public void setActive(){
        this.active = true;
    }

    public void setInactive(){
        this.active = false;
    }

    public boolean getActive(){
        return active;
    }

    /**
     * This method adds a user to the challenge user list and then sorts the list
     * @param user
     */
    public void addUser(User user){
        userList.add(user);
        sortUserList();
    }

    /**
     * This methos sorts the list descending
     */
    public void sortUserList(){
        Collections.sort(userList, new UserSortPoints());
    }

    public void closeChallenge(){

    }

    /**
     * This method calculates the days which still remain from today
     * @return
     */
    public int getRemainingDays(){

        Date today = todayCalendar.getTime();

        int result = (int)((endDate.getTime()/(24*60*60*1000))-(int)(today.getTime()/(24*60*60*1000)));

        return result;
    }

    /**
     * This method checks whether the end date was achieved and returns then true or false
     * @return true if the end date has been achieved
     */
    private Boolean checkFinish() {

        Date todaydate;

            todaydate = todayCalendar.getTime();

        if (todaydate.after(endDate)) {
                return true;
            }
        else
            return false;
    }

}
