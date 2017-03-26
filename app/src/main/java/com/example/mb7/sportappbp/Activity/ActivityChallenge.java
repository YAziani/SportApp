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

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Adapters.ChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Challenges;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

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

    DataSnapshot dataSnapshotChallenges;
    boolean validSnapshotChallenges = false;
    DataSnapshot dataSnapshotUsers;
    boolean validSnapshotUsers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        DAL_Challenges.getRegisteredChallengesToChallenge(ActivityChallenge.this);

        challengeName = ActivityMain.getMainUser(this  ).getChallangeName();

        //todo load challenge and user from database
        challenge = ActivityMain.getMainUser(this  ).getChallenge();

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

    /**
     * This method checks, if a challenge already and returns true. When the challenge name doesn't
     * exist, the method returns false.
     * @param challengeName searched challenge name
     * @return The user you are looking for or null
     */
    private Challenge getChallenge(String challengeName){

        Challenge result = null;

        if(validSnapshotChallenges) {
            if (dataSnapshotChallenges.getValue() != null) {
                for (DataSnapshot d : dataSnapshotChallenges.getChildren()) {

                    if (d.getKey().equals(challengeName)) {
                        result.setName(d.getKey().toString());

                        for(DataSnapshot child : d.getChildren()){
                            if(child.getKey().equals("startDate")){

                            }
                        }
                        //durchlaufen der challenge Kinder
                    }
                }
            }
        }
        return result;
    }


    /**
     * This method checks, if a user with an email address exist and returns him. When the user doesn't
     * exist, the method returns null.
     * @param searchedEmail email address of the user
     * @return The user you are looking for or null
     */
    private User getUser(String searchedEmail){

        User user = null;

        if(dataSnapshotUsers.getValue() != null) {
            for(DataSnapshot d : dataSnapshotUsers.getChildren()) {

                if(d.child("email").getValue()!= null) {
                    if (d.child("email").getValue().equals(searchedEmail)) {
                        user = User.Create(d.getKey().toString());
                        user.setEmail(d.child("email").getValue().toString());

                        if (d.hasChildren()) {
                            Iterator<DataSnapshot> p = d.getChildren().iterator();
                            while (p.hasNext()) {
                                DataSnapshot dataSnapshotDiary = (DataSnapshot) p.next();
                                if(dataSnapshotDiary.getKey().equals("diary")){

                                    for(DataSnapshot child : dataSnapshotDiary.getChildren()){
                                    }
                                }
                            }
                        }
                        return user;


                    }
                }
            }
        }
        return  null;
    }

    /**
     * return the snapshot with registered users to this activity
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot) {
        this.dataSnapshotUsers = dataSnapshot;
        validSnapshotUsers = true;
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
