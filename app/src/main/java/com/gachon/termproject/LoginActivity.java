package com.gachon.termproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private PermissionSupport permission;

    private EditText txtID, txtPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionCheck();
        }

        Button loginbtn = findViewById(R.id.buttonLogin);
        Button joinbtn = findViewById(R.id.buttonJoin);

        txtID = findViewById(R.id.loginID);
        txtPW = findViewById(R.id.logingPW);
        //Users에서 찾음
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        joinbtn.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v) {

                String ID = txtID.getText().toString();
                String PW = txtPW.getText().toString();

                // Firebase Realtime Database에서 해당 사용자 ID에 대한 정보 조회
                DatabaseReference userRef = databaseReference.child(ID);
                if(ID.isEmpty() || PW.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please fill out all of your information", Toast.LENGTH_SHORT).show();
                else userRef.addListenerForSingleValueEvent(new ValueEventListener(){
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        if(dataSnapshot.exists()){
                            String storedPW=dataSnapshot.getValue(String.class);
                            JSONObject jsonObject = null;
                            String password = null;
                            try {
                                jsonObject = new JSONObject(storedPW);
                                password = jsonObject.getString("PW");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }


                            Log.d("가져온 비밀번호", password);
                            // 입력한 비밀번호와 저장된 비밀번호 비교
                            if(password.equals(PW)){
                                // 로그인 성공
                                Toast.makeText(LoginActivity.this,"Login successful.",Toast.LENGTH_SHORT).show();
                                // FrameActivity로 이동
                                Intent intent=new Intent(getApplicationContext(),FrameActivity.class);
                                intent.putExtra("UID", ID);
                                startActivity(intent);
                                finish();
                            }else{
                                // 비밀번호가 일치하지 않음
                                Toast.makeText(LoginActivity.this,"Incorrect password.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            // 사용자 정보가 존재하지 않음
                            Toast.makeText(LoginActivity.this,"ID does not exist.",Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                        // 데이터 조회 실패
                        Toast.makeText(LoginActivity.this,"Unable to retrieve data from the server.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void permissionCheck() {
        permission = new PermissionSupport(this, this);
        if (!permission.checkPermission()) {
            permission.requestPermission();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            permission.requestPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}



