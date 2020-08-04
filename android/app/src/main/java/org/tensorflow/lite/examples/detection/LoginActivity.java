package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // login userID 전역변수로 설정
    public static String user_ID;
    public static String user_profile="";

    String email="", pw="";
    EditText emailET,pwET;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET=findViewById(R.id.userEmail);
        pwET=findViewById(R.id.password);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.my_progress_bar);

        // RegisterActivity 에서 가입 이메일 가져온다
        if(getIntent().getExtras() != null){
            EditText username = (EditText)findViewById(R.id.userEmail);
            Intent registerIntent = getIntent();
            username.setText(registerIntent.getStringExtra("Username"));
        }

        // INTENT: LOGIN -> MAIN
        TextView login = (TextView) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO :
                 * DB에서 가입 유무 구분
                 * 로그인 유저 전역변수 설정 */
                //uid=usernameET.getText().toString();
                //pw=passwordET.getText().toString();

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

                //authenticate the user
                fAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
//                            user_ID=fAuth.getCurrentUser().getUid();
//                            Log.d("Login Success: ", user_ID);
                            Toast.makeText(LoginActivity.this,"Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),DetectorActivity.class));
                        } else{
                            Toast.makeText(LoginActivity.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // INTENT: LOGIN -> REGISTER
        TextView signup = (TextView)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signupIntent);
            }
        });
    }
}