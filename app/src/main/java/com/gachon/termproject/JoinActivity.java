package com.gachon.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    private   EditText Nametxt, IDtxt, PWtxt ;
    private Button signbtn ;

    private String name, ID, PW;
    private boolean equal;

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
        IDtxt = findViewById(R.id.editTextID);
        PWtxt = findViewById(R.id.editTextPassword);
        signbtn =findViewById(R.id.buttonSign);

        //SIGN UP 버튼 클릭시
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name =Nametxt.getText().toString();
                ID = IDtxt.getText().toString();
                PW = PWtxt.getText().toString();

                //공백체크
                SpecialCharacterCheck checker = new SpecialCharacterCheck();
                if(name.isEmpty()||ID.isEmpty()||PW.isEmpty()){
                    Toast.makeText(JoinActivity.this, "Please fill out all of your information", Toast.LENGTH_SHORT).show();
                }
                //특수문자 및 숫자만 있는 경우 체크
                else if (checker.hasSpecialCharacters(ID)||TextUtils.isDigitsOnly(ID)) {
                    Toast.makeText(JoinActivity.this, "You can't use special characters or only digits ID.", Toast.LENGTH_SHORT).show();
                }
                else{
                    equal =true;

                    //firebase 탐색후 중복 ID 체크
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // 데이터 조회 성공 시 호출되는 콜백 메서드
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if(((String)snapshot.getKey()).equals(ID))
                                {
                                    equal =false;
                                    break;
                                }
                            }
                            if(equal){
                                User user = new User(name, ID, PW);
                                Gson gson = new Gson();
                                String json = gson.toJson(user);

                                databaseReference.child(user.ID).setValue(json);
                                Toast.makeText(JoinActivity.this, "The signup went successfully.", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            else{
                                Toast.makeText(JoinActivity.this, "A duplicate identity exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //탐색 실패 예외처리
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(JoinActivity.this, "Unable to retrieve data from the server.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }
    //특수문자 및 한글 처리
    public class SpecialCharacterCheck {
        private static final String PATTERN = "^[a-zA-Z0-9]*$";

        public boolean hasSpecialCharacters(String input) {
            return !input.matches(PATTERN);
        }
    }
}