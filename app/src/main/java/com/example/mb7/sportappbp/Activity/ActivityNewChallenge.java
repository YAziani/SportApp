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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.NewChallengeViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Challenge;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.R;

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
    private int day,month,year;


    private ListView listview;
    private NewChallengeViewAdapter newChallengeViewAdapter;
    private CharSequence[] durationItems = {"7 - Tage", "14 - Tage", "28 - Tage" };
    private ArrayList<User> userArrayList = new ArrayList<User>();
    private int duration = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchallenge);

        User u1 = User.Create("Bernd");
        u1.setPoints(250);
        User u2 = User.Create("Peter");
        u2.setPoints(40);
        User u3 = User.Create("Hans");
        u3.setPoints(500);

        userArrayList.add(u1);
        userArrayList.add(u2);
        userArrayList.add(u3);

        //Create listview and adapter
        listview = (ListView) findViewById(R.id.listViewNewChallengeUser);
        newChallengeViewAdapter = new NewChallengeViewAdapter(ActivityNewChallenge.this, userArrayList);
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
        standardCalendar.add(Calendar.DATE, duration);
        Date standardDateEnd = standardCalendar.getTime();
        textViewEndDate.setText(sdf.format(standardDateEnd));

    }


    /**
     * Create a on click listener for btnAddUser
     */
    View.OnClickListener btnAddUserClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogAddUser();
        }
    };

    /**
     * Create a on click listener for textViewStartDate
     */
    View.OnClickListener textViewStartDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            calendarDialog();

        }
    };

    /**
     * Create a on Click listener for textViewEndDate
      */
    View.OnClickListener textViewEndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            dialogSetDuration();

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
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:
                dialogAddUser();

                return true;
            case R.id.icon_save:

                Challenge challenge = new Challenge();
                challenge.setName(editTextName.getText().toString());
                challenge.setStartDate(startCalendar.getTime());
                challenge.setEndDate(endCalendar.getTime());

                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method creates the dialog window with radio button, to enter the duration of the challenge.
     */
    private void dialogSetDuration(){

        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder dialogSetDuraiton = new AlertDialog.Builder(ActivityNewChallenge.this);

        //Chain together various setter methods to set the dialog characteristics
        dialogSetDuraiton.setTitle("Wie lange soll die challenge gehen?");

        //set the durationItems and the standard position of the radio button list
        dialogSetDuraiton.setSingleChoiceItems(durationItems, 0, radioButtonDialogSetDurationOnClickListener);

        //Set action for the negative button
        dialogSetDuraiton.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
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

    /**
     * Create a on click listener for the radio list of the dialog window to enter the duration
     * of the challenge
     */
    DialogInterface.OnClickListener radioButtonDialogSetDurationOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            //check selected item and set duration
            if(i == 0)
                duration = 6;
            else if(i == 1)
                duration = 13;
            else if(i == 2)
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
     * This method creates and shows a dialog window. In this window, the user can enter the
     * E-mail address of users. With the ok button the user will be added to the users list
     * of the challenge group. When the user doen't exist a information appears.
     */
    private void dialogAddUser(){

        //create dialog window
        final Dialog dialog = new Dialog(ActivityNewChallenge.this);

        //set the layout for the dialog window
        dialog.setContentView(R.layout.dialog_entertext);

        //Create button
        Button btnOk = (Button)dialog.findViewById(R.id.btnChallengeOk);
        Button btnCancel = (Button)dialog.findViewById(R.id.btnChallengeCancel);

        //create editText
        final EditText editTextMailAddress = (EditText) dialog.findViewById(R.id.textChallengeMailAddress);

        //set action for btnOK
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //todo if user exists in database and send him a request
                //user aus Database suchen
                //user zur Gruppe hinzufuegen
                //User ist bereits in einer Challenge

                Toast.makeText(ActivityNewChallenge.this, editTextMailAddress.getText()  + " konnte nicht gefunden werden ", Toast.LENGTH_SHORT).show();
            }
        });
        //set action for btnCancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close window
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * This method creates a dialog window with a date picker (calendar). The window will be opened
     * with the day of today.
     */
    public void calendarDialog(){

        //set actually date
        day = startCalendar.get(Calendar.DAY_OF_MONTH);
        year = startCalendar.get(Calendar.YEAR);
        month = startCalendar.get(Calendar.MONTH);

        //create dialog window with the date of today and a on click listener
        DatePickerDialog dpDialog = new DatePickerDialog(ActivityNewChallenge.this, calenderDialogStartDateListener, year, month, day);

        dpDialog.show();
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
            endCalendar.setTime(startDate);
            //set selected date to textview
            textViewStartDate.setText(String.valueOf(sdf.format(startDate)));

            //add the duration and set the new value to textview
            endCalendar.add(startCalendar.DATE, duration);
            Date endDate = endCalendar.getTime();
            textViewEndDate.setText(String.valueOf(sdf.format(endDate)));

        }};


    /**
     * This method creates an date object made of int values. Enter the int values of the day
     * month and the year, and the method converts them to a Date object.
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
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
}
