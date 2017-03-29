package com.example.mb7.sportappbp.DataAccessLayer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

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
    public static void init() throws Exception{
        Context instrumentationCtx = InstrumentationRegistry.getContext();
        Firebase.setAndroidContext(instrumentationCtx);
        // wait for initializing of database
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // write data into database
        writeDAL_RegisteredUsers();
        writeAlternatingGroupsUpdate();
        writeFitnessFragebogen();


        try {
            Thread.sleep(3000);
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
    }

    /**
     * write data for fitness questineers
     */
    private static void writeFitnessFragebogen() {

        DAL_User.InsertFitnessFragebogen(User.Create("TestUserFelix"),testfitness1());
        DAL_User.InsertFitnessFragebogen(User.Create("TestUserFelix"),testfitness2());
        DAL_User.InsertFitnessFragebogen(User.Create("TestUserFelix"),testfitness3());

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
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL + "users");
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
            Thread.sleep(100);
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

    @Test
    public void testFitness1() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL + "users/TestUserFelix/FitnessFragebogen/"+
                testfitness1().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(testfitness1().Score_Ausdauer, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Score_Ausdauer);
                        assertEquals(testfitness1().Score_Koordination, dataSnapshot.getValue(FitnessFragebogen
                                .class).Score_Koordination);
                        assertEquals(testfitness1().Score_Beweglichkeit, dataSnapshot.getValue(FitnessFragebogen
                                .class).Score_Beweglichkeit);
                        assertEquals(testfitness1().Score_Gesamt, dataSnapshot.getValue(FitnessFragebogen.class)
                         .Score_Gesamt);
                        assertEquals(testfitness1().vom_Stuhl_aufstehen, dataSnapshot.getValue(FitnessFragebogen
                                .class).vom_Stuhl_aufstehen);
                        assertEquals(testfitness1().Einkaufskorb_tragen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Einkaufskorb_tragen);
                        assertEquals(testfitness1().Kiste_tragen, dataSnapshot.getValue(FitnessFragebogen.class).Kiste_tragen);
                        assertEquals(testfitness1().Situp, dataSnapshot.getValue(FitnessFragebogen.class).Situp);
                        assertEquals(testfitness1().Koffer_hoch_heben, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Koffer_hoch_heben);
                        assertEquals(testfitness1().Koffer_tragen, dataSnapshot.getValue(FitnessFragebogen.class).Koffer_tragen);
                        assertEquals(testfitness1().Hantel_stemmen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Hantel_stemmen);
                        assertEquals(testfitness1().flott_gehen, dataSnapshot.getValue(FitnessFragebogen.class).flott_gehen);
                        assertEquals(testfitness1().Treppen_gehen, dataSnapshot.getValue(FitnessFragebogen.class).Treppen_gehen);
                        assertEquals(testfitness1().Zwei_km_gehen, dataSnapshot.getValue(FitnessFragebogen.class).Zwei_km_gehen);
                        assertEquals(testfitness1().Ein_km_joggen, dataSnapshot.getValue(FitnessFragebogen.class).Ein_km_joggen);
                        assertEquals(testfitness1().Dreißig_min_joggen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Dreißig_min_joggen);
                        assertEquals(testfitness1().Sechzig_min_joggen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Sechzig_min_joggen);
                        assertEquals(testfitness1().Marathon, dataSnapshot.getValue(FitnessFragebogen.class).Marathon);
                        assertEquals(testfitness1().Socken_anziehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Socken_anziehen);
                        assertEquals(testfitness1().Boden_im_Sitzen_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Boden_im_Sitzen_beruehren);
                        assertEquals(testfitness1().Schuhe_binden, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Schuhe_binden);
                        assertEquals(testfitness1().Ruecken_beruehren, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ruecken_beruehren);
                        assertEquals(testfitness1().Im_Stehen_Boden_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Im_Stehen_Boden_beruehren);
                        assertEquals(testfitness1().Mit_Kopf_das_Knie_beruehren, dataSnapshot.getValue(FitnessFragebogen.class).Mit_Kopf_das_Knie_beruehren);
                        assertEquals(testfitness1().Bruecke, dataSnapshot.getValue(FitnessFragebogen.class).Bruecke);
                        assertEquals(testfitness1().Treppe_runter_gehen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Treppe_runter_gehen);
                        assertEquals(testfitness1().Einbeinstand, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Einbeinstand);
                        assertEquals(testfitness1().Purzelbaum, dataSnapshot.getValue(FitnessFragebogen.class).Purzelbaum);
                        assertEquals(testfitness1().Ball_prellen, dataSnapshot.getValue(FitnessFragebogen.class).Ball_prellen);
                        assertEquals(testfitness1().Zaunsprung, dataSnapshot.getValue(FitnessFragebogen.class).Zaunsprung);
                        assertEquals(testfitness1().Kurve_fahren_ohne_Hand, dataSnapshot.getValue(FitnessFragebogen
                                .class).Kurve_fahren_ohne_Hand);
                        assertEquals(testfitness1().Rad_schlagen, dataSnapshot.getValue(FitnessFragebogen.class)
                        .Rad_schlagen);



                    } catch (Exception e) {
                        e.printStackTrace();
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

    @Test
    public void testFitness2() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL + "users/TestUserFelix/FitnessFragebogen/"+ testfitness2().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(testfitness2().Score_Ausdauer, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Score_Ausdauer);
                        assertEquals(testfitness2().Score_Koordination, dataSnapshot.getValue(FitnessFragebogen
                                .class).Score_Koordination);
                        assertEquals(testfitness2().Score_Beweglichkeit, dataSnapshot.getValue(FitnessFragebogen
                                .class).Score_Beweglichkeit);
                        assertEquals(testfitness2().Score_Gesamt, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Score_Gesamt);
                        assertEquals(testfitness2().vom_Stuhl_aufstehen, dataSnapshot.getValue(FitnessFragebogen
                                .class).vom_Stuhl_aufstehen);
                        assertEquals(testfitness2().Einkaufskorb_tragen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Einkaufskorb_tragen);
                        assertEquals(testfitness2().Kiste_tragen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Kiste_tragen);
                        assertEquals(testfitness2().Situp, dataSnapshot.getValue(FitnessFragebogen.class).Situp);
                        assertEquals(testfitness2().Koffer_hoch_heben, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Koffer_hoch_heben);
                        assertEquals(testfitness2().Koffer_tragen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Koffer_tragen);
                        assertEquals(testfitness2().Hantel_stemmen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Hantel_stemmen);
                        assertEquals(testfitness2().flott_gehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .flott_gehen);
                        assertEquals(testfitness2().Treppen_gehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Treppen_gehen);
                        assertEquals(testfitness2().Zwei_km_gehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Zwei_km_gehen);
                        assertEquals(testfitness2().Ein_km_joggen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ein_km_joggen);
                        assertEquals(testfitness2().Dreißig_min_joggen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Dreißig_min_joggen);
                        assertEquals(testfitness2().Sechzig_min_joggen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Sechzig_min_joggen);
                        assertEquals(testfitness2().Marathon, dataSnapshot.getValue(FitnessFragebogen.class).Marathon);
                        assertEquals(testfitness2().Socken_anziehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Socken_anziehen);
                        assertEquals(testfitness2().Boden_im_Sitzen_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Boden_im_Sitzen_beruehren);
                        assertEquals(testfitness2().Schuhe_binden, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Schuhe_binden);
                        assertEquals(testfitness2().Ruecken_beruehren, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ruecken_beruehren);
                        assertEquals(testfitness2().Im_Stehen_Boden_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Im_Stehen_Boden_beruehren);
                        assertEquals(testfitness2().Mit_Kopf_das_Knie_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Mit_Kopf_das_Knie_beruehren);
                        assertEquals(testfitness2().Bruecke, dataSnapshot.getValue(FitnessFragebogen.class).Bruecke);
                        assertEquals(testfitness2().Treppe_runter_gehen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Treppe_runter_gehen);
                        assertEquals(testfitness2().Einbeinstand, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Einbeinstand);
                        assertEquals(testfitness2().Purzelbaum, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Purzelbaum);
                        assertEquals(testfitness2().Ball_prellen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ball_prellen);
                        assertEquals(testfitness2().Zaunsprung, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Zaunsprung);
                        assertEquals(testfitness2().Kurve_fahren_ohne_Hand, dataSnapshot.getValue(FitnessFragebogen
                                .class).Kurve_fahren_ohne_Hand);
                        assertEquals(testfitness2().Rad_schlagen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Rad_schlagen);



                    } catch (Exception e) {
                        e.printStackTrace();
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

    @Test
    public void testFitness3() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/FitnessFragebogen/"+
                testfitness3().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(testfitness3().Score_Ausdauer, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Score_Ausdauer);
                        assertEquals(testfitness3().Score_Koordination, dataSnapshot.getValue(FitnessFragebogen
                                .class).Score_Koordination);
                        assertEquals(testfitness3().Score_Beweglichkeit, dataSnapshot.getValue(FitnessFragebogen
                                .class).Score_Beweglichkeit);
                        assertEquals(testfitness3().Score_Gesamt, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Score_Gesamt);
                        assertEquals(testfitness3().vom_Stuhl_aufstehen, dataSnapshot.getValue(FitnessFragebogen
                                .class).vom_Stuhl_aufstehen);
                        assertEquals(testfitness3().Einkaufskorb_tragen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Einkaufskorb_tragen);
                        assertEquals(testfitness3().Kiste_tragen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Kiste_tragen);
                        assertEquals(testfitness3().Situp, dataSnapshot.getValue(FitnessFragebogen.class).Situp);
                        assertEquals(testfitness3().Koffer_hoch_heben, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Koffer_hoch_heben);
                        assertEquals(testfitness3().Koffer_tragen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Koffer_tragen);
                        assertEquals(testfitness3().Hantel_stemmen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Hantel_stemmen);
                        assertEquals(testfitness3().flott_gehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .flott_gehen);
                        assertEquals(testfitness3().Treppen_gehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Treppen_gehen);
                        assertEquals(testfitness3().Zwei_km_gehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Zwei_km_gehen);
                        assertEquals(testfitness3().Ein_km_joggen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ein_km_joggen);
                        assertEquals(testfitness3().Dreißig_min_joggen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Dreißig_min_joggen);
                        assertEquals(testfitness3().Sechzig_min_joggen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Sechzig_min_joggen);
                        assertEquals(testfitness3().Marathon, dataSnapshot.getValue(FitnessFragebogen.class).Marathon);
                        assertEquals(testfitness3().Socken_anziehen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Socken_anziehen);
                        assertEquals(testfitness3().Boden_im_Sitzen_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Boden_im_Sitzen_beruehren);
                        assertEquals(testfitness3().Schuhe_binden, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Schuhe_binden);
                        assertEquals(testfitness3().Ruecken_beruehren, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ruecken_beruehren);
                        assertEquals(testfitness3().Im_Stehen_Boden_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Im_Stehen_Boden_beruehren);
                        assertEquals(testfitness3().Mit_Kopf_das_Knie_beruehren, dataSnapshot.getValue
                                (FitnessFragebogen.class).Mit_Kopf_das_Knie_beruehren);
                        assertEquals(testfitness3().Bruecke, dataSnapshot.getValue(FitnessFragebogen.class).Bruecke);
                        assertEquals(testfitness3().Treppe_runter_gehen, dataSnapshot.getValue(FitnessFragebogen
                                .class).Treppe_runter_gehen);
                        assertEquals(testfitness3().Einbeinstand, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Einbeinstand);
                        assertEquals(testfitness3().Purzelbaum, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Purzelbaum);
                        assertEquals(testfitness3().Ball_prellen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Ball_prellen);
                        assertEquals(testfitness3().Zaunsprung, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Zaunsprung);
                        assertEquals(testfitness3().Kurve_fahren_ohne_Hand, dataSnapshot.getValue(FitnessFragebogen
                                .class).Kurve_fahren_ohne_Hand);
                        assertEquals(testfitness3().Rad_schlagen, dataSnapshot.getValue(FitnessFragebogen.class)
                                .Rad_schlagen);



                    } catch (Exception e) {
                        e.printStackTrace();
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


    public static FitnessFragebogen testfitness1(){
        FitnessFragebogen testfitness=new FitnessFragebogen();
        testfitness.Date="20000101";
        testfitness.FirebaseDate="20000101";
        testfitness.Score_Kraft=0;
        testfitness.Score_Ausdauer=0;
        testfitness.Score_Koordination=0;
        testfitness.Score_Beweglichkeit=0;
        testfitness.Score_Gesamt=0;

        testfitness.vom_Stuhl_aufstehen=0;
        testfitness.Einkaufskorb_tragen=0;
        testfitness.Kiste_tragen=0;
        testfitness.Situp=0;
        testfitness.Koffer_hoch_heben=0;
        testfitness.Koffer_tragen=0;
        testfitness.Hantel_stemmen=0;

        testfitness.flott_gehen=0;
        testfitness. Treppen_gehen=0;
        testfitness. Zwei_km_gehen=0;
        testfitness. Ein_km_joggen=0;
        testfitness. Dreißig_min_joggen=0;
        testfitness. Sechzig_min_joggen=0;
        testfitness. Marathon=0;

        testfitness. Socken_anziehen=0;
        testfitness.Boden_im_Sitzen_beruehren=0;
        testfitness. Schuhe_binden=0;
        testfitness. Ruecken_beruehren=0;
        testfitness. Im_Stehen_Boden_beruehren=0;
        testfitness. Mit_Kopf_das_Knie_beruehren=0;
        testfitness. Bruecke=0;

        testfitness. Treppe_runter_gehen=0;
        testfitness. Einbeinstand=0;
        testfitness. Purzelbaum=0;
        testfitness. Ball_prellen=0;
        testfitness.Zaunsprung=0;
        testfitness. Kurve_fahren_ohne_Hand=0;
        testfitness. Rad_schlagen=0;
        return testfitness;
    }

    public static FitnessFragebogen testfitness2(){
        FitnessFragebogen testfitness=new FitnessFragebogen();
        testfitness.Date="20170101";
        testfitness.FirebaseDate="20170101";
        testfitness.Score_Kraft=50;
        testfitness.Score_Ausdauer=20;
        testfitness.Score_Koordination=20;
        testfitness.Score_Beweglichkeit=10;
        testfitness.Score_Gesamt=100;

        testfitness.vom_Stuhl_aufstehen=3;
        testfitness.Einkaufskorb_tragen=2;
        testfitness.Kiste_tragen=3;
        testfitness.Situp=2;
        testfitness.Koffer_hoch_heben=3;
        testfitness.Koffer_tragen=4;
        testfitness.Hantel_stemmen=0;

        testfitness.flott_gehen=0;
        testfitness. Treppen_gehen=0;
        testfitness. Zwei_km_gehen=0;
        testfitness. Ein_km_joggen=0;
        testfitness. Dreißig_min_joggen=0;
        testfitness. Sechzig_min_joggen=2;
        testfitness. Marathon=4;

        testfitness. Socken_anziehen=0;
        testfitness.Boden_im_Sitzen_beruehren=3;
        testfitness. Schuhe_binden=0;
        testfitness. Ruecken_beruehren=0;
        testfitness. Im_Stehen_Boden_beruehren=2;
        testfitness. Mit_Kopf_das_Knie_beruehren=0;
        testfitness. Bruecke=4;

        testfitness. Treppe_runter_gehen=2;
        testfitness. Einbeinstand=3;
        testfitness. Purzelbaum=0;
        testfitness. Ball_prellen=4;
        testfitness.Zaunsprung=0;
        testfitness. Kurve_fahren_ohne_Hand=3;
        testfitness. Rad_schlagen=0;
        return testfitness;
    }

    public static FitnessFragebogen testfitness3(){
        FitnessFragebogen testfitness=new FitnessFragebogen();
        testfitness.Date="20170201";
        testfitness.FirebaseDate="20170201";
        testfitness.Score_Kraft=9999;
        testfitness.Score_Ausdauer=9999;
        testfitness.Score_Koordination=9999;
        testfitness.Score_Beweglichkeit=9999;
        testfitness.Score_Gesamt=5;

        testfitness.vom_Stuhl_aufstehen=31;
        testfitness.Einkaufskorb_tragen=45;
        testfitness.Kiste_tragen=0;
        testfitness.Situp=79;
        testfitness.Koffer_hoch_heben=6;
        testfitness.Koffer_tragen=32;
        testfitness.Hantel_stemmen=4;

        testfitness.flott_gehen=33;
        testfitness. Treppen_gehen=46;
        testfitness. Zwei_km_gehen=33;
        testfitness. Ein_km_joggen=21;
        testfitness. Dreißig_min_joggen=24;
        testfitness. Sechzig_min_joggen=21;
        testfitness. Marathon=11;

        testfitness. Socken_anziehen=3;
        testfitness.Boden_im_Sitzen_beruehren=2;
        testfitness. Schuhe_binden=1;
        testfitness. Ruecken_beruehren=44;
        testfitness. Im_Stehen_Boden_beruehren=0;
        testfitness. Mit_Kopf_das_Knie_beruehren=3;
        testfitness. Bruecke=9;

        testfitness. Treppe_runter_gehen=4;
        testfitness. Einbeinstand=22;
        testfitness. Purzelbaum=7;
        testfitness. Ball_prellen=6;
        testfitness.Zaunsprung=71;
        testfitness. Kurve_fahren_ohne_Hand=3;
        testfitness. Rad_schlagen=0;
        return testfitness;
    }


}