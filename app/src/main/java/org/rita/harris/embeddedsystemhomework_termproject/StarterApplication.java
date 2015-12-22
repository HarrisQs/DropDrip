package org.rita.harris.embeddedsystemhomework_termproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

import org.rita.harris.embeddedsystemhomework_termproject.GoogleMap.Map_CatchHistoryLocation;

/**
 * Created by Harris on 2015/12/17.
 */
public class StarterApplication extends Application {
    public Map_CatchHistoryLocation GlobalMapData;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);//Once the local datastore is enabled, you can store an object by pinning it.
        Parse.initialize(this);
        GlobalMapData = new Map_CatchHistoryLocation();
    }
}
