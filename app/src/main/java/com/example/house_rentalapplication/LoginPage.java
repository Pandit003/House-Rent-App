package com.example.house_rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    EditText loginpersonmail,loginpersonpass;
    TextView textviewregister;
    Button  loginbutton12;
    FirebaseAuth mAuth;
//    public void onStart() {
//
//        super.onStart();
//        FirebaseUser currentUser=mAuth.getCurrentUser();
//        if(currentUser!=null){
//            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);
        loginpersonmail=findViewById(R.id.loginpersonmail);
        loginpersonpass=findViewById(R.id.loginpersonpass);
        textviewregister=findViewById(R.id.textviewregister);
        loginbutton12=findViewById(R.id.loginbutton12);

        mAuth=FirebaseAuth.getInstance();
        textviewregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        loginbutton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1, password1;
                email1 = String.valueOf(loginpersonmail.getText());
                password1 = String.valueOf(loginpersonpass.getText());
                if (TextUtils.isEmpty(email1)) {
                    Toast.makeText(LoginPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(LoginPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    mAuth.signInWithEmailAndPassword(email1, password1)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginPage.this, "Login sucessfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginPage.this, "Invalid user",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}