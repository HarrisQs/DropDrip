package org.rita.harris.embeddedsystemhomework_termproject.GoogleMap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;
import org.rita.harris.embeddedsystemhomework_termproject.R;
import org.rita.harris.embeddedsystemhomework_termproject.StarterApplication;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private StarterApplication globalMap;
    private int Match;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        globalMap = (StarterApplication) MainActivity.MainActivity_Context().getApplicationContext();
        Bundle mBundle =this.getIntent().getExtras();
        Match = mBundle.getInt("Show");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        for(int i = 0 ; i<globalMap.GlobalMapData.getDetail_Size() ;i++)
        {
            addMarker(googleMap,
                    Double.parseDouble(globalMap.GlobalMapData.getDetail(i).get("Latitude")),
                    Double.parseDouble(globalMap.GlobalMapData.getDetail(i).get("Longitude")),
                    globalMap.GlobalMapData.getDetail(i).get("Type"),
                    "地點 : "+globalMap.GlobalMapData.getDetail(i).get("Place")+
                    "\n連絡電話 : "+globalMap.GlobalMapData.getDetail(i).get("Contact")+
                    "\n聯絡人 : "+globalMap.GlobalMapData.getDetail(i).get("TrueName")+
                    "\n時間 : "+globalMap.GlobalMapData.getDetail(i).get("UpdateAt")+
                    "\n描述 : "+globalMap.GlobalMapData.getDetail(i).get("Description"),
                    (Match==i)?Boolean.TRUE:Boolean.FALSE);
            googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            googleMap.setOnInfoWindowClickListener(this);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }
    private void addMarker(GoogleMap map, double lat, double lon,
                           String title, String snippet,Boolean IsMove) {
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .snippet(snippet));
        if(IsMove) {
            LatLng position = new LatLng(lat, lon);
            map.moveCamera(CameraUpdateFactory.newLatLng(position));
            marker.showInfoWindow();
        }
    }
}
