package com.example.mb7.sportappbp.DataAccessLayer;

import android.app.Activity;

import com.example.mb7.sportappbp.BusinessLayer.MethodChooser;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.List;

/**
 * Created by Aziani on 22.02.2017.
 */

public class DAL_Allocation {

    public static void getAllocation(
            final Activity activity,
            final List<MotivationMethod> fixMotivationMethods,
            final List<MotivationMethod> variableMotivationMethods) {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "/Administration/Zuweisung/");
            Firebase root = new Firebase(url.toString());
            System.out.println(root.getRef());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MethodChooser.chooseMethods(
                            dataSnapshot, fixMotivationMethods, variableMotivationMethods, activity);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

