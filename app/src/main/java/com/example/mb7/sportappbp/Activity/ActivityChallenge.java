package com.example.mb7.sportappbp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.mb7.sportappbp.Adapters.ChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

public class ActivityChallenge extends AppCompatActivity {

    ChallengeViewAdapter challengeViewAdapter;
    ListView listView;
    ArrayList<User> userList;

    boolean hasChallenge = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);


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



        listView = (ListView) findViewById(R.id.listViewChallenge);
        challengeViewAdapter = new ChallengeViewAdapter(ActivityChallenge.this, userList);
        listView.setAdapter(challengeViewAdapter);



    }
}
