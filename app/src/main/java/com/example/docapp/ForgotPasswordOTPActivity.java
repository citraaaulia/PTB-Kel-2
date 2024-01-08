package com.example.docapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

public class ForgotPasswordOTPActivity extends AppCompatActivity {

    private EditText inputOTP;
    private Button btnNext;
    private String email, generatedOTP;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_forget_password);

        inputOTP = findViewById(R.id.inputotp);
        btnNext = findViewById(R.id.btnNext);

        email = getIntent().getStringExtra("email");
        generatedOTP = getIntent().getStringExtra("otp");

        auth = FirebaseAuth.getInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOTP = inputOTP.getText().toString().trim();

                if (enteredOTP.equals(generatedOTP)) {
                    // Verifikasi OTP berhasil, arahkan pengguna ke halaman reset password
                    Intent intent = new Intent(ForgotPasswordOTPActivity.this, ForgotPasswordResetActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgotPasswordOTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


