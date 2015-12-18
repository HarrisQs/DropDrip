package org.rita.harris.embeddedsystemhomework_termproject.AccountData;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;


import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.rita.harris.embeddedsystemhomework_termproject.R;


public class RegesterActivity extends AppCompatActivity  {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNcikNameView;
    private View mProgressView;
    private View mLoginFormView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNcikNameView = (EditText) findViewById(R.id.RegesterNickname);
        mNcikNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mContext = this;
    }

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNcikNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nickname = mNcikNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (TextUtils.isEmpty(password))
        {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
         if (TextUtils.isEmpty(nickname))
        {
            mNcikNameView.setError(getString(R.string.error_field_required));
            focusView = mNcikNameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(email, password,nickname);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return (password.length() > 4 || password.length() ==0);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

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

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mNickName;
        private boolean IsPass = true;//判斷有無通過
        private boolean IsEnter = true;//判斷有無通過

        UserRegisterTask(String email, String password, String NickName) {
            mEmail = email;
            mPassword = password;
            mNickName = NickName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            final ParseUser user = new ParseUser();
            user.setUsername(mEmail);
            user.setPassword(mPassword);
            user.setEmail(mEmail);
            user.put("NickName", mNickName);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        sendEmail("harris32916@gmail.com", user.getObjectId());

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Enter your Authenticate key (in your Email box)");
                        final EditText input = new EditText(mContext);
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Authenticate = "";
                                m_Authenticate = input.getText().toString();
                                if(m_Authenticate == user.getObjectId())//成功
                                {
                                    IsEnter = false;
                                }
                                else//他驗證碼輸入錯誤，所以建立帳號失敗
                                {
                                    IsEnter = false;
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IsEnter = false;
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    } else {
                        Toast.makeText(mContext,e.toString().substring(e.toString().indexOf(" ")), Toast.LENGTH_LONG).show();
                        IsPass = false;
                    }
                }
            });
            try {
                // Simulate network access.
                Thread.sleep(4000);
                while(IsEnter)
                    Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return IsPass;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(mContext,"Registration Successfully", Toast.LENGTH_SHORT).show();//顯示更新時間 TODO:因為現在沒有信箱認證，所以要用嗎?
                finish();
            } else {
                mEmailView.setError("Try a new Email/username");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
        protected void sendEmail(String MailGoal , String UniqueToken) {
            try {
                org.rita.harris.embeddedsystemhomework_termproject.Mail.GmailSender sender =
                        new org.rita.harris.embeddedsystemhomework_termproject.Mail.GmailSender("harris32916@gmail.com", "XO3HZ3b94");
                sender.sendMail("Authenticate  Mail",
                        "Hi! We are MoonBear,\n\n" +
                                "\tThanks for your registration.\n"+
                                "\tYour Authenticate Key : "+UniqueToken+
                                        "\n\n\t\t\t\t\t\t\t\tMoonBear",
                        MailGoal,
                        MailGoal);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
        }
    }
}

