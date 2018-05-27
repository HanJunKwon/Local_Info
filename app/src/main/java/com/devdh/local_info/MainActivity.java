package com.devdh.local_info;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GpsInfo gps;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GpsInfo(MainActivity.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("latitude", latitude+"");
        editor.putString("longitude", longitude+"");
        editor.commit();

        getFragmentManager().beginTransaction().replace(R.id.contentContainer, new MenuFragment()).commit();
    }
}
