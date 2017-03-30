package com.example.mb7.sportappbp.Activity;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

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

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    DataSnapshot dataSnapshotChallenges;
    boolean validSnapshotChallenges = false;
    DataSnapshot dataSnapshotChallengeUsers;
    boolean validSnapshotChallengeUsers = false;
    DataSnapshot dataSnapshotUsers;
    boolean validSnapshotUsers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);


        // Now read the extra key - exerciseList
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {

            challenge = (Challenge) extras.getSerializable(getString(R.string.Challenges));
            strUSer = new LinkedList<String>();
            DAL_Challenges.getRegisteredUsersToChallenge(this, challenge.getName());
            DAL_RegisteredUsers.getRegisteredUsersChallenge(this);
            setTitle(challenge.getName());

            // read the datetime as this is the unique value in the db for the notification
            String notificationDate = (String) extras.get("NotificationDate");
            if (notificationDate != null) {
                removeNofication(this, notificationDate);
                askToEnterDialog(challenge);
            }


        } else {

            challenge = new Challenge();
            Toast.makeText(this, getString(R.string.ChallengeKonnteNichtGeladenWerdenVersuchenSieBitteErneut), Toast
                    .LENGTH_SHORT).show();
            //finish();

        }


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
        String remainingDays = String.valueOf(challenge.getRemainingDays());
        //set the difference to the textview
        //check if challenge finished and set counter
        if (endDate.before(todayDate))
            textViewCountdown.setText(getString(R.string.ChallengeBeendet));
        else
            textViewCountdown.setText(getString(R.string.VerbleibendeTage) + " " + remainingDays);


    }

    private void askToEnterDialog(final Challenge challenge) {

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
                })
                .setNegativeButton(R.string.Nein, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        challenge.RemoveInvitation(ActivityMain.getMainUser(ActivityChallenge.this));
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

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

                //final EditText editTextMailAddress = (EditText) addUserDialog.findViewById(R.id
                // .textChallengeMailAddress);

                //Create button to add a user to the userlist
                Button btnOk = (Button) addUserDialog.findViewById(R.id.btnChallengeOk);
                btnOk.setOnClickListener(btnOkAddUserDialogListener(addUserDialog));

                //create button to cancel the dialog
                Button btnCancel = (Button) addUserDialog.findViewById(R.id.btnChallengeCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addUserDialog.dismiss();
                    }
                });

            //case android.R.id.home:
              //  finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method creates an on click listener for the ok button from the add user dialog.
     * The method checks if the user exists or has already been added. If not, the user will
     * be added to the user list
     *
     * @param addUserDialog dialog object from the dialog to add an user
     * @return returns a onClickListener for the button
     **/
    private View.OnClickListener btnOkAddUserDialogListener(final Dialog addUserDialog) {

        final EditText editTextMailAddress = (EditText) addUserDialog.findViewById(R.id.textChallengeMailAddress);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the entered email address
                String mail = editTextMailAddress.getText().toString();

                //get user form database
                User user = getUser(editTextMailAddress.getText().toString());
                //check if user exist
                if (user != null) {
                    //user already added
                    if (userAlreadyInList(user))
                        Toast.makeText(ActivityChallenge.this, getString(R.string.BenutzerBereitshinzugefügt), Toast
                                .LENGTH_SHORT).show();
                        //add user and close dialog
                    else {
                        //Add User on Database
                        challenge.InviteUser(user);
                        challengeViewAdapter.notifyDataSetChanged();
                        addUserDialog.dismiss();
                    }
                }
                //if user doesn't exist
                else
                    Toast.makeText(ActivityChallenge.this, mail + " " + getString(R.string.konnteNichtGefundenWerden)
                            , Toast.LENGTH_SHORT).show();

            }
        };
        return listener;
    }

    /**
     * This method checks, if a user with an email address exist and returns him. When the user doesn't
     * exist, the method returns null.
     *
     * @param searchedEmail email address of the user
     * @return The user you are looking for or null
     */
    private User getUser(String searchedEmail) {

        User user = null;

        if (dataSnapshotUsers.getValue() != null) {
            for (DataSnapshot d : dataSnapshotUsers.getChildren()) {

                if (d.child("email").getValue() != null) {
                    if (d.child("email").getValue().equals(searchedEmail)) {
                        user = User.Create(d.getKey().toString());
                        user.setEmail(d.child("email").getValue().toString());


                        return user;
                    }
                }
            }
        }
        return null;
    }

    private boolean userAlreadyInList(User user) {

        boolean result = false;

        for (User u : userList) {
            if (user.getEmail().equals(u.getEmail()))
                result = true;
        }
        return result;
    }


    /**
     * Reads all users of the challenge from the database
     *
     * @return The user you are looking for or null
     */
    private void getUSers() {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        if (validSnapshotChallengeUsers) {
            if (dataSnapshotChallengeUsers.getValue() != null) {
                for (DataSnapshot d : dataSnapshotChallengeUsers.getChildren()) {

                    strUSer.add(d.getKey());
                }
            }
        }

        for (final String u : strUSer) {

            URL url = null;
            try {
                url = new URL(DAL_Utilities.DatabaseURL + "users/" + u + "/Diary/");
                final Firebase root = new Firebase(url.toString());

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int totalPoints = 0;

                        //day
                        for (DataSnapshot child1Date : dataSnapshot.getChildren()) {

                            try {
                                Date date = sdf.parse(child1Date.getKey());

                                if (date.after(challenge.getStartDate()) || date.equals(challenge.getStartDate()) ||
                                        date.equals(challenge.getEndDate()) || date.before(challenge.getEndDate())) {
                                    //time
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
        getUSers();
    }

    /**
     * return the snapshot with registered users to this activity
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot) {
        this.dataSnapshotUsers = dataSnapshot;
        validSnapshotUsers = true;
    }


    /**
     * return the snapshot with registered challenges to this activity
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredChallenges(DataSnapshot dataSnapshot) {
        this.dataSnapshotChallenges = dataSnapshot;
        validSnapshotChallenges = true;
    }

}
