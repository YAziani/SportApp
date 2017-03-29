package com.example.mb7.sportappbp.DataAccessLayer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * database operation tests
 * Created by Aziani on 24.03.2017.
 */


@RunWith(AndroidJUnit4.class)
@MediumTest
public class FirebaseTest {

    // context of the test
    private static Context instrumentationCtx;

    @BeforeClass
    // initializing method, run before every test
    public static void init() {
        instrumentationCtx = InstrumentationRegistry.getContext();
        Firebase.setAndroidContext(instrumentationCtx);
        // wait for initializing of database
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writeDAL_RegisteredUsers();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * write data through DAL_RegisteredUsers
     */
    private static void writeDAL_RegisteredUsers() {
        DAL_RegisteredUsers.insertRegistration("TestRegistration", "testPassword");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = User.Create("TestRegistration");
        DAL_RegisteredUsers.insertMail("testMail@gmail.com",user);
    }

    @Test
    public void testReadRegistration() {
        Firebase root = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean containsRegistration = false;
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        if (d.getKey().equals("TestRegistration")) {
                            containsRegistration = true;
                            assertEquals("testPassword",d.child("password").getValue());
                            assertEquals("testMail@gmail.com",d.child("email").getValue());
                        }
                    }
                    assertTrue(containsRegistration);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    assertTrue(false);
                }
            });
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
