package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.hardware.usb.UsbRequest;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Comparator.UserSortPoints;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

/**
 * Sends a notification if a challenge has ended
 * Created by Basti on 24.03.2017.
 */

public class ObserverChallengeWinner extends Observer {

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

        final Calendar calendar = Calendar.getInstance();
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

                    Challenge challenge;

                    //here are the names of the challenges
                    for (DataSnapshot nameChild : dataSnapshot.getChildren()) {
                        challenge = new Challenge();
                        //set end Date
                        String EndDate = (nameChild.getChildren().iterator().next().getValue().toString());
                        //here is the start and end date of the challenge
                        for (DataSnapshot dateChild : nameChild.getChildren()) {

                            if (dateChild.getKey().equals("startDate")) {

                                try {
                                    //check if the challenge is still active
                                    Date startDate = sdf.parse(dateChild.getValue().toString());
                                    Date endDate = sdf.parse(EndDate);

                                    if (endDate.before(today)) {
                                        //start notification for all users, which are in the gym
                                        challenge = new Challenge();
                                        challenge.setEndDate(endDate);
                                        challenge.setStartDate(startDate);
                                        challenge.setName(nameChild.getKey());
                                        getUSers(challenge);
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

    private void getUSers(final Challenge challenge) {


        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "Challenges/" + challenge.getName() + "/Users");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    LinkedList<User> users = new LinkedList<User>();

                    for (DataSnapshot usersChild : dataSnapshot.getChildren()) {
                        //save the username of the person
                        User usr = User.createUser(usersChild.getKey().toString(), context);
                        users.add(usr);


                    }

                    calcpoints(users, challenge);

                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void calcpoints(final LinkedList<User> users, final Challenge challenge) {


        for (final User usrPointer : users) {

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            try {
                URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + usrPointer.getName() + "/Diary/");
                final Firebase root = new Firebase(url.toString());

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int totalPoints = 0;

                        //day
                        for (DataSnapshot child1Date : dataSnapshot.getChildren()) {

                            try {
                                //get date of exercise
                                Date date = sdf.parse(child1Date.getKey());
                                //check if the date of the exercise is in the interval of the challenge
                                if (date.after(challenge.getStartDate()) || date.equals(challenge.getStartDate()) ||
                                        date.equals(challenge.getEndDate()) || date.before(challenge.getEndDate())) {
                                    //for the case, that a user did more then one challenge on a day
                                    for (DataSnapshot child2 : child1Date.getChildren()) {
                                        //calculate the total points
                                        for (DataSnapshot child3 : child2.getChildren()) {
                                            if (child3.getKey().equals("totalPoints"))
                                                totalPoints = totalPoints + Integer.valueOf(child3.getValue()
                                                        .toString());

                                        }
                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        usrPointer.setPoints(totalPoints);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(users, new UserSortPoints());

        if (challenge != null) {
            // we are in the interval where we should raise a notification
            // just check if the user hasn't got a notification before
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Boolean sendNotif = preferences.getBoolean(challenge.getName(), false);
            if (!sendNotif) {
                // insert in the preferences that notification has been sent
                preferences.edit().putBoolean(challenge.getName(), true).commit();
                ;
                // send notification

                String place = String.valueOf(challenge.getPositionOfTheChallenge(ActivityMain.getMainUser(context)));

                sendNotification(
                        context,
                        context.getString(R.string.Challenge),
                        ActivityMain.class, context.getString(R.string.Challenge),
                        ("Glückwunsch! " + challenge.getName() + " Challenge auf dem " + place + ". Platz beendet"),
                        R.mipmap.ic_stimmungs_abgabe);


            }
        }
    }
}

