package com.tud.bp.fitup.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tud.bp.fitup.Adapters.ChallengeLstViewAdapter;
import com.tud.bp.fitup.BusinessLayer.Challenge;
import com.tud.bp.fitup.DataAccessLayer.DAL_Utilities;
import com.tud.bp.fitup.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class shows all existing diary entries.
 * You can open prepare each or start to create a new one
 */

public class Activity_lst_Challenge extends AppCompatActivity {

    Activity_lst_Challenge activityLstChallenge = null;
    ArrayList<String> strChallengeList;
    List<Challenge> challenges;
    RecyclerView rv;
    Activity_lst_Challenge activity_lst_challenge = this;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_challenge);

        //Set the title of the class
        setTitle(getString(R.string.Challenges));
        activityLstChallenge = this;
        strChallengeList = new ArrayList<String>();

        // Now receive and read data from the notification
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {
            // read the datetime as this is the unique value in the db for the notification
            String notificationDate = (String) extras.get("NotificationDate");
            if (notificationDate != null) {
                removeNofication(this, notificationDate);
            }
        }

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_challenge);
        registerForContextMenu(rv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_challenge) {
            MenuInflater inflater = getMenuInflater();

            //set the context with prepare and remove
            inflater.inflate(R.menu.menu_item_leave, menu);
        }
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

            case R.id.icon_add: //Open a the new activity to create a new challenge
                Intent open = new Intent(activity_lst_challenge, ActivityNewChallenge.class);
                startActivity(open);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) { //delete the item and give a feedback to the user
            case R.id.leaveItem:
                deleteChallenge(((ChallengeLstViewAdapter) rv.getAdapter()).getSelectedObject());
                Toast.makeText(this, getString(R.string.erfolgreichgeloescht), Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
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

    /**
     * remove all reverences from user to challenge and the other way round
     *
     * @param challenge the object to remove
     */
    private void deleteChallenge(Challenge challenge) {
        challenge.RemoveUser(ActivityMain.getMainUser(this));
        strChallengeList.remove(challenge.getName());

    }


    @Override
    protected void onStart() {
        //open dialog with loading
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        loadChallenges();
    }


    /**
     * This method loads all challenges from the user. It loads the
     * name, end date and start date of the challenge from the database
     */
    private void loadChallenges() {


        //date format of the saved data
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/Challenges/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    challenges = new LinkedList<Challenge>();

                    //set the name of all callenges
                    for (DataSnapshot name : dataSnapshot.getChildren()) {
                        Challenge challenge = new Challenge();
                        challenge.setName(name.getKey());

                        //set the dates of all challenges and recover the date objects
                        for (DataSnapshot childName : name.getChildren()) {

                            if (childName.getKey().equals("startDate")) {
                                try {
                                    challenge.setStartDate(sdfDate.parse(childName.getValue().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (childName.getKey().equals("endDate")) {
                                try {
                                    challenge.setEndDate(sdfDate.parse(childName.getValue().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        challenges.add(challenge);
                    }

                    if (challenges.size() > 0) {
                        // reverse the list to get the newest first
                        Collections.reverse(challenges);
                        // fill the recycler
                        LinearLayoutManager lm = new LinearLayoutManager(activity_lst_challenge);
                        rv.setLayoutManager(lm);
                        // just create a list of tasks
                        rv.setAdapter(new ChallengeLstViewAdapter(challenges, activity_lst_challenge));
                    }
                    // close the progress dialog
                    pd.dismiss();

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
