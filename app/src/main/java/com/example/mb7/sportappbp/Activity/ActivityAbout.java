package com.example.mb7.sportappbp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mb7.sportappbp.R;

public class ActivityAbout extends AppCompatActivity {

    TextView txtLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        txtLink = (TextView) findViewById(R.id.txtLink);
        txtLink.setText("http://ww.freepik.com/free-vector/icons-for-business_957351.htm");
    }
}
