package org.rita.harris.embeddedsystemhomework_termproject.AccountData;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;
import org.rita.harris.embeddedsystemhomework_termproject.R;
import org.rita.harris.embeddedsystemhomework_termproject.UserData.User_BasicData;

import java.net.URL;

public class LoginActivity extends AppCompatActivity  {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
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
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mContext = this;
    }
   //先進行輸入一個判斷
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

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

        if (cancel) {//沒有通過前端的判斷
            focusView.requestFocus();
        } else {//通過了要去後台進行真的認證
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        // Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        // Replace this with your own logic
        return (password.length() > 4 || password.length() ==0);
    }

    /**
     * Show 出進度狀態
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

    /**
     * 後來的判斷.  查查 AsyncTask<Void, Void, Boolean> 用途
     * 3個參數的意思分別是:
     * Params，啟動任務執行的輸入參數
     * Progress，後台任務執行的百分比
     * Result，後台計算的結果類型
     * 　AsyncTask能夠適當地、簡單地用於 UI線程。
     * 這個類不需要操作線程(Thread)就可以完成後台操作將結果返回UI。
     * 異步任務的定義是一個在後台線程上運行，其結果是在 UI線程上發佈的計算。 異步任務被定義成
     * 三種泛型類型： arams，Progress和 Result；和
     * 四個步驟： begin ,doInBackground，processProgress 和end
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;//存輸入的email
        private final String mPassword;//存輸入的Password
        private boolean IsPass = false;//判斷有無通過

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override //這裡是將會花時間的工作放到背景去處理
        protected Boolean doInBackground(Void... params)
        {
        // CHECK the account here.
            ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        IsPass =true;
                    } else {// Signup failed.
                        IsPass = false;
                    }
                }
            });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }
            return IsPass;
        }

        @Override//當doInBackground方法中的程式執行完畢後，就會執行這個方法
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            User_BasicData LoginUser_BasicData = new User_BasicData();

            if (success) {
                LoginUser_BasicData.setNickName(ParseUser.getCurrentUser().getString("NickName"));
                LoginUser_BasicData.setAccount(mEmail);
                LoginUser_BasicData.setPassword(mPassword);
                Toast.makeText(mContext, "Log In Successfully", Toast.LENGTH_LONG).show();//顯示更新時間
                finish();
            } else {
                mEmailView.setError("Something Wrong, Please try again! ");
                mPasswordView.setError("Something Wrong, Please try again! ");
                mEmailView.requestFocus();
            }
        }

        @Override//取消的時候
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

