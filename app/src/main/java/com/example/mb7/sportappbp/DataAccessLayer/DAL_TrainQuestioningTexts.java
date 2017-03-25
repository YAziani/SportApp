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
                URL url = new URL("https://sportapp-cbd6b.firebaseio.com/Administration/motivationText/neg");
                Firebase root = new Firebase(url.toString());
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        activityTrainQuestioning.returnTexts(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        activityTrainQuestioning.returnTexts(null);
                    }
                });
            }
            catch (Exception e)
            {
                activityTrainQuestioning.returnTexts(null);
                e.printStackTrace();
            }
        }else {
            try {
                URL url = new URL("https://sportapp-cbd6b.firebaseio.com/Administration/motivationText/pos");
                Firebase root = new Firebase(url.toString());
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        activityTrainQuestioning.returnTexts(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        activityTrainQuestioning.returnTexts(null);
                    }
                });
            }
            catch (Exception e)
            {
                activityTrainQuestioning.returnTexts(null);
                e.printStackTrace();
            }
        }
    }
}
