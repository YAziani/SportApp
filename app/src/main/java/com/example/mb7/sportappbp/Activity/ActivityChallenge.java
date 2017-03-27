package com.example.mb7.sportappbp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.ChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.DiaryEntry;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Challenges;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActivityChallenge extends AppCompatActivity {

    ChallengeViewAdapter challengeViewAdapter;
    ListView listView;
    ArrayList<User> userList;
    Challenge challenge;
    TextView textViewCountdown;
    Calendar calendar = Calendar.getInstance();
    Date todayDate;
    Date endDate;
    String challengeName;
    LinkedList<String> strUSer;

    DataSnapshot dataSnapshotChallenges;
    boolean validSnapshotChallenges = false;
    DataSnapshot dataSnapshotUsers;
    boolean validSnapshotUsers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        //challenge = new Challenge();

        // Now read the extra key - exerciseList
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if(extras!=null ) {

            challenge = (Challenge) extras.getSerializable(getString(R.string.Challenges));
            strUSer = new LinkedList<String>();
            DAL_Challenges.getRegisteredUsersToChallenge(this, challenge.getName());

        }
        else{

            challenge = new Challenge();
            Toast.makeText(this,getString(R.string.ChallengeKonnteNichtGeladenWerdenVersuchenSieBitteErneut),Toast.LENGTH_SHORT).show();
            //finish();

        }

        //DAL_Challenges.getRegisteredChallengesToChallenge(ActivityChallenge.this);

        //challengeName = ActivityMain.mainUser.getChallangeName();

        //todo load challenge and user from database
        //challenge = ActivityMain.mainUser.getChallenge();

        userList = new ArrayList<User>();


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

    /**
     * This method checks, if a user with an email address exist and returns him. When the user doesn't
     * exist, the method returns null.
     * @return The user you are looking for or null
     */
    private void getUSers() {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        if(validSnapshotUsers) {
            if (dataSnapshotUsers.getValue() != null) {
                for (DataSnapshot d : dataSnapshotUsers.getChildren()) {

                    strUSer.add(d.getKey());
                }
            }
        }

        for(final String u : strUSer) {

            URL url = null;
            try {
                url = new URL(DAL_Utilities.DatabaseURL + "users/" + u + "/Diary/");
                final Firebase root = new Firebase(url.toString());

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int totalPoints = 0;

                        //day
                        for(DataSnapshot child1Date : dataSnapshot.getChildren()){

                            try {
                                Date date = sdf.parse(child1Date.getKey());

                                if(date.after(challenge.getStartDate()) || date.equals(challenge.getStartDate()) || date.equals(challenge.getEndDate()) || date.before(challenge.getEndDate())){
                                    //time
                                    for(DataSnapshot child2 : child1Date.getChildren()) {
                                        //look for totalpoints
                                        for (DataSnapshot child3 : child2.getChildren()) {
                                            if (child3.getKey().equals("totalPoints"))
                                                totalPoints = totalPoints + Integer.valueOf(child3.getValue().toString());
                                        }
                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        User user = User.Create(u);
                        user.setPoints(totalPoints);
                        userList.add(user);
                        challengeViewAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * return the snapshot with registered users to this activity
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot) {
        this.dataSnapshotUsers = dataSnapshot;
        validSnapshotUsers = true;
        getUSers();
    }


    /**
     * return the snapshot with registered challenges to this activity
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredChallenges(DataSnapshot dataSnapshot) {
        this.dataSnapshotChallenges = dataSnapshot;
        validSnapshotChallenges = true;
    }

}
