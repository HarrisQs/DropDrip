package org.rita.harris.embeddedsystemhomework_termproject.Rescue_team;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;
import org.rita.harris.embeddedsystemhomework_termproject.R;
import org.rita.harris.embeddedsystemhomework_termproject.StarterApplication;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Add_Rescue_Team extends AppCompatActivity {


    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mTitle;
    private EditText mTrueNameView;
    private EditText mCellPhoneView;
    private EditText mPlaceView;
    private EditText mDescriptionView;
    private static StarterApplication mUser_BasicData;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__rescue__team);
        // Set up the login form.
        mTitle = (EditText) findViewById(R.id.Title);
        mTrueNameView = (EditText) findViewById(R.id.TrueName);
        mCellPhoneView = (EditText) findViewById(R.id.CellPhone);
        mPlaceView = (EditText) findViewById(R.id.Place);
        mDescriptionView = (EditText) findViewById(R.id.Description);
        mUser_BasicData = (StarterApplication) MainActivity.MainActivity_Context().getApplicationContext();
        Button mEmailSignInButton = (Button) findViewById(R.id.Add);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTrueNameView.setError(null);
        mCellPhoneView.setError(null);
        mPlaceView.setError(null);
        mDescriptionView.setError(null);
        mTitle.setError(null);

        // Store values at the time of the login attempt.
        String TrueName = mTrueNameView.getText().toString();
        String CellPhone = mCellPhoneView.getText().toString();
        String Place = mPlaceView.getText().toString();
        String Description = mDescriptionView.getText().toString();
        String Title = mTitle.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(TrueName)) {
            mTrueNameView.setError(getString(R.string.error_field_required));
            focusView = mTrueNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(CellPhone)) {
            mCellPhoneView.setError(getString(R.string.error_field_required));
            focusView = mCellPhoneView;
            cancel = true;
        }
        if (TextUtils.isEmpty(Place)) {
            mPlaceView.setError(getString(R.string.error_field_required));
            focusView = mPlaceView;
            cancel = true;
        }
        if (TextUtils.isEmpty(Description)) {
            mDescriptionView.setError(getString(R.string.error_field_required));
            focusView = mDescriptionView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(TrueName, CellPhone, Place, Description,Title);
            mAuthTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mTrueName;
        private final String mCellPhone;
        private final String mPlace;
        private final String mDescription;
        private final String mTitle;

        UserLoginTask(String TrueName, String CellPhone,String Place,
                      String Description, String Title) {
            mTrueName = TrueName;
            mCellPhone = CellPhone;
            mPlace = Place;
            mDescription = Description;
            mTitle = Title;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ParseObject SaveObject = new ParseObject("RescueTeam");
            SaveObject.put("Title", mTitle);
            SaveObject.put("TrueName", mTrueName);
            SaveObject.put("CellPhone", mCellPhone);
            SaveObject.put("Place", mPlace);
            SaveObject.put("Description", mDescription);
            SaveObject.put("IsTitle", mUser_BasicData.mRescue_team_Data.getDetail_Size()+1);
            SaveObject.put("UserName", mUser_BasicData.mUser_BasicData.getAccount());
            SaveObject.saveInBackground();
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(MainActivity.MainActivity_Context(), "Add Successfully! ", Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

