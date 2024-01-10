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
        //mengatur layout yang akan digunakan di acitvity ini
        setContentView(R.layout.activity_choose_role);

        //inisialisasi objek button
        Button btnDoctor = findViewById(R.id.button_dokter);
        Button btnPasien = findViewById(R.id.button_pasien);

        //event listener btnDoctor
        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            //override metode onClik akan dijalankan ketika tombol diklik
            public void onClick(View v) {

                //membuat metode intent untuk pindah dari choose role ke onboarding activity
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
