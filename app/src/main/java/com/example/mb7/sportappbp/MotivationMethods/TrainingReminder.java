package com.example.mb7.sportappbp.MotivationMethods;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * class implements a reminder which reminds the user about his planned training
 * Created by Aziani on 23.12.2016.
 */
public class TrainingReminder extends MotivationMethod {

    // objects required to determine the user and studio positions
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final int  LOCATION_PERMISSION_REQUEST = 1440;

    // objects describing the user and studio positions
    private Address userAddress;
    private Address studioAddress;
    private Location userLocation;
    private Location studioLocation;
    private AppCompatActivity activity;

    /**
     * initialize the reminder and collect the address of the users fitness studio
     */
    public TrainingReminder(AppCompatActivity activity) {

        this.activity = activity;

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE) ;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("ATTENTION: location changed");

                userLocation = location;
                if(getUserAddress(location) != null) {
                    userAddress = getUserAddress(location);
                } else {
                    System.err.println("ATTENTION: address is null");
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

        if(activity instanceof ActivityMain) {
            ActivityCompat.requestPermissions(
                    this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
        }
    }

    @Override
    public void run(String trainingStartTime) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        // update studio location
        compareStudioPosition(preferences.getString("Studioadresse", ""));
        if(!(studioAddress == null) && !(userAddress == null)) {
            // if necessary, notify user
            if (checkNecessityOfNotification(trainingStartTime)) {
                notifyUser(trainingStartTime);
            }
        }
    }

    @Override
    public void rate() {

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
        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("ATTENTION: Address could not be determined.");
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
    public String compareStudioPosition(String givenPosition) {
        List<Address> determinedAddresses = null;
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

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
            System.err.println("ATTENTION: address could not be determined");
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
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
    private boolean checkNecessityOfNotification(String time) {

        // interval of time between checks in minute
        int period = 1;

        // start of the training
        int trainingHour = Integer.valueOf(time.split(":")[0]);
        int trainingMinute = Integer.valueOf(time.split(":")[1]);
        int trainingMinuteOfDay = trainingHour * 60 + trainingMinute;

        // current time
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentMinuteOfDay = currentHour * 60 + currentMinute;
        // time needed to get to the studio
        int timeNeeded = getTimeToStudio();

        // check if one could wait another period before user has to go
        return trainingMinuteOfDay - currentMinuteOfDay - timeNeeded >= 0
                && trainingMinuteOfDay - currentMinuteOfDay - timeNeeded < period;
    }

    /**
     * notify the user and provide the time needed until the training begins
     */
    private void notifyUser(String trainingStartTime) {

        final int notificationId = 4292;
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int trainingHourOfDay = Integer.valueOf(trainingStartTime.split(":")[0]);
        int trainingMinuteOfDay = Integer.valueOf(trainingStartTime.split(":")[1]);
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinuteOfDay = calendar.get(Calendar.MINUTE);

        // get time until training begins
        int timeTillTraining = (trainingHourOfDay * 60 + trainingMinuteOfDay)
                - (currentHourOfDay * 60 + currentMinuteOfDay);

        // setup notification builder
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.weight_icon)
                        .setContentTitle("Trainingserinnerung")
                        .setContentText("Zeit sich fertig zu machen");
        // specify which activity should be started upon clicking on the notification
        Intent notificationIntent = new Intent(activity,ActivityMain.class);
        notificationIntent.putExtra("startTab",1);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent intent = PendingIntent.getActivity(activity,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(intent);
        // setup notification manager
        final NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        // send notification
        notificationManager.notify(
                notificationId,
                notificationBuilder.setContentText(
                        "Ihr Training beginnt in etwa " + timeTillTraining + " Minuten").build()
        );
    }

    /**
     * handles the result of the permission request sent to the user
     * @param requestCode code of the requested permission
     * @param permissions array of all requested permissions
     * @param grantResults permissions which have been granted
     */
    @Override
    public void evaluatePermissionResults(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String best = locationManager.getBestProvider(criteria, false);
                if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    System.out.println("ATTENTION: permission denied");
                }else{
                    System.out.println("ATTENTION: permission granted");
                    locationManager.requestLocationUpdates(best, 0, 1, locationListener);
                }
            }catch(SecurityException e){
                e.printStackTrace();
            }
        }
    }
}