package com.example.docapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your splash screen layout
        setContentView(R.layout.splash_screen); // Use the splash screen layout

        // Set a delay before transitioning to the OnboardingActivity
        int SPLASH_DISPLAY_LENGTH = 2000; // Splash screen duration in milliseconds
        new Handler().postDelayed(() -> {
            // After the splash screen duration, start the OnboardingActivity
            Intent onboardingIntent = new Intent(MainActivity.this, ChooseRoleActivity.class);
            startActivity(onboardingIntent);
            finish(); // Close the splash screen activity
        }, SPLASH_DISPLAY_LENGTH);
    }
}