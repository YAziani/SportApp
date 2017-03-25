package com.example.mb7.sportappbp.Activity;
/* Sample List
        userList = new ArrayList<User>();
        User u1 = User.Create("Bernd");
        u1.setPoints(250);
        User u2 = User.Create("Peter");
        u2.setPoints(40);
        User u3 = User.Create("Hans");
        u3.setPoints(500);

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Adapters.ChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityChallenge extends AppCompatActivity {

    ChallengeViewAdapter challengeViewAdapter;
    ListView listView;
    ArrayList<User> userList;
    Challenge challenge;
    TextView textViewCountdown;
    Calendar calendar = Calendar.getInstance();
    Date todayDate;
    Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        //todo load challenge and user from database
        challenge = ActivityMain.mainUser.getChallenge();

        userList = challenge.getUserList();

        //Create listview
        listView = (ListView) findViewById(R.id.listViewChallenge);
        challengeViewAdapter = new ChallengeViewAdapter(ActivityChallenge.this, userList);
        listView.setAdapter(challengeViewAdapter);

        //set TextView
        textViewCountdown = (TextView) findViewById(R.id.textViewChallengeCountDown);

        //calculate the difference between today and the end date
        todayDate = calendar.getTime();
        endDate = challenge.getEndDate();
        String  remainingDays = String.valueOf(challenge.getRemainingDays());
        //set the difference to the textview
        textViewCountdown.setText(getString(R.string.VerbleibendeTage) + " " + remainingDays);

    }

}
