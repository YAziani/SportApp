package com.example.mb7.sportappbp.DataAccessLayer;

import android.app.Dialog;

import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.ActivityLogin;
import com.example.mb7.sportappbp.Activity.ActivityNewChallenge;
import com.example.mb7.sportappbp.BusinessLayer.RegisterCatcher;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;

/**
 * class to access registered users
 * Created by Aziani on 23.03.2017.
 */

public class DAL_RegisteredUsers {

    /**
     * get registered users and hand it to the activity
     *
     * @param activity the displaying activity
     */
    public static void getRegisteredUsers(
            final ActivityLogin activity) {
        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activity.returnRegisteredUsers(dataSnapshot);
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
     * get registered users and hand it to the activity with dialog and usermail
     *
     * @param activity the displaying activity
     * @param searchedUser the searched mail address of the user
     * @param dialog dialog where the mail address has been entered
     *
     */
    public static void checkRegisteredUsersNewChallenge(
            final ActivityNewChallenge activity, final String searchedUser, final Dialog dialog) {
        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activity.returnRegisteredUsers(dataSnapshot, searchedUser, dialog);
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
     * get registered users and hand it to the activity
     *
     * @param activity the displaying activity
     */
    public static void checkRegisteredUsersChallenge(
            final ActivityChallenge activity, final String searchedUser, final Dialog dialog) {
        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activity.returnRegisteredUsers(dataSnapshot, searchedUser, dialog);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertRegistration(String username, String password) {
        try {
            // setting up url for the database
            URL url = new URL(DAL_Utilities.DatabaseURL + "users");
            Firebase root = new Firebase(url.toString());
            // insert user
            root.child(username).child("password").setValue(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadRegistration(final RegisterCatcher registerCatcher) {
        try {
            URL url = new URL(DAL_Utilities.KompassURL +  "users");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    registerCatcher.returnRegistrations(dataSnapshot);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    registerCatcher.returnRegistrations(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertMail(String mail, User user) {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + user.getName());
            Firebase root = new Firebase(url.toString());
            // insert user
            root.child("email").setValue(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
