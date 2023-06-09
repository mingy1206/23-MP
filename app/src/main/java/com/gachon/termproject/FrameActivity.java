package com.gachon.termproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


    //작업전 인터넷 연결을 위해서 menifest에
    //<uses-permission android:name="android.permission.INTERNET"/> 와
    //http 접속을 위한 android:usesCleartextTraffic="true" 추가
    public class FrameActivity extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private FrameLayout container;
        private Fragment homeFragment;
        private Fragment archiveFragment;
        private Fragment searchFragment;

        public String ID;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_frame);
            Log.d("frame 전환", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View homeView = inflater.inflate(R.layout.fragment_home, container, false);

            bottomNavigationView = findViewById(R.id.bottom_navigationview);
            container = findViewById(R.id.container);

            homeFragment = new home();
            searchFragment = new search();
            archiveFragment = new archive();

            // Set the initial fragment

            loadFragment(homeFragment);
            bottomNavigationView.setSelectedItemId(R.id.Home);



            Intent intent = getIntent();
            ID= intent.getStringExtra("UID");

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
                            loadFragment(archiveFragment);
                            return true;
                        case R.id.Search:
                            loadFragment(searchFragment);
                            return true;

                    }
                    return false;
                }
            });

        }
            public void loadFragment(Fragment fragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }


        public String valueOfID(){
            return ID;
        }
    }





