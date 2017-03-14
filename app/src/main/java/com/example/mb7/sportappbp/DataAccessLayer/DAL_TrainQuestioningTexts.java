package com.example.mb7.sportappbp.DataAccessLayer;

import com.example.mb7.sportappbp.Activity.ActivityTrainQuestioning;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.net.URL;

/**
 * access motivation texts if user does not want to train from database
 * Created by Aziani on 26.02.2017.
 */

public class DAL_TrainQuestioningTexts {

    /**
     * get texts from database and hand it to the displaying activity
     * @param activityTrainQuestioning the displaying activity
     */
    public static void getTrainQuestioningTexts(
            final ActivityTrainQuestioning activityTrainQuestioning,
            int praiseOrWarn) {

        if(praiseOrWarn == 1) {
            // access data in database and hand it to activity
            try {
                URL url = new URL(DAL_Utilities.DatabaseURL + "/texts/trainNo");
                Firebase root = new Firebase(url.toString());
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        activityTrainQuestioning.returnTexts(dataSnapshot);
                        return;
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
        }else {
            try {
                URL url = new URL(DAL_Utilities.DatabaseURL + "/texts/trainYes");
                Firebase root = new Firebase(url.toString());
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        activityTrainQuestioning.returnTexts(dataSnapshot);
                        return;
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
}
