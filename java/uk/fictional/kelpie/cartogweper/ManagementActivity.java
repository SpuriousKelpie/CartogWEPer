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

public class ManagementActivity extends AppCompatActivity implements View.OnClickListener,
        CreateScanDialog.CreateDialogListener, LoadScanDialog.LoadDialogListener,
        DeleteScanDialog.DeleteDialogListener {

    TextView tv;
    Button btn1, btn2, btn3;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String scan_name;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv = (TextView)findViewById(R.id.tv);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);

        pref = getApplicationContext().getSharedPreferences("WDA_Preferences", 0);
        tv.setText("Current Scan: " + pref.getString("scan_name", "N/A"));

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
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
        if (create){
            scan_name = name;
            db = new DBHandler(this, name);
            db.getSSIDs(); // cheat to create db
            editor = pref.edit();
            editor.putString("scan_name", name);
            editor.apply();
            tv.setText("Current Scan: " + pref.getString("scan_name", "N/A"));
        }

        else if (!delete) {
            scan_name = name;
            editor = pref.edit();
            editor.putString("scan_name", scan_name);
            editor.apply();
            tv.setText("Current Scan: " + pref.getString("scan_name", "N/A"));
        }
        else if (delete){
            scan_name = name;
            this.deleteDatabase(name);
            editor = pref.edit();
            editor.putString("scan_name", "N/A");
            editor.apply();
            tv.setText("Current Scan: " + pref.getString("scan_name", "N/A"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                CreateScanDialog create_dialog = new CreateScanDialog();
                create_dialog.show(getSupportFragmentManager(), "create scan dialog");
                break;
            case R.id.btn2:
                LoadScanDialog load_dialog = new LoadScanDialog();
                load_dialog.show(getSupportFragmentManager(), "load scan dialog");
                break;
            case R.id.btn3:
                DeleteScanDialog delete_dialog = new DeleteScanDialog();
                delete_dialog.show(getSupportFragmentManager(), "delete scan dialog");
                break;
        }
    }
}
