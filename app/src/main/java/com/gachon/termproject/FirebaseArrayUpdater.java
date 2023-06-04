package com.gachon.termproject;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FirebaseArrayUpdater {
    private FirebaseDatabase database;
    private DatabaseReference reference ;
    private String ID;
    private String json;
    public FirebaseArrayUpdater(String id) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Music").child(id);
        ID = id;
    }

    public void addValue(String newValue) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(((String)snapshot.getKey()).equals(ID))
                    {
                        json = snapshot.getValue().toString();
                        Log.d("user 정보", json);
                    }
                }

                Gson gson = new Gson();
                ArrayList<String> musicList = new ArrayList<>();
                musicList.add(newValue);


                reference.push().setValue(gson.toJson(musicList));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
