package com.kelpie.cartogweper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

// TODO: Improve location accuracy

public class APReceiver extends BroadcastReceiver {

    final static String ACTION = "GPS ACTION";
    String latitude, longitude;
    WifiManager wifi;
    DBHandler db;
    List<ScanResult> result_list;
    ArrayList<CustomElement> networks;
    Boolean mac_exists;

    public APReceiver(ArrayList<CustomElement> networks) {
        this.networks = networks;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION))
        {
            latitude = intent.getStringExtra("lati");
            longitude = intent.getStringExtra("long");
            return;
        }

        wifi = (WifiManager)context.getApplicationContext().getSystemService(WIFI_SERVICE);
        SharedPreferences pref = context.getSharedPreferences("WDA_Preferences", 0);
        db = new DBHandler(context, pref.getString("scan_name", "N/A"));

        try {
            result_list = wifi.getScanResults();
        }
        catch (NullPointerException e) {
            Log.e("ERROR", "NullPointerException: " + e);
        }

        if (result_list != null) {
            for (ScanResult network : result_list) {
                // Get SSID
                String ssid = network.SSID;

                // Get MAC address
                String mac = network.BSSID;

                // Get protocol
                String capabilities = network.capabilities;
                String protocol;

                if (capabilities.toUpperCase().contains("WEP")) {
                    protocol = "WEP";
                } else if (capabilities.toUpperCase().contains("WPA")) {
                    protocol = "WPA";
                } else if (capabilities.toUpperCase().contains("WPA2")) {
                    protocol = "WPA";
                } else {
                    protocol = "Open";
                }

                // Get signal strength
                int rssi = WifiManager.calculateSignalLevel(network.level, 5);

                // Get frequency band
                int frequency = network.frequency;

                List<String>array1 = db.getMacs();
                mac_exists = false;

                for (int i = 0; i < array1.size(); i++){
                    if (mac.trim().equals(array1.get(i).trim())){
                        mac_exists = true;
                    }
                }

                if (!mac_exists) {
                    networks.add(new CustomElement(ssid, mac, protocol, latitude, longitude, rssi, frequency));
                    db.insertRecord(ssid, mac, protocol, latitude, longitude, rssi, frequency);
                }
            }
        }
    }
}