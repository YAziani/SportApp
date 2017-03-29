package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.util.Calendar;
import java.util.Random;

/**
 * observer for motivation images
 * Created by Aziani on 25.03.2017.
 */

public class ObserverMotivationMessage extends Observer {

    private static short timeOutCounter = 0;

    @Override
    public void update(Context context) {
        this.context = context;

        // check if method allocated
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.getString("allocatedMethods", "").contains("motivationimages")) {
            return;
        }

        if (timeOutCounter > 0) {
            timeOutCounter--;
        } else {
            if (!getNextTrainingTimeString(context).equals("")
                    && MotivationMethod.timeTillTraining(getNextTrainingTimeString(context)) == 5) {
                if (checkIntensifier()) {
                    sendNotification(
                            context,
                            "Motivationsbilder",
                            ActivityMotivationMessage.class,
                            context.getString(R.string.mmNotiTitle),
                            context.getString(R.string.mmNotiSmallTitle),
                            R.mipmap.ic_tagebuch_eintrag);
                    timeOutCounter = 5;
                }
            }
        }
    }

    /**
     * check if intensifier allows notification
     *
     * @return true if notification allowed
     */
    private boolean checkIntensifier() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long daysFromStart = Calendar.getInstance().getTimeInMillis() - preferences.getLong("firstDay", 0);
        daysFromStart = daysFromStart / 86400000;
        String intensifier = preferences.getString("intensifier", "");
        if (intensifier.equals("")) {
            return true;
        } else {
            // go through all entries and evaluate intensifier settings
            for (String s : intensifier.split(";")) {
                try {
                    if (s.split(",").length == 3 &&
                            !s.split(",")[0].equals("")
                            && !s.split(",")[1].equals("")
                            && !s.split(",")[2].equals("")) {
                        if (daysFromStart > Integer.valueOf(s.split(",")[0])) {
                            daysFromStart -= Integer.valueOf(s.split(",")[0]);
                            continue;
                        }
                        if (daysFromStart > Integer.valueOf(s.split(",")[1])) {
                            Random random = new Random();
                            return Double.valueOf(s.split(",")[2]) > random.nextDouble();
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }
        }
        return true;
    }
}
