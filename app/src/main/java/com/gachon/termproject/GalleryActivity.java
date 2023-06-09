package com.gachon.termproject;
//recycle view에 띄우기
//출처 :https://hanyeop.tistory.com/201
import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Music");
    private String imageUrl;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        List<String> imageUrls = new ArrayList<>();
        id = getIntent().getStringExtra("id");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getKey().equals(id)){
                            Map<String, String> data = (Map<String, String>) snapshot.getValue();
                            for (Map.Entry<String, String> entry : data.entrySet()) {
                                imageUrl = String.valueOf(entry.getValue());
                                imageUrl = imageUrl.substring(1,imageUrl.length()-1);
                                imageUrls.add(imageUrl);

                            }
                        }

                    }
                }else{
                    Toast.makeText(GalleryActivity.this, "not exists", Toast.LENGTH_SHORT).show();
                }
                Log.d("불러온 값", String.valueOf(imageUrls));
                imageAdapter = new ImageAdapter(imageUrls, GalleryActivity.this);
                recyclerView.setAdapter(imageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}
