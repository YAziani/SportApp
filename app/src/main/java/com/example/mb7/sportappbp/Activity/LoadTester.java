package com.example.mb7.sportappbp.Activity;

import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.mb7.sportappbp.Adapters.StimmungsAngabeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_RegisteredUsers;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.InterfaceAddress;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by M.Braei on 30.03.2017.
 */

public class LoadTester {


    void Run(){
     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread1","PassUserThread1");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread2","PassUserThread2");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread3","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread4","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread5","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread6","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread7","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread8","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread9","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDAL_ThreadUsers("UserThread10","PassUserThread3");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        int THREAD_NUMBER = 100;
        //TestRead();
      //  deleteUsers(THREAD_NUMBER);

        for (int i = 0; i < THREAD_NUMBER; i++)
        {
            final int counter = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    writeDAL_ThreadUsers("UserThread" + Integer.toString(counter), "PassUserThread3");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }


    /**
     * write data through DAL_RegisteredUsers
     */
    private void writeDAL_ThreadUsers(String username, String password) {
        DAL_RegisteredUsers.insertRegistration(username, password);
        User user = User.Create(username);
        DAL_RegisteredUsers.insertMail(username + "@gmail.com", user);        try {
            for (int i=0; i<100; i++) {
                long time= System.currentTimeMillis();
                DAL_User.InsertFitnessFragebogen(user,testfitness1(Integer.toString(i)));
                Log.e("Performance Thread" + username, "Thread " + username + " writeDAL Nr." + i +
                        ": " + Long.toString(System.currentTimeMillis() - time));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void TestRead()
    {
        try{

        URL url = new URL(DAL_Utilities.DatabaseURL + "users/");//+ "users/" + username +                "/FitnessFragebogen/");
        final Firebase root = new Firebase(url.toString());

        root.addListenerForSingleValueEvent(new ValueEventListener() {

                                       // Hier kriegst du den Knoten date zurueck
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {

                                           //users
                                           for (DataSnapshot child : dataSnapshot.getChildren()) {
                                               if (((String) child.getKey()).contains("Thread")) {
                                                   Log.e("Reading User", ((String) child.getKey()));
                                                   // Here I get the time
                                                   for (DataSnapshot child2 : child.getChildren()) {
                                                       if (((String) child2.getKey()).equals("FitnessFragebogen")) {

                                                           // Here I have V or N
                                                           Integer i = 0;
                                                           for (DataSnapshot child2L : child2.getChildren()) {
                                                               final String sTime = child2L.getKey();
                                                               FitnessFragebogen f1 = child2L.getValue
                                                                       (FitnessFragebogen.class);

                                                               if (equalValues(f1, testfitness1(Integer.toString(i)))) {/*
                                                                   Log.e(((String)child.getKey()) + " - Nr." + i +
                                                                           " - Correct" +
                                                                           "True:", child
                                                                           .getValue().toString
                                                                           ());*/
                                                               } else {
                                                                   Log.e(((String)child.getKey()) + " - Nr." + i +
                                                                           "Correct" +" False:", child.getValue().toString());
                                                               }
i++;
                                                           }
                                                       }
                                                   }
                                               }

                                           }


                                       }

                                       @Override
                                       public void onCancelled(FirebaseError firebaseError) {

                                       }
                                   }
        );
    } catch (Exception ex) {
        Log.e("Exc", ex.getMessage());

    }

    }



    public static FitnessFragebogen testfitness1(String date){
        FitnessFragebogen testfitness=new FitnessFragebogen();
        testfitness.Date = date;
        testfitness.FirebaseDate = date;
        testfitness.Score_Kraft= Integer.parseInt(date);
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

    public static Boolean equalValues(FitnessFragebogen f1 , FitnessFragebogen f2)
    {
        if (f1.Score_Kraft == f2.Score_Kraft && f1.Score_Ausdauer == f2.Score_Ausdauer && f1.Score_Koordination == f2.Score_Koordination && f1.Score_Beweglichkeit == f2.Score_Beweglichkeit && f1.Score_Gesamt == f2.Score_Gesamt && f1.vom_Stuhl_aufstehen == f2.vom_Stuhl_aufstehen && f1.Einkaufskorb_tragen == f2.Einkaufskorb_tragen && f1.Kiste_tragen == f2.Kiste_tragen && f1.Situp == f2.Situp && f1.Koffer_hoch_heben == f2.Koffer_hoch_heben && f1.Koffer_tragen == f2.Koffer_tragen && f1.Hantel_stemmen == f2.Hantel_stemmen && f1.flott_gehen == f2.flott_gehen && f1. Treppen_gehen == f2. Treppen_gehen && f1. Zwei_km_gehen == f2. Zwei_km_gehen && f1. Ein_km_joggen == f2. Ein_km_joggen && f1. Dreißig_min_joggen == f2. Dreißig_min_joggen && f1. Sechzig_min_joggen == f2. Sechzig_min_joggen && f1. Marathon == f2. Marathon && f1. Socken_anziehen == f2. Socken_anziehen && f1.Boden_im_Sitzen_beruehren == f2.Boden_im_Sitzen_beruehren && f1. Schuhe_binden == f2. Schuhe_binden && f1. Ruecken_beruehren == f2. Ruecken_beruehren && f1. Im_Stehen_Boden_beruehren == f2. Im_Stehen_Boden_beruehren && f1. Mit_Kopf_das_Knie_beruehren == f2. Mit_Kopf_das_Knie_beruehren && f1. Bruecke == f2. Bruecke && f1. Treppe_runter_gehen == f2. Treppe_runter_gehen && f1. Einbeinstand == f2. Einbeinstand && f1. Purzelbaum == f2. Purzelbaum && f1. Ball_prellen == f2. Ball_prellen && f1.Zaunsprung == f2.Zaunsprung && f1. Kurve_fahren_ohne_Hand == f2. Kurve_fahren_ohne_Hand && f1. Rad_schlagen == f2. Rad_schlagen
                )
            return true;
        return false;
    }

    public void deleteUsers(int ThreadNum)
    {
        for (int i = 1; i <= ThreadNum; i++)
        {
            deleteuser("UserThread" + Integer.toString(i));
        }
    }
    public  void deleteuser(String username)
    {
        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + username );
        ref.removeValue();

    }


}
