package com.example.mb7.sportappbp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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


    Button testBtn;
    Button testBtn2;
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
        testBasti(view);
        testBasti2(view);

        return view;
    }

    private void testBasti(View view){
        testBtn = (Button) view.findViewById(R.id.testbutton);
        testBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Firebase firebaseChild = firebase.child("MyChildTest");
                firebaseChild.setValue("Mo B");


            }
        });
    }



    abstract  class  MyDownloadTask extends AsyncTask<Object,Void,String> implements ClientIF{
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

    private void testBasti2(View view){
        testBtn2 = (Button) view.findViewById(R.id.testbutton2);
        testBtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MyDownloadTask asyncTask = new MyDownloadTask("https://sportapp-cbd6b.firebaseio.com/MyChildTest.json?print=pretty")
                {
                    @Override
                    public void onResponseReceived(String result) {
                        // set the control in the caller
                        txtRequest.setText(result.toString());

                    }
                };
                // run the background task to read from the database
                asyncTask.execute();


            }
        });
    }

}
