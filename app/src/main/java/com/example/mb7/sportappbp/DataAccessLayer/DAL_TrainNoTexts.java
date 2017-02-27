package com.example.mb7.sportappbp.DataAccessLayer;

import com.example.mb7.sportappbp.Activity.ActivityTrainNo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.net.URL;

/**
 * access motivation texts if user does not want to train from database
 * Created by Aziani on 26.02.2017.
 */

public class DAL_TrainNoTexts {

    /**
     * get texts from database and hand it to the displaying activity
     * @param activityTrainNo the displaying activity
     */
    public static void getTrainNoTexts(
            final ActivityTrainNo activityTrainNo) {

        // access data in database and hand it to activity
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "/texts/trainNo");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    activityTrainNo.returnTexts(dataSnapshot);
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
