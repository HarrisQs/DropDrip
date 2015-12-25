package org.rita.harris.embeddedsystemhomework_termproject.AddNewItem;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.parse.ParseObject;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;
import org.rita.harris.embeddedsystemhomework_termproject.R;
import org.rita.harris.embeddedsystemhomework_termproject.StarterApplication;


public class Add_Asylum_PointActivity extends AppCompatActivity implements LocationListener{

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mTrueNameView;
    private EditText mCellPhoneView;
    private EditText mPlaceView;
    private EditText mDescriptionView;
    private RadioButton mAsylum_Point;
    private static StarterApplication mUser_BasicData;
    private View mProgressView;
    private View mLoginFormView;

    /*GPS*/
    private TextView LongitudeLatitude_txt;
    private String longitude_txt,latitude_txt;
    private boolean getService = false;     //是否已開啟定位服務
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asylum_point);
        // Set up the login form.
        mTrueNameView = (EditText) findViewById(R.id.TrueName);
        mCellPhoneView = (EditText) findViewById(R.id.CellPhone);
        mPlaceView = (EditText) findViewById(R.id.Place);
        mDescriptionView = (EditText) findViewById(R.id.Description);
        mAsylum_Point = (RadioButton) findViewById(R.id.Asylum_Point);
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
        LongitudeLatitude_txt = (TextView) findViewById(R.id.GpsLocation);
        //取得系統定位服務
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            getService = true; //確認開啟定位服務
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
        }
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

        // Store values at the time of the login attempt.
        String TrueName = mTrueNameView.getText().toString();
        String CellPhone = mCellPhoneView.getText().toString();
        String Place = mPlaceView.getText().toString();
        String Description = mDescriptionView.getText().toString();
        String Type = null;

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
            if (mAsylum_Point.isChecked())
                Type = "庇護點";
            else
                Type = "緊急事件";
            mAuthTask = new UserLoginTask(TrueName, CellPhone, Place, Description, Type,longitude_txt ,latitude_txt);
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
        private final String mType;
        private final String mlongitude_txt;
        private final String mlatitude_txt;
        //TODO : private final String mLatitude;
        //private final String mLongitude;

        UserLoginTask(String TrueName, String CellPhone,String Place,
                      String Description,String Type , String longitude_txt , String latitude_txt) {
            mTrueName = TrueName;
            mCellPhone = CellPhone;
            mPlace = Place;
            mDescription = Description;
            mType = Type;
            mlongitude_txt = longitude_txt;
            mlatitude_txt = latitude_txt;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ParseObject SaveObject = new ParseObject("Location");
            SaveObject.put("Type", mType);
            SaveObject.put("TrueName", mTrueName);
            SaveObject.put("Contact", mCellPhone);
            SaveObject.put("Place", mPlace);
            SaveObject.put("Description", mDescription);
            SaveObject.put("GET", true);
            SaveObject.put("Latitude", mlatitude_txt);
            SaveObject.put("Longitude", mlongitude_txt);
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


    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
         /*做法一,由程式判斷用GPS_provider
           if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
               location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標
         }
         else if ( lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
         { location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用GPS定位座標
         }
         else {}*/
        // 做法二,由Criteria物件判斷提供最準確的資訊
        Criteria criteria = new Criteria();  //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        Location location = lms.getLastKnownLocation(bestProvider);

        getLocation(location);
    }

    private void getLocation(Location location) { //將定位資訊顯示在畫面中
        if (location != null) {
            Double longitude = location.getLongitude();   //取得經度
            Double latitude = location.getLatitude();     //取得緯度
            longitude_txt = String.valueOf(longitude);
            latitude_txt = String.valueOf(latitude);
            LongitudeLatitude_txt.setText("Your Gps Location : ( " +longitude_txt+" , "+latitude_txt+" )");
        } else {
            longitude_txt = "121";
            latitude_txt = "24";
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onLocationChanged(Location location) {  //當地點改變時
        // TODO 自動產生的方法 Stub
        getLocation(location);
    }

    @Override
    public void onProviderDisabled(String arg0) {//當GPS或網路定位功能關閉時
        // TODO 自動產生的方法 Stub
        Toast.makeText(this, "請開啟gps或3G網路", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String arg0) { //當GPS或網路定位功能開啟
        // TODO 自動產生的方法 Stub
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) { //定位狀態改變
        // TODO 自動產生的方法 Stub
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (getService) {
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (getService) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            lms.removeUpdates(this);   //離開頁面時停止更新
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

