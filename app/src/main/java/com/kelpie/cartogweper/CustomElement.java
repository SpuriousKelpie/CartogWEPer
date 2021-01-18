package com.kelpie.cartogweper;

public class CustomElement {
    String ssid;
    String mac;
    String protocol;
    String latitude;
    String longitude;
    int rssi;
    int frequency;

    public CustomElement(String ssid, String mac, String protocol, String lati, String longi, int rssi, int frequency){
        this.ssid = ssid;
        this.mac = mac;
        this.protocol = protocol;
        this.latitude = lati;
        this.longitude = longi;
        this.rssi = rssi;
        this.frequency = frequency;
    }

    public String getSSID() {
        return ssid;
    }

    public String getMac() {
        return mac;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getRSSI() {
        return rssi;
    }

    public int getFrequency() {
        return frequency;
    }
}
