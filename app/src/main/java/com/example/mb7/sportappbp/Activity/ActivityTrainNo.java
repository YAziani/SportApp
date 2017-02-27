package com.example.mb7.sportappbp.Activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_TrainNoTexts;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import java.util.Iterator;

/**
 * activity that displays text if user does not want to train
 * Created by Aziani on 26.02.2017.
 */
public class ActivityTrainNo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_no);

        int notificationId = getIntent().getIntExtra("notificationId",-1);

        // close notification
        if(notificationId != -1) {
            ((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
        }

        // access texts in data base
        DAL_TrainNoTexts.getTrainNoTexts(this);
    }

    /**
     * function to return the texts from the database to this activity
     * @param dataSnapshot the snapshot from the database
     */
    public void returnTexts(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getChildrenCount() > 0){
            int textIndex;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            textIndex = preferences.getInt("trainNoTextIndex",0);
            preferences.edit().putInt("trainNoTextIndex", (textIndex+1)%((int)dataSnapshot.getChildrenCount())).apply();
            // get the text with the index textIndex
            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
            for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                if(i == textIndex) {
                    // show the chosen text
                    showText((String)iterator.next().getValue());
                    break;
                }else {
                    iterator.next();
                }
            }
        }else {
            this.finish();
        }
    }

    /**
     * show the text in the text vie
     * @param text the text to be shown
     */
    private void showText(String text) {
        TextView textView = (TextView)findViewById(R.id.textViewTrainNo);
        // format text
        SpannableString s = new SpannableString(text);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,text.split(" ")[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new RelativeSizeSpan(1.5f),1,s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new RelativeSizeSpan(2f),0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // display text view
        textView.setText(s);
    }
}
