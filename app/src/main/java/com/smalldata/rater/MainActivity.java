package com.smalldata.rater;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void startTravel(View view) {
        Toast.makeText(MainActivity.this,"Travel started", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, PhotoTakerActivity.class);
        myIntent.putExtra("driverId", "V-00000001"); //Optional parameters
        myIntent.putExtra("vehicleId", "54e59440-7bd6-415a-970b-82ace29a1814");
        startActivity(myIntent);
    }
}
