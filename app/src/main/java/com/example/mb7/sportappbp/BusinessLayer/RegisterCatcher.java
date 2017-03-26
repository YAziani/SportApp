package com.example.mb7.sportappbp.BusinessLayer;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_RegisteredUsers;
import com.firebase.client.DataSnapshot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *
 * catch registration data
 * Created by Aziani on 25.03.2017.
 */

public class RegisterCatcher {

    Thread t;
    LinkedList<String> oldRegisteredUsers = new LinkedList<>();

    public void catchRegistration(final ActivityMain activityMain) {
        DAL_RegisteredUsers.loadRegistration(activityMain,RegisterCatcher.this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 60 ; i++) {
                    DAL_RegisteredUsers.loadRegistration(activityMain,RegisterCatcher.this);
                    try {
                        Thread.sleep(10000);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();

    }

    /**
     * return texts of registered users
     * @param activity the main activity
     * @param dataSnapshot snapshot with users
     */
    public void returnRegistrations(ActivityMain activity, DataSnapshot dataSnapshot) {
        if(dataSnapshot == null) {
            return;
        }
        // check if old registrations saved
        if(oldRegisteredUsers.size() == 0) {
            oldRegisteredUsers = fillList(dataSnapshot);
        }else {
            LinkedList<String> difference = fillList(dataSnapshot);
            LinkedList<String> removeList = new LinkedList<>();
            // remove copies
            for (Iterator<String> iterator = difference.iterator(); iterator.hasNext(); ) {
                String d = iterator.next();
                for(String o : oldRegisteredUsers) {
                    if(d.equals(o)) {
                        removeList.add(d);
                    }
                }
            }
            difference.removeAll(removeList);
            if(difference.size() > 0) {
                DAL_RegisteredUsers.insertMail(difference.getFirst());
                ActivityMain.mainUser.setEmail(difference.getFirst());
                if(t!=null){
                    t.interrupt();
                    t = null;
                }

            }
        }
    }

    public LinkedList<String> fillList(DataSnapshot dataSnapshot) {
        LinkedList<String> list = new LinkedList<>();

        // fill list with registrations
        for(DataSnapshot d : dataSnapshot.getChildren()) {
            list.add((String)d.child("email").getValue());
        }
        return list;
    }

}
