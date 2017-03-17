package com.example.mb7.sportappbp;



import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */




public class ExampleUnitTest {
    User Testperson1=User.Create("Testperson1");

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }




    public void stimmungtest1() throws Exception{
        StimmungAbfrage teststimmung = new StimmungAbfrage();
        teststimmung.Date="20170101";
        teststimmung.Vor=true;
        teststimmung.Angespannt=0;
        teststimmung.Mitteilsam=0;
        teststimmung.Muede=0;
        teststimmung.Selbstsicher=0;
        teststimmung.Tatkraeftig=0;
        teststimmung.Traurig=0;
        teststimmung.Wuetend=0;
        teststimmung.Zerstreut=0;
        //Testperson1.SaveStimmung(teststimmung);

        //FirebaseDatabase.getInstance().getReference().child("players").child("Testperson1").child("Stimmungsabfrage").addListenerForSingleValueEvent( });

    }


}
