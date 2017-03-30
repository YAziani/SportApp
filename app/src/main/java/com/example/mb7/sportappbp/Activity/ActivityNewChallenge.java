package com.example.mb7.sportappbp.Activity;


import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.NewChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Challenges;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_RegisteredUsers;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This Class creates a new challenge
 * Created by Sebastian
 */

public class ActivityNewChallenge extends AppCompatActivity {


    private EditText editTextName;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd. MMMM yyyy");
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private Calendar standardCalendar = Calendar.getInstance();
    private int day,month,year;
    DataSnapshot dataSnapshotUsers;
    DataSnapshot dataSnapshotChallenges;
    boolean validSnapshotUsers = false;
    boolean validSnapshotChallenges = false;

    private ListView listview;
    private  NewChallengeViewAdapter newChallengeViewAdapter;
    private  ArrayList<User> userList = new ArrayList<User>();
    private int duration = 6;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchallenge);

        //Load datasnapshots of all registerd challenges and users in the database
        DAL_Challenges.getRegisteredChallengesToNewChallenge(ActivityNewChallenge.this);

        //Create listview and adapter to show all added users
        listview = (ListView) findViewById(R.id.listViewNewChallengeUser);
        newChallengeViewAdapter = new NewChallengeViewAdapter(ActivityNewChallenge.this, userList);
        listview.setAdapter(newChallengeViewAdapter);

        //get textview and set action to enter the start date of the challenge
        textViewStartDate = (TextView) findViewById(R.id.textViewNewChallengeStartDate);
        textViewStartDate.setOnClickListener(textViewStartDateClickListener);

        //get textview and set action to enter the end date of the challenge
        textViewEndDate = (TextView) findViewById(R.id.textViewNewChallengeEndDate);
        textViewEndDate.setOnClickListener(textViewEndDateClickListener);

        //get edittext for the challenge name
        editTextName = (EditText) findViewById(R.id.editTextNewChallengeName);

        /// // /Set standard text for start textfield
        Date standardDateStart = standardCalendar.getTime();
        textViewStartDate.setText(sdf.format(standardDateStart));

        //Set standard text for end textfield
        endCalendar.add(Calendar.DATE, duration);
        Date standardDateEnd = endCalendar.getTime();
        textViewEndDate.setText(sdf.format(standardDateEnd));

    }
    /**
     * Create a on click listener for textViewStartDate
     * Opens a dialog with a calender to select the start date of the challenge
     */
    View.OnClickListener textViewStartDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //set actually date for the calender
            day = startCalendar.get(Calendar.DAY_OF_MONTH);
            year = startCalendar.get(Calendar.YEAR);
            month = startCalendar.get(Calendar.MONTH);

            //create dialog window with the date of today and a on click listener
            DatePickerDialog dpDialog = new DatePickerDialog(ActivityNewChallenge.this,
                    calenderDialogStartDateListener, year, month, day);

            dpDialog.show();

        }
    };

    /**
     * Create a on Click listener for textViewEndDate
     * Opens a dialog window with radio button to select the duration of the challenge
     */
    View.OnClickListener textViewEndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //set actually date for the calender
            day = endCalendar.get(startCalendar.DAY_OF_MONTH);
            year = endCalendar.get(startCalendar.YEAR);
            month = endCalendar.get(startCalendar.MONTH);

            //create dialog window with the date of today and a on click listener
            DatePickerDialog dpDialog = new DatePickerDialog(ActivityNewChallenge.this,
                    calenderDialogEndDateListener, year, month, day);

            dpDialog.show();

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //set the menu with add and save icon
        inflater.inflate(R.menu.menu_add_and_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //check which icon was hidden in the toolbar
        switch (item.getItemId()) {

            case R.id.icon_add://open dialog window to add a user to the list

                final Dialog addUserDialog = new Dialog(ActivityNewChallenge.this);
                //set the layout for the dialog window
                addUserDialog.setContentView(R.layout.dialog_entertext);

                addUserDialog.show();

                //Create button to add a user to the userlist
                Button btnOk = (Button) addUserDialog.findViewById(R.id.btnChallengeOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                         EditText editTextMailAddress = (EditText) addUserDialog.
                                 findViewById(R.id.textChallengeMailAddress);

                        //get the entered email address
                        String mail = editTextMailAddress.getText().toString();

                        DAL_RegisteredUsers.checkRegisteredUsersNewChallenge(ActivityNewChallenge.this,
                                mail, addUserDialog);
                    }
                });



                //create button to cancel the dialog
                Button btnCancel = (Button) addUserDialog.findViewById(R.id.btnChallengeCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addUserDialog.dismiss();
                    }
                });
                return true;

            //save challenge button
            case R.id.icon_save:

                //EditText to enter the mail address
                EditText editTextChallengeName = (EditText) findViewById(R.id.editTextNewChallengeName);
                String challengename = editTextChallengeName.getText().toString();

                Date endDate = endCalendar.getTime();
                Date startDate = startCalendar.getTime();


                //check if name and date are valid
                if(startDate.after(endDate))
                    Toast.makeText(ActivityNewChallenge.this, R.string.UngültigsEnddatum, Toast.LENGTH_LONG).show();
                if(endDate.equals(startDate))
                    Toast.makeText(ActivityNewChallenge.this, R.string.UngültigsEnddatum, Toast.LENGTH_LONG).show();
                if(startDate.before(standardCalendar.getTime()))
                    Toast.makeText(ActivityNewChallenge.this, R.string.UngültigsStartdatum, Toast.LENGTH_LONG).show();
                else if (challengename.matches("[[a-zA-Z_0-9]]"))
                    Toast.makeText(ActivityNewChallenge.this, R.string.UngültigerName, Toast.LENGTH_LONG).show();
                    //check if name already exist
                else if (checkChallengeName(challengename)) {
                    Toast.makeText(ActivityNewChallenge.this, R.string.NameExistiertBereits, Toast.LENGTH_LONG).show();
                } else if (userList.size() == 0)
                    Toast.makeText(ActivityNewChallenge.this, R.string.MindestensEinBenutzerMussHinzugefügtWerden,
                            Toast.LENGTH_LONG).show();

                else { //the name is correct and can be added
                    Challenge challenge = new Challenge();
                    challenge.setName(editTextName.getText().toString());
                    challenge.setStartDate(startDate);
                    challenge.setEndDate(endDate);
                    challenge.setUserList(userList);


                    //invite user to challenge
                    inviteUsers(challenge);
                    //Save challenge to firebase
                    challenge.SaveNewChallenge();
                    //Add user too challenge
                    challenge.AddUser(ActivityMain.getMainUser(this));
                    //The creator is automatically the admin
                    challenge.AddAdmin(ActivityMain.getMainUser(this));

                    Toast.makeText(ActivityNewChallenge.this, R.string.ChallengeWurdeErstellt, Toast.LENGTH_SHORT)
                            .show();

                    finish();
                }
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }

    /**
     * Create a on click listener for DatePickerDialog. This will happen, when someone entered a new date
     * in the calender dialog window.
     */
    DatePickerDialog.OnDateSetListener calenderDialogStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            //convert int date to date object
            Date startDate = getDateofInt(year, monthOfYear, dayOfMonth);
            //set selected date to calendar
            startCalendar.setTime(startDate);
            //set selected date to textview
            textViewStartDate.setText(String.valueOf(sdf.format(startDate)));

        }};


    /**
     * Create a on click listener for DatePickerDialog. This will happen, when someone entered a new date
     * in the calender dialog window.
     */
    DatePickerDialog.OnDateSetListener calenderDialogEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            //convert int date to date object
            Date endDate = getDateofInt(year, monthOfYear, dayOfMonth);
            //set selected date to calendar
            endCalendar.setTime(endDate);
            //set selected date to textview
            textViewEndDate.setText(String.valueOf(sdf.format(endDate)));

        }};


    /**
     * This method creates a date object made of int values. Enter the int values of the day
     * month and the year, and the method converts them to a Date object.
     * @param year year of the date to convert
     * @param monthOfYear  month  of the date to convert
     * @param dayOfMonth day of the date to convert
     * @return date object made of input
     */
    private Date getDateofInt(int year, int monthOfYear, int dayOfMonth){
        //create calendar object
        Calendar cal = Calendar.getInstance();

        //set the new values
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.YEAR, year);

        //convert them to date
        Date date = cal.getTime();

        return date;
    }

    /**
     * This method checks the entered email adress from the dialog window and evaluate it. If the user exist
     * or has already been added. else the user will be added
     *
     * @param searchedEmail email address of the user
     * @param dialog dialog window where the address was entered
     */
    private void checkRegisteredUsers(String searchedEmail, Dialog dialog) {

        User user = null;

        if (dataSnapshotUsers.getValue() != null) {
            for (DataSnapshot d : dataSnapshotUsers.getChildren()) {
                //recover the user if it it equals
                if (d.child("email").getValue() != null) {
                    if (d.child("email").getValue().equals(searchedEmail)) {
                        user = User.Create(d.getKey().toString());
                        user.setEmail(d.child("email").getValue().toString());
                    }
                }
            }

            if (user != null) {
                //user already added
                if (userAlreadyInList(user) || user.getName().equals(ActivityMain.getMainUser(this).getName()))
                    Toast.makeText(ActivityNewChallenge.this, getString(R.string.BenutzerBereitshinzugefügt),
                            Toast.LENGTH_SHORT).show();
                    //add user and close dialog
                else {
                    userList.add(user);
                    newChallengeViewAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
            //if user doesn't exist
            else
                Toast.makeText(ActivityNewChallenge.this, searchedEmail + " " + getString(R.string
                        .konnteNichtGefundenWerden), Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * This method checks, if a challenge already and returns true. When the challenge name doesn't
     * exist, the method returns false.
     * @param challengeName searched challenge name
     * @return The user you are looking for or null
     */
    private boolean checkChallengeName(String challengeName){

        if(validSnapshotChallenges) {
            if (dataSnapshotChallenges.getValue() != null) {
                for (DataSnapshot d : dataSnapshotChallenges.getChildren()) {
                    //return true if the name already exists
                    if (d.getKey().equals(challengeName))
                        return true;

                }
            }
        }
        return false;
    }


    /**
     * This method checks if a user has been already added to the user list.
     * @param user user you wanna add
     * @return true if user already exist, else null
     */
    private boolean userAlreadyInList(User user){

        boolean result = false;

        for(User u : userList){
            if(user.getEmail().equals(u.getEmail()))
                result = true;
        }
        return result;
    }

    /**
     * Send an invitation to users
     * @param challenge
     */
    public void inviteUsers(Challenge challenge){
        for(User user : userList){
            challenge.InviteUser(user);
        }
    }

    /**
     * return the snapshot with registered users of a challenge to this activity and starts
     * the method to recover the users out of the snapshot
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot, String searchedUser, Dialog dialog) {
        this.dataSnapshotUsers = dataSnapshot;
        validSnapshotUsers = true;
        checkRegisteredUsers(searchedUser, dialog);
    }

    /**
     * return the snapshot with registered challenges to this activity
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredChallenges(DataSnapshot dataSnapshot) {
        this.dataSnapshotChallenges = dataSnapshot;
        validSnapshotChallenges = true;
    }
}

