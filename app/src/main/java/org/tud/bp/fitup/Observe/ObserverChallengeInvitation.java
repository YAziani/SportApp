package com.tud.bp.fitup.Observe;

import android.content.Context;
import android.preference.PreferenceManager;

import com.tud.bp.fitup.Activity.ActivityChallenge;
import com.tud.bp.fitup.Activity.ActivityMain;
import com.tud.bp.fitup.BusinessLayer.Challenge;
import com.tud.bp.fitup.DataAccessLayer.DAL_Utilities;
import com.tud.bp.fitup.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class checks if a user has an invitation to a challenge and sends a notification
 * Created by Basti on 24.03.2017.
 */

public class ObserverChallengeInvitation extends Observer {

    /**
     * then main update method that is called from the Observable
     * Here you check your condition and do call then notify process
     * @param context
     */
    @Override
    public void update(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // show Notification if user gets an invitation
        shouldNotify();
    }


    /**
     * First check the database if the user has any invitation. Then send him a notificationjust one time for this name
     */

    private void shouldNotify(){

        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(context).getName()+
                    "/Invitations/Challenges/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String strName = null;
                    Challenge challenge = new Challenge();

                    //get all names of challenges which send a invitation
                    for(DataSnapshot name : dataSnapshot.getChildren()) {
                        challenge = new Challenge();
                        challenge.setName(name.getKey());
                        strName = name.getKey().toString();


                        //recover the dates of the challenge
                        for (DataSnapshot childName : name.getChildren()) {
                            //start date
                            if (childName.getKey().equals("startDate")) {
                                try {
                                    challenge.setStartDate(sdfDate.parse(childName.getValue().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //set end date
                            if (childName.getKey().equals("endDate")) {
                                try {
                                    challenge.setEndDate(sdfDate.parse(childName.getValue().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }


                        if(challenge.getName() != null) {
                            // we are in the interval where we should raise a notification
                            // just check if the user hasn't got a notification before
                            preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            String date = android.text.format.DateFormat.format("yyyy-MM-dd", new Date()).toString();
                            Boolean sendNotif = preferences.getBoolean(challenge.getName() + ActivityMain.getMainUser(context), false);
                            if (!sendNotif) {
                                // insert in the preferences that notification has been sent
                                preferences.edit().putBoolean(challenge.getName() + ActivityMain.getMainUser(context), true).commit();
                                ;
                                // send notification
                                sendNotification(context,"Einladung " + context.getString(R.string.Challenge),
                                        ActivityChallenge.class ,
                                        context.getString(R.string.Challenge) ,
                                        context.getString(R.string.ntf_ChallengeEinladung),
                                        R.mipmap.ic_challenge,
                                        challenge, context.getString(R.string.Challenges) );


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
