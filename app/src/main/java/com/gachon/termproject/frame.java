package com.gachon.termproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


    //작업전 인터넷 연결을 위해서 menifest에
    //<uses-permission android:name="android.permission.INTERNET"/> 와
    //http 접속을 위한 android:usesCleartextTraffic="true" 추가
    public class frame extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private FrameLayout container;
        private Fragment interestFragment;
        private Fragment homeFragment;
        private Fragment harvestingFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View homeView = inflater.inflate(R.layout.fragment_home, container, false);

            bottomNavigationView = findViewById(R.id.bottom_navigationview);
            container = findViewById(R.id.container);

            homeFragment = new home();
            bottomNavigationView.setSelectedItemId(R.id.Home);


            // Set the initial fragment
            loadFragment(homeFragment);
        }
        @Override
        protected void onStart() {
            super.onStart();

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.Home:
                            loadFragment(homeFragment);
                            return true;
                        case R.id.Archive:
                            loadFragment(harvestingFragment);
                            return true;
                        case R.id.Interest:
                            loadFragment(interestFragment);
                            return true;

                    }
                    return false;
                }
            });

        }
            private void loadFragment(Fragment fragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }





