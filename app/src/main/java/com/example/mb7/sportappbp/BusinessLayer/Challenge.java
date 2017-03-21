package com.example.mb7.sportappbp.BusinessLayer;

import java.util.ArrayList;

/**
 * Created by Basti on 21.03.2017.
 */

public class Challenge {



    String name;
    ArrayList<User> UserList;
    String startDate;
    String endDate;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



}
