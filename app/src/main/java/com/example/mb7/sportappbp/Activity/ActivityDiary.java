package com.example.mb7.sportappbp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.mb7.sportappbp.Adapters.DiaryViewAdapterNew;
import com.example.mb7.sportappbp.BusinessLayer.LeistungstestsExercise;
import com.example.mb7.sportappbp.BusinessLayer.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.BusinessLayer.TrainingExercise;
import com.example.mb7.sportappbp.BusinessLayer.WellnessExercise;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.BusinessLayer.DiaryEntry;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

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
        setContentView(R.layout.activity_lst_stimmungsabfrage);

        activityDiary = this;

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_stmAbfrage);
        registerForContextMenu(rv);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_stmAbfrage)
        {
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

                //dialog window zum Datum bestimmen
                Intent open = new Intent(activityDiary, ActivityDiaryEntry.class);
                startActivity(open);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.deleteItem:
                deleteDiaryEntry( ((DiaryViewAdapterNew)rv.getAdapter()).getSelectedObject());
                Toast.makeText(this,getString(R.string.erfolgreichgeloescht),Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        ArrayList<Exercise> result;

        //check the correctness of the received packages
        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){

            //get and set the result of the request
            result = data.getParcelableArrayListExtra("newExercises");
            exerciseList = result;
            diaryEntry.setExerciseList(result);

        }
    }

    /**
     * This method start notifyDataSetChanged of the view adapter. When data has been changed in
     * the AllDiaryEntries object from external, this method has to be executed
     */
    public static void notifyDataChanged(){
        diaryViewAdapter.notifyDataSetChanged();
    }


    /**
     * this method send the exerciseList of the selected diaryEntry to the diaryEntry Activity.
     * Furthermore this method sends a request to receive the prepared exerciseList
     */
    public void sendOldAndRequestNewExerciseList(){
        ArrayList<Exercise> oldList = exerciseList;
        //set goal activity
        Intent pickExerciseIntent = new Intent(this, ActivityDiaryEntry.class);
        //List in package
        pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        //send package and open the goal activity
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

    /**
     * delete a diaryEntry
     * @param diaryEntry the object to delete
     */
    private void deleteDiaryEntry(DiaryEntry diaryEntry)
    {
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" +ActivityMain.mainUser.getName() + "/Diary/" );

        ref.child(firebasedate.format(diaryEntry.getDate())).child(firebasetime.format(diaryEntry.getDate())).removeValue();
    }


    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString( R.string.wird_geladen));
        pd.show();
        readDiaryEntry();
    }

    private void readDiaryEntry(){
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd,HH:mm:ss");
        final SimpleDateFormat sdfDateDiary = new SimpleDateFormat("dd.MM.yyyy");

        try
        {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.mainUser.getName()+ "/Diary/");
            final Firebase root = new Firebase(url.toString());

            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    diaryEntries = new LinkedList<DiaryEntry>();

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        //date yyyyMMdd
                        final String strDate = child.getKey();

                        root.child(strDate).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    //time
                                    final String strTime = child.getKey();

                                    root.child(strDate).child(strTime).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //get instance to add all diaryentries after restore

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


                                            diaryEntries.add(diaryEntry);


                                            //restore diaryEntry object
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {

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

                                            if (diaryEntries.size() != 0) {
                                                // reverse the list to get the newest first
                                                Collections.reverse(diaryEntries);
                                                // fill the recycler
                                                LinearLayoutManager lm = new LinearLayoutManager(activityDiary);
                                                rv.setLayoutManager(lm);
                                                // just create a list of tasks
                                                rv.setAdapter(new DiaryViewAdapterNew(diaryEntries, activityDiary));
                                                pd.dismiss();
                                            }
                                        }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                        }

                                    });
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });



        }
        catch (Exception e)
        {
            Log.d("ERROR", e.getMessage());

        }
    }

}
