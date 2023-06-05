package com.gachon.termproject;

import android.util.Log;
import android.widget.Toast;

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
    private Object json;
    public FirebaseArrayUpdater(String id) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Music").child(id);
        ID = id;
    }

    public void addValue(String newValue) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean equal;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(((String)snapshot.getValue()).equals(newValue))
                    {
                        equal =true;
                        break;
                    }

                }

                if(!equal){
                    Gson gson = new Gson();
                    reference.push().setValue(gson.toJson(newValue));
                    Log.d("넣은 값",newValue);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
