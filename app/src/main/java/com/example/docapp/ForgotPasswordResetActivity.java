package com.example.docapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordResetActivity extends AppCompatActivity {

    private EditText inputPassword1, inputPassword2;
    private Button btnReset;
    private String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        inputPassword1 = findViewById(R.id.inputpass1);
        inputPassword2 = findViewById(R.id.inputpass2);
        btnReset = findViewById(R.id.btnreset);

        email = getIntent().getStringExtra("email");
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = inputPassword1.getText().toString().trim();
                String password2 = inputPassword2.getText().toString().trim();

                if (password1.equals(password2)) {
                    // Passwords cocok, lakukan reset password
                    resetPassword(email, password1);
                } else {
                    Toast.makeText(ForgotPasswordResetActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPassword(String email, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordResetActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            // Password berhasil diubah, arahkan pengguna ke aktivitas login atau halaman utama
                            startActivity(new Intent(ForgotPasswordResetActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(ForgotPasswordResetActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(ForgotPasswordResetActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

}

