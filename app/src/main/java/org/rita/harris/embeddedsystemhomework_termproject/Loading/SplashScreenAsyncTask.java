package org.rita.harris.embeddedsystemhomework_termproject.Loading;

/**
 * Created by HarrisQs on 2015/12/25.
 */
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.parse.ParseException;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;
import org.rita.harris.embeddedsystemhomework_termproject.StarterApplication;

public class SplashScreenAsyncTask extends AsyncTask<URL, Integer, String> {

    private final static String TAG = "SplashScreen-AsyncTask";
    private Activity mParentActivity;

    private String doInReturn = "ok";
    private ProgressBar spinTask;
    private static StarterApplication globalMap;

    public SplashScreenAsyncTask(Activity parentActivity, ProgressBar bar) {
        // TODO Auto-generated constructor stub
        super();
        globalMap = (StarterApplication) LoadingActivity.LoadingActivity_Context();
        Log.i(TAG, "SplashScreenAsyncTask().");
        mParentActivity = parentActivity;
        spinTask = bar;
    }

    @Override
    protected String doInBackground(URL... params) {
        // TODO Auto-generated method stub
        try {
            globalMap.GlobalMapData.RefreshData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            globalMap.mRescue_team_Data.RefreshData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doInReturn;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        Log.i(TAG, "onPreExecute().");
        spinTask.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        Log.i(TAG, "onPostExecute().");
        spinTask.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.setClass(mParentActivity, MainActivity.class);
        mParentActivity.startActivity(intent);
    }
}