package com.example.isc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sdr.lib.isc.SDR_HIKVISION_ISC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        SDR_HIKVISION_ISC.getInstance().start(this, "http://58.240.174.254:9059/mcs/", "123456");
    }
}
