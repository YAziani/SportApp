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


                meansOfTransportation = MeansOfTransportation.AFOOT;
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


    // determines the address object of the given location object
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

    // returns the distance in meters between the user position and the studio postition
    private float getDistanceToStudio() {
        // 2 * squareroot(2)
        float cityBlockFactor = 2.8284f;
        return userLocation.distanceTo(studioLocation) * cityBlockFactor;
    }

    // determines the address object of the given address and returns it for comparing
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

    public float getTimeToStudio() {

        // time to studio in minutes
        float time;

        // standard amount of time which always will be added to the net time
        float buffer = 15;

        // distance to studio in meter
        float distance = getDistanceToStudio();

        // speed in meter/minutes
        float speed;
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
                        speed = 1500.0f;
                    }
                }
            }
        }

        time = distance / speed;
        return time + buffer;
    }

    // handles the result of the permission request sent to the user
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
