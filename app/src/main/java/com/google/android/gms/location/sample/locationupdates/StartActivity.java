package com.google.android.gms.location.sample.locationupdates;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void goToMap(View view) {
        try {
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");
            Intent k = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(k);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void goToSensorTest(View view) {
        try {
            Intent k = new Intent(this, SensorTest.class);
            startActivity(k);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void goToRoutes(View view) {
        try {
            setContentView(R.layout.activity_routen);
        //    Intent k = new Intent(this, Routen.class);
        //    startActivity(k);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void goToLogin(View view) {
        try {
            Intent k = new Intent(this, Login.class);
            startActivity(k);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close(View view) {
        try {
            finish();
            System.exit(0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
