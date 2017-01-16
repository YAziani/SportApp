package com.example.mb7.sportappbp.MotivationMethods;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.mb7.sportappbp.MainActivity;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aziani on 23.12.2016.
 *
 * class implements a reminder which reminds the user about his planned training
 */

public class TrainingReminder extends MotivationMethod {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Address userAddress;
    private Address studioAddress;
    private Location userLocation;
    private Location studioLocation;
    private final int  LOCATION_PERMISSION_REQUEST = 1440;
    private MainActivity mainActivity;
    private enum MeansOfTransportation {AFOOT, BICYCLE, CAR, BUS, TRAIN};
    private MeansOfTransportation  meansOfTransportation = null;

    /**
     * initialize the reminder and collect the address of the users fitness studio
     */
    public TrainingReminder(MainActivity mainActivity) {

        // TODO implement the collection of the studio address
        this.mainActivity = mainActivity;
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE) ;
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


                // TODO remove debug method
                debug();
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

        ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
    }

    @Override
    public void run() {

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
        geocoder = new Geocoder(mainActivity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("ATTENTION: Address could not be determined.");
        }

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
        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());

        try {
            determinedAddresses = geocoder.getFromLocationName(givenPosition, 1);
        }catch(Exception e){
            e.printStackTrace();
        }

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
        // 2 * squareroot(2)
        float cityBlockFactor = 2.8284f;
        return userLocation.distanceTo(studioLocation) * cityBlockFactor;
    }

    /**
     * determines time needed to get to the studio starting at the users position
     * @return time needed in minutes
     */
    private float getTimeToStudio() {
        // time to studio in minutes
        float time;
        // standard amount of time which always will be added to the net time
        float buffer = 15;
        // distance to studio in meter
        float distance = getDistanceToStudio();

        // speed in meter/minutes
        float speed = 1.0f;
        if (meansOfTransportation == MeansOfTransportation.AFOOT) {
            speed = 85.0f;
        } else {
            if (meansOfTransportation == MeansOfTransportation.BICYCLE) {
                speed = 250.0f;
            } else {
                if (meansOfTransportation == MeansOfTransportation.BUS) {
                    speed = 400.0f;
                } else {
                    if (meansOfTransportation == MeansOfTransportation.CAR) {
                        speed = 700.0f;
                    } else {
                        if (meansOfTransportation == MeansOfTransportation.TRAIN) {
                            speed = 1500.0f;
                        }
                    }
                }
            }
        }
        time = distance / speed;
        return time + buffer;
    }

    /**
     * checks if the time has come to remember the user to go to the studio
     * @return boolean, which is true if user has to be reminded and false if not
     */
    private boolean checkNecessityOfNotification() {

        // interval of time between checks in minute
        int periode = 10;

        // start of the training
        int trainingHour = 16;
        int trainingMinute = 30;
        int trainingMinuteOfDay = trainingHour * 60 + trainingMinute;

        // current time
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentMinuteOfDay = currentHour * 60 + currentMinute;

        // time needed to get to the studio
        int timeNeeded = (int) getTimeToStudio();

        if(trainingMinuteOfDay - currentMinuteOfDay > 0
                && trainingMinuteOfDay - currentMinuteOfDay - timeNeeded < periode) {
            return true;
        }else {
            return false;
        }
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
            System.out.println("case permission granted");
            try {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String best = locationManager.getBestProvider(criteria, false);
                if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    System.out.println("ATTENTION: permission denied");
                }else{
                    System.out.println("ATTENTION: permission granted");
                    locationManager.requestLocationUpdates(best, 0, 1, locationListener);
                }
            }catch(SecurityException e){
                e.printStackTrace();
            }
        } else {
            System.out.println("case permission denied");
        }
    }

    // TODO remove debug method
    public void debug() {
        meansOfTransportation = MeansOfTransportation.AFOOT;
        System.out.println(compareStudioPosition("rüsselsheim marktplatz"));
        if(userAddress != null) {
            System.out.println("user: " + userAddress.getAddressLine(0));
            System.out.println("distance: " + getDistanceToStudio());
            System.out.println("time: " + getTimeToStudio());
        } else {
            System.err.println("WARNING: USERADDRESS IS NULL");
        }
    }
}
