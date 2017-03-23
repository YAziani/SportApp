package com.example.mb7.sportappbp.BusinessLayer;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Basti on 21.03.2017.
 */

public class Challenge {



    String name;
    ArrayList<User> UserList;
    Date startDate;
    Date endDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUserList() {
        return UserList;
    }

    public void setUserList(ArrayList<User> userList) {
        UserList = userList;
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



}
