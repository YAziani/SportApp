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
import java.util.List;

public class ActivityChallenge extends AppCompatActivity {

    ChallengeViewAdapter challengeViewAdapter;
    ListView listView;
    List<User> userList;
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

        //challenge = new Challenge();

        // Now read the extra key - exerciseList
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if(extras!=null ) {


            //Get unpack the exerciseList und date
            challenge = (Challenge) extras.getSerializable(getString(R.string.Challenges));

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

        userList = challenge.getUserList();

        /*
        //Create listview
        listView = (ListView) findViewById(R.id.listViewChallenge);
        challengeViewAdapter = new ChallengeViewAdapter(ActivityChallenge.this, userList);
        listView.setAdapter(challengeViewAdapter);
*/
        //set TextView
        //textViewCountdown = (TextView) findViewById(R.id.textViewChallengeCountDown);

        //calculate the difference between today and the end date
        //todayDate = calendar.getTime();
       // endDate = challenge.getEndDate();
        //String  remainingDays = String.valueOf(challenge.getRemainingDays());
        //set the difference to the textview
        //textViewCountdown.setText(getString(R.string.VerbleibendeTage) + " " + remainingDays);

    }


    @Override
    protected void onStart() {
        super.onStart();
       // pd = new ProgressDialog(this);
        //pd.setMessage(getString( R.string.wird_geladen));
        //pd.show();

    }


    private void loadUsers(){

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "Challenges/" + challenge.getName() + "/Users/" );
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getChildren();

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        readUsers(child.getKey().toString());
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    private void readUsers(String username){

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() + "/Diary/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getChildren();

                    for(DataSnapshot child : dataSnapshot.getChildren()){

                        try {
                            Date curDate = sdf.parse(child.getKey());

                            if(curDate.after(challenge.getStartDate()) || curDate.equals(challenge.getStartDate())  || curDate.before(challenge.getEndDate()) || curDate.equals(challenge.getEndDate())){

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


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
