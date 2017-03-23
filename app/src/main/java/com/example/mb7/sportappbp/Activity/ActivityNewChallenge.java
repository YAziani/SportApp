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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityNewChallenge extends AppCompatActivity {

    private CharSequence[] items = {"7 - Tage", "14 - Tage", "1 - Monat" };
    private ImageButton btnAddUser;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd. MMMM yyyy");
    private Calendar dialogCalendar = Calendar.getInstance();
    private Calendar standardCalendar = Calendar.getInstance();
    private int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchallenge);




        //Create button to add an user to the userlist
        btnAddUser = (ImageButton) findViewById(R.id.btnChallengeAddUser);
        btnAddUser.setOnClickListener(btnAddUserClickListener);

        //get textview and set action to enter the start date of the challenge
        textViewStartDate = (TextView) findViewById(R.id.textViewNewChallengeStartDate);
        textViewStartDate.setOnClickListener(textViewStartDateClickListener);

        //get textview and set action to enter the end date of the challenge
        textViewEndDate = (TextView) findViewById(R.id.textViewNewChallengeEndDate);
        textViewEndDate.setOnClickListener(textViewEndDateClickListener);

        //Set standard text for start textfield
        standardCalendar.add(Calendar.DATE, 1);
        Date standardDateStart = standardCalendar.getTime();
        textViewStartDate.setText(sdf.format(standardDateStart));

        //Set standard text for end textfield
        standardCalendar.add(Calendar.DATE, 7);
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

            calendarDialog(calenderDialogStartDateListener);

        }
    };

    /**
     * Create a on click listener for DatePickerDialog. This will happen, when someone entered a new date
     * in the calender dialog window.
     */
    DatePickerDialog.OnDateSetListener calenderDialogStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            //convert int date to date object
            Date date = getDateofInt(year, monthOfYear, dayOfMonth);
            //set selected date to textview
            textViewStartDate.setText(String.valueOf(date.getTime()));
        }};

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
        inflater.inflate(R.menu.menu_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:

                return true;

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

        //set the items and the standard position of the radio button list
        dialogSetDuraiton.setSingleChoiceItems(items, 0, radioButtonDialogSetDurationOnClickListener);

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
     * @param datePickerDialogListener
     */
    public void calendarDialog(DatePickerDialog.OnDateSetListener datePickerDialogListener ){

        //set actually date
        day = dialogCalendar.get(Calendar.DAY_OF_MONTH);
        year = dialogCalendar.get(Calendar.YEAR);
        month = dialogCalendar.get(Calendar.MONTH);

        //create dialog window with the date of today and a on click listener
        DatePickerDialog dpDialog = new DatePickerDialog(ActivityNewChallenge.this, datePickerDialogListener, year, month, day);

        dpDialog.show();
    }


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

            //convert them ta date
            Date date = cal.getTime();

        return date;
    }
}
