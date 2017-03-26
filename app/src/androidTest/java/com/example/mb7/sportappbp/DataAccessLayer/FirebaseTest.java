package com.example.mb7.sportappbp.DataAccessLayer;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.Utilities.MyApplication;
import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * Created by Aziani on 24.03.2017.
 */


@RunWith(AndroidJUnit4.class)
public class FirebaseTest extends ApplicationTestCase<MyApplication> {

    private static MyApplication application;

    public FirebaseTest() {
        super(MyApplication.class);
        try {
            setUp();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUp() throws Exception {
        System.out.println("setUp");
        super.setUp();
        if (application == null) {
            application = getApplication();
        }
        if (application == null) {
            application = (MyApplication) getContext().getApplicationContext();
            assertNotNull(application);
            long start = System.currentTimeMillis();
            while (!application.isInitialized){
                Thread.sleep(3000);  //wait until FireBase is totally initialized
                if ( (System.currentTimeMillis() - start ) >= 1000 )
                    throw new TimeoutException(this.getClass().getName() +"Setup timeOut");
            }
        }
    }


    @Test
    public void testWrite(){
        System.out.println("testWrite");
        Firebase cloud = new Firebase("https://sportapp-cbd6b.firebaseio.com/");
        cloud.child("FIREBASETEST").setValue("data test");
    }

    @Test
    public void testTest() {
        System.out.println("testTest");
        assertEquals(9,3*3);
    }
}
