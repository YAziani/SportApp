package com.example.mb7.sportappbp;



import com.example.mb7.sportappbp.Activity.FireApp;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.net.URL;

import org.junit.Before;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */




public class ExampleUnitTest   {

    private static FireApp application;

    @Before
    public void setUp() throws Exception {
        DAL_Utilities.DatabaseURL = "https://sportapp-cbd6b.firebaseio.com/";
        User user = User.Create("TestUniUser");

        StimmungsAngabe stimmungsAngabe = new StimmungsAngabe();
        stimmungsAngabe.Muede = 2;
        stimmungsAngabe.Vor = true;
        DAL_User.InsertStimmung(user  , stimmungsAngabe,new Date());

    }



    static public StimmungsAngabe getStimmungsabfrageFromDb(Date date){
        final StimmungsAngabe sti=new StimmungsAngabe();
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

                                                                        StimmungsAngabe stimmungsAngabe = child.getValue(StimmungsAngabe.class);
                                                                        stimmungsAngabe =sti;
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





    public static StimmungsAngabe stimmung1(Date testdate1) throws Exception{
        User Testperson1=User.Create("Testperson1");
        StimmungsAngabe teststimmung = new StimmungsAngabe();
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
        StimmungsAngabe teststimmung = new StimmungsAngabe();
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

