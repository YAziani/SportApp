package com.example.mb7.sportappbp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.BusinessLayer.RegisterCatcher;
import com.example.mb7.sportappbp.BusinessLayer.User;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_RegisteredUsers;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity {

    private DataSnapshot dataSnapshot = null;
    private boolean validSnapshot = false;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set text above login
        TextView textView = (TextView) findViewById(R.id.textViewLogin);
        String textToShow = "Wählen Sie sich bitte einen Nutzernamen und ein Passwort aus";
        SpannableString s = new SpannableString(textToShow);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, textToShow.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new RelativeSizeSpan(1.75f), 0, textToShow.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 28, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 48, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 28, 39, Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 48, 56, Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(s);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailLoginButton = (Button) findViewById(R.id.buttonLogin);
        mEmailLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mEmailRegisterButton = (Button) findViewById(R.id.buttonRegister);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        DAL_RegisteredUsers.getRegisteredUsers(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // check if database is available
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.alinternetneed), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validSnapshot) {
            Toast.makeText(this, getString(R.string.aldberror), Toast.LENGTH_SHORT).show();
            DAL_RegisteredUsers.getRegisteredUsers(this);
            return;
        }

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.alpwerror));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.alallfields));
            focusView = mUsernameView;
            cancel = true;
        }

        //check if credentials are valid
        boolean validLogin = false;
        String errMessage = getString(R.string.alunknownusername);
        TextView focus = mUsernameView;
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                if (d.getKey().equals(username)) {
                    if (d.child("password").getValue() != null && d.child("password").getValue().equals(password)) {
                        validLogin = true;
                        break;
                    } else {
                        errMessage = getString(R.string.alwrongpw);
                        focus = mPasswordView;
                        break;
                    }
                }
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt
            if (validLogin) {
                mAuthTask = new UserLoginTask(username, password, true);
                mAuthTask.execute((Void) null);
            } else {
                focusView = focus;
                focusView.requestFocus();
                focus.setError(errMessage);

            }
        }
    }

    private void attemptRegister() {
        // check if database is available
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.alinternetneed), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validSnapshot) {
            Toast.makeText(this, getString(R.string.aldberror), Toast.LENGTH_SHORT).show();
            DAL_RegisteredUsers.getRegisteredUsers(this);
            return;
        }

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.alpwerror));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.alallfields));
            focusView = mUsernameView;
            cancel = true;
        }

        //check if credentials are valid
        boolean validLogin = true;
        String errMessage = "";
        TextView focus = mUsernameView;
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                if (d.getKey().equals(username)) {
                    validLogin = false;
                    errMessage = getString(R.string.alusernametaken);
                    focus = mUsernameView;
                    break;
                }
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt
            if (validLogin) {
                mAuthTask = new UserLoginTask(username, password, false);
                mAuthTask.execute((Void) null);
            } else {
                focusView = focus;
                focusView.requestFocus();
                focus.setError(errMessage);
            }
        }
    }

    /**
     * return the snapshot with registered users to this activity
     *
     * @param dataSnapshot snapshot containing registered users
     */
    public void returnRegisteredUsers(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
        validSnapshot = true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final boolean mLogin;

        UserLoginTask(String username, String password, boolean login) {
            mUsername = username;
            mPassword = password;
            mLogin = login;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (mLogin) {
                User.createUser(mUsername, getApplicationContext());
            } else {
                User.createUser(mUsername, getApplicationContext()).saveRegistration(mUsername, mPassword);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Toast.makeText(ActivityLogin.this, getString(R.string.allogcomplete), Toast.LENGTH_SHORT).show();
            PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putString("logedIn", mUsername)
                    .apply();
            if (!mLogin) {
                User user = User.Create(mUsername);
                Intent kompassIntent = new Intent(ActivityLogin.this, ActivityKompass.class);
                startActivity(kompassIntent);
                RegisterCatcher registerCatcher = new RegisterCatcher();
                registerCatcher.catchRegistration(user);
            }
            ActivityLogin.this.finish();
        }

        @Override
        protected void onCancelled() {
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

