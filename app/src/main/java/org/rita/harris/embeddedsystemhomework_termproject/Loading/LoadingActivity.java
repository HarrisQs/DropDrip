package org.rita.harris.embeddedsystemhomework_termproject.Loading;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import org.rita.harris.embeddedsystemhomework_termproject.R;
import static org.rita.harris.embeddedsystemhomework_termproject.R.id.progressBar1;

public class LoadingActivity extends AppCompatActivity {

    // String for LogCat documentation
    private final static String TAG = "SplashScreenActivity";
    private ProgressBar spinbar;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_loading);
        Log.i(TAG, "onCreate().");
        mContext = getApplicationContext();
        spinbar = (ProgressBar) findViewById(progressBar1);
        spinbar.setVisibility(View.GONE);
        new SplashScreenAsyncTask(this, spinbar).execute();
    }

    public static Context LoadingActivity_Context() {
        return mContext;
    }
}
