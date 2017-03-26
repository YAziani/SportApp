package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.ChallengeLstViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_challenge);

        setTitle(getString(R.string.Challenges));

        activityLstChallenge = this;
        strChallengeList = new ArrayList<String>();

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_challenge);
        registerForContextMenu(rv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_challenge)
        {
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

        switch (item.getItemId())
        {
            case R.id.leaveItem:
                deleteChallenge( ((ChallengeLstViewAdapter)rv.getAdapter()).getSelectedObject());
                Toast.makeText(this,getString(R.string.erfolgreichgeloescht),Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

    /**
     * delete all reverences from user to challenge and the other way round
     * @param challenge the object to remove
     */
    private void deleteChallenge(Challenge challenge)
    {
        //// TODO: 26.03.2017 stuaryt ab
        challenge.RemoveUser(ActivityMain.mainUser);
        strChallengeList.remove(challenge.getName());

    }


    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString( R.string.wird_geladen));
        pd.show();
        loadChallenges();
    }


    private void loadChallenges(){

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.mainUser.getName()+ "/Challenges/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getChildren();

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        readChallengesDate(child.getKey().toString());
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


    void readChallengesDate(String name) {
        URL url = null;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        challenges = new LinkedList<Challenge>();

            try {
                url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.mainUser.getName() + "/Challenges/" + name + "/");
                final Firebase root = new Firebase(url.toString());

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getChildren();

                            Challenge challenge = new Challenge();
                            challenge.setName(dataSnapshot.getKey());
                            try {
                                challenge.setEndDate(sdf.parse(dataSnapshot.getValue().toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            challenges.add(challenge);


                        if (challenges != null) {
                            // reverse the list to get the newest first
                            Collections.reverse(challenges);
                            // fill the recycler
                            LinearLayoutManager lm = new LinearLayoutManager(activity_lst_challenge);
                            rv.setLayoutManager(lm);
                            // just create a list of tasks
                            rv.setAdapter(new ChallengeLstViewAdapter(challenges, activity_lst_challenge));
                            pd.dismiss();
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




}
