package com.example.docapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding); // Ganti dengan layout onboarding Anda

        Button btnSignIn = findViewById(R.id.btnSignIn); // Ganti dengan ID button "Masuk"
        Button btnSignUp = findViewById(R.id.btnSignUp); // Ganti dengan ID button "Daftar"

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Board", "Role: "+getIntent().getStringExtra("role"));
                // Navigasi ke LoginActivity
                if(Objects.equals(getIntent().getStringExtra("role"), "doctor"))
                {
                    Log.e("Board", "masuk");
                    Intent intent = new Intent(OnboardingActivity.this, LoginDoctorActivity.class);
                    startActivity(intent);
                }
                else if(Objects.equals(getIntent().getStringExtra("role"), "pasien"))
                {
                    Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke SignUpActivity
                if(Objects.equals(getIntent().getStringExtra("role"), "doctor"))
                {
                    Log.e("Board", "masuk");
                    Intent intent = new Intent(OnboardingActivity.this, SignUpDoctorActivity.class);
                    startActivity(intent);
                }
                else if(Objects.equals(getIntent().getStringExtra("role"), "pasien"))
                {
                    Log.e("Board", "masuk");
                    Intent intent = new Intent(OnboardingActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Setelah onboarding selesai, simpan status di SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("hasSeenOnboarding", true);
        editor.apply();
    }
}
