package com.muhammadv2.going_somewhere;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.muhammadv2.going_somewhere.model.data.TravelsDbContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = TravelsDbContract.PlaceEntry.CONTENT_URI;

                ContentValues testValues = new ContentValues();
                testValues.put(TravelsDbContract.PlaceEntry.COLUMN_PLACE_NAME, "test name");

                testValues.put(TravelsDbContract.PlaceEntry.COLUMN_TIME_END, "12/12/2012");
                testValues.put(TravelsDbContract.PlaceEntry.COLUMN_CITY_ID, "12/12/2012");

                Uri rows = getContentResolver().insert(uri, testValues);
                String ss = rows.getPathSegments().get(1);
                Toast.makeText(getApplication(), "add " + ss + "row", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
