package com.example.mb7.sportappbp.DataAccessLayer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.BusinessLayer.MethodChooser;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.List;

/**
 * access allocation of motivation methods from database
 * Created by Aziani on 22.02.2017.
 */

public class DAL_Allocation {

    /**
     * get allocated methods from db
     *
     * @param activity calling activity
     */
    public static void getAllocation(final Activity activity) {

        // access data in database and hand it to MethodChooser
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "Administration/assignment/");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MethodChooser.chooseMethods(dataSnapshot, activity);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getIntensifier(final Context context) {
        try {

            URL url = new URL(DAL_Utilities.DatabaseURL + "Administration/ruleintensifier/");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    saveIntensifier(context, dataSnapshot);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveIntensifier(Context context, DataSnapshot dataSnapshot) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String s = "";

        for (DataSnapshot d : dataSnapshot.getChildren()) {
            if (!s.equals("")) {
                s += ";";
            }
            if (d.child("days").getValue() != null) {
                s += d.child("days").getValue() + ",";
            } else {
                s += ",";
            }
            if (d.child("nmbrOfPN").getValue() != null) {
                s += d.child("nmbrOfPN").getValue() + ",";
            } else {
                s += ",";
            }
            if (d.child("probability").getValue() != null) {
                s += Double.valueOf(((String) d.child("probability").getValue()).split("%")[0]) / 100;
            }
        }

        preferences.edit().putString("intensifier", s).commit();
    }
}

