package com.example.mb7.sportappbp.Activity;

/* Test

        User u1 = User.Create("Bernd");
        u1.setPoints(250);
        User u2 = User.Create("Peter");
        u2.setPoints(40);
        User u3 = User.Create("Hans");
        u3.setPoints(500);

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
 */

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityNewChallenge extends AppCompatActivity {


    private EditText editTextName;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd. MMMM yyyy");
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private Calendar standardCalendar = Calendar.getInstance();
    private int day, month, year;
    DataSnapshot dataSnapshotUsers;
    DataSnapshot dataSnapshotChallenges;
    boolean validSnapshotUsers = false;
    boolean validSnapshotChallenges = false;

    private ListView listview;
    private NewChallengeViewAdapter newChallengeViewAdapter;
    private ArrayList<User> userList = new ArrayList<User>();
    private int duration = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchallenge);

        DAL_RegisteredUsers.getRegisteredUsersNewChallenge(ActivityNewChallenge.this);
        DAL_Challenges.getRegisteredChallengesToNewChallenge(ActivityNewChallenge.this);

        //Create listview and adapter
        listview = (ListView) findViewById(R.id.listViewNewChallengeUser);
        newChallengeViewAdapter = new NewChallengeViewAdapter(ActivityNewChallenge.this, userList);
        listview.setAdapter(newChallengeViewAdapter);

        //get textview and set action to enter the start date of the challenge
        textViewStartDate = (TextView) findViewById(R.id.textViewNewChallengeStartDate);
        textViewStartDate.setOnClickListener(textViewStartDateClickListener);

        //get textview and set action to enter the end date of the challenge
        textViewEndDate = (TextView) findViewById(R.id.textViewNewChallengeEndDate);
        textViewEndDate.setOnClickListener(textViewEndDateClickListener);

        //get edittext
        editTextName = (EditText) findViewById(R.id.editTextNewChallengeName);
        //set start
        startCalendar.add(Calendar.DATE, 1);
        //Set standard text for start textfield
        standardCalendar.add(Calendar.DATE, 1);
        Date standardDateStart = standardCalendar.getTime();
        textViewStartDate.setText(sdf.format(standardDateStart));

        //Set standard text for end textfield
        // + 1 because the earliest day, when a challenge can start, ist the day after today
        endCalendar.add(Calendar.DATE, duration + 1);
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

            //set actually date
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

            //Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder dialogSetDuraiton = new AlertDialog.Builder(ActivityNewChallenge.this);

            //Chain together various setter methods to set the dialog characteristics
            dialogSetDuraiton.setTitle(R.string.WieLangeSolldieChallengeGehen);

            //set the durationItems and the standard position of the radio button list
            dialogSetDuraiton.setSingleChoiceItems(R.array.ArrayChallengeDuration, 0,
                    radioButtonDialogSetDurationOnClickListener);

            //Set action for the negative button
            dialogSetDuraiton.setNegativeButton(R.string.Abbrechen, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //close the window
                    dialogInterface.dismiss();
                }
            });

            //Get the AlertDialog from create()
            AlertDialog dialog = dialogSetDuraiton.create();

            //open the window
            dialog.show();

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
            //add user to user list button
            case R.id.icon_add:

                //create dialog
                final Dialog addUserDialog = new Dialog(ActivityNewChallenge.this);
                //set the layout for the dialog window
                addUserDialog.setContentView(R.layout.dialog_entertext);

                addUserDialog.show();

                //final EditText editTextMailAddress = (EditText) addUserDialog.findViewById(R.id
                // .textChallengeMailAddress);

                //Create button to add a user to the userlist
                Button btnOk = (Button) addUserDialog.findViewById(R.id.btnChallengeOk);
                btnOk.setOnClickListener(btnOkAddUserDialogListener(addUserDialog));

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

                //todo regex ueberarbeiten
                //todo Notifikation senden

                //check if name is valid
                if (challengename.matches("[[a-zA-Z_0-9]]"))
                    Toast.makeText(ActivityNewChallenge.this, R.string.UngültigerName, Toast.LENGTH_LONG).show();
                    //check if name already exist
                else if (checkChallengeName(challengename)) {
                    Toast.makeText(ActivityNewChallenge.this, R.string.NameExistiertBereits, Toast.LENGTH_LONG).show();
                } else if (userList.size() == 0)
                    Toast.makeText(ActivityNewChallenge.this, R.string.MindestensEinBenutzerMussHinzugefügtWerden,
                            Toast.LENGTH_LONG).show();

                else {
                    //Create challenge object and set data
                    Challenge challenge = new Challenge();
                    challenge.setName(editTextName.getText().toString());
                    challenge.setStartDate(startCalendar.getTime());
                    challenge.setEndDate(endCalendar.getTime());
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method creates an on click listener for the ok button from the add user dialog.
     * The method checks if the user exists or has already been added. If not, the user will
     * be added to the user list
     *
     * @param addUserDialog dialog object from the dialog to add an user
     * @return returns a onClickListener for the button
     **/
    private View.OnClickListener btnOkAddUserDialogListener(final Dialog addUserDialog) {

        final EditText editTextMailAddress = (EditText) addUserDialog.findViewById(R.id.textChallengeMailAddress);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the entered email address
                String mail = editTextMailAddress.getText().toString();

                //get user form database
                User user = getUser(editTextMailAddress.getText().toString());
                //check if user exist
                if (user != null) {
                    //user already added
                    if (userAlreadyInList(user))
                        Toast.makeText(ActivityNewChallenge.this, getString(R.string.BenutzerBereitshinzugefügt),
                                Toast.LENGTH_SHORT).show();
                        //add user and close dialog
                    else {
                        userList.add(user);
                        newChallengeViewAdapter.notifyDataSetChanged();
                        addUserDialog.dismiss();
                    }
                }
                //if user doesn't exist
                else
                    Toast.makeText(ActivityNewChallenge.this, mail + " " + getString(R.string
                            .konnteNichtGefundenWerden), Toast.LENGTH_SHORT).show();

            }
        };
        return listener;
    }


    /**
     * Create a on click listener for the radio list of the dialog window to enter the duration
     * of the challenge
     */
    DialogInterface.OnClickListener radioButtonDialogSetDurationOnClickListener = new DialogInterface.OnClickListener
            () {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            //check selected item and set duration
            if (i == 0)
                duration = 6;
            else if (i == 1)
                duration = 13;
            else if (i == 2)
                duration = 27;

            //calculate and set the the end date
            endCalendar.setTime(startCalendar.getTime());
            endCalendar.add(endCalendar.DATE, duration);
            Date date = endCalendar.getTime();
            textViewEndDate.setText(String.valueOf(sdf.format(date)));

            //close window
            dialogInterface.dismiss();
        }
    };

    /**
     * Create a on click listener for DatePickerDialog. This will happen, when someone entered a new date
     * in the calender dialog window.
     */
    DatePickerDialog.OnDateSetListener calenderDialogStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //convert int date to date object
            Date startDate = getDateofInt(year, monthOfYear, dayOfMonth);
            //set selected date to calendar
            startCalendar.setTime(startDate);
            endCalendar.setTime(startDate);
            //set selected date to textview
            textViewStartDate.setText(String.valueOf(sdf.format(startDate)));

            //add the duration and set the new value to textview
            endCalendar.add(startCalendar.DATE, duration);
            Date endDate = endCalendar.getTime();
            textViewEndDate.setText(String.valueOf(sdf.format(endDate)));

        }
    };


    /**
     * This method creates a date object made of int values. Enter the int values of the day
     * month and the year, and the method converts them to a Date object.
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return date object made of input
     */
    private Date getDateofInt(int year, int monthOfYear, int dayOfMonth) {
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
     * This method checks, if a user with an email address exist and returns him. When the user doesn't
     * exist, the method returns null.
     *
     * @param searchedEmail email address of the user
     * @return The user you are looking for or null
     */
    private User getUser(String searchedEmail) {

        User user = null;

        if (dataSnapshotUsers.getValue() != null) {
            for (DataSnapshot d : dataSnapshotUsers.getChildren()) {

                if (d.child("email").getValue() != null) {
                    if (d.child("email").getValue().equals(searchedEmail)) {
                        user = User.Create(d.getKey().toString());
                        user.setEmail(d.child("email").getValue().toString());

                        return user;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method checks, if a challenge already and returns true. When the challenge name doesn't
     * exist, the method returns false.
     *
     * @param challengeName searched challenge name
     * @return The user you are looking for or null
     */
    private boolean checkChallengeName(String challengeName) {

        if (validSnapshotChallenges) {
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
     *
     * @param user user you wanna add
     * @return true if user already exist, else null
     */
    private boolean userAlreadyInList(User user) {

        boolean result = false;

        for (User u : userList) {
            if (user.getEmail().equals(u.getEmail()))
                result = true;
        }
        return result;
    }

    /**
     * Send an invitation to users
     *
     * @param challenge
     */
    public void inviteUsers(Challenge challenge) {
        for (User user : userList) {
            challenge.InviteUser(user);
        }
    }

    /**
     * return the snapshot with registered users to this activity
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot) {
        this.dataSnapshotUsers = dataSnapshot;
        validSnapshotUsers = true;
    }

    /**
     * return the snapshot with registered challenges to this activity
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredChallenges(DataSnapshot dataSnapshot) {
        this.dataSnapshotChallenges = dataSnapshot;
        validSnapshotChallenges = true;
    }
}

