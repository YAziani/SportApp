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
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.BsaFragebogenViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Felix on 26.03.2017.
 */
public class Activity_lst_bsafragebogen extends AppCompatActivity {

    Activity_lst_bsafragebogen activityLstBsaFragebogen = null;
    List<Fragebogen> FragebogenList;

    RecyclerView rv;

    Activity_lst_bsafragebogen activity_lst_bsafragebogen = this;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_bsafragebogen);
        setTitle(getString(R.string.aktivitaetsfragebogen));

        activityLstBsaFragebogen = this;

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_bsafragebogen);
        registerForContextMenu(rv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_bsafragebogen) {
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
                InsertFragebogen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteItem:
                deleteFragebogen(((BsaFragebogenViewAdapter) rv.getAdapter()).getSelectedObject());
                Toast.makeText(this, getString(R.string.erfolgreichgeloescht), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Removes "BSAFragebogen" in Firebase
     *
     * @param fragebogen the object to delete
     */
    private void deleteFragebogen(Fragebogen fragebogen) {
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + ActivityMain.getMainUser
                (this).getName() + "/BSAFragebogen/");
        ref.child(fragebogen.FirebaseDate).removeValue();
    }

    private void InsertFragebogen() {

        Intent open = new Intent(activity_lst_bsafragebogen, ActivityFragebogen.class);
        startActivity(open);
    }

    /**
     * Shows ProgressDialog until "readFragebogen()" is ready
     */
    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        readFragebogen();
    }

    /**
     * Reads all "BsaFragebogen" out of Firebase
     */
    void readFragebogen() {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/BSAFragebogen");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FragebogenList = new LinkedList<Fragebogen>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        final String sDate = child.getKey();

                        Fragebogen fragebogen = child.getValue(Fragebogen.class);
                        fragebogen.FirebaseDate = sDate;
                        fragebogen.Date = DAL_Utilities.ConvertFirebaseStringNoSpaceToDateString(sDate);
                        FragebogenList.add(fragebogen);
                    }


                    if (FragebogenList != null) {
                        Collections.reverse(FragebogenList);

                        LinearLayoutManager lm = new LinearLayoutManager(activityLstBsaFragebogen);
                        rv.setLayoutManager(lm);

                        rv.setAdapter(new BsaFragebogenViewAdapter(FragebogenList, activityLstBsaFragebogen));
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
