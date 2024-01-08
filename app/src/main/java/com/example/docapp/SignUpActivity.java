package com.example.docapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import java.util.Map;
import java.util.HashMap;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.Log;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText nameEditText, emailEditText, passwordEditText;
    private CheckBox termsCheckBox;
    private MaterialButton signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ganti dengan layout sign up Anda

        // Inisialisasi instance FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        signUpButton = findViewById(R.id.signUpButton);

        // Inisialisasi TextView untuk prompt sign in

        // Set click listener untuk TextView
        findViewById(R.id.tvSignInPrompt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memulai LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input dan checkbox
                if (validateForm()) {
                    // Daftarkan pengguna dengan email dan password
                    registerUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        // Cek apakah nama diisi
        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Nama diperlukan.");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        // Cek apakah email diisi
        if (emailEditText.getText().toString().isEmpty()) {
            emailEditText.setError("Email diperlukan.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        // Cek apakah password diisi dan minimal 6 karakter
        if (passwordEditText.getText().toString().isEmpty() || passwordEditText.getText().length() < 6) {
            passwordEditText.setError("Password diperlukan dan minimal 6 karakter.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        // Cek apakah checkbox disetujui
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "Anda harus menyetujui Syarat dan Ketentuan.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Pendaftaran berhasil, simpan data pengguna ke Firestore
                        saveUserToFirestore(email);
                        // Tampilkan dialog konfirmasi
                        showSignUpSuccessDialog();
                    } else {
                        // Jika pendaftaran gagal, tampilkan pesan ke pengguna
                        Toast.makeText(SignUpActivity.this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSignUpSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil!")
                .setMessage("Akunmu berhasil didaftarkan ke Medics")
                .setPositiveButton("Masuk", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User memilih "Masuk", navigasi ke LoginActivity
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void saveUserToFirestore(String email) {
        // Dapatkan ID pengguna yang baru terdaftar
        String userId = mAuth.getCurrentUser().getUid();
        // Buat objek Map untuk menyimpan data pengguna
        Map<String, Object> user = new HashMap<>();
        user.put("name", nameEditText.getText().toString());
        user.put("email", email);
        user.put("avatar", "https://firebasestorage.googleapis.com/v0/b/docapp-45843.appspot.com/o/profile%2Fprofile_4945750.png?alt=media&token=e8c089c5-f0b0-478d-bc80-c295dacbadce");
        user.put("password", passwordEditText.getText().toString()); // Storing passwords in plaintext is not secure

        // Dapatkan instance Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Tambahkan dokumen baru dengan ID pengguna ke koleksi 'users'
        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    // Data pengguna berhasil disimpan
                    Log.d("SignUpActivity", "DocumentSnapshot successfully written!");
                })
                .addOnFailureListener(e -> {
                    // Gagal menyimpan data pengguna
                    Log.w("SignUpActivity", "Error writing document", e);
                });
    }

}

