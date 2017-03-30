package com.example.mb7.sportappbp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.ChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.Comparator.UserSortPoints;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Challenges;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_RegisteredUsers;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

/**
 * This class shows the ranking of the challenge
 * Created by Sebastian
 */
public class ActivityChallenge extends AppCompatActivity {

    ChallengeViewAdapter challengeViewAdapter;
    ListView listView;
    ArrayList<User> userList;
    Challenge challenge;
    TextView textViewCountdown;
    Calendar calendar = Calendar.getInstance();
    Date todayDate;
    Date endDate;
    LinkedList<String> strUSer;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    DataSnapshot dataSnapshotChallengeUsers;
    boolean validSnapshotChallengeUsers = false;
    DataSnapshot dataSnapshotUsers;
    boolean validSnapshotUsers = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);


        // Now read the extra key -  the notification and pressed challenge
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {

            //unpack the data of the challenge and load the users datasnapshot from all users of the
            // challenge and all registered users from the database
            challenge = (Challenge) extras.getSerializable(getString(R.string.Challenges));
            strUSer = new LinkedList<String>();

            //set the title of the activity with the challenge name


            // read the datetime as this is the unique value in the db for the notification
            String notificationDate = (String) extras.get("NotificationDate");
            if (notificationDate != null) {
                removeNofication(this, notificationDate);

                //get the name of the challenge and the dates out of the text and create the object
                challenge = new Challenge();
                String subtext = (String) extras.get("Subtext");
                int StartPosName= subtext.indexOf("'");
                int EndPosName= subtext.indexOf("'",StartPosName+1);
                String text = subtext.substring(StartPosName+1,EndPosName);
                String startDatum = subtext.substring(EndPosName+8,EndPosName+18);
                String endDatum = subtext.substring(EndPosName+23);

                challenge.setName(text);
                try {
                    challenge.setStartDate(sdf.parse(startDatum));
                    challenge.setEndDate(sdf.parse(endDatum));

                    askToEnterDialog();
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }


        } else {//if the challenge wasn't in the package, give a feedbac to the user

            finish();
            Toast.makeText(this, getString(R.string.ChallengeKonnteNichtGeladenWerdenVersuchenSieBitteErneut), Toast
                    .LENGTH_SHORT).show();

        }

        setTitle(challenge.getName());
        DAL_Challenges.getRegisteredUsersToChallenge(this, challenge.getName());
        userList = new ArrayList<User>();


        //Create listview to show all users with their points in a ranking
        listView = (ListView) findViewById(R.id.listViewChallenge);
        challengeViewAdapter = new ChallengeViewAdapter(ActivityChallenge.this, userList);
        listView.setAdapter(challengeViewAdapter);


        //set TextView
        textViewCountdown = (TextView) findViewById(R.id.textViewChallengeCountDown);

        //calculate the difference between today and the end date
        todayDate = calendar.getTime();
        endDate = challenge.getEndDate();
        String remainingDays = String.valueOf(challenge.getRemainingDays());
        //set the difference to the textview
        //check if challenge finished
        if (endDate.before(todayDate))
            textViewCountdown.setText(getString(R.string.ChallengeBeendet));
        else
            textViewCountdown.setText(getString(R.string.VerbleibendeTage) + " " + remainingDays);


    }

    /**
     * This method opens a dialog of the invitation. The user can join the challenge, the data will be added to the
     * database, or reject the invitation and nothing will happen
     *
     */
    private void askToEnterDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityChallenge.this);
        builder.setMessage(getString(R.string.MöchtenSieAnDerChallenge) + " " + challenge.getName() + "\n" +
                " " + getString(R.string.vom) + " " + sdf.format(challenge.getStartDate()).toString() + " " +
                getString(R.string.bisZum) + " " +
                sdf.format(challenge.getEndDate()).toString() + " \n"
                + getString(R.string.beitreten))
                .setPositiveButton(R.string.Ja, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        challenge.AddUser(ActivityMain.getMainUser(ActivityChallenge.this));
                        challenge.RemoveInvitation(ActivityMain.getMainUser(ActivityChallenge.this));

                    }
                })//remove the invitation from the database
                .setNegativeButton(R.string.Nein, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        challenge.RemoveInvitation(ActivityMain.getMainUser(ActivityChallenge.this));
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

    /**
     * This method removes the notification in the notification frame.
     * @param context current context
     * @param notificationDate the notification to remove
     */
    void removeNofication(Context context, String notificationDate) {
        // get the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        // build the current URL
        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + preferences.getString
                ("logedIn", "") + "/Notifications/");
        ref.child(context.getString(R.string.Challenge)).child(notificationDate).removeValue();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //set the menu with add and save icon
        inflater.inflate(R.menu.menu_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_add:

                //create dialog
                final Dialog addUserDialog = new Dialog(ActivityChallenge.this);
                //set the layout for the dialog window
                addUserDialog.setContentView(R.layout.dialog_entertext);

                addUserDialog.show();

                //Create button to add a user to the user list
                Button btnOk = (Button) addUserDialog.findViewById(R.id.btnChallengeOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                EditText editTextMailAddress = (EditText) addUserDialog.
                                        findViewById(R.id.textChallengeMailAddress);
                                //get the entered email address
                                String mail = editTextMailAddress.getText().toString();
                                //first load a snapshot from the database with all registered users and
                                // then search for the mail address
                                DAL_RegisteredUsers.checkRegisteredUsersChallenge(ActivityChallenge.this,
                                        editTextMailAddress.getText().toString(), addUserDialog);

                            }});



                //create button to cancel the dialog
                Button btnCancel = (Button) addUserDialog.findViewById(R.id.btnChallengeCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addUserDialog.dismiss();
                    }
                });
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");

        }
    }

    /**
     * This method checks the entered email adress from the dialog window and evaluate it. If the user exist
     * or has already been added. else the user will be added
     *
     * @param searchedEmail email address of the user
     * @param dialog dialog window where the address was entered
     */
    private void checkRegisteredUsers(String searchedEmail, Dialog dialog) {

        User user = null;

        if (dataSnapshotUsers.getValue() != null) {
            for (DataSnapshot d : dataSnapshotUsers.getChildren()) {
                //recover the user if it it equals
                if (d.child("email").getValue() != null) {
                    if (d.child("email").getValue().equals(searchedEmail)) {
                        user = User.Create(d.getKey().toString());
                        user.setEmail(d.child("email").getValue().toString());
                    }
                }
            }

            if (user != null) {
                //user already added
                if (userAlreadyInList(user) || user.getName().equals(ActivityMain.getMainUser(this).getName()))
                    Toast.makeText(ActivityChallenge.this, getString(R.string.BenutzerBereitshinzugefügt),
                            Toast.LENGTH_SHORT).show();
                    //add user and close dialog
                else {
                    userList.add(user);
                    challengeViewAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
            //if user doesn't exist
            else
                Toast.makeText(ActivityChallenge.this, searchedEmail + " " + getString(R.string
                        .konnteNichtGefundenWerden),
                        Toast.LENGTH_SHORT).show();

        }
    }


    /**
     * Checks if a users has already been added to the challenge
     * @param user selected user to add
     * @return truw when the user exists else false
     */
    private boolean userAlreadyInList(User user) {

        boolean result = false;

        for (User u : userList) {
            if (user.getName().equals(u.getName()))
                result = true;
        }
        return result;
    }


    /**
     * Reads and calculates the total points (diary Entries) from all challenge user from the datasnapshot,
     * which one was loaded at the beginning
     * of the class
     *
     * @return The user you are looking for or null
     */
    private void getChallengeUsers() {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //first get all usernames of all challenge members and add him the list
        if (validSnapshotChallengeUsers) {
            if (dataSnapshotChallengeUsers.getValue() != null) {
                for (DataSnapshot d : dataSnapshotChallengeUsers.getChildren()) {

                    strUSer.add(d.getKey());
                }
            }
        }

        //load points for each user of the list
        for (final String u : strUSer) {

            URL url = null;
            try {
                url = new URL(DAL_Utilities.DatabaseURL + "users/" + u + "/Diary/");
                final Firebase root = new Firebase(url.toString());

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int totalPoints = 0;

                        //go to each day of diaryEntries
                        for (DataSnapshot child1Date : dataSnapshot.getChildren()) {

                            try {
                                //recover the date object
                                Date date = sdf.parse(child1Date.getKey());

                                if (date.after(challenge.getStartDate()) || date.equals(challenge.getStartDate()) ||
                                        date.equals(challenge.getEndDate()) || date.before(challenge.getEndDate())) {
                                    //go threw all entries of a day
                                    for (DataSnapshot child2 : child1Date.getChildren()) {
                                        //look for totalpoints
                                        for (DataSnapshot child3 : child2.getChildren()) {
                                            if (child3.getKey().equals("totalPoints"))
                                                totalPoints = totalPoints + Integer.valueOf(child3.getValue()
                                                        .toString());
                                        }
                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        //create a user object and set  name and points and sort the lost
                        User user = User.Create(u);
                        user.setPoints(totalPoints);
                        userList.add(user);

                        Collections.sort(userList, new UserSortPoints());
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
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredChallengeUsers(DataSnapshot dataSnapshot) {
        this.dataSnapshotChallengeUsers = dataSnapshot;
        validSnapshotChallengeUsers = true;
        getChallengeUsers();
    }

    /**
     * return the snapshot with registered users of a challenge to this activity and starts
     * the method to recover the users out of the snapshot
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot, String searchedUser, Dialog dialog) {
        this.dataSnapshotUsers = dataSnapshot;
        validSnapshotUsers = true;
        checkRegisteredUsers(searchedUser, dialog);
    }

}
