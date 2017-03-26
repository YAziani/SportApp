package com.example.mb7.sportappbp.DataAccessLayer;

import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.ActivityNewChallenge;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Basti on 25.03.2017.
 */

public class DAL_Challenges {


    /**
     * get registered challenges and hand it to the activity
     *
     * @param activity the displaying activity
     */
    public static void getRegisteredChallengesToNewChallenge(
            final ActivityNewChallenge activity) {
        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "/challenges/");
            Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activity.returnRegisteredChallenges(dataSnapshot);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get registered challenges and hand it to the activity
     *
     * @param activity the displaying activity
     */
    public static void getRegisteredChallengesToChallenge(
            final ActivityChallenge activity) {
        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "/challenges/");
            Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activity.returnRegisteredChallenges(dataSnapshot);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void InsertChallenge(Challenge challenge) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "challenges/" + challenge.getName());

        Firebase childStartDate = ref.child("startDate");
        childStartDate.setValue(sdf.format(challenge.getStartDate()));

        Firebase childEndDate = ref.child("endDate");
        childEndDate.setValue(sdf.format(challenge.getEndDate()).toString());


        int i = 0;
        for (User user : challenge.getUserList()) {
            Firebase userChild = ref.child("user " + String.valueOf(i) + " :");
            userChild.setValue(user.getEmail());


            i++;
        }

    }
}
