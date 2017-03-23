package com.example.mb7.sportappbp;



import android.test.ApplicationTestCase;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.FireApp;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.firebase.client.collection.ArraySortedMap;
import com.firebase.client.snapshot.ChildrenNode;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;


import static java.security.AccessController.getContext;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */




public class ExampleUnitTest extends ApplicationTestCase<FireApp> {

    private static FireApp application;

    public ExampleUnitTest() {
        super(FireApp.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (application == null) {
            application = getApplication();
        }
        if (application == null) {
            application = (FireApp) getContext().getApplicationContext();
            assertNotNull(application);
            long start = System.currentTimeMillis();
            while (!application.isInitialized){
                Thread.sleep(300);  //wait until FireBase is totally initialized
                if ( (System.currentTimeMillis() - start ) >= 1000 )
                    throw new TimeoutException(this.getClass().getName() +"Setup timeOut");
            }
        }
    }


    static public StimmungAbfrage getStimmungsabfrageFromDb(Date date){
        final StimmungAbfrage sti=new StimmungAbfrage();
        try{
            final String fdate = DAL_Utilities.ConvertDateToFirebaseDate(date);
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/Testperson1/Stimmungsabfrage/" + fdate);
            final Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener(){

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String strKey = "";
                                                        //Object obj=dataSnapshot.getChildren();
                                                        //((ChildrenNode) (dataSnapshot.node.node).children).values;
                                                        //((ChildrenNode) dataSnapshot..node.node).children.iterator().next().getValue();
                                                        //[.iterator().next()]
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            // Hier bekommst du den Knoten V oder N
                                                            strKey = child.getKey();
                                                            root.child(strKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                        // Hier bekommst du dann letztlich die Stimmungsabfrage

                                                                        StimmungAbfrage stimmungAbfrage = child.getValue(StimmungAbfrage.class);
                                                                        stimmungAbfrage=sti;




                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(FirebaseError firebaseError) {

                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(FirebaseError firebaseError) {
                                                    }
                                                }

            );
            return sti;
        }
        catch (Exception e){
            e.printStackTrace();
            return sti;
        }

    }





    public static StimmungAbfrage stimmung1(Date testdate1) throws Exception{
        User Testperson1=User.Create("Testperson1");
        StimmungAbfrage teststimmung = new StimmungAbfrage();
        //teststimmung.Date=testdate;
        teststimmung.Vor=true;
        teststimmung.Angespannt=0;
        teststimmung.Mitteilsam=0;
        teststimmung.Muede=0;
        teststimmung.Selbstsicher=0;
        teststimmung.Tatkraeftig=0;
        teststimmung.Traurig=0;
        teststimmung.Wuetend=0;
        teststimmung.Zerstreut=0;
        DAL_User.InsertStimmung(Testperson1, teststimmung, testdate1);
        return teststimmung;


    }

        public boolean stimmung2(){
        User Testperson1=User.Create("Testperson1");
        Date testdate=new Date();
        StimmungAbfrage teststimmung = new StimmungAbfrage();
        String fdate = DAL_Utilities.ConvertDateToFirebaseDate(testdate);
        teststimmung.Date=fdate;
        teststimmung.Vor=true;
        teststimmung.Angespannt=0;
        teststimmung.Mitteilsam=0;
        teststimmung.Muede=0;
        teststimmung.Selbstsicher=0;
        teststimmung.Tatkraeftig=0;
        teststimmung.Traurig=0;
        teststimmung.Wuetend=0;
        teststimmung.Zerstreut=0;
        DAL_User.InsertStimmung(Testperson1, teststimmung, testdate);
        return true;
    }

    @Test
    public void SampleTrueTest(){
        assertTrue(true);
    }

    //Stimmung2 auf Firebase uploaden
    /*@Test
    public void uploadstimmung() throws Exception {
        String s = "";
        s = "sdflkjs";
        assertTrue(stimmung2());
    }
*/

/*
    //Test mit Get Methode aus DAL_User
    @Test
    public void stimmungstest2() throws Exception {
        Date testdate= new Date();
        //assertEquals(stimmung1(Testperson1, testdate), DAL_User.GetStimmnungsabfrage(Testperson1,testdate));
        //(assertEquals(stimmung1(Testperson1,testdate).Angespannt, DAL_User.GetStimmnungsabfrage(Testperson1, testdate).Angespannt));
    }

    //Test mit Get Methode aus Testperson1(User)
    @Test
    public void stimmungstest3() throws Exception {
        User Testperson1=User.Create("Testperson1");
        Date testdate= new Date();
        assertEquals(stimmung1(Testperson1, testdate), Testperson1.GetStimmnungsabfrage(testdate));
    }


    //Test mit Get Methode aus ExampleUnitTest
    @Test
    public void stimmungstest4() throws Exception {
        User Testperson1=User.Create("Testperson1");
        Date testdate= new Date();
        assertEquals(stimmung1(Testperson1, testdate), getStimmungsabfrageFromDb(testdate));
    }
*/

}

