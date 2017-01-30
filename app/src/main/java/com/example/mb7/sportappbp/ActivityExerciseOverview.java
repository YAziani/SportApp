package com.example.mb7.sportappbp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.Objects.TrainingExercise;

import java.util.ArrayList;


public class ActivityExerciseOverview extends AppCompatActivity {

    final static int REQUEST_ID = 2;

    ListView listView;
    ExerciseViewAdapter exerciseViewAdapter;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Exercise> actLst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_overview);

        actLst = receiveExerciseList();

        listView = (ListView)findViewById(R.id.listviewActivityOverview);
        exerciseViewAdapter = new ExerciseViewAdapter(ActivityExerciseOverview.this, actLst);
        listView.setAdapter(exerciseViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
        inflater.inflate(R.menu.menu_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:
                sendOldAndRequestNewExerciseList(actLst);
                return true;
            case R.id.icon_save:
                returnResult();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){
            ArrayList<Exercise> result = data.getParcelableArrayListExtra("newExercises");
                //this.actLst = result;

            Integer test = result.size();

            setnewList(actLst, result);
            exerciseViewAdapter.notifyDataSetChanged();

            Toast.makeText(ActivityExerciseOverview.this, test.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    private ArrayList<Exercise> receiveExerciseList(){

        ArrayList<Exercise> result;
        final Bundle extra = getIntent().getExtras();

        if (extra != null) {
            result = extra.getParcelableArrayList("oldExercises");
            return result;
        }
        else
        return result = new ArrayList<Exercise>();
    }

    private void sendOldAndRequestNewExerciseList(ArrayList<Exercise> oldList){
        Intent pickExerciseIntent = new Intent(this, ActivityCategories.class);
        //Optional, wenn bereits ausgewehlte makiert werden sollen
        //pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

    private void setnewList(ArrayList<Exercise> oldLst, ArrayList<Exercise> newLst){
        for(Exercise i : newLst)
            oldLst.add(i);
    }

    private void returnResult(){

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("newExercises",actLst);
        setResult(RESULT_OK, intent);
        finish();

    }
}
