package com.kelpie.cartogweper;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

// TODO: Investigate why storage permission is not being requested at run-time
// TODO: Fix onRequestPermissionsResult & provide explanations for why each permission is required
// TODO: Implement onSaveInstanceState and onRestoreInstanceState to store network list
// TODO: Solve crashing when ListView item selected during active scan
// TODO: Remove some ListView items and objects after a period of time to avoid memory crashes

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    static final int REQUEST_FINE_LOCATION = 0;
    static final int REQUEST_COARSE_LOCATION = 1;
    static final int REQUEST_READ_EXT_STORAGE = 2;
    static final int REQUEST_WRITE_EXT_STORAGE = 3;
    ListAdapter adapter;
    ListView lv;
    DBHandler db;
    Button btn;
    WifiManager wifi;
    APReceiver receiver;
    SharedPreferences pref;
    int scan_interval;
    ArrayList<CustomElement> networks = new ArrayList<>();
    int scan_in_progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerToggleButton();
        requestPermissions();

        adapter = new CustomAdapter(this, networks);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get data from selected AP
                        CustomElement ap = networks.get(position);
                        String ssid = ap.getSSID();
                        String mac = ap.getMac();
                        String protocol = ap.getProtocol();
                        String latitude = ap.getLatitude();
                        String longitude = ap.getLongitude();
                        int rssi = ap.getRSSI();
                        int frequency = ap.getFrequency();
                        // Create ListView fragment and set its arguments
                        Fragment fragment = new ListviewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("ssid", ssid);
                        bundle.putString("mac", mac);
                        bundle.putString("proto", protocol);
                        bundle.putString("lati", latitude);
                        bundle.putString("longi", longitude);
                        bundle.putString("rssi", String.valueOf(rssi));
                        bundle.putString("band", String.valueOf(frequency));
                        fragment.setArguments(bundle);
                        // Display ListView fragment
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }
                }
        );

        pref = getApplicationContext().getSharedPreferences("WDA_Preferences", 0);
        db = new DBHandler(this, pref.getString("scan_name", "N/A"));

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                networks.remove(position);
                db.deleteRecord(position);
                lv.setAdapter(adapter);
                return true;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        btn = (Button) findViewById(R.id.start_button);
        btn.setOnClickListener(this);

        wifi = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        receiver = new APReceiver(networks);

        // Set scan interval value from shared preferences
        if (pref.getInt("scan_interval", 0) > 0) {
            scan_interval = pref.getInt("scan_interval", 0) * 1000;
        }
        else {
            scan_interval = 1000;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item1 = menu.findItem(R.id.view);
        MenuItem item2 = menu.findItem(R.id.export);
        MenuItem item3 = menu.findItem(R.id.statistics);
        item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void drawerToggleButton(){
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch(item.getItemId()){
            case R.id.manage:
                Intent management_intent = new Intent(MainActivity.this, ManagementActivity.class);
                startActivity(management_intent);
                return true;
            case R.id.settings:
                Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manage: {
                Intent management_intent = new Intent(MainActivity.this, ManagementActivity.class);
                startActivity(management_intent);
                break;
            }
            case R.id.view: {
                Intent view_intent = new Intent(MainActivity.this, ViewResultsActivity.class);
                startActivity(view_intent);
                break;
            }
            case R.id.export: {
                Intent export_intent = new Intent(MainActivity.this, ExportResultsActivity.class);
                startActivity(export_intent);
                break;
            }
            case R.id.statistics: {
                Intent view_stats_intent = new Intent(MainActivity.this, ViewStatisticsActivity.class);
                startActivity(view_stats_intent);
                break;
            }
            case R.id.settings: {
                Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings_intent);
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        // If no scan has been selected, go to management activity
        if (pref.getString("scan_name", "N/A").equals("N/A")){
            Toast.makeText(this, "No scan selected", Toast.LENGTH_SHORT).show();
            Intent management_intent = new Intent(MainActivity.this, ManagementActivity.class);
            startActivity(management_intent);
            return;
        }

        if (scan_in_progress == 0){
            //btn.setBackgroundColor(Color.parseColor("#ff0000"));
            btn.setText(R.string.stop_scan);

            // Enable Wi-Fi if disabled
            if (!wifi.isWifiEnabled()){
                wifi.setWifiEnabled(true);
                Toast.makeText(this, "Wifi is now enabled", Toast.LENGTH_SHORT).show();
            }

            scan_in_progress = 1;

            // Start GPS service to receive location updates
            startService(new Intent(getBaseContext(), GPSService.class));

            // Register BroadcastReceiver to store AP information
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            intentFilter.addAction(GPSService.ACTION);
            registerReceiver(receiver, intentFilter);

            new Thread(new Runnable(){
                @Override
                public void run() {
                    while (scan_in_progress != 0) {
                        wifi.startScan();
                        SystemClock.sleep(scan_interval);
                        lv.post(new Runnable() {
                            public void run() {
                                ((CustomAdapter)lv.getAdapter()).notifyDataSetChanged();
                            }
                        });
                    }
                }
            }).start();
        }
        else if (scan_in_progress == 1){
            //btn.setBackgroundColor(Color.parseColor("#00ff00"));
            btn.setText(R.string.start_scan);
            scan_in_progress = 0;
            stopService(new Intent(getBaseContext(), GPSService.class));
            unregisterReceiver(receiver);
        }
    }

    public void requestPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXT_STORAGE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXT_STORAGE);
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Application requires location permissions", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_COARSE_LOCATION:
                if (grantResults[1] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Application requires location permissions", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_READ_EXT_STORAGE:
                if (grantResults[2] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Application requires storage permissions", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_WRITE_EXT_STORAGE:
                if (grantResults[3] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Application requires storage permissions", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }*/

    @Override
    protected void onDestroy() {
        // Stop service
        try {
            stopService(new Intent(getBaseContext(), GPSService.class));
        }
        catch(IllegalArgumentException e){
            Log.i("INFO", "Service not started");
            e.printStackTrace();
        }

        // Unregister BroadcastReceiver
        try {
            unregisterReceiver(receiver);
        }
        catch (IllegalArgumentException e){
            Log.i("INFO", "Receiver not registered");
            e.printStackTrace();
        }

        super.onDestroy();
    }
}
