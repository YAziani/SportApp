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

    private static String mNextActiveGroup = null;

    @BeforeClass
    // initializing method, run before every test
    public static void init() {
        Context instrumentationCtx = InstrumentationRegistry.getContext();
        Firebase.setAndroidContext(instrumentationCtx);
        // wait for initializing of database
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writeDAL_RegisteredUsers();
        writeAlternatingGroupsUpdate();

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
        DAL_RegisteredUsers.insertMail("testMail@gmail.com", user);
    }

    /**
     * write data to change active group of the alternating group assignment
     */
    private static void writeAlternatingGroupsUpdate() {

        Firebase root = new Firebase(DAL_Utilities.DatabaseURL + "Administration/assignment/altern/altern1");
        // access data in database and hand it to MethodChooser
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot currentActiveGroup = null;
                    DataSnapshot nextActiveGroup = null;
                    DataSnapshot firstGroup = null;
                    boolean foundActiveGroup = false;

                    //find active group
                    for (DataSnapshot group : dataSnapshot.child("groups").getChildren()) {
                        // if found active group, save next active group
                        if (foundActiveGroup) {
                            nextActiveGroup = group;
                            break;
                        }
                        // save first group
                        if (firstGroup == null && group.getKey().substring(0, 5).equals("group")) {
                            firstGroup = group;
                        }
                        if (group.getKey().substring(0, 5).equals("group")
                                && group.child("groupactive").getValue() instanceof Boolean
                                && (boolean) group.child("groupactive").getValue()) {
                            currentActiveGroup = group;
                            foundActiveGroup = true;
                        }
                    }

                    // if active group is last one, next group is first group
                    if (foundActiveGroup && nextActiveGroup == null) {
                        nextActiveGroup = firstGroup;
                    }

                    // save groups for comparison
                    saveAlternatingGroups(nextActiveGroup.getKey());

                    // start update of database
                    DAL_User.insertAlternGroupUpdate(currentActiveGroup.getKey(),
                            nextActiveGroup.getKey(), "altern1");

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    assertTrue(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * save the alternating groups to compare them in the tests
     *
     * @param nextActiveGroup the next alternating group to be active
     */
    private static void saveAlternatingGroups(String nextActiveGroup) {
        mNextActiveGroup = nextActiveGroup;
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
                            assertEquals("testPassword", d.child("password").getValue());
                            assertEquals("testMail@gmail.com", d.child("email").getValue());
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

    @Test
    public void testAlternatingGroupsUpdate() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL + "Administration/assignment/altern/altern1/groups");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        if (d.getKey().equals(mNextActiveGroup)) {
                            assertEquals(true, d.child("groupactive").getValue());
                        } else {
                            assertEquals(false, d.child("groupactive").getValue());
                        }
                    }
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
