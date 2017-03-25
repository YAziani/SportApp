package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMessage;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Intirion on 25.03.2017.
 */

public class ObserverMotivationMessage extends Observer {

    short timeOutCounter = 0;

    @Override
    public void update(Context context) {
        this.context = context;



        // check if method allocated
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!preferences.getString("allocatedMethods","").contains("motivationimages")) {
            return;
        }

        if(timeOutCounter > 0) {
            timeOutCounter--;
        }else {
            //sendNotification(context,"a",ActivityMain.class,String.valueOf(getNextTrainingTimeString(context)),String.valueOf(MotivationMethod.timeTillTraining(getNextTrainingTimeString(context))),R.mipmap.ic_trainingseinheit);
            if(!getNextTrainingTimeString(context).equals("")
                    && MotivationMessage.timeTillTraining(getNextTrainingTimeString(context)) == 5) {
                if(checkIntensifier()) {
                    sendNotification(
                            context,
                            "Motivationsbilder",
                            ActivityMotivationMessage.class,
                            context.getString(R.string.mmNotiTitle),
                            context.getString(R.string.mmNotiSmallTitle),
                            R.mipmap.ic_trainingseinheit);
                    timeOutCounter = 5;
                }
            }
        }
    }

    /**
     * check if intensifier allows notification
     * @return true if notification allowed
     */
    private boolean checkIntensifier() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long daysFromStart = Calendar.getInstance().getTimeInMillis() - preferences.getLong("firstDay",0);
        daysFromStart = daysFromStart / 86400000;
        String intensifier = preferences.getString("intensifier","");
        if(intensifier.equals("")) {
            return true;
        }else {
            // go through all entries
            for(String s : intensifier.split(";")) {
                if(s.split(",").length == 3 &&
                        !s.split(",")[0].equals("") && !s.split(",")[1].equals("") && !s.split(",")[2].equals("")
                        && Pattern.matches("[0-9]+",s.split(",")[0])
                        && Pattern.matches("[0-9]+",s.split(",")[1])
                        && Pattern.matches("[0-1][,][0-9]+",s.split(",")[2])) {
                    if(daysFromStart > Integer.valueOf(s.split(",")[0])) {
                        daysFromStart -= Integer.valueOf(s.split(",")[0]);
                        continue;
                    }
                    if(daysFromStart > Integer.valueOf(s.split(",")[1])) {
                        Random random = new Random();
                        return Double.valueOf(s.split(",")[2]) > random.nextDouble();
                    }else {
                        return false;
                    }
                }else {
                    return true;
                }
            }
        }
        return true;
    }
}
