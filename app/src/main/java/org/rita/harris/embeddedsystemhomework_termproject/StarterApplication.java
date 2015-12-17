package org.rita.harris.embeddedsystemhomework_termproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by Harris on 2015/12/17.
 */
public class StarterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);//Once the local datastore is enabled, you can store an object by pinning it.
        Parse.initialize(this);
    }
}
