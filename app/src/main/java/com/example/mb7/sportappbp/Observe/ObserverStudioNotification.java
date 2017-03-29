package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by M.Braei on 24.03.2017.
 */

public class ObserverStudioNotification extends Observer {

    /**
     * then main update method that is called from the Observable
     * Here you check your condition and do call then notify process
     *
     * @param context
     */
    @Override
    public void update(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // show Notification if a user of a challenge is in the fitness studio
        searchChallengesToNotify();
    }


    // get the current time in minutes
    private int getMinutesofDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentMinuteOfDay = currentHourOfDay * 60 + currentMinute;
        return currentMinuteOfDay;
    }

    /**
     * This method searches all challenges, which are not finished and starts a notification for the challenge
     *
     * @return
     */


    private void searchChallengesToNotify() {

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final Date today = calendar.getTime();
        //URL url = null;
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "/users/" + ActivityMain.getMainUser(context).getName() +
                    "/Challenges/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getChildren();
                    //here are the names of the challenges
                    for (DataSnapshot nameChild : dataSnapshot.getChildren()) {
                        //her is the start and end date of the challenge
                        for (DataSnapshot dateChild : nameChild.getChildren()) {
                            if (dateChild.getKey().equals("endDate")) {
                                try {
                                    //check if the challenge is still active
                                    Date endDate = null;
                                    endDate = sdf.parse(dateChild.getValue().toString());
                                    if (endDate.after(today)) {
                                        //start notification for all users, which are in the gym
                                        doNotify(nameChild.getKey().toString());
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * show Notification if we anyone of the challenges is in the gym
     * if it is show notification and save that you have showed it in the preferences to not repeat it for the same
     * event
     *
     * @return
     */

    private void doNotify(final String challengeName) {

        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "Challenges/" + challengeName + "/AtStudio");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String usrName = "";

                    for (DataSnapshot name : dataSnapshot.getChildren()) {
                        //save the username of the person
                        usrName = name.getKey().toString();

                        //the user should not get a notification when he is in the gym
                        if (!usrName.equals(ActivityMain.getMainUser(context).getName())) {
                            if (usrName != "") {
                                // we are in the interval where we should raise a notification
                                // just check if the user hasn't got a notification before
                                preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                String date = android.text.format.DateFormat.format("yyyy-MM-dd", new Date())
                                        .toString();
                                Boolean sendNotif = preferences.getBoolean(usrName + date, false);
                                if (!sendNotif) {
                                    // insert in the preferences that notification has been sent
                                    preferences.edit().putBoolean(usrName + date, true).commit();
                                    ;


                                    // send notification
                                    sendNotification(
                                            context,
                                            context.getString(R.string.Challenge),
                                            ActivityMain.class, context.getString(R.string.stimmungsabgabe),
                                            (usrName + " " + context.getString(R.string.IstGeradeImFitnessStudio)),
                                            R.mipmap.ic_challenge);
                                }
                            }


                        }
                    }

                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


}
