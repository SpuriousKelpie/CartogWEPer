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

public class ViewStatisticsActivity extends AppCompatActivity implements View.OnClickListener, LoadScanDialog.LoadDialogListener {

    Button btn;
    TextView tv1, tv2, tv3, tv4;
    String scan_name;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = this.getSharedPreferences("WDA_Preferences", 0);
        db = new DBHandler(this, pref.getString("scan_name", "N/A"));

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);

        tv1 = (TextView)findViewById(R.id.stat1);
        tv2 = (TextView)findViewById(R.id.stat2);
        tv3 = (TextView)findViewById(R.id.stat3);
        tv4 = (TextView)findViewById(R.id.stat4);

        tv1.setText("Number of networks scanned: " + db.getTotalRecords());
        tv2.setText("Number of WPA networks: " + db.getTotalWPARecords());
        tv3.setText("Number of WEP networks: " + db.getTotalWEPRecords());
        tv4.setText("Number of open networks: " + db.getTotalOpenRecords());
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
        if (!delete) {
            scan_name = name;
            db = new DBHandler(this, scan_name);
            tv1.setText("Number of networks scanned: " + db.getTotalRecords());
            tv2.setText("Number of WPA networks: " + db.getTotalWPARecords());
            tv3.setText("Number of WEP networks: " + db.getTotalWEPRecords());
            tv4.setText("Number of open networks: " + db.getTotalOpenRecords());
        }
    }

    @Override
    public void onClick(View v) {
        LoadScanDialog load_dialog = new LoadScanDialog();
        load_dialog.show(getSupportFragmentManager(), "load scan dialog");
    }
}
