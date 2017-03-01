package com.example.mb7.sportappbp.DataAccessLayer;

import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.Firebase;

import java.net.URL;
import java.util.List;

/**
 * Created by MB7 on 31.01.2017.
 */

public class DAL_User {

    static public void InsertStimmung(User user, StimmungAbfrage stimmungAbfrage)
    {
        try
        {

            URL url = new URL(DAL_Utilities.DatabaseURL + "players/" + user.getName() + "/Stimmungsabfrage_" + stimmungAbfrage.Date + (stimmungAbfrage.Vor?"_V":"_N") + "/"  );
            Firebase root = new Firebase(url.toString());
            if (stimmungAbfrage.Angespannt >= 0) {
                Firebase childAngespannt = root.child("Angespannt");
                childAngespannt.setValue(stimmungAbfrage.Angespannt);
            }
            if(stimmungAbfrage.Mitteilsam >=0) {
                Firebase childMitteilsam = root.child("Mitteilsam");
                childMitteilsam.setValue(stimmungAbfrage.Mitteilsam);
            }
            if(stimmungAbfrage.Muede >= 0) {
                Firebase childMuede = root.child("Muede");
                childMuede.setValue(stimmungAbfrage.Muede);
            }
            if(stimmungAbfrage.Selbstsicher >=0) {
                Firebase childSelbstsicher = root.child("Selbstsicher");
                childSelbstsicher.setValue(stimmungAbfrage.Selbstsicher);
            }
            if(stimmungAbfrage.Tatkraeftig >= 0) {
                Firebase childTatkraeftig = root.child("Tatkraeftig");
                childTatkraeftig.setValue(stimmungAbfrage.Tatkraeftig);
            }
            if(stimmungAbfrage.Traurig >=0) {
                Firebase childTraurig = root.child("Traurig");
                childTraurig.setValue(stimmungAbfrage.Traurig);
            }
            if(stimmungAbfrage.Wuetend >=0) {
                Firebase childWuetend = root.child("Wuetend");
                childWuetend.setValue(stimmungAbfrage.Wuetend);
            }
            if(stimmungAbfrage.Zerstreut >= 0) {
                Firebase childZerstreut = root.child("Zerstreut");
                childZerstreut.setValue(stimmungAbfrage.Zerstreut);
            }


        }
        catch (Exception ex)
        {
           String s = ex.getMessage();
        }
        finally
        {

        }

    }

    /**
     * inserts ratings into the database
     * @param user the current user
     * @param listMethod list containing the rated methods
     * @param listRating list containing the ratings
     */
    static public void insertRating(User user, List<String> listMethod, List<String> listRating) {
        try
        {
            // setting up url for the database
            URL url = new URL(DAL_Utilities.DatabaseURL + "players/" + user.getName() + "/methodRatings");
            Firebase root = new Firebase(url.toString());
            Firebase child;
            // insert ratings for each method
            for(int i = 0; i < Math.min(listMethod.size(),listRating.size()); i++) {
                child = root.child(listMethod.get(i));
                child.setValue(listRating.get(i));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
