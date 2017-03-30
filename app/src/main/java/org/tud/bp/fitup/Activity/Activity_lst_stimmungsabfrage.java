package com.tud.bp.fitup.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tud.bp.fitup.Adapters.StimmungsAngabeViewAdapter;
import com.tud.bp.fitup.BusinessLayer.StimmungsAngabe;
import com.tud.bp.fitup.DataAccessLayer.DAL_Utilities;
import com.tud.bp.fitup.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Activity_lst_stimmungsabfrage extends AppCompatActivity {

    Activity_lst_stimmungsabfrage activityLstStimmungsabfrage = null;
    List<StimmungsAngabe> stimmungsAngaben;
    RecyclerView rv;
    Activity_lst_stimmungsabfrage activity_lst_stimmungsabfrage = this;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_stimmungsabfrage);

        setTitle(getString(R.string.stimmungsabgaben));

        activityLstStimmungsabfrage = this;

        // we want to make a context menu for our RecyclerView to show delelete Button when long clicked
        rv = (RecyclerView) findViewById(R.id.recycler_stmAbfrage);
        registerForContextMenu(rv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_stmAbfrage) {
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
                InsertStimmungsabgabe();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteItem:
                deleteStimmungsabgabe(((StimmungsAngabeViewAdapter) rv.getAdapter()).getSelectedObject());
                Toast.makeText(this, getString(R.string.erfolgreichgeloescht), Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

    /**
     * delete a Stimmungsangabe
     *
     * @param stimmungsAngabe the object to delete
     */
    private void deleteStimmungsabgabe(StimmungsAngabe stimmungsAngabe) {
        Firebase ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser
                (this).getName() + "/Stimmungsabfrage/");
        String V_N = (stimmungsAngabe.Vor) ? "V" : "N";
        ref.child(stimmungsAngabe.FirebaseDate).child(stimmungsAngabe.Time).child(V_N).removeValue();
        try {
            ref = new Firebase(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this)
                    .getName() + "/StimmungabfrageScore/");
            ref.child(stimmungsAngabe.FirebaseDate).child(stimmungsAngabe.Time).child(V_N).removeValue();

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
    }


    /**
     * open Stimmungsanfrage to insert a new one
     */
    private void InsertStimmungsabgabe() {
        // Run a dialog and ask the user about Vor and Nach
        // then insert the answer in the Extra and open StimmungsAnfrage
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.stimmungsabgabe));
        builder.setMessage(getString(R.string.stimm_vor_nach_question));

        // set the positive button
        builder.setPositiveButton(getString(R.string.vor), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Intent open = new Intent(activity_lst_stimmungsabfrage, ActivityStimmungsAbgabe.class);
                open.putExtra("Vor", "1");
                startActivity(open);

            }
        });

        // set the negative button
        builder.setNegativeButton(R.string.nach, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Intent open = new Intent(activity_lst_stimmungsabfrage, ActivityStimmungsAbgabe.class);
                open.putExtra("Vor", "0");
                startActivity(open);

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Pop up a dialog which asks the user if this new Stimmungsabgabe is Vor or Nach Training
     */
    private void AskIfVorTraining() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        readStimmungsabgaben();
    }

    void readStimmungsabgaben() {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.getMainUser(this).getName() +
                    "/Stimmungsabfrage/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {

                                           // Hier kriegst du den Knoten date zurueck
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               //final String sDate = dataSnapshot.getKey();

                                               // dataSnapshot.getKey() declares which strategy the notification
                                               // belongs to (Stimmungsabgabe....)
                                               stimmungsAngaben = new LinkedList<StimmungsAngabe>();
                                               // the child.key of dataSnapshop declare the unique datetime of the
                                               // notification
                                               for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                   // Here I get the time
                                                   final String sDate = child.getKey();// Date

                                                   // Here I have V or N

                                                   for (DataSnapshot child2L : child.getChildren()) {
                                                       final String sTime = child2L.getKey();

                                                       // create the object and insert it in the list
                                                       for (DataSnapshot child3L : child2L.getChildren()) {
                                                           Boolean V = child3L.getKey().equals("V");
                                                           StimmungsAngabe stimmungsAngabe = child3L.getValue
                                                                   (StimmungsAngabe.class);
                                                           stimmungsAngabe.Vor = V;
                                                           stimmungsAngabe.FirebaseDate = sDate;
                                                           stimmungsAngabe.Date = DAL_Utilities
                                                                   .ConvertFirebaseStringNoSpaceToDateString(sDate);
                                                           stimmungsAngabe.Time = sTime;
                                                           stimmungsAngaben.add(stimmungsAngabe);
                                                       }

                                                   }

                                               }


                                               if (stimmungsAngaben != null) {
                                                   // reverse the list to get the newest first
                                                   Collections.reverse(stimmungsAngaben);
                                                   // fill the recycler
                                                   LinearLayoutManager lm = new LinearLayoutManager
                                                           (activityLstStimmungsabfrage);
                                                   rv.setLayoutManager(lm);
                                                   // just create a list of tasks
                                                   rv.setAdapter(new StimmungsAngabeViewAdapter(stimmungsAngaben, activityLstStimmungsabfrage));
                                               }


                                               // close the progress dialog
                                               pd.dismiss();


                                           }

                                           @Override
                                           public void onCancelled(FirebaseError firebaseError) {

                                           }
                                       }
            );
        } catch (Exception ex) {
            Log.e("Exc", ex.getMessage());
        }
    }
}
