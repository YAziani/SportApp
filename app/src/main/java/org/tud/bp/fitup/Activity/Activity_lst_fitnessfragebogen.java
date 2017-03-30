package com.tud.bp.fitup.Activity;

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
import android.widget.Toast;

import com.tud.bp.fitup.Adapters.FitnessFrageViewAdapter;
import com.tud.bp.fitup.BusinessLayer.FitnessFragebogen;
import com.tud.bp.fitup.DataAccessLayer.DAL_Utilities;
import com.tud.bp.fitup.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Felix on 26.03.2017.
 */
public class Activity_lst_fitnessfragebogen extends AppCompatActivity {
    Activity_lst_fitnessfragebogen activityLstFitnessfragebogen = null;
    List<FitnessFragebogen> FitnessFragebogenList;
    RecyclerView rv;
    Activity_lst_fitnessfragebogen activity_lst_fitnessfragebogen = this;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_fitnessfragebogen);
        setTitle(getString(R.string.fitnessfragebogen));
        activityLstFitnessfragebogen = this;
        rv = (RecyclerView) findViewById(R.id.recycler_fitnessfragebogen);
        registerForContextMenu(rv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_fitnessfragebogen) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_item_delete, menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_add:
                InsertFitnessFragebogen();
                break;
            default: throw new InvalidParameterException("The item is not declared!");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteItem:
                deleteFitnessFragebogen(((FitnessFrageViewAdapter) rv.getAdapter()).getSelectedObject());
                Toast.makeText(this, getString(R.string.erfolgreichgeloescht), Toast.LENGTH_SHORT).show();
                break;
            default: throw new InvalidParameterException("The item is not declared!");
        }
        return super.onContextItemSelected(item);
    }

    /**
     * @param fitnessFragebogen the object to delete
     */
    private void deleteFitnessFragebogen(FitnessFragebogen fitnessFragebogen) {
        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser
                (this).getName() + "/FitnessFragebogen/");
        ref.child(fitnessFragebogen.FirebaseDate).removeValue();
    }

    private void InsertFitnessFragebogen() {
        Intent open = new Intent(activity_lst_fitnessfragebogen, ActivityFitnessFragebogen.class);
        startActivity(open);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        readFitnessFragebogen();
    }

    /**
     * Read FitnessFragebogen out of Firebase
     */
    void readFitnessFragebogen() {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/FitnessFragebogen/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    FitnessFragebogenList = new LinkedList<FitnessFragebogen>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        final String sDate = child.getKey();

                        FitnessFragebogen fitnessFragebogen = child.getValue(FitnessFragebogen.class);
                        fitnessFragebogen.FirebaseDate = sDate;
                        fitnessFragebogen.Date = DAL_Utilities.ConvertFirebaseStringNoSpaceToDateString(sDate);
                        FitnessFragebogenList.add(fitnessFragebogen);
                    }

                    if (FitnessFragebogenList != null) {
                        Collections.reverse(FitnessFragebogenList);
                        LinearLayoutManager lm = new LinearLayoutManager(activityLstFitnessfragebogen);
                        rv.setLayoutManager(lm);
                        rv.setAdapter(new FitnessFrageViewAdapter(FitnessFragebogenList, activityLstFitnessfragebogen));
                    }

                    // close the progress dialog
                    pd.dismiss();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } catch (Exception ex) {
            Log.e("Exc", ex.getMessage());
        }
    }
}
