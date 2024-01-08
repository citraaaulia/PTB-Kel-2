package com.example.docapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        Button btnDoctor = findViewById(R.id.button_dokter);
        Button btnPasien = findViewById(R.id.button_pasien);

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRoleActivity.this, OnboardingActivity.class);
                intent.putExtra("role", "doctor");
                startActivity(intent);
            }
        });

        btnPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRoleActivity.this, OnboardingActivity.class);
                intent.putExtra("role", "pasien");
                startActivity(intent);
            }
        });
    }
}
