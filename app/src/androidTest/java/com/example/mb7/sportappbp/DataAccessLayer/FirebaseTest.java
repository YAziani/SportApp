package com.example.mb7.sportappbp.DataAccessLayer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Aziani on 24.03.2017.
 */


@RunWith(AndroidJUnit4.class)
@MediumTest
public class FirebaseTest {

    // context of the test
    private Context instrumentationCtx;

    @Before
    // initializing method, run before every test
    public void init() {
        instrumentationCtx = InstrumentationRegistry.getContext();
        Firebase.setAndroidContext(instrumentationCtx);
        // wait for initializing of database
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Firebase cloud = new Firebase("https://sportapp-cbd6b.firebaseio.com/");
        // write values into database
        cloud.child("TestNode").child("testValue00").setValue("qwertz");
        cloud.child("TestNode").child("testValue01").setValue("qwerty");
        DAL_RegisteredUsers.insertRegistration("TestRegistration", "testPassword");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @Test
    public void testWrite(){

    }
     */

    @Test
    public void testRead00() {
        // define reading target
        Firebase root = new Firebase("https://sportapp-cbd6b.firebaseio.com/TestNode/testValue00");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    assertEquals("qwertz", dataSnapshot.getValue());
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

    @Test
    public void testRead01() {
        Firebase root = new Firebase("https://sportapp-cbd6b.firebaseio.com/TestNode/testValue01");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    assertEquals("qwerty", dataSnapshot.getValue());
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

    @Test
    public void testReadRegistration() {
        Firebase root = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean containsValue = false;
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        if (d.getKey().equals("TestRegistration")
                                && d.child("password").getValue() != null
                                && d.child("password").getValue().equals("testPassword")) {
                            containsValue = true;
                            break;
                        }
                    }
                    assertTrue(containsValue);
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

    @Test
    public void testAssert() {
        assertEquals(9, 3 * 3);
    }
}
