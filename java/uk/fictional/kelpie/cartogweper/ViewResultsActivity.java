package uk.spurious.kelpie.cartogweper;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewResultsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    DBHandler db;
    List<String> ssids = new ArrayList<String>();
    List<String> protocols = new ArrayList<String>();
    List<Double> latitudes = new ArrayList<Double>();
    List<Double> longitudes = new ArrayList<Double>();
    List<String> signals = new ArrayList<String>();
    List<String> freqs = new ArrayList<String>();
    BitmapDescriptor icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences pref = this.getSharedPreferences("WDA_Preferences", 0);
        db = new DBHandler(this, pref.getString("scan_name", "cartogweper"));
        ssids = db.getSSIDs();
        protocols = db.getProtocols();
        latitudes = db.getLatitudes();
        longitudes = db.getLongitudes();
        signals = db.getRSSIs();
        freqs = db.getFrequencies();

        // mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < ssids.size(); i++) {
            if (protocols.get(i).equals("WPA")) {
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            }
            else if (protocols.get(i).equals("WEP")) {
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            }
            else if (protocols.get(i).equals("open")) {
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            }

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitudes.get(i), longitudes.get(i)))
                    .title(ssids.get(i))
                    .snippet(protocols.get(i))
                    .icon(icon));
        }

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
