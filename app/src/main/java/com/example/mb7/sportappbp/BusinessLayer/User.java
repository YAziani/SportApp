package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;

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

    public boolean SaveStimmung(StimmungAbfrage stimmungAbfrage)
    {
        DAL_User.InsertStimmung(this,stimmungAbfrage);
        return true;
    }
}
