package com.example.mb7.sportappbp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mb7.sportappbp.Activity.ActivityCategories;
import com.example.mb7.sportappbp.Activity.ActivityChallenge;
import com.example.mb7.sportappbp.Activity.ActivityDiary;
import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityKompass;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityNewChallenge;
import com.example.mb7.sportappbp.ClientIF;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_User;
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by MB7 on 07.01.2017.
 */

public class TbReportContent extends TabFragment{


    Button btnChallenge;
    Button btnDiary;
    View view;
    public TextView txtRequest;
    private Firebase firebase;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setTitle("Berichte");
        view = inflater.inflate(R.layout.tbreportcontent, container, false);
       // asyncTask.delegate = this;

        Firebase.setAndroidContext(this.getActivity());
        firebase = new Firebase("https://sportapp-cbd6b.firebaseio.com/");
        txtRequest = (TextView) view.findViewById(R.id.txtRequestTest);

        btnDiary = (Button) view.findViewById(R.id.btndiary);
        btnDiary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DAL_User.GetStimmnungsabfrage(ActivityMain.mainUser,"20170325");
                Intent open = new Intent(getActivity(), ActivityDiary.class);
                startActivity(open);

            }
        });

        Button btnDiaryEntry = (Button) view.findViewById(R.id.btnDiaryEntry);
        btnDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(getActivity(), ActivityCategories.class);
                startActivity(open);
            }
        });

        Button btnNewChallenge = (Button) view.findViewById(R.id.btnNewChallenge1);
        btnNewChallenge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent open = new Intent(getActivity(), ActivityNewChallenge.class);
                startActivity(open);

            }
        });

        btnChallenge = (Button) view.findViewById(R.id.btnNewChallenge);
        btnChallenge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                Intent open = new Intent(getActivity(), ActivityChallenge.class);
                startActivity(open);
            }
        });

        //testMo(view);
        return view;
    }

    private void Btn1_Clic(){

    }

    private void testMo(View view){
        btnChallenge = (Button)view.findViewById(R.id.btndiary);
        btnChallenge.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent open = new Intent(ActivityMain.activityMain, ActivityKompass.class);
                ActivityMain.activityMain.startActivity(open);

            }
        });

    }

    private void testBasti(View view){
        btnChallenge = (Button) view.findViewById(R.id.btndiary);
        btnChallenge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Firebase firebaseChild = firebase.child("MyChildTest");
                //firebaseChild.setValue("Mo B");

                Intent open = new Intent(getActivity(), ActivityDiaryEntry.class);
                startActivity(open);
            }
        });
    }



    abstract  class  MyDownloadTask extends AsyncTask<Object,Void,String> implements ClientIF {
        String url ="";

        public MyDownloadTask(String url)
        {
            this.url = url;
        }

        TbReportContent tbReportContent;
        public void Initialize(TbReportContent tb)
        {
            tbReportContent = tb;
        }

        // abstract function that mus be implemented
        // this function receives the result value and can be passed to the caller
        public abstract void onResponseReceived(String result);
        protected void onPreExecute() {
            //display progress dialog.

        }


        @Override
        protected void onPostExecute(String s) {
            onResponseReceived(s);
        }

        // function that is called after calling the execute() function by the caller
        @Override
        protected String doInBackground(Object... params) {
            String s = "";
            try
            {
                URL url = new URL(this.url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
                s = total.toString();
               // delegate.processFinish(s);

            }
            catch (Exception ex)
            {
                s = ex.getMessage();
            }
            finally
            {

            }
            return  s;
        }
    }

}
