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
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.BsaFragebogenViewAdapter;
import com.example.mb7.sportappbp.Adapters.StimmungsAngabeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
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
        if (v.getId() == R.id.recycler_bsafragebogen)
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
                InsertFragebogen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.deleteItem:
                deleteFragebogen( ((BsaFragebogenViewAdapter)rv.getAdapter()).getSelectedObject());
                Toast.makeText(this,getString(R.string.erfolgreichgeloescht),Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

    /**
     * delete a Fragebogen
     * @param fragebogen the object to delete
     */
    private void deleteFragebogen(Fragebogen fragebogen)
    {
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" +ActivityMain.mainUser.getName() + "/BSAFragebogen" );
        ref.child(fragebogen.FirebaseDate).removeValue();
    }


    /**
     * open Fragebogen to insert a new one
     */
    private void InsertFragebogen() {

                Intent open = new Intent(activity_lst_bsafragebogen, ActivityFragebogen.class);
                //open.putExtra("Vor", "1");
                startActivity(open);

    }


    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString( R.string.wird_geladen));
        pd.show();
        readFragebogen();
    }

    void readFragebogen() {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.mainUser.getName() + "/BSAFragebogen");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {

                                           // Hier kriegst du den Knoten date zurueck
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               //final String sDate = dataSnapshot.getKey();

                                               // dataSnapshot.getKey() declares which strategy the notification belongs to (BSAFragebogen..)
                                               FragebogenList = new LinkedList<Fragebogen>();
                                               // the child.key of dataSnapshop declare the unique datetime of the notification
                                               for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                   // Here I get the time
                                                   final String sDate= child.getKey();// Date

                                                   // Here I have V or N

                                                    // create the object and insert it in the list
                                                       for (DataSnapshot child3L : child.getChildren()) {

                                                           Fragebogen fragebogen = child3L.getValue(Fragebogen.class);
                                                           fragebogen.FirebaseDate = sDate;
                                                           fragebogen.Date = DAL_Utilities.ConvertFirebaseStringNoSpaceToDateString( sDate);
                                                           FragebogenList.add(fragebogen);
                                                       }

                                                   }




                                               if (FragebogenList != null)
                                               {
                                                   // reverse the list to get the newest first
                                                   Collections.reverse(FragebogenList);
                                                   // fill the recycler
                                                   LinearLayoutManager lm = new LinearLayoutManager(activityLstBsaFragebogen);
                                                   rv.setLayoutManager(lm);
                                                   // just create a list of tasks
                                                   rv.setAdapter(new BsaFragebogenViewAdapter(FragebogenList, activityLstBsaFragebogen));
                                               }


                                               // close the progress dialog
                                               pd.dismiss();


                                           }
                                           @Override
                                           public void onCancelled(FirebaseError firebaseError) {

                                           }
                                       }
            );
        }
        catch (Exception ex)
        {
            Log.e("Exc",ex.getMessage());
        }
    }




}
