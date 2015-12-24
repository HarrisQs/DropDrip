package org.rita.harris.embeddedsystemhomework_termproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;

import org.rita.harris.embeddedsystemhomework_termproject.GoogleMap.Map_CatchHistoryLocation;
import org.rita.harris.embeddedsystemhomework_termproject.Rescue_team.Rescue_team_CatchData;
import org.rita.harris.embeddedsystemhomework_termproject.UserData.User_BasicData;

/**
 * Created by Harris on 2015/12/17.
 */
public class StarterApplication extends Application {
    public Map_CatchHistoryLocation GlobalMapData;
    public User_BasicData mUser_BasicData;
    public Rescue_team_CatchData mRescue_team_Data;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);//Once the local datastore is enabled, you can store an object by pinning it.
        Parse.initialize(this);
        try {
            GlobalMapData = new Map_CatchHistoryLocation();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mUser_BasicData = new User_BasicData ();
        try {
            mRescue_team_Data = new Rescue_team_CatchData ();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
