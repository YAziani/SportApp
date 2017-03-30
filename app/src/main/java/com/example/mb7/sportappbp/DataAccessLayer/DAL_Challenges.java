package com.example.mb7.sportappbp.DataAccessLayer;

import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.ActivityNewChallenge;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
            URL url = new URL(DAL_Utilities.DatabaseURL + "Challenges/");
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
    public static void getRegisteredUsersToChallenge(
            final ActivityChallenge activity, String challengeName) {
        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "Challenges/" + challengeName + "/Users/");
            Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activity.returnRegisteredChallengeUsers(dataSnapshot);
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
     * Inserts challenge to database
     *
     * @param challenge current challenge
     */
    public static void InsertChallenge(Challenge challenge) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "Challenges/" + challenge.getName());

        Firebase startDate = ref.child("startDate");
        startDate.setValue(sdf.format(challenge.getStartDate()));

        Firebase endDate = ref.child("endDate");
        endDate.setValue(sdf.format(challenge.getEndDate()).toString());
    }

    /**
     * Inserts user to database as user of the challenge
     *
     * @param user      user to add as admin
     * @param challenge current challenge
     */
    public static void InsertUser(User user, Challenge challenge) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "Challenges/" + challenge.getName());

        Firebase users = ref.child("Users");

        Firebase usersChild = users.child(user.getName());
        usersChild.setValue(user.getName());

    }

    /**
     * Inserts admin of the challenge to database
     *
     * @param user      user to add as admin
     * @param challenge current challenge
     */
    public static void InsertAdmin(User user, Challenge challenge) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "Challenges/" + challenge.getName());

        Firebase admin = ref.child("Admin");

        Firebase adminChild = admin.child("admin");
        adminChild.setValue(user.getName());


    }

    /**
     * Removes the reference to user
     *
     * @param user      to remove
     * @param challenge current challenge
     */
    public static void InsertInvitation(User user, Challenge challenge) {

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Invitations/Challenges");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Firebase name = ref.child(challenge.getName());

        Firebase nameChildStart = name.child("startDate");
        nameChildStart.setValue(sdf.format(challenge.getStartDate()));

        Firebase nameChildEnd = name.child("endDate");
        nameChildEnd.setValue(sdf.format(challenge.getEndDate()));
    }

    /**
     * Removes the reference to user
     *
     * @param user      to invite
     * @param challenge current challenge
     */
    public static void RemoveInvitation(User user, Challenge challenge) {

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + user.getName() + "/Invitations/Challenges/");
        ref.child(challenge.getName()).removeValue();
    }


    /**
     * Removes the reference to user
     *
     * @param user      to invite
     * @param challenge current challenge
     */
    public static void RemoveUser(User user, Challenge challenge) {

        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "Challenges/" + challenge.getName() + "/Users/");
        ref.child(user.getName()).removeValue();
    }


}
