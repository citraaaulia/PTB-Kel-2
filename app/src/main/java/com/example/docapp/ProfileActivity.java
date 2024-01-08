package com.example.docapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String currentUserId;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.action_profile).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.action_profile) {
                return true;
            } else {
                return false;
            }
        });

        TextView changePassButton = findViewById(R.id.gantisandi);
        changePassButton.setOnClickListener(item -> {
            startActivity(new Intent(ProfileActivity.this, ForgotPasswordResetActivity.class));
        });

        TextView keluarTextView = findViewById(R.id.keluar);
        keluarTextView.setOnClickListener(view -> {
            // Menampilkan alert
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Logout user
                        mAuth.signOut();

                        // Pindah ke LoginActivity
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                        finish(); // Menutup activity saat ini
                    })
                    .setNegativeButton("Tidak", (dialog, which) -> {
                        // Do nothing, hanya menutup dialog
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        ImageView cameraImageView = findViewById(R.id.camera);
        cameraImageView.setOnClickListener(view -> {
            // Membuka galeri untuk memilih gambar
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        });


        retrieveUserData();
    }

    private void retrieveUserData() {
        mFirestore.collection("users").document(currentUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Mendapatkan data dari Firestore
                            String nama = document.getString("name");
                            String avatarUrl = document.getString("avatar");
                            Log.d("Profile", "Nama: "+nama);
                            // Menampilkan data pada komponen dengan id usernameTextView
                            TextView usernameTextView = findViewById(R.id.usernameTextView);
                            usernameTextView.setText(nama);

                            // Menampilkan gambar menggunakan Glide pada ImageView dengan id imgDrlaki
                            ImageView imgDrlaki = findViewById(R.id.imgDrlaki);
                            Glide.with(ProfileActivity.this)
                                    .load(avatarUrl)
                                    .into(imgDrlaki);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Jika hanya satu gambar yang dipilih
                Uri selectedImageUri = data.getData();
                uploadImage(selectedImageUri);
            } else if (data.getClipData() != null) {
                // Jika lebih dari satu gambar yang dipilih (mulai dari Android API level 18)
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    uploadImage(selectedImageUri);
                }
            } else {
                // Tidak ada gambar yang dipilih
                Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri imageUri) {
        // Mendapatkan referensi ke Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImageRef = storageRef.child("profile/" + currentUserId + ".jpg");

        // Mengunggah gambar ke Firebase Storage
        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Mendapatkan URL download gambar setelah berhasil diunggah
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Mengupdate field avatar di Firestore dengan URL download gambar
                        String imageUrl = uri.toString();
                        updateAvatarInFirestore(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Menangani kegagalan unggah
                    Toast.makeText(ProfileActivity.this, "Gagal mengunggah gambarrr.", Toast.LENGTH_SHORT).show();
                    Log.e("Profile", "Errorrr: "+ e);
                });
    }

    private void updateAvatarInFirestore(String imageUrl) {
        // Mengupdate field avatar di Firestore
        mFirestore.collection("users").document(currentUserId)
                .update("avatar", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Menangani keberhasilan update
                    Toast.makeText(ProfileActivity.this, "Gambar profil berhasil diupdate.", Toast.LENGTH_SHORT).show();
                    retrieveUserData(); // Memperbarui tampilan dengan data terbaru
                })
                .addOnFailureListener(e -> {
                    // Menangani kegagalan update
                    Toast.makeText(ProfileActivity.this, "Gagal mengupdate avatar.", Toast.LENGTH_SHORT).show();
                });
    }
}
