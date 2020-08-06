package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    String fullName="", email="", pw="", phone="";
    EditText fullNameET,emailET,pwET,phoneET;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();
    private final String TAG = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullNameET=(EditText) findViewById(R.id.fullNameET);
        emailET=(EditText) findViewById(R.id.emailET);
        pwET=(EditText) findViewById(R.id.pwET);
        phoneET=(EditText) findViewById(R.id.phoneET);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.my_progress_bar2);

//        if(fAuth.getCurrentUser() != null){ // -> 이미 가입된 사용자는 그대로 메인으로 보내는 (?)
//            startActivity(new Intent(getApplicationContext(), CapturedImageAcvtivity.class));
//            finish();
//        }
//
        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText parse -> Firebase
                //fullName = fullNameET.getText().toString();
                email = emailET.getText().toString().trim();
                pw = pwET.getText().toString().trim();
                //phone = phoneET.getText().toString();

                if(TextUtils.isEmpty(email)){
                    emailET.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(pw)){
                    pwET.setError("Password is required.");
                    return;
                }

                if(pw.length() < 6){
                    pwET.setError("Password must be longer than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User Created.", Toast.LENGTH_SHORT).show();


                            SimpleDateFormat s = new SimpleDateFormat("yyyy-mm-dd");
                            String format = s.format(new Date());

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("generated", format);

                            mFireStoreRef.collection("Users").document(email).set(data);

                            startActivity(new Intent(getApplicationContext(),CapturedImageAcvtivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*TODO
                 *  버튼 클릭시 존재하는 사용자 정보인지인지 ?? */

                // INTENT: REGISTER -> LOGIN
                // 막 회원가입한 사용자의 로그인 이메일은 로그인창의 edittext에 띄워준다.
                EditText username = (EditText) findViewById(R.id.emailET);
                Intent signupIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                signupIntent.putExtra("Username", username.getText().toString());
                startActivity(signupIntent);
            }
        });
    }
}