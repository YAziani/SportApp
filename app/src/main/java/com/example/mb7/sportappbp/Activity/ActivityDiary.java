package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.DiaryViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.LeistungstestsExercise;
import com.example.mb7.sportappbp.BusinessLayer.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.BusinessLayer.TrainingExercise;
import com.example.mb7.sportappbp.BusinessLayer.WellnessExercise;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.BusinessLayer.DiaryEntry;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

/**
 * This class shows all diary entries and you can add and prepare them.
 * You can also start to create a new one
 * Created by Sebastian
 */

public class ActivityDiary extends AppCompatActivity {

    final static int REQUEST_ID = 555;
    ActivityDiary activityDiary = null;
    RecyclerView rv;
    ProgressDialog pd;


    static DiaryViewAdapter diaryViewAdapter;
    ListView listView;
    // AllDiaryEntries allDiaryEntries;
    DiaryEntry diaryEntry;
    LinkedList<DiaryEntry> diaryEntries;

    ArrayList<Exercise> exerciseList;

    SimpleDateFormat firebasedate = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat firebasetime = new SimpleDateFormat("HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        activityDiary = this;

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_diary);
        registerForContextMenu(rv);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //set recycler for card view and a context menu with delete and prepare
        if (v.getId() == R.id.recycler_diary) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_item_delete, menu);
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

                //open activity to create a new diaryEntry
                Intent open = new Intent(activityDiary, ActivityCategories.class);
                startActivity(open);
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteItem: //delete pressed item and give a feedbacl
                deleteDiaryEntry(((DiaryViewAdapter) rv.getAdapter()).getSelectedObject());
                Toast.makeText(this, getString(R.string.erfolgreichgeloescht), Toast.LENGTH_SHORT).show();
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");

        }

    }


    /**
     * receive and unpack a package of a request from an activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        ArrayList<Exercise> result;

        //check the correctness of the received packages
        if (resultCode == RESULT_OK && requestCode == REQUEST_ID) {

            //get and set the result of the request
            result = data.getParcelableArrayListExtra("newExercises");
            exerciseList = result;
            diaryEntry.setExerciseList(result);

        }
    }

    /**
     * delete a diaryEntry in the database
     *
     * @param diaryEntry the object to delete
     */
    private void deleteDiaryEntry(DiaryEntry diaryEntry) {
        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL+ "users/" + ActivityMain.getMainUser
                (this).getName() + "/Diary/");

        ref.child(firebasedate.format(diaryEntry.getDate())).child(firebasetime.format(diaryEntry.getDate()))
                .removeValue();
    }


    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        readDiaryEntry();
    }

    /**
     * this method recovers all diary entries of a users completely from the database
     */
    private void readDiaryEntry() {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
        final SimpleDateFormat sdfDateDiary = new SimpleDateFormat("dd.MM.yyyy");

        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/Diary/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    diaryEntries = new LinkedList<DiaryEntry>();

                    //recover of each day
                    for (DataSnapshot childDate : dataSnapshot.getChildren()) {
                        //date yyyyMMdd
                        final String strDate = childDate.getKey();

                        //recovering diary entry from each team of a day
                        for (DataSnapshot childTime : childDate.getChildren()) {
                            //time
                            final String strTime = childTime.getKey();

                            //create object
                            DiaryEntry diaryEntry = new DiaryEntry();

                            //restore date from diaryEntry
                            try {
                                Date date = sdfDate.parse(strDate + "," + strTime);
                                diaryEntry.setDate(date);
                                diaryEntry.setDate(date);
                                diaryEntry.sDate = sdfDateDiary.format(date);
                                diaryEntry.sTime = strTime;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //add the recovered entry to the list
                            diaryEntries.add(diaryEntry);


                            //restore diaryEntry object
                            for (DataSnapshot child : childTime.getChildren()) {

                                //restore totalPoints from diaryEntry
                                if (child.getKey().equals("totalpoints"))
                                    diaryEntry.setTotalPoints(Integer.getInteger(child.getValue().toString()));
                                    //restore exercises from diaryEntry
                                else if (child.getKey().startsWith("exercise")) {
                                    //get the category of the stores exercise
                                    String category = child.getChildren().iterator().next().getValue().toString();

                                    Exercise exercise = null;
                                    //create the exercise object from the right class and restore the attribute
                                    if (category.equals("Training")) {
                                        exercise = new TrainingExercise();
                                        exercise = child.getValue(TrainingExercise.class);
                                    } else if (category.equals("Leistungstests")) {
                                        exercise = new LeistungstestsExercise();
                                        exercise = child.getValue(LeistungstestsExercise.class);
                                    } else if (category.equals("ReinerAufenthalt")) {
                                        exercise = new ReinerAufenthaltExercise();
                                        exercise = child.getValue(ReinerAufenthaltExercise.class);
                                    } else if (category.equals("Wellness")) {
                                        exercise = new WellnessExercise();
                                        exercise = child.getValue(WellnessExercise.class);
                                    }
                                    //after restoring of the exercise, these has to be added to the diaryEntry
                                    if (exercise != null)
                                        diaryEntry.addExercise(exercise);
                                }


                            }
                        }
                    }


                    if (diaryEntries != null) {
                        // reverse the list to get the newest first
                        Collections.reverse(diaryEntries);
                        // fill the recycler
                        LinearLayoutManager lm = new LinearLayoutManager(activityDiary);
                        rv.setLayoutManager(lm);
                        // just create a list of tasks
                        rv.setAdapter(new DiaryViewAdapter(diaryEntries, activityDiary));
                    }
                    pd.dismiss();
                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());

        }
    }
}
