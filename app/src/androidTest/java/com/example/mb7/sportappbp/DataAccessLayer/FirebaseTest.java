package com.example.mb7.sportappbp.DataAccessLayer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrageScore;
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
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writeDAL_RegisteredUsers();
        writeAlternatingGroupsUpdate();
        writeFitnessFragebogen();
        writeBSAFragebogen();
        writeStimmung();

        
        try {
            Thread.sleep(2000);
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
     * write data for fitness questineers
     */
    private static void writeFitnessFragebogen() {

        DAL_User.InsertFitnessFragebogen(User.Create("TestUserFelix"),testfitness1());
        DAL_User.InsertFitnessFragebogen(User.Create("TestUserFelix"),testfitness2());
        DAL_User.InsertFitnessFragebogen(User.Create("TestUserFelix"),testfitness3());

        DAL_User.UpdateFitnessFragebogen(User.Create("TestUserFelix"),testfitness1());
        DAL_User.UpdateFitnessFragebogen(User.Create("TestUserFelix"),testfitness2());
        DAL_User.UpdateFitnessFragebogen(User.Create("TestUserFelix"),testfitness3());



        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeBSAFragebogen() {

        DAL_User.InsertFragebogen(User.Create("TestUserFelix"),testbsa1());
        DAL_User.InsertFragebogen(User.Create("TestUserFelix"),testbsa2());
        DAL_User.InsertFragebogen(User.Create("TestUserFelix"),testbsa3());
        DAL_User.InsertFragebogen(User.Create("TestUserFelix"),testbsa3());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void writeStimmung() {

        DAL_User.InsertStimmung(User.Create("TestUserFelix"),teststimmung1());
        DAL_User.InsertStimmungScore(User.Create("TestUserFelix"),teststimmungscore1() );
        DAL_User.InsertStimmung(User.Create("TestUserFelix"),teststimmung2());
        DAL_User.InsertStimmung(User.Create("TestUserFelix"),teststimmung3());
        DAL_User.InsertStimmung(User.Create("TestUserFelix"),teststimmung4());


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
    public String VorNach(StimmungsAngabe teststimmung){
        if (teststimmung.Vor)
            return "V";
        else
            return "/N";
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

    @Test
    public void testBSA1() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/BSAFragebogen/"+
                testbsa1().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(testbsa1().Berufstaetig, dataSnapshot.getValue(Fragebogen.class).Berufstaetig);
                        assertEquals(testbsa1().sitzende_Taetigkeiten, dataSnapshot.getValue(Fragebogen.class).sitzende_Taetigkeiten);
                        assertEquals(testbsa1().maeßige_Bewegung, dataSnapshot.getValue(Fragebogen.class).maeßige_Bewegung);
                        assertEquals(testbsa1().intensive_Bewegung, dataSnapshot.getValue(Fragebogen.class).intensive_Bewegung);
                        assertEquals(testbsa1().sportlich_aktiv, dataSnapshot.getValue(Fragebogen.class).sportlich_aktiv);
                        assertEquals(testbsa1().Zu_Fuß_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_zur_Arbeit);
                        assertEquals(testbsa1().Zu_Fuß_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_zur_Arbeit_Tag);
                        assertEquals(testbsa1().Zu_Fuß_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_zur_Arbeit_Minuten);
                        assertEquals(testbsa1().Zu_Fuß_einkaufen, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_einkaufen);
                        assertEquals(testbsa1().Zu_Fuß_einkaufen_Tag, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_einkaufen_Tag);
                        assertEquals(testbsa1().Zu_Fuß_einkaufen_Minuten, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_einkaufen_Minuten);
                        assertEquals(testbsa1().Rad_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit);
                        assertEquals(testbsa1().Rad_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit_Tag);
                        assertEquals(testbsa1().Rad_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit_Minuten);
                        assertEquals(testbsa1().Radfahren, dataSnapshot.getValue(Fragebogen.class).Radfahren);
                        assertEquals(testbsa1().Radfahren_Tag, dataSnapshot.getValue(Fragebogen.class).Radfahren_Tag);
                        assertEquals(testbsa1().Radfahren_Minuten, dataSnapshot.getValue(Fragebogen.class).Radfahren_Minuten);
                        assertEquals(testbsa1().Spazieren, dataSnapshot.getValue(Fragebogen.class).Spazieren);
                        assertEquals(testbsa1().Spazieren_Tag, dataSnapshot.getValue(Fragebogen.class).Spazieren_Tag);
                        assertEquals(testbsa1().Spazieren_Minuten, dataSnapshot.getValue(Fragebogen.class).Spazieren_Minuten);
                        assertEquals(testbsa1().Gartenarbeit, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit);
                        assertEquals(testbsa1().Gartenarbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit_Tag);
                        assertEquals(testbsa1().Gartenarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit_Minuten);
                        assertEquals(testbsa1().Hausarbeit, dataSnapshot.getValue(Fragebogen.class).Hausarbeit);
                        assertEquals(testbsa1().Hausarbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Hausarbeit_Tag);
                        assertEquals(testbsa1().Hausarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Hausarbeit_Minuten);
                        assertEquals(testbsa1().Pflegearbeit, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit);
                        assertEquals(testbsa1().Pflegearbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit_Tag);
                        assertEquals(testbsa1().Pflegearbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit_Minuten);
                        assertEquals(testbsa1().Treppensteigen, dataSnapshot.getValue(Fragebogen.class).Treppensteigen);
                        assertEquals(testbsa1().Treppensteigen_Tag, dataSnapshot.getValue(Fragebogen.class).Treppensteigen_Tag);
                        assertEquals(testbsa1().Treppensteigen_Stockwerke, dataSnapshot.getValue(Fragebogen.class).Treppensteigen_Stockwerke);
                        assertEquals(testbsa1().Aktivitaet_A_Name, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Name);
                        assertEquals(testbsa1().Aktivitaet_A_Zeit, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Zeit);
                        assertEquals(testbsa1().Aktivitaet_A_Einheiten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Einheiten);
                        assertEquals(testbsa1().Aktivitaet_A_Minuten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Minuten);
                        assertEquals(testbsa1().Aktivitaet_B_Name, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Name);
                        assertEquals(testbsa1().Aktivitaet_B_Zeit, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Zeit);
                        assertEquals(testbsa1().Aktivitaet_B_Einheiten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Einheiten);
                        assertEquals(testbsa1().Aktivitaet_B_Minuten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Minuten);
                        assertEquals(testbsa1().Aktivitaet_C_Name, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Name);
                        assertEquals(testbsa1().Aktivitaet_C_Zeit, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Zeit);
                        assertEquals(testbsa1().Aktivitaet_C_Einheiten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Einheiten);
                        assertEquals(testbsa1().Aktivitaet_C_Minuten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Minuten);
                        assertEquals(testbsa1().Bewegungsscoring, dataSnapshot.getValue(Fragebogen.class).Bewegungsscoring);
                        assertEquals(testbsa1().Sportscoring, dataSnapshot.getValue(Fragebogen.class).Sportscoring);
                        assertEquals(testbsa1().Gesamtscoring, dataSnapshot.getValue(Fragebogen.class).Gesamtscoring);
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
    public void testBSA2() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/BSAFragebogen/"+
                testbsa2().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    // define behavior once data had been captured
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        // assert equality of captured data
                                                        try {
                                                            assertEquals(testbsa2().Berufstaetig, dataSnapshot.getValue(Fragebogen.class).Berufstaetig);
                                                            assertEquals(null, dataSnapshot.getValue(Fragebogen.class).sitzende_Taetigkeiten);
                                                            assertEquals(null, dataSnapshot.getValue(Fragebogen.class).maeßige_Bewegung);
                                                            assertEquals(null, dataSnapshot.getValue(Fragebogen.class).intensive_Bewegung);
                                                            assertEquals(testbsa2().sportlich_aktiv, dataSnapshot.getValue(Fragebogen.class).sportlich_aktiv);
                                                            assertEquals(testbsa2().Zu_Fuß_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_zur_Arbeit);
                                                            assertEquals(testbsa2().Zu_Fuß_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_zur_Arbeit_Tag);
                                                            assertEquals(testbsa2().Zu_Fuß_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_zur_Arbeit_Minuten);
                                                            assertEquals(testbsa2().Zu_Fuß_einkaufen, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_einkaufen);
                                                            assertEquals(testbsa2().Zu_Fuß_einkaufen_Tag, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_einkaufen_Tag);
                                                            assertEquals(testbsa2().Zu_Fuß_einkaufen_Minuten, dataSnapshot.getValue(Fragebogen.class).Zu_Fuß_einkaufen_Minuten);
                                                            assertEquals(testbsa2().Rad_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit);
                                                            assertEquals(testbsa2().Rad_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit_Tag);
                                                            assertEquals(testbsa2().Rad_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit_Minuten);
                                                            assertEquals(testbsa2().Radfahren, dataSnapshot.getValue(Fragebogen.class).Radfahren);
                                                            assertEquals(testbsa2().Radfahren_Tag, dataSnapshot.getValue(Fragebogen.class).Radfahren_Tag);
                                                            assertEquals(testbsa2().Radfahren_Minuten, dataSnapshot.getValue(Fragebogen.class).Radfahren_Minuten);
                                                            assertEquals(testbsa2().Spazieren, dataSnapshot.getValue(Fragebogen.class).Spazieren);
                                                            assertEquals(testbsa2().Spazieren_Tag, dataSnapshot.getValue(Fragebogen.class).Spazieren_Tag);
                                                            assertEquals(testbsa2().Spazieren_Minuten, dataSnapshot.getValue(Fragebogen.class).Spazieren_Minuten);
                                                            assertEquals(testbsa2().Gartenarbeit, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit);
                                                            assertEquals(testbsa2().Gartenarbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit_Tag);
                                                            assertEquals(testbsa2().Gartenarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit_Minuten);
                                                            assertEquals(testbsa2().Hausarbeit, dataSnapshot.getValue(Fragebogen.class).Hausarbeit);
                                                            assertEquals(testbsa2().Hausarbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Hausarbeit_Tag);
                                                            assertEquals(testbsa2().Hausarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Hausarbeit_Minuten);
                                                            assertEquals(testbsa2().Pflegearbeit, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit);
                                                            assertEquals(testbsa2().Pflegearbeit_Tag, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit_Tag);
                                                            assertEquals(testbsa2().Pflegearbeit_Minuten, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit_Minuten);
                                                            assertEquals(testbsa2().Treppensteigen, dataSnapshot.getValue(Fragebogen.class).Treppensteigen);
                                                            assertEquals(testbsa2().Treppensteigen_Tag, dataSnapshot.getValue(Fragebogen.class).Treppensteigen_Tag);
                                                            assertEquals(testbsa2().Treppensteigen_Stockwerke, dataSnapshot.getValue(Fragebogen.class).Treppensteigen_Stockwerke);
                                                            assertEquals(testbsa2().Aktivitaet_A_Name, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Name);
                                                            assertEquals(testbsa2().Aktivitaet_A_Zeit, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Zeit);
                                                            assertEquals(testbsa2().Aktivitaet_A_Einheiten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Einheiten);
                                                            assertEquals(testbsa2().Aktivitaet_A_Minuten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Minuten);
                                                            assertEquals(testbsa2().Aktivitaet_B_Name, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Name);
                                                            assertEquals(testbsa2().Aktivitaet_B_Zeit, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Zeit);
                                                            assertEquals(testbsa2().Aktivitaet_B_Einheiten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Einheiten);
                                                            assertEquals(testbsa2().Aktivitaet_B_Minuten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Minuten);
                                                            assertEquals(testbsa2().Aktivitaet_C_Name, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Name);
                                                            assertEquals(testbsa2().Aktivitaet_C_Zeit, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Zeit);
                                                            assertEquals(testbsa2().Aktivitaet_C_Einheiten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Einheiten);
                                                            assertEquals(testbsa2().Aktivitaet_C_Minuten, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Minuten);
                                                            assertEquals(testbsa2().Bewegungsscoring, dataSnapshot.getValue(Fragebogen.class).Bewegungsscoring);
                                                            assertEquals(testbsa2().Sportscoring, dataSnapshot.getValue(Fragebogen.class).Sportscoring);
                                                            assertEquals(testbsa2().Gesamtscoring, dataSnapshot.getValue(Fragebogen.class).Gesamtscoring);

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
    public void testBSA3() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/BSAFragebogen/"+
                testbsa3().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(testbsa3().Berufstaetig, dataSnapshot.getValue(Fragebogen.class).Berufstaetig);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).sitzende_Taetigkeiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).maeßige_Bewegung);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).intensive_Bewegung);
                        assertEquals(testbsa3().sportlich_aktiv, dataSnapshot.getValue(Fragebogen.class)
                                .sportlich_aktiv);
                        assertEquals(testbsa3().Zu_Fuß_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_zur_Arbeit);
                        assertEquals(testbsa3().Zu_Fuß_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_zur_Arbeit_Tag);
                        assertEquals(testbsa3().Zu_Fuß_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_zur_Arbeit_Minuten);
                        assertEquals(testbsa3().Zu_Fuß_einkaufen, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_einkaufen);
                        assertEquals(testbsa3().Zu_Fuß_einkaufen_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_einkaufen_Tag);
                        assertEquals(testbsa3().Zu_Fuß_einkaufen_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_einkaufen_Minuten);
                        assertEquals(testbsa3().Rad_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit);
                        assertEquals(testbsa3().Rad_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Rad_zur_Arbeit_Tag);
                        assertEquals(testbsa3().Rad_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Rad_zur_Arbeit_Minuten);
                        assertEquals(testbsa3().Radfahren, dataSnapshot.getValue(Fragebogen.class).Radfahren);
                        assertEquals(testbsa3().Radfahren_Tag, dataSnapshot.getValue(Fragebogen.class).Radfahren_Tag);
                        assertEquals(testbsa3().Radfahren_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Radfahren_Minuten);
                        assertEquals(testbsa3().Spazieren, dataSnapshot.getValue(Fragebogen.class).Spazieren);
                        assertEquals(testbsa3().Spazieren_Tag, dataSnapshot.getValue(Fragebogen.class).Spazieren_Tag);
                        assertEquals(testbsa3().Spazieren_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Spazieren_Minuten);
                        assertEquals(testbsa3().Gartenarbeit, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit);
                        assertEquals(testbsa3().Gartenarbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Gartenarbeit_Tag);
                        assertEquals(testbsa3().Gartenarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Gartenarbeit_Minuten);
                        assertEquals(testbsa3().Hausarbeit, dataSnapshot.getValue(Fragebogen.class).Hausarbeit);
                        assertEquals(testbsa3().Hausarbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Hausarbeit_Tag);
                        assertEquals(testbsa3().Hausarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Hausarbeit_Minuten);
                        assertEquals(testbsa3().Pflegearbeit, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit);
                        assertEquals(testbsa3().Pflegearbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Pflegearbeit_Tag);
                        assertEquals(testbsa3().Pflegearbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Pflegearbeit_Minuten);
                        assertEquals(testbsa3().Treppensteigen, dataSnapshot.getValue(Fragebogen.class).Treppensteigen);
                        assertEquals(testbsa3().Treppensteigen_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Treppensteigen_Tag);
                        assertEquals(testbsa3().Treppensteigen_Stockwerke, dataSnapshot.getValue(Fragebogen.class)
                                .Treppensteigen_Stockwerke);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Name);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Zeit);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Einheiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Minuten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Name);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Zeit);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Einheiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Minuten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Name);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Zeit);
                        assertEquals(null,dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Einheiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Minuten);
                        assertEquals(testbsa3().Bewegungsscoring, dataSnapshot.getValue(Fragebogen.class).Bewegungsscoring);
                        assertEquals(testbsa3().Sportscoring, dataSnapshot.getValue(Fragebogen.class).Sportscoring);
                        assertEquals(testbsa3().Gesamtscoring, dataSnapshot.getValue(Fragebogen.class).Gesamtscoring);

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
    public void testBSA4() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/BSAFragebogen/"+
                testbsa4().Date);
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Berufstaetig);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).sitzende_Taetigkeiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).maeßige_Bewegung);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).intensive_Bewegung);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).sportlich_aktiv);
                        assertEquals(testbsa4().Zu_Fuß_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_zur_Arbeit);
                        assertEquals(testbsa4().Zu_Fuß_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_zur_Arbeit_Tag);
                        assertEquals(testbsa4().Zu_Fuß_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_zur_Arbeit_Minuten);
                        assertEquals(testbsa4().Zu_Fuß_einkaufen, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_einkaufen);
                        assertEquals(testbsa4().Zu_Fuß_einkaufen_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_einkaufen_Tag);
                        assertEquals(testbsa4().Zu_Fuß_einkaufen_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Zu_Fuß_einkaufen_Minuten);
                        assertEquals(testbsa4().Rad_zur_Arbeit, dataSnapshot.getValue(Fragebogen.class).Rad_zur_Arbeit);
                        assertEquals(testbsa4().Rad_zur_Arbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Rad_zur_Arbeit_Tag);
                        assertEquals(testbsa4().Rad_zur_Arbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Rad_zur_Arbeit_Minuten);
                        assertEquals(testbsa4().Radfahren, dataSnapshot.getValue(Fragebogen.class).Radfahren);
                        assertEquals(testbsa4().Radfahren_Tag, dataSnapshot.getValue(Fragebogen.class).Radfahren_Tag);
                        assertEquals(testbsa4().Radfahren_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Radfahren_Minuten);
                        assertEquals(testbsa4().Spazieren, dataSnapshot.getValue(Fragebogen.class).Spazieren);
                        assertEquals(testbsa4().Spazieren_Tag, dataSnapshot.getValue(Fragebogen.class).Spazieren_Tag);
                        assertEquals(testbsa4().Spazieren_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Spazieren_Minuten);
                        assertEquals(testbsa4().Gartenarbeit, dataSnapshot.getValue(Fragebogen.class).Gartenarbeit);
                        assertEquals(testbsa4().Gartenarbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Gartenarbeit_Tag);
                        assertEquals(testbsa4().Gartenarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Gartenarbeit_Minuten);
                        assertEquals(testbsa4().Hausarbeit, dataSnapshot.getValue(Fragebogen.class).Hausarbeit);
                        assertEquals(testbsa4().Hausarbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Hausarbeit_Tag);
                        assertEquals(testbsa4().Hausarbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Hausarbeit_Minuten);
                        assertEquals(testbsa4().Pflegearbeit, dataSnapshot.getValue(Fragebogen.class).Pflegearbeit);
                        assertEquals(testbsa4().Pflegearbeit_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Pflegearbeit_Tag);
                        assertEquals(testbsa4().Pflegearbeit_Minuten, dataSnapshot.getValue(Fragebogen.class)
                                .Pflegearbeit_Minuten);
                        assertEquals(testbsa4().Treppensteigen, dataSnapshot.getValue(Fragebogen.class).Treppensteigen);
                        assertEquals(testbsa4().Treppensteigen_Tag, dataSnapshot.getValue(Fragebogen.class)
                                .Treppensteigen_Tag);
                        assertEquals(testbsa4().Treppensteigen_Stockwerke, dataSnapshot.getValue(Fragebogen.class)
                                .Treppensteigen_Stockwerke);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Name);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Zeit);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Einheiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_A_Minuten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Name);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Zeit);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Einheiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_B_Minuten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Name);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Zeit);
                        assertEquals(null,dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Einheiten);
                        assertEquals(null, dataSnapshot.getValue(Fragebogen.class).Aktivitaet_C_Minuten);
                        assertEquals(testbsa3().Bewegungsscoring, dataSnapshot.getValue(Fragebogen.class).Bewegungsscoring);
                        assertEquals(testbsa3().Sportscoring, dataSnapshot.getValue(Fragebogen.class).Sportscoring);
                        assertEquals(testbsa3().Gesamtscoring, dataSnapshot.getValue(Fragebogen.class).Gesamtscoring);

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
    public void testStimmung1() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/FitnessFragebogen/" +
                teststimmung1().Date+ "/" +teststimmung1().Time + "/" + VorNach(teststimmung1()));
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(teststimmung1().Angespannt, dataSnapshot.getValue(StimmungsAngabe.class).Angespannt);
                        assertEquals(teststimmung1().Mitteilsam, dataSnapshot.getValue(StimmungsAngabe.class).Mitteilsam);
                        assertEquals(teststimmung1().Muede, dataSnapshot.getValue(StimmungsAngabe.class).Muede);
                        assertEquals(teststimmung1().Selbstsicher, dataSnapshot.getValue(StimmungsAngabe.class).Selbstsicher);
                        assertEquals(teststimmung1().Tatkraeftig, dataSnapshot.getValue(StimmungsAngabe.class).Tatkraeftig);
                        assertEquals(teststimmung1().Traurig, dataSnapshot.getValue(StimmungsAngabe.class).Traurig);
                        assertEquals(teststimmung1().Wuetend, dataSnapshot.getValue(StimmungsAngabe.class).Wuetend);
                        assertEquals(teststimmung1().Zerstreut, dataSnapshot.getValue(StimmungsAngabe.class).Zerstreut);



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
    public void testStimmung2() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/FitnessFragebogen/" +
                teststimmung2().Date+ "/" +teststimmung2().Time + "/" + VorNach(teststimmung2()));
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(teststimmung2().Angespannt, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Angespannt);
                        assertEquals(teststimmung2().Mitteilsam, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Mitteilsam);
                        assertEquals(teststimmung2().Muede, dataSnapshot.getValue(StimmungsAngabe.class).Muede);
                        assertEquals(teststimmung2().Selbstsicher, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Selbstsicher);
                        assertEquals(teststimmung2().Tatkraeftig, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Tatkraeftig);
                        assertEquals(teststimmung2().Traurig, dataSnapshot.getValue(StimmungsAngabe.class).Traurig);
                        assertEquals(teststimmung2().Wuetend, dataSnapshot.getValue(StimmungsAngabe.class).Wuetend);
                        assertEquals(teststimmung2().Zerstreut, dataSnapshot.getValue(StimmungsAngabe.class).Zerstreut);



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
    public void testStimmung3() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/FitnessFragebogen/" +
                teststimmung3().Date+ "/" +teststimmung3().Time + "/" + VorNach(teststimmung3()));
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(teststimmung3().Angespannt, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Angespannt);
                        assertEquals(teststimmung3().Mitteilsam, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Mitteilsam);
                        assertEquals(teststimmung3().Muede, dataSnapshot.getValue(StimmungsAngabe.class).Muede);
                        assertEquals(teststimmung3().Selbstsicher, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Selbstsicher);
                        assertEquals(teststimmung3().Tatkraeftig, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Tatkraeftig);
                        assertEquals(teststimmung3().Traurig, dataSnapshot.getValue(StimmungsAngabe.class).Traurig);
                        assertEquals(teststimmung3().Wuetend, dataSnapshot.getValue(StimmungsAngabe.class).Wuetend);
                        assertEquals(teststimmung3().Zerstreut, dataSnapshot.getValue(StimmungsAngabe.class).Zerstreut);



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
    public void testStimmung4() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/FitnessFragebogen/" +
                teststimmung4().Date+ "/" +teststimmung4().Time + "/" + VorNach(teststimmung4()));
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(teststimmung4().Angespannt, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Angespannt);
                        assertEquals(teststimmung4().Mitteilsam, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Mitteilsam);
                        assertEquals(teststimmung4().Muede, dataSnapshot.getValue(StimmungsAngabe.class).Muede);
                        assertEquals(teststimmung4().Selbstsicher, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Selbstsicher);
                        assertEquals(teststimmung4().Tatkraeftig, dataSnapshot.getValue(StimmungsAngabe.class)
                                .Tatkraeftig);
                        assertEquals(teststimmung4().Traurig, dataSnapshot.getValue(StimmungsAngabe.class).Traurig);
                        assertEquals(teststimmung4().Wuetend, dataSnapshot.getValue(StimmungsAngabe.class).Wuetend);
                        assertEquals(teststimmung4().Zerstreut, dataSnapshot.getValue(StimmungsAngabe.class).Zerstreut);



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
    public void testStimmungScore1() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/StimmungsabfrageScore/" +
                teststimmungscore1().Date+ "/" +teststimmungscore1().Time + "/V");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(teststimmungscore1().AngespanntScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).AngespanntScore);
                        assertEquals(teststimmungscore1().TraurigScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).TraurigScore);
                        assertEquals(teststimmungscore1().TatkraeftigScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).TatkraeftigScore);
                        assertEquals(teststimmungscore1().ZerstreutScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).ZerstreutScore);
                        assertEquals(teststimmungscore1().WuetendScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).WuetendScore);
                        assertEquals(teststimmungscore1().MuedeScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).MuedeScore);
                        assertEquals(teststimmungscore1().SelbstsicherScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).SelbstsicherScore);
                        assertEquals(teststimmungscore1().MitteilsamScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).MitteilsamScore);
                        assertEquals(teststimmungscore1().StimmungsBarometerScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).StimmungsBarometerScore);
                        assertEquals(teststimmungscore1().EnergieIndexScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).EnergieIndexScore);







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
    public void testStimmungScore2() {
        Firebase root = new Firebase(DAL_Utilities.DatabaseURL+ "users/TestUserFelix/StimmungsabfrageScore/" +
                teststimmungscore2().Date+ "/" +teststimmungscore2().Time + "/V");
        try {
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // define behavior once data had been captured
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // assert equality of captured data
                    try {
                        assertEquals(teststimmungscore2().AngespanntScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).AngespanntScore);
                        assertEquals(teststimmungscore2().TraurigScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).TraurigScore);
                        assertEquals(teststimmungscore2().TatkraeftigScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).TatkraeftigScore);
                        assertEquals(teststimmungscore2().ZerstreutScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).ZerstreutScore);
                        assertEquals(teststimmungscore2().WuetendScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).WuetendScore);
                        assertEquals(teststimmungscore2().MuedeScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).MuedeScore);
                        assertEquals(teststimmungscore2().SelbstsicherScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).SelbstsicherScore);
                        assertEquals(teststimmungscore2().MitteilsamScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).MitteilsamScore);
                        assertEquals(teststimmungscore2().StimmungsBarometerScore, dataSnapshot.getValue
                                (StimmungAbfrageScore
                                .class).StimmungsBarometerScore);
                        assertEquals(teststimmungscore2().EnergieIndexScore, dataSnapshot.getValue(StimmungAbfrageScore
                                .class).EnergieIndexScore);







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

    public static Fragebogen testbsa1(){
        Fragebogen fragebogen=new Fragebogen();
        fragebogen.Date="11";
        fragebogen.FirebaseDate="20100101";
        fragebogen.Berufstaetig=0;
        fragebogen.sitzende_Taetigkeiten=1;
        fragebogen.maeßige_Bewegung=3;
        fragebogen.intensive_Bewegung=3;
        fragebogen.sportlich_aktiv=0;

        fragebogen.Zu_Fuß_zur_Arbeit=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Tag=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Minuten=3;

        fragebogen.Zu_Fuß_einkaufen=3;
        fragebogen.Zu_Fuß_einkaufen_Tag=3;
        fragebogen.Zu_Fuß_einkaufen_Minuten=3;

        fragebogen.Rad_zur_Arbeit=3;
        fragebogen.Rad_zur_Arbeit_Tag=3;
        fragebogen.Rad_zur_Arbeit_Minuten=3;

        fragebogen.Radfahren=3;
        fragebogen.Radfahren_Tag=3;
        fragebogen.Radfahren_Minuten=3;

        fragebogen.Spazieren=3;
        fragebogen.Spazieren_Tag=3;
        fragebogen.Spazieren_Minuten=3;

        fragebogen.Gartenarbeit=3;
        fragebogen.Gartenarbeit_Tag=3;
        fragebogen.Gartenarbeit_Minuten=3;

        fragebogen.Hausarbeit=3;
        fragebogen.Hausarbeit_Tag=3;
        fragebogen.Hausarbeit_Minuten=3;

        fragebogen.Pflegearbeit=3;
        fragebogen.Pflegearbeit_Tag=3;
        fragebogen.Pflegearbeit_Minuten=3;

        fragebogen.Treppensteigen=3;
        fragebogen.Treppensteigen_Tag=3;
        fragebogen.Treppensteigen_Stockwerke=3;

        fragebogen.Aktivitaet_A_Name="Sport 1";
        fragebogen.Aktivitaet_A_Zeit=3;
        fragebogen.Aktivitaet_A_Einheiten=3;
        fragebogen.Aktivitaet_A_Minuten=3;
        fragebogen.Aktivitaet_B_Name="Sport 2";
        fragebogen.Aktivitaet_B_Zeit=3;
        fragebogen.Aktivitaet_B_Einheiten=3;
        fragebogen.Aktivitaet_B_Minuten=3;
        fragebogen.Aktivitaet_C_Name="Sport 3";
        fragebogen.Aktivitaet_C_Zeit=3;
        fragebogen.Aktivitaet_C_Einheiten=3;
        fragebogen.Aktivitaet_C_Minuten=3;

        fragebogen.Bewegungsscoring=3;
        fragebogen.Sportscoring=3;
        fragebogen.Gesamtscoring=3;
        return fragebogen;
    }
    public static Fragebogen testbsa2(){
        Fragebogen fragebogen=new Fragebogen();
        fragebogen.Date="22";
        fragebogen.FirebaseDate="20100101";
        fragebogen.Berufstaetig=1;
        fragebogen.sitzende_Taetigkeiten=1;
        fragebogen.maeßige_Bewegung=3;
        fragebogen.intensive_Bewegung=3;
        fragebogen.sportlich_aktiv=0;

        fragebogen.Zu_Fuß_zur_Arbeit=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Tag=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Minuten=3;

        fragebogen.Zu_Fuß_einkaufen=3;
        fragebogen.Zu_Fuß_einkaufen_Tag=3;
        fragebogen.Zu_Fuß_einkaufen_Minuten=3;

        fragebogen.Rad_zur_Arbeit=3;
        fragebogen.Rad_zur_Arbeit_Tag=3;
        fragebogen.Rad_zur_Arbeit_Minuten=3;

        fragebogen.Radfahren=3;
        fragebogen.Radfahren_Tag=3;
        fragebogen.Radfahren_Minuten=3;

        fragebogen.Spazieren=3;
        fragebogen.Spazieren_Tag=3;
        fragebogen.Spazieren_Minuten=3;

        fragebogen.Gartenarbeit=3;
        fragebogen.Gartenarbeit_Tag=3;
        fragebogen.Gartenarbeit_Minuten=3;

        fragebogen.Hausarbeit=3;
        fragebogen.Hausarbeit_Tag=3;
        fragebogen.Hausarbeit_Minuten=3;

        fragebogen.Pflegearbeit=3;
        fragebogen.Pflegearbeit_Tag=3;
        fragebogen.Pflegearbeit_Minuten=3;

        fragebogen.Treppensteigen=3;
        fragebogen.Treppensteigen_Tag=3;
        fragebogen.Treppensteigen_Stockwerke=3;

        fragebogen.Aktivitaet_A_Name="Sport 1";
        fragebogen.Aktivitaet_A_Zeit=3;
        fragebogen.Aktivitaet_A_Einheiten=3;
        fragebogen.Aktivitaet_A_Minuten=3;
        fragebogen.Aktivitaet_B_Name="Sport 2";
        fragebogen.Aktivitaet_B_Zeit=3;
        fragebogen.Aktivitaet_B_Einheiten=3;
        fragebogen.Aktivitaet_B_Minuten=3;
        fragebogen.Aktivitaet_C_Name="Sport 3";
        fragebogen.Aktivitaet_C_Zeit=3;
        fragebogen.Aktivitaet_C_Einheiten=3;
        fragebogen.Aktivitaet_C_Minuten=3;

        fragebogen.Bewegungsscoring=3;
        fragebogen.Sportscoring=3;
        fragebogen.Gesamtscoring=3;
        return fragebogen;
    }
    public static Fragebogen testbsa3(){
        Fragebogen fragebogen=new Fragebogen();
        fragebogen.Date="33";
        fragebogen.FirebaseDate="20100101";
        fragebogen.Berufstaetig=1;
        fragebogen.sitzende_Taetigkeiten=1;
        fragebogen.maeßige_Bewegung=3;
        fragebogen.intensive_Bewegung=3;
        fragebogen.sportlich_aktiv=1;

        fragebogen.Zu_Fuß_zur_Arbeit=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Tag=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Minuten=3;

        fragebogen.Zu_Fuß_einkaufen=3;
        fragebogen.Zu_Fuß_einkaufen_Tag=3;
        fragebogen.Zu_Fuß_einkaufen_Minuten=3;

        fragebogen.Rad_zur_Arbeit=3;
        fragebogen.Rad_zur_Arbeit_Tag=3;
        fragebogen.Rad_zur_Arbeit_Minuten=3;

        fragebogen.Radfahren=3;
        fragebogen.Radfahren_Tag=3;
        fragebogen.Radfahren_Minuten=3;

        fragebogen.Spazieren=3;
        fragebogen.Spazieren_Tag=3;
        fragebogen.Spazieren_Minuten=3;

        fragebogen.Gartenarbeit=3;
        fragebogen.Gartenarbeit_Tag=3;
        fragebogen.Gartenarbeit_Minuten=3;

        fragebogen.Hausarbeit=3;
        fragebogen.Hausarbeit_Tag=3;
        fragebogen.Hausarbeit_Minuten=3;

        fragebogen.Pflegearbeit=3;
        fragebogen.Pflegearbeit_Tag=3;
        fragebogen.Pflegearbeit_Minuten=3;

        fragebogen.Treppensteigen=3;
        fragebogen.Treppensteigen_Tag=3;
        fragebogen.Treppensteigen_Stockwerke=3;

        fragebogen.Aktivitaet_A_Name="Sport 1";
        fragebogen.Aktivitaet_A_Zeit=3;
        fragebogen.Aktivitaet_A_Einheiten=3;
        fragebogen.Aktivitaet_A_Minuten=3;
        fragebogen.Aktivitaet_B_Name="Sport 2";
        fragebogen.Aktivitaet_B_Zeit=3;
        fragebogen.Aktivitaet_B_Einheiten=3;
        fragebogen.Aktivitaet_B_Minuten=3;
        fragebogen.Aktivitaet_C_Name="Sport 3";
        fragebogen.Aktivitaet_C_Zeit=3;
        fragebogen.Aktivitaet_C_Einheiten=3;
        fragebogen.Aktivitaet_C_Minuten=3;

        fragebogen.Bewegungsscoring=3;
        fragebogen.Sportscoring=3;
        fragebogen.Gesamtscoring=3;
        return fragebogen;
    }
    public static Fragebogen testbsa4(){
        Fragebogen fragebogen=new Fragebogen();
        fragebogen.Date="44";
        fragebogen.FirebaseDate="20100101";
        fragebogen.Berufstaetig=4;
        fragebogen.sitzende_Taetigkeiten=1;
        fragebogen.maeßige_Bewegung=3;
        fragebogen.intensive_Bewegung=3;
        fragebogen.sportlich_aktiv=4;

        fragebogen.Zu_Fuß_zur_Arbeit=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Tag=3;
        fragebogen.Zu_Fuß_zur_Arbeit_Minuten=3;

        fragebogen.Zu_Fuß_einkaufen=3;
        fragebogen.Zu_Fuß_einkaufen_Tag=3;
        fragebogen.Zu_Fuß_einkaufen_Minuten=3;

        fragebogen.Rad_zur_Arbeit=3;
        fragebogen.Rad_zur_Arbeit_Tag=3;
        fragebogen.Rad_zur_Arbeit_Minuten=3;

        fragebogen.Radfahren=3;
        fragebogen.Radfahren_Tag=3;
        fragebogen.Radfahren_Minuten=3;

        fragebogen.Spazieren=3;
        fragebogen.Spazieren_Tag=3;
        fragebogen.Spazieren_Minuten=3;

        fragebogen.Gartenarbeit=3;
        fragebogen.Gartenarbeit_Tag=3;
        fragebogen.Gartenarbeit_Minuten=3;

        fragebogen.Hausarbeit=3;
        fragebogen.Hausarbeit_Tag=3;
        fragebogen.Hausarbeit_Minuten=3;

        fragebogen.Pflegearbeit=3;
        fragebogen.Pflegearbeit_Tag=3;
        fragebogen.Pflegearbeit_Minuten=3;

        fragebogen.Treppensteigen=3;
        fragebogen.Treppensteigen_Tag=3;
        fragebogen.Treppensteigen_Stockwerke=3;

        fragebogen.Aktivitaet_A_Name="Sport 1";
        fragebogen.Aktivitaet_A_Zeit=3;
        fragebogen.Aktivitaet_A_Einheiten=3;
        fragebogen.Aktivitaet_A_Minuten=3;
        fragebogen.Aktivitaet_B_Name="Sport 2";
        fragebogen.Aktivitaet_B_Zeit=3;
        fragebogen.Aktivitaet_B_Einheiten=3;
        fragebogen.Aktivitaet_B_Minuten=3;
        fragebogen.Aktivitaet_C_Name="Sport 3";
        fragebogen.Aktivitaet_C_Zeit=3;
        fragebogen.Aktivitaet_C_Einheiten=3;
        fragebogen.Aktivitaet_C_Minuten=3;

        fragebogen.Bewegungsscoring=3;
        fragebogen.Sportscoring=3;
        fragebogen.Gesamtscoring=3;
        return fragebogen;
    }

    public static StimmungsAngabe teststimmung1(){
        StimmungsAngabe teststimmung=new StimmungsAngabe();
        teststimmung.Date="01";
        teststimmung.FirebaseDate="01";
        teststimmung.Vor=true;
        teststimmung.Angespannt=0;
        teststimmung.Mitteilsam=0;
        teststimmung.Muede=0;
        teststimmung.Selbstsicher=0;
        teststimmung.Tatkraeftig=0;
        teststimmung.Traurig=0;
        teststimmung.Wuetend=0;
        teststimmung.Zerstreut=0;
        return teststimmung;
    }
    public static StimmungsAngabe teststimmung2(){
        StimmungsAngabe teststimmung=new StimmungsAngabe();
        teststimmung.Date="02";
        teststimmung.FirebaseDate="02";
        teststimmung.Vor=false;
        teststimmung.Angespannt=0;
        teststimmung.Mitteilsam=0;
        teststimmung.Muede=0;
        teststimmung.Selbstsicher=0;
        teststimmung.Tatkraeftig=0;
        teststimmung.Traurig=0;
        teststimmung.Wuetend=0;
        teststimmung.Zerstreut=0;
        return teststimmung;
    }
    public static StimmungsAngabe teststimmung3(){
        StimmungsAngabe teststimmung=new StimmungsAngabe();
        teststimmung.Date="03";
        teststimmung.FirebaseDate="03";
        teststimmung.Vor=false;
        teststimmung.Angespannt=4;
        teststimmung.Mitteilsam=11;
        teststimmung.Muede=21;
        teststimmung.Selbstsicher=33;
        teststimmung.Tatkraeftig=42;
        teststimmung.Traurig=6;
        teststimmung.Wuetend=42;
        teststimmung.Zerstreut=45;
        return teststimmung;
    }
    public static StimmungsAngabe teststimmung4(){
        StimmungsAngabe teststimmung=new StimmungsAngabe();
        teststimmung.Date="04";
        teststimmung.FirebaseDate="04";
        teststimmung.Vor=true;
        teststimmung.Angespannt=2;
        teststimmung.Mitteilsam=0;
        teststimmung.Muede=-1;
        teststimmung.Selbstsicher=4;
        teststimmung.Tatkraeftig=2;
        teststimmung.Traurig=null;
        teststimmung.Wuetend=0;
        teststimmung.Zerstreut=222;
        return teststimmung;
    }

    public static StimmungAbfrageScore teststimmungscore1(){
        StimmungAbfrageScore stimmungsscore= new StimmungAbfrageScore();
        stimmungsscore.Date="11";
        stimmungsscore.FirebaseDate="11";
        stimmungsscore.AngespanntScore=0;
        stimmungsscore.TraurigScore=0;
        stimmungsscore.TatkraeftigScore=0;
        stimmungsscore.ZerstreutScore=0;
        stimmungsscore. WuetendScore=0;
        stimmungsscore.MuedeScore=0;
        stimmungsscore.SelbstsicherScore=0;
        stimmungsscore.MitteilsamScore=0;
        stimmungsscore.StimmungsBarometerScore=0;
        stimmungsscore.EnergieIndexScore=0;
        stimmungsscore.Vor=true;
        return stimmungsscore;
    }
    public static StimmungAbfrageScore teststimmungscore2(){
        StimmungAbfrageScore stimmungsscore= new StimmungAbfrageScore();
        stimmungsscore.Date="22";
        stimmungsscore.FirebaseDate="22";
        stimmungsscore.AngespanntScore=2;
        stimmungsscore.TraurigScore=5;
        stimmungsscore.TatkraeftigScore=7;
        stimmungsscore.ZerstreutScore=3;
        stimmungsscore. WuetendScore=4;
        stimmungsscore.MuedeScore=9;
        stimmungsscore.SelbstsicherScore=2;
        stimmungsscore.MitteilsamScore=3;
        stimmungsscore.StimmungsBarometerScore=1;
        stimmungsscore.EnergieIndexScore=4;
        stimmungsscore.Vor=true;
        return stimmungsscore;
    }



}

