package com.example.mb7.sportappbp.Observe;


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
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * observer for training reminder
 * Created by Aziani on 24.03.2017.
 */

public class ObserverStudioSetRemoveNotification extends Observer {

    DataSnapshot dataSnapshotChallenges;
    boolean validSnapshotChallenges;

    LinkedList<String> challenges;

    // objects describing the user and studio positions
    private static Address userAddress;
    private static Address studioAddress;
    private static Location userLocation;
    private static Location studioLocation;

    private short timeOutCounter = 0;

    public ObserverStudioSetRemoveNotification(Context context) {
        this.context = context;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location;
                if (getUserAddress(location) != null) {
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
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                System.out.println("ATTENTION: permission denied");
            } else {
                System.out.println("ATTENTION: permission granted");
                locationManager.requestLocationUpdates(best, 0, 1, locationListener);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Context context) {


        // check if method allocated
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.getString("allocatedMethods", "").contains("trainingreminder")) {
            return;
        }

        if (timeOutCounter > 0) {
            timeOutCounter--;
        } else {
            //if(!getNextTrainingTimeString(context).equals("")) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            // update studio location
            compareStudioPosition(preferences.getString("Studioadresse", ""));
            if (!(studioAddress == null) && !(userAddress == null)) {
                // if necessary, notify user
                float distanceStudio = getDistanceToStudio();

                if (distanceStudio < 50) {
                    addUserToNotifyTheOther();
                } else
                    removeUserFromNotification();
            }
        }
    }

    @Override
    public void createNotification(Context context, String NotificationDate, Class<?> cls, String title, String text,
                                   Integer icon) {

    }


    /**
     * determines the address object of the given location object
     *
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
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ATTENTION: user address could not be determined.");
        }

        // check if address is valid
        if (addresses != null && addresses.size() > 0 && addresses.get(0) != null) {
            tmpUserAddress = addresses.get(0);
        } else {
            System.out.println("ATTENTION: Address equals null.");
        }
        return tmpUserAddress;
    }

    /**
     * determines the address object of the given address and returns it for comparing
     *
     * @param givenPosition: the user entered name of the studio address
     * @return address object matching the user given address the most
     */
    private String compareStudioPosition(String givenPosition) {

        List<Address> determinedAddresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // determine the address matching the given address the most
        try {
            determinedAddresses = geocoder.getFromLocationName(givenPosition, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set the studio position to the determined address
        if (determinedAddresses != null && determinedAddresses.size() > 0 && determinedAddresses.get(0) != null) {
            studioAddress = determinedAddresses.get(0);
            studioLocation = new Location("studioLocation");
            studioLocation.setLatitude(studioAddress.getLatitude());
            studioLocation.setLongitude(studioAddress.getLongitude());
            return determinedAddresses.get(0).getAddressLine(0) + ", " + determinedAddresses.get(0).getLocality();
        } else {
            System.err.println("ATTENTION: studio address could not be determined");
            return null;
        }
    }

    /**
     * determines the distance between the user position and the studio postition
     *
     * @return distance in meter
     */
    private float getDistanceToStudio() {
        // 2 * squareroot(2) = 2.8284f
        // approximate distance compared to beeline about 125%
        float cityBlockFactor = 1.25f;

        // return the approximate distance between user and studio
        if (userLocation != null && studioLocation != null) {
            return userLocation.distanceTo(studioLocation) * cityBlockFactor;
        }
        return 0;
    }


    /**
     * This method adds the user from the notification list, that he is in the fitness studio
     */
    private void addUserToNotifyTheOther() {

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


                    for (DataSnapshot nameChild : dataSnapshot.getChildren()) {

                        for (DataSnapshot dateChild : nameChild.getChildren()) {
                            if (dateChild.getKey().equals("endDate")) {
                                try {
                                    //check if the challenge is still active
                                    Date endDate = null;
                                    endDate = sdf.parse(dateChild.getValue().toString());
                                    if (endDate.after(today)) {
                                        //add challenge to notify list
                                        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "Challenges/" +
                                                nameChild.getKey());

                                        Firebase studio = ref.child("AtStudio");

                                        Firebase nameChildStart = studio.child(ActivityMain.getMainUser(context)
                                                .getName());
                                        nameChildStart.setValue(ActivityMain.getMainUser(context).getName());
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
     * This method removes the user from the notification list, that he is in the fitness studio
     */
    private void removeUserFromNotification() {

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


                    for (DataSnapshot nameChild : dataSnapshot.getChildren()) {

                        for (DataSnapshot dateChild : nameChild.getChildren()) {
                            if (dateChild.getKey().equals("endDate")) {
                                try {
                                    //check if the challenge is still active
                                    Date endDate = null;
                                    endDate = sdf.parse(dateChild.getValue().toString());
                                    if (endDate.after(today)) {
                                        //add challenge to notify list
                                        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "Challenges/" +
                                                nameChild.getKey() + "/AtStudio");

                                        ref.child(ActivityMain.getMainUser(context).getName()).removeValue();
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


}

