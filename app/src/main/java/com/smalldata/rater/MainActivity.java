package com.smalldata.rater;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTravel(View view) {
        Toast.makeText(MainActivity.this,"Your Message", Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(this, PhotoTakerActivity.class);
        myIntent.putExtra("key_example", "val_example"); //Optional parameters
        startActivity(myIntent);
    }
}
