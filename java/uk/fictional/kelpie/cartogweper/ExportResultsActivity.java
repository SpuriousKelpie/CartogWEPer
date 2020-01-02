package uk.spurious.kelpie.cartogweper;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExportResultsActivity extends AppCompatActivity implements View.OnClickListener,
        LoadScanDialog.LoadDialogListener {

    KMLHandler kml;
    TextView tv;
    Button btn1, btn2;
    String scan_name;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    DBHandler db;
    List<String> ssids = new ArrayList<String>();
    List<String> protocols = new ArrayList<String>();
    List<Double> latitudes = new ArrayList<Double>();
    List<Double> longitudes = new ArrayList<Double>();
    List<String> signals = new ArrayList<String>();
    List<String> freqs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_results);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv = (TextView)findViewById(R.id.tv);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        pref = getApplicationContext().getSharedPreferences("WDA_Preferences", 0);
        kml = new KMLHandler(this);

        db = new DBHandler(this, scan_name);
        ssids = db.getSSIDs();
        protocols = db.getProtocols();
        latitudes = db.getLatitudes();
        longitudes = db.getLongitudes();
        signals = db.getRSSIs();
        freqs = db.getFrequencies();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item1 = menu.findItem(R.id.manage);
        MenuItem item2 = menu.findItem(R.id.view);
        MenuItem item3 = menu.findItem(R.id.export);
        MenuItem item4 = menu.findItem(R.id.statistics);
        MenuItem item5 = menu.findItem(R.id.settings);
        item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);
        item5.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void applyChange(String name, Boolean create, Boolean delete) {
        scan_name = name;
        editor = pref.edit();
        editor.putString("scan_name", scan_name);
        editor.apply();
        tv.setText("Selected Scan: " + pref.getString("scan_name", "N/A"));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
                LoadScanDialog load_dialog = new LoadScanDialog();
                load_dialog.show(getSupportFragmentManager(), "load scan dialog");
                break;
            case R.id.btn2:
                db = new DBHandler(this, scan_name);
                ssids = db.getSSIDs();
                protocols = db.getProtocols();
                latitudes = db.getLatitudes();
                longitudes = db.getLongitudes();
                signals = db.getRSSIs();
                freqs = db.getFrequencies();
                kml.exportToKML(scan_name, ssids, protocols, latitudes, longitudes, signals, freqs);
                break;
        }
    }
}
