package com.example.mb7.sportappbp.Observe;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityTrainQuestioning;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * observer for training reminder
 * Created by Aziani on 24.03.2017.
 */

public class ObserverTrainingReminder extends Observer {

    // objects describing the user and studio positions
    private static Address userAddress;
    private static Address studioAddress;
    private static Location userLocation;
    private static Location studioLocation;

    private short timeOutCounter = 0;

    public ObserverTrainingReminder(Context context) {
        this.context = context;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE) ;
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location;
                if(getUserAddress(location) != null) {
                    userAddress = getUserAddress(location);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String best = locationManager.getBestProvider(criteria, false);
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                System.out.println("ATTENTION: permission denied");
            }else{
                System.out.println("ATTENTION: permission granted");
                locationManager.requestLocationUpdates(best, 0, 1, locationListener);
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Context context) {

        // check if method allocated
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!preferences.getString("allocatedMethods","").contains("trainingreminder")) {
            return;
        }

        if(timeOutCounter > 0) {
            timeOutCounter--;
        }else {
            if(!getNextTrainingTimeString(context).equals("")) {
                preferences = PreferenceManager.getDefaultSharedPreferences(context);
                // update studio location
                compareStudioPosition(preferences.getString("Studioadresse", ""));
                if(!(studioAddress == null) && !(userAddress == null)) {
                    // if necessary, notify user
                    if (checkNecessityOfNotification(getNextTrainingTimeString(context))) {
                        if(!preferences.getBoolean("reminderNotified", false)) {
                            sendNotification(
                                    context,
                                    "Trainingserinnerung",
                                    ActivityMain.class,
                                    context.getString(R.string.trNotiTitle),
                                    context.getString(R.string.trNotiSmallTitle1)
                                            +" "+ MotivationMethod.timeTillTraining(getNextTrainingTimeString(context))
                                            +" "+ context.getString(R.string.trNotiSmallTitle2),
                                    R.mipmap.ic_tagebuch_eintrag);
                            timeOutCounter = 5;
                            preferences.edit().putBoolean("reminderNotified", true).apply();
                            preferences.edit().putString("nextTrainingTime", getNextTrainingTimeString(context)).apply();
                            return;
                        }
                        return;
                    }
                }
                preferences.edit().putBoolean("reminderNotified",false).apply();
                preferences.edit().remove("nextTrainingTime").apply();
            }
        }
    }

    @Override
    public void createNotification(Context context, String NotificationDate, Class<?> cls,String title, String text, Integer icon ){

        Intent contentClass = new Intent(context, cls);
        contentClass.putExtra("NotificationDate",NotificationDate);
        // Used to stack tasks across activites so we go to the proper place when back is clicked
        // create(context): context is the context that will launch the new task stack or a PendingIndent
        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(context);

        // Add all parents of this activity to the stack
        // The parentstck of MoreInfoNotifaction is defined in the Manifest -> <android:parentActivityName=".MainActivity">
        tStackBuilder.addParentStack(cls);

        // Add our new Intent to the stack
        tStackBuilder.addNextIntent(contentClass);

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setTicker(text);
        // specify which activity should be started upon clicking on the notification
        Intent intent = new Intent(context,ActivityMain.class);
        intent.putExtra("startTab",1);
        intent.putExtra("notificationId", 133);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        // setting up buttons for question (will you go to training?)
        Intent intentYes = new Intent(context,ActivityTrainQuestioning.class);
        intentYes.setAction("YES_ACTION");
        intentYes.putExtra("notificationId", 133);
        intentYes.putExtra("praiseOrWarn", 0);
        intentYes.putExtra("preTrain", true);
        PendingIntent pendingIntentYes = PendingIntent.getActivity(context,0,intentYes,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.box,"Ja",pendingIntentYes);

        Intent intentNo = new Intent(context,ActivityTrainQuestioning.class);
        intentNo.setAction("NO_ACTION");
        intentNo.putExtra("notificationId", 133);
        intentNo.putExtra("praiseOrWarn", 1);
        intentYes.putExtra("preTrain", true);
        PendingIntent pendingIntentNo = PendingIntent.getActivity(context,0,intentNo,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.box,"Nein",pendingIntentNo);

        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

        // setup notification manager
        final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // send notification
        notificationManager.notify(133,notificationBuilder.build());

    }


    /**
     * determines the address object of the given location object
     * @param location: location object, describing the users position
     * @return address object describing users position
     */
    private Address getUserAddress(Location location) {
        Address tmpUserAddress = null;
        // get address from coordinates
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("ATTENTION: user address could not be determined.");
        }

        // check if address is valid
        if(addresses != null && addresses.size() > 0 && addresses.get(0) != null) {
            tmpUserAddress = addresses.get(0);
        } else {
            System.out.println("ATTENTION: Address equals null.");
        }
        return tmpUserAddress;
    }

    /**
     * determines the address object of the given address and returns it for comparing
     * @param givenPosition: the user entered name of the studio address
     * @return address object matching the user given address the most
     */
    private String compareStudioPosition(String givenPosition) {
        List<Address> determinedAddresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // determine the address matching the given address the most
        try {
            determinedAddresses = geocoder.getFromLocationName(givenPosition, 1);
        }catch(Exception e){
            e.printStackTrace();
        }

        // set the studio position to the determined address
        if(determinedAddresses != null && determinedAddresses.size() > 0 && determinedAddresses.get(0) != null) {
            studioAddress = determinedAddresses.get(0);
            studioLocation = new Location("studioLocation");
            studioLocation.setLatitude(studioAddress.getLatitude());
            studioLocation.setLongitude(studioAddress.getLongitude());
            return determinedAddresses.get(0).getAddressLine(0) + ", " + determinedAddresses.get(0).getLocality();
        }else {
            System.err.println("ATTENTION: studio address could not be determined");
            return null;
        }
    }

    /**
     * determines the distance between the user position and the studio postition
     * @return distance in meter
     */
    private float getDistanceToStudio() {
        // 2 * squareroot(2) = 2.8284f
        // approximate distance compared to beeline about 125%
        float cityBlockFactor = 1.25f;

        // return the approximate distance between user and studio
        if(userLocation != null && studioLocation != null) {
            return userLocation.distanceTo(studioLocation) * cityBlockFactor;
        }
        return 0;
    }

    /**
     * determines time needed to get to the studio starting at the users position
     * @return time needed in minutes
     */
    private int getTimeToStudio() {
        
        // time to studio in minutes
        float time;
        // standard amount of time which always will be added to the net time
        float buffer = 15;
        // distance to studio in meter
        float distance = getDistanceToStudio();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String meansOfTransportation = preferences.getString("lstVerkehrsmittel","");

        // speed in meter/minutes
        float speed = 85.0f;
        if (meansOfTransportation.equals("1")) {
            speed = 85.0f;
        } else {
            if (meansOfTransportation.equals("2")) {
                speed = 250.0f;
            } else {
                if (meansOfTransportation.equals("3")) {
                    speed = 450.0f;
                } else {
                    if (meansOfTransportation.equals("4")) {
                        speed = 750.0f;
                    }
                }
            }
        }
        time = distance / speed;
        // round time needed to the next lowest multiple of five
        time -= (time % 5);
        return (int)(time + buffer);
    }

    /**
     * checks if the time has come to remember the user to go to the studio
     * @return boolean, which is true if user has to be reminded and false if not
     */
    private boolean checkNecessityOfNotification(String trainingStartTime) {

        if(trainingStartTime == null || trainingStartTime.equals("")) {
            return false;
        }

        // time needed to get to the studio
        int timeNeeded = getTimeToStudio();
        int timeTillTraining = MotivationMethod.timeTillTraining(trainingStartTime);

        // check if one could wait another period before user has to go
        return timeTillTraining > 1 && timeTillTraining <= timeNeeded;
    }
}
