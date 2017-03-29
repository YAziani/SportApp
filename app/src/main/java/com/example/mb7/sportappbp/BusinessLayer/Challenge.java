package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.Comparator.UserSortPoints;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Challenges;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * When you want to add a user to the challenge, you have to use the addUser() method. Otherwise the list will not be
 * sorted.
 * Created by Basti on 21.03.2017.
 */

public class Challenge implements Serializable {


    private String name;
    private List<User> userList;
    private Date startDate;
    private Date endDate;
    private String admin;
    private Boolean active;
    final Calendar todayCalendar = Calendar.getInstance();

    public Challenge() {
        this.userList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserList(List<User> list) {
        this.userList = list;
    }

    public List<User> getUserList() {
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

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public boolean getActive() {
        return active;
    }

    /**
     * This method adds a user to the challenge user list and then sorts the list
     *
     * @param user
     */
    public void addUser(User user) {
        userList.add(user);
        sortUserList();
    }

    /**
     * This methos sorts the list descending
     */
    public void sortUserList() {
        Collections.sort(userList, new UserSortPoints());
    }

    public void closeChallenge() {

    }

    /**
     * This method calculates the days which still remain from today
     *
     * @return
     */
    public int getRemainingDays() {

        Date today = todayCalendar.getTime();

        int result = (int) ((endDate.getTime() / (24 * 60 * 60 * 1000)) - (int) (today.getTime() / (24 * 60 * 60 *
                1000)));

        return result;
    }

    /**
     * Returns the place of the challenge from a user. If the user is not in the list, the method
     * returns 0
     *
     * @param user search user
     * @return position or 0 if user not in list
     */
    public int getPositionOfTheChallenge(User user) {
        sortUserList();
        int i = 1;
        for (User p : userList) {

            if (p.getName().equals(user.getName()))
                return i;
            i++;
        }
        return 1;
    }

    /**
     * This method checks whether the end date was achieved and returns then true or false
     *
     * @return true if the end date has been achieved
     */
    private Boolean checkFinish() {

        Date todaydate;

        todaydate = todayCalendar.getTime();

        if (todaydate.after(endDate)) {
            return true;
        } else
            return false;
    }

    /**
     * Checks if the challenge has been finished and returns true
     *
     * @return true if finished
     */
    public Boolean finished() {
        String result;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

        if (todayCalendar.getTime().after(endDate))
            return true;
        else
            return false;
    }


    /**
     * Challenge to DAL_Challenge to save its to database
     *
     * @return
     */
    public boolean SaveNewChallenge() {
        DAL_Challenges.InsertChallenge(this);
        return true;
    }

    /**
     * User to DAL_Challenge to add reference to user on database
     * challenge to DAL_User to add reference to challenge on database
     *
     * @param user user to add
     * @return
     */
    public boolean AddUser(User user) {
        DAL_Challenges.InsertUser(user, this);
        DAL_User.InsertChallenge(user, this);
        return true;
    }

    /**
     * User to DAL_Challenge to save its to database
     *
     * @param user user to add
     * @return
     */
    public boolean AddAdmin(User user) {
        DAL_Challenges.InsertAdmin(user, this);
        return true;
    }

    /**
     * User to DAL_Challenge to remove reference to user on database
     * challenge to DAL_User to remove reference to challenge on database
     *
     * @param user user to add
     * @return
     */
    public boolean RemoveUser(User user) {
        DAL_Challenges.RemoveUser(user, this);
        DAL_User.RemoveChallenge(user, this);
        return true;
    }

    /**
     * User to DAL_Challenge to save its to database
     *
     * @param user user to add
     * @return
     */
    public boolean InviteUser(User user) {
        DAL_Challenges.InsertInvitation(user, this);
        return true;
    }

    /**
     * User to DAL_Challenge to delete invitation from database
     *
     * @param user user to add
     * @return
     */
    public boolean RemoveInvitation(User user) {
        DAL_Challenges.RemoveInvitation(user, this);
        return true;
    }
}
