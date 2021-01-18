package com.kelpie.cartogweper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// TODO: Handle SSIDs which contain ampersand symbols
// TODO: Add Mac address field

public class KMLHandler {

    String filepath = "/wda";
    String contents = "";

    public KMLHandler(Context context) {

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void exportToKML(String filename, List<String> ssids, List<String> protocols, List<Double> latitudes,
                            List<Double> longitudes, List<String> signals, List<String> frequencies) {

        contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                   "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                   "\t<Document>\n";

        for (int i = 0; i < ssids.size(); i++){

            contents += "\t<Placemark>\n" +
                        "\t\t<name>"+ssids.get(i)+"</name>\n" +
                        "\t\t<ExtendedData>\n" +
                        "\t\t\t<Data name='Protocol'>\n" +
                        "\t\t\t\t<value>"+protocols.get(i)+"</value>\n" +
                        "\t\t\t</Data>\n" +
                        "\t\t\t<Data name='RSSI'>\n" +
                        "\t\t\t\t<value>"+signals.get(i)+"</value>\n" +
                        "\t\t\t</Data>\n" +
                        "\t\t\t<Data name='Frequency'>\n" +
                        "\t\t\t\t<value>"+frequencies.get(i)+"</value>\n" +
                        "\t\t\t</Data>\n" +
                        "\t\t</ExtendedData>\n" +
                        "\t\t<Point>\n" +
                        "\t\t\t<coordinates>"+longitudes.get(i)+","+latitudes.get(i)+"</coordinates>\n" +
                        "\t\t</Point>\n" +
                        "\t</Placemark>\n";
        }

        contents += "\t</Document>\n" +
                    "</kml>\n";

        if (!isExternalStorageWritable() || !isExternalStorageReadable()) {
            Log.d("tag", "Issue with external storage");
            return;
        }

        // Save to wda directory on sdcard
        File sdcard_dir = Environment.getExternalStorageDirectory();
        File dir = new File (sdcard_dir.getAbsolutePath() + filepath);

        Boolean result = dir.mkdirs();
        if (!result){
            Log.d("MSG", "Directory not created");
        }

        File file = new File(dir, filename+".kml");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(contents.getBytes());
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("MSG", "File not found");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
