package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;

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

    public boolean SaveStimmung(StimmungAbfrage stimmungAbfrage)
    {
        DAL_User.InsertStimmung(this,stimmungAbfrage);
        return true;
    }

    /**
     * hand ratings to DAL_User to save them into database
     * @param listMethod list containing the rated methods
     * @param listRating list containing the ratings
     */
    public void saveRating(List<String> listMethod, List<String> listRating){
        DAL_User.insertRating(this,listMethod,listRating);
    }
}
