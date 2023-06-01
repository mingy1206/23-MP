package com.gachon.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class JoinActivity extends AppCompatActivity {
    private   EditText Nametxt, IDtxt, PWtxt ;
    private Button signbtn ;

    private String name, ID, PW;

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Nametxt = findViewById(R.id.editTextName);
        IDtxt = findViewById(R.id.editTextEmail);
        PWtxt = findViewById(R.id.editTextPassword);
        signbtn =findViewById(R.id.buttonSign);





        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = Nametxt.getText().toString();
                ID = IDtxt.getText().toString();
                PW = PWtxt.getText().toString();



                if (containsSpecialCharacter(ID)) {
                    Toast.makeText(JoinActivity.this, "특수 문자는 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("name", name);
                    Log.d("ID", ID);
                    Log.d("PW", PW);

                    User user = new User(name, ID, PW);

                    Gson gson = new Gson();
                    String json = gson.toJson(user);

                    databaseReference.child(user.ID).setValue(json);
                }



            }
        });

    }
    private boolean containsSpecialCharacter(String text) {
        return !TextUtils.isEmpty(text) && !TextUtils.isDigitsOnly(text) && !TextUtils.isGraphic(text);
    }
}