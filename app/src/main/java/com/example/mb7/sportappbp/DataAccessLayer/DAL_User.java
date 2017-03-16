package com.example.mb7.sportappbp.DataAccessLayer;

import android.util.Log;

import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by MB7 on 31.01.2017.
 */

public class DAL_User {
    static public void GetLastTodayStimmungsabfrage(User user, Date date)
    {
        try {
            String sDate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL  +  "players/" + user.getName() + "/Stimmungsabfrage/" + sDate + "/");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                       StimmungAbfrage st = ds.getValue(StimmungAbfrage.class);
                        st.Zerstreut = st.Zerstreut;
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("DAL_User.GetLTSabfrage",firebaseError.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    static public void InsertStimmung(User user, StimmungAbfrage stimmungAbfrage)
    {
        try
        {

            URL url = new URL(DAL_Utilities.DatabaseURL + "players/" + user.getName() + "/Stimmungsabfrage/" + stimmungAbfrage.Date + (stimmungAbfrage.Vor?"/V":"/N") + "/"  );
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

    /**
     * update groups of alternating group assignment
     * @param user the active user
     * @param currentActiveGroup the currently active group
     * @param nextActiveGroup the next group to be active
     * @param alternGroup the set of groups currently used
     */
    static public void insertAlternGroupUpdate(User user, String currentActiveGroup, String nextActiveGroup, String alternGroup) {
        try
        {
            // setting up url for the database
            URL url = new URL(DAL_Utilities.DatabaseURL + "/Administration/assignment/altern/" + alternGroup);
            Firebase root = new Firebase(url.toString());
            // update group values
            root.child(currentActiveGroup).child("groupactive").setValue(false);
            root.child(nextActiveGroup).child("groupactive").setValue(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
