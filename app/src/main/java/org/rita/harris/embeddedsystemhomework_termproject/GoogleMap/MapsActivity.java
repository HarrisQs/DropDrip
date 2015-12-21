package org.rita.harris.embeddedsystemhomework_termproject.GoogleMap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.rita.harris.embeddedsystemhomework_termproject.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(sydney);
        markerOpt.title("台北101");
        markerOpt.snippet("於1999年動工，2004年12月31日完工啟用，樓高509.2公尺。");
        markerOpt.draggable(false);
        markerOpt.visible(true);
        markerOpt.anchor(0.5f, 0.5f);//設為圖片中心
        mMap.addMarker(markerOpt);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
