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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.BackgroundClock;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_TrainQuestioningTexts;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;

import java.util.Iterator;

/**
 * activity that displays text if user does not want to train
 * Created by Aziani on 26.02.2017.
 */
public class ActivityTrainQuestioning extends AppCompatActivity {

    int praiseOrWarn = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_questioning);

        // close notification
        int notificationId = getIntent().getIntExtra("notificationId",-1);
        if(notificationId != -1) {
            ((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
        }

        ImageView imageView = (ImageView)findViewById(R.id.imageViewTrainNo);
        praiseOrWarn = getIntent().getIntExtra("praiseOrWarn", -1);

        // act depending on whether the user trained or not
        if(praiseOrWarn == 1) {
            // access texts in data base
            DAL_TrainQuestioningTexts.getTrainQuestioningTexts(this,praiseOrWarn);
            // put background image
            imageView.setImageResource(R.drawable.train_no);
            if(getIntent().getBooleanExtra("preTrain",false)) {
                // save information that user won't train
                PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())
                        .edit()
                        .putBoolean("willTrain",false)
                        .apply();
            }
            // notify BackgroundClock about user activity
            BackgroundClock.startRating(false);
        }else {
            DAL_TrainQuestioningTexts.getTrainQuestioningTexts(this,praiseOrWarn);
            imageView.setImageResource(R.drawable.train_yes);
            BackgroundClock.startRating(true);
        }
        imageView.setAlpha(0.5f);

        /*
        // reference for the firebase storage image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("trainNo.jpg");

        // Load the image using Glide
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(imageView);

        // set alpha and color
        imageView.setColorFilter(Color.argb(100, 117, 66, 20));
        imageView.setAlpha(0.4f);
         */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set up save button
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // close activity
        finish();
        return true;
    }

    /**
     * function to return the texts from the database to this activity
     * @param dataSnapshot the snapshot from the database
     */
    public void returnTexts(DataSnapshot dataSnapshot) {
        if(dataSnapshot != null && dataSnapshot.getChildrenCount() > 0){
            int textIndex;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            textIndex = preferences.getInt("trainNoTextIndex",0) % ((int)dataSnapshot.getChildrenCount());
            preferences.edit().putInt("trainNoTextIndex", (textIndex+1) % ((int)dataSnapshot.getChildrenCount())).apply();
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