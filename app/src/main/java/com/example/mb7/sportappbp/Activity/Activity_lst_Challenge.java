package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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

import com.example.mb7.sportappbp.Adapters.ChallengeLstViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.User;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Activity_lst_Challenge extends AppCompatActivity {

    Activity_lst_Challenge activityLstChallenge = null;
    ArrayList<String> strChallengeList;
    List<Challenge> challenges;
    RecyclerView rv;
    Activity_lst_Challenge activity_lst_challenge = this;
    ProgressDialog pd;

    Challenge challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_challenge);

        setTitle(getString(R.string.Challenges));


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

        activityLstChallenge = this;
        strChallengeList = new ArrayList<String>();

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_challenge);
        registerForContextMenu(rv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_challenge) {
            MenuInflater inflater = getMenuInflater();
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
            case R.id.icon_add:

                Intent open = new Intent(activity_lst_challenge, ActivityNewChallenge.class);
                startActivity(open);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.leaveItem:
                deleteChallenge(((ChallengeLstViewAdapter) rv.getAdapter()).getSelectedObject());
                Toast.makeText(this, getString(R.string.erfolgreichgeloescht), Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

    void removeNofication(Context context, String notificationDate) {
        // get the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        // build the current URL
        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + preferences.getString
                ("logedIn", "") + "/Notifications/");
        ref.child(context.getString(R.string.Challenge)).child(notificationDate).removeValue();

    }

    /**
     * delete all reverences from user to challenge and the other way round
     *
     * @param challenge the object to remove
     */
    private void deleteChallenge(Challenge challenge) {
        //// TODO: 26.03.2017 stuaryt ab
        challenge.RemoveUser(ActivityMain.getMainUser(this));
        strChallengeList.remove(challenge.getName());

    }


    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        loadChallenges();
    }


    private void loadChallenges() {


        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/Challenges/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    challenges = new LinkedList<Challenge>();


                    for (DataSnapshot name : dataSnapshot.getChildren()) {
                        Challenge challenge = new Challenge();
                        challenge.setName(name.getKey());

                        for (DataSnapshot childName : name.getChildren()) {

                            //start date
                            if (childName.getKey().equals("startDate")) {
                                try {
                                    challenge.setStartDate(sdfDate.parse(childName.getValue().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //set end date
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
