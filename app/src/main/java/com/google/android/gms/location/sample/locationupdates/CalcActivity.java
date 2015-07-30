package com.google.android.gms.location.sample.locationupdates;

import  android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CalcActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private FileOutputStream mRecordingFile;
    private long startTime;
    private double[] values = new double[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);


        EditText loc1angle1 = (EditText) findViewById(R.id.loc1angle1);
        loc1angle1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent key) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    // show toast for input
                    String input = textView.getText().toString();
                    values[0] = Double.parseDouble(input);
                    Toast toast = Toast.makeText(CalcActivity.this, "The first angle of the first location is: " +
                            input, Toast.LENGTH_LONG);
                    toast.show();
                }

                return handled;
            }
        });

        EditText loc1angle2 = (EditText) findViewById(R.id.loc1angle2);
        loc1angle2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent key) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    // show toast for input
                    String input = textView.getText().toString();
                    values[1] = Double.parseDouble(input);
                    Toast toast = Toast.makeText(CalcActivity.this, "The second angle of the first location is: " +
                            input, Toast.LENGTH_LONG);
                    toast.show();
                }

                return handled;
            }
        });

        EditText loc2angle1 = (EditText) findViewById(R.id.loc2angle1);
        loc2angle1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent key) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    // show toast for input
                    String input = textView.getText().toString();
                    values[2] = Double.parseDouble(input);
                    Toast toast = Toast.makeText(CalcActivity.this, "The first angle of the second location is: " +
                            input, Toast.LENGTH_LONG);
                    toast.show();
                }

                return handled;
            }
        });

        EditText loc2angle2 = (EditText) findViewById(R.id.loc2angle2);
        loc2angle2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent key) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    // show toast for input
                    String input = textView.getText().toString();
                    values[3] = Double.parseDouble(input);
                    Toast toast = Toast.makeText(CalcActivity.this, "The second angle of the second location is: " +
                            input, Toast.LENGTH_LONG);
                    toast.show();

                    // close keyboard and
                    InputMethodManager iManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    iManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    handled = true;
                }

                return handled;
            }
        });

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.
        final float alpha = 0.8f;
        float x, y, z;
        float[] gravity = new float[3];
        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        // Remove the gravity contribution with the high-pass filter.
        x = event.values[0] - gravity[0];
        y = event.values[1] - gravity[1];
        z = event.values[2] - gravity[2];

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_x);
        progressBar.setProgress((int) (x * 10));
        progressBar = (ProgressBar) findViewById(R.id.progressBar_y);
        progressBar.setProgress((int) (y * 10));
        progressBar = (ProgressBar) findViewById(R.id.progressBar_z);
        progressBar.setProgress((int) (z * 10));
        float t = (float) (System.nanoTime() - startTime) / 1E6F;
        String string = t + "\t" + x + "\t" + y + "\t" + z + "\r\n";
        string = string.replace('.', ',');
        if (mRecordingFile != null) {
            try {
                mRecordingFile.write(string.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Create a csvFile
     */
    private File getOutputCSVFile() {
        File fileStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File csvFile;
        csvFile = new File(fileStorageDir.getPath() + File.separator +
                "AccelerationSensorData_" + timeStamp + ".csv");
        return csvFile;
    }

    public void startRecording(View view) {
    //Log.d("SensorTest", "startRecording");
        try {
            mRecordingFile = new FileOutputStream(getOutputCSVFile());
        } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startTime = System.nanoTime();
    }

    public void stopRecording(View view) {
    //Log.d("SensorTest", "stopRecording");
        try {
            if(mRecordingFile != null)
                mRecordingFile.close();
            mRecordingFile = null;
        } catch (IOException e) {
    // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void goToMain(View view) {
        try {
            setContentView(R.layout.sensor_test);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getDistance() {
        double distance = calculate(values);
        setContentView(R.layout.activity_calc);
        TextView textView = (TextView)findViewById(R.id.distanceValue);
        textView.setText("The distance is: " + distance + "km");
    }

    public double calculate(double[] values) {
        double fromOne = values[0];
        double fromTwo = values[1];
        double toOne = values[2];
        double toTwo = values[3];
        double flat = 1/298.257223563;
        double aquator = 6378.137;
        try {
            double fGrad = (fromOne + toOne) / 2;
            double gGrad = (fromOne - toOne) / 2;
            double lGrad = (fromTwo + toTwo) / 2;

            // Umrechnung ins Bogenmass
            double fRad = Math.PI / 180 * fGrad;
            double gRad = Math.PI / 180 * gGrad;
            double lRad = Math.PI / 180 * lGrad;

            // grober Abstand
            double S = Math.pow(Math.sin(gRad), 2) * Math.pow(Math.cos(lRad), 2) + Math.pow(Math.cos(fRad), 2) *
                    Math.pow(Math.sin(lRad), 2);
            double C = Math.pow(Math.cos(gRad), 2) * Math.pow(Math.cos(lRad), 2) + Math.pow(Math.sin(fRad), 2) *
                    Math.pow(Math.sin(lRad), 2);
            double w = Math.atan(Math.sqrt(S / C));

            double D = 2 * w * aquator;

            // Korrektur des Abstands
            double R = Math.sqrt(S * C) / w;
            double H1 = (3 * R - 1) / (2 * C);
            double H2 = (3 * R + 1) / (2 * S);

            // endgueltiger Abstand in km
            double s = D * (1 + flat * H1 * Math.pow(Math.sin(fRad), 2) * Math.pow(Math.cos(gRad), 2) -
                    flat * H2 * Math.pow(Math.sin(fRad), 2) * Math.pow(Math.cos(gRad), 2));

            return s;
        } catch(NullPointerException e) {
            System.err.format("Not enough values to calculate! " + e);
            setContentView(R.layout.activity_calc);
            TextView textView = (TextView)findViewById(R.id.distanceValue);
            textView.setText("Not enough values to calculate!");
        }
        return 0.0;
    }

    public void showDistance() {

    }

}