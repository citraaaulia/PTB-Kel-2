package com.example.docapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class HomeDoctorActivity extends AppCompatActivity {
    private RecyclerView rvDikonfirmasi;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Booking> bookingList;
    private BookingAdapter adapter;

    private TextView dijadwalkanTextView;
    private TextView selesaiTextView;
    private TextView dibatalkanTextView;
    private TextView pengajuanTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homedoctor);

        //inisialisasi recyclerview
        rvDikonfirmasi = findViewById(R.id.rv_dikonfirmasi);
        rvDikonfirmasi.setLayoutManager(new LinearLayoutManager(this));

        //deklarasi koneksi ke database
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //array list untuk simpan data booking
        bookingList = new ArrayList<>();

        //deklarasi objek yg berfungsi untuk menghibingkan data dari bookinglist ke tampilan RecyclerView
        adapter = new BookingAdapter(bookingList, R.layout.item_dijadwalkan);

        //menghubungkan rv dengan adapter
        rvDikonfirmasi.setAdapter(adapter);

        //inisialisasi textview
        dijadwalkanTextView = findViewById(R.id.dijadwalkanBtn);
        selesaiTextView = findViewById(R.id.selesaiBtn);
        dibatalkanTextView = findViewById(R.id.ditolakBtn);
        pengajuanTextView = findViewById(R.id.pengajuanBtn);

        //method untuk memuat list booking yang telah dikonfirmasi
        loadBookingsWithConfirmation("book", true, R.layout.item_antrian);

        //textview bisa diklik
        dijadwalkanTextView.setOnClickListener(v -> {
            //menangani potensi kealahan
            try {
                //kembalikan elemen visual seperti keadaan awal
                resetTextViews();
                //atur warna text jadi primary colour
                dijadwalkanTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                //atur typeface jadi bold
                dijadwalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                //memuat daftar pesanan yang telah dikonfirmasi
                loadBookingsWithConfirmation("book", true, R.layout.item_antrian);
            } catch (Exception e) {
                Log.e("HomeDoctorActivity", "Error on click: ", e);
            }
        });


        selesaiTextView.setOnClickListener(v -> {
            try {
                resetTextViews();
                selesaiTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                selesaiTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                loadBookings("finish", R.layout.item_selesai_doctor);
            } catch (Exception e) {
                Log.e("HomeDoctorActivity", "Error on click: ", e);
            }
        });

        dibatalkanTextView.setOnClickListener(v -> {
            try {
                resetTextViews();
                dibatalkanTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                dibatalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                loadBookings("cancel", R.layout.item_ditolak_doctor);
            } catch (Exception e) {
                Log.e("HomeActivity", "Error on click: ", e);
            }
        });

        pengajuanTextView.setOnClickListener(v -> {
            try {
                resetTextViews();
                pengajuanTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                pengajuanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                loadPendingBookings();
            } catch (Exception e) {
                Log.e("HomeDoctorActivity", "Error on click: ", e);
            }
        });

        //deklarasi bottom navigation dengan id
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //atur listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
           //cek apakah item yang diklik memiliki id action_home
            if (item.getItemId() == R.id.action_home) {
                // Item Home yang aktif
                return true;

            //cek nika yang diklik memiliki id action_profile
            } else if (item.getItemId() == R.id.action_profile) {
                //masuk ke laman profile dokter
                startActivity(new Intent(HomeDoctorActivity.this, ProfileDoctorActivity.class));
                finish(); // Akhiri HomeActivity jika pindah ke ProfileActivity
                return true;
            }
            return false;
        });

        //deklarasi button tambahkan jadwal
        Button AddScheduleButton = findViewById(R.id.button_tambahkanjadwal);
        //button tambahkan jadwal bisa diklik
        AddScheduleButton.setOnClickListener(v -> {
            //buka laman addschedule
            Intent intent = new Intent(HomeDoctorActivity.this, AddScheduleActivity.class);
            startActivity(intent);
        });
    }

    //mengembalikan tampilan dari beberapa elemen ke keadaan awal
    private void resetTextViews() {
        dijadwalkanTextView.setTextColor(getResources().getColor(R.color.black));
        dijadwalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        selesaiTextView.setTextColor(getResources().getColor(R.color.black));
        selesaiTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        dibatalkanTextView.setTextColor(getResources().getColor(R.color.black));
        dibatalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        pengajuanTextView.setTextColor(getResources().getColor(R.color.black));
        pengajuanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
    }



    //metode untuk ambil data booking yang telah dikonformasi
    //deklarasi method
    private void loadBookingsWithConfirmation(String status, boolean confirmation, int layoutId) {
        //ambil dari koleksi books
        db.collection("books")
                //filter dokumen berdasarkan id dokter yang saat ini login
                .whereEqualTo("doctorId", mAuth.getCurrentUser().getUid())
                //filter status
                .whereEqualTo("status", status)
                //filter konfirmasi
                .whereEqualTo("confirmation", confirmation)
                //eksekusi query
                .get()
                //tambahkan listener untuk tangani hasil query
                .addOnCompleteListener(task -> {
                    //jika operasi berhasil
                    if (task.isSuccessful()) {
                        //hapus data sebelumnya dari booking list
                        bookingList.clear();
                        //ambil setiap dokumen dari hasil query
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //parse data booking dan tambahkan ke booking list
                            parseBookingData(document);
                        }
                        //buat adapter baru dengan data dari bookinglist dan layoutid
                        adapter = new BookingAdapter(bookingList, layoutId);
                        //atur adapter ke rvdikonfirmasi
                        rvDikonfirmasi.setAdapter(adapter);
                    } else {
                        Log.d("HomeDoctorActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void loadBookings(String status, int layoutId) {
        db.collection("books")
                .whereEqualTo("doctorId", mAuth.getCurrentUser().getUid())
                .whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            parseBookingData(document);
                        }
                        adapter = new BookingAdapter(bookingList, layoutId);
                        rvDikonfirmasi.setAdapter(adapter);
                    } else {
                        Log.d("HomeDoctorActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void loadPendingBookings() {
        db.collection("books")
                .whereEqualTo("status", "book")
                .whereEqualTo("confirmation", false)
                .whereEqualTo("doctorId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            parseBookingData(document);
                        }
                        adapter = new BookingAdapter(bookingList, R.layout.item_pengajuan);
                        rvDikonfirmasi.setAdapter(adapter);
                    } else {
                        Log.d("HomeDoctorActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    //ambil data dari dokumen booking dan tambahan dari dokumen lain
    private void parseBookingData(QueryDocumentSnapshot document) {
        //dapatkan nilai atribut dari dokumen booking
        String userId = document.getString("userId");
        String doctorId = document.getString("doctorId");
        String id = document.getString("id");
        Timestamp date = document.getTimestamp("date");
        boolean confirmation = document.getBoolean("confirmation");

        // Get the user document to retrieve the avatarUrl
        db.collection("users").document(userId)
                //eksekusi query dan tampilkan hasilnya
                .get()
                //tambahkan listerner
                .addOnSuccessListener(userDocument -> {
                    // Assuming "avatar" is the field name in the "users" collection
                    String avatarUrl = userDocument.getString("avatar");

                    // Create the Booking object
                    Booking booking = new Booking(userId, doctorId, date, id, confirmation);

                    // Set the avatarUrl
                    booking.setAvatarUrl(avatarUrl);

                    // Add the booking to the list
                    bookingList.add(booking);

                    // Notify the adapter about the data change
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("HomeDoctorActivity", "Error getting user document: ", e));
    }


    private class Booking {
        private String userId;
        private String doctorId;
        private String id;
        private Timestamp date;
        private boolean confirmation;
        private String avatarUrl; // Add this line

        public Booking(String userId, String doctorId, Timestamp date, String id, boolean confirmation) {
            this.userId = userId;
            this.doctorId = doctorId;
            this.id = id;
            this.date = date;
            this.confirmation = confirmation;
        }

        // getters and setters
        public String getUserId() { return userId; }
        public String getDoctorId() { return doctorId; }
        public String getId() { return id; }
        public Timestamp getDate() { return date; }
        public boolean getConfirmation() { return confirmation; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }


    //adapter kelola data booking dan menghubungkan ke recyclerview
    private class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
        //berisi data booking
        private List<Booking> bookingList;
        //menentukan tata letak item di recylerview
        private int layoutId;

        public BookingAdapter(List<Booking> bookingList, int layoutId) {
            this.bookingList = bookingList;
            this.layoutId = layoutId;
        }

        @NonNull
        @Override
        //mengembalikan objek BookingViewHolder
        public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //buat objek LayoutInflater
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            //membuat instance BookingViewHolder
            return new BookingViewHolder(view);
        }


        @Override
        //menghubungkan recyclerview dengan dataset bookinglist
        //position item dalam dataset akan dihubungkan dengan ViewHolder
        public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
            //mengambil data booking berdasarkan posisi
            Booking booking = bookingList.get(position);

            //cek jika layoutid-nya item_pengajuan
            if (layoutId == R.layout.item_pengajuan) {
                //inisialisasi button tolak
                Button tolakButton = holder.itemView.findViewById(R.id.tolakBtn);
                tolakButton.setOnClickListener(v -> {
                    //update status booking jadi cancel
                    updateBookingStatus(booking.getId(), "cancel");
                });
                //inisialisasi button konfirmasi
                Button konfirmasiButton = holder.itemView.findViewById(R.id.konfirmasiBtn);
                konfirmasiButton.setOnClickListener(v -> {
                //memanggil metode updateBookingConfirmation dengan ID booking
                    updateBookingConfirmation(booking.getId());
                });

                //inisialisasi avatar dan textview
                ImageView avatarImageView = holder.itemView.findViewById(R.id.avatar);
                TextView namaPasienTextView = holder.itemView.findViewById(R.id.namaPasien);
                TextView tanggalTextView = holder.itemView.findViewById(R.id.tanggal);
                TextView waktuTextView = holder.itemView.findViewById(R.id.waktu);

                // Set avatar using Glide
                Glide.with(holder.itemView.getContext()).load(booking.getAvatarUrl()).into(avatarImageView);

                // Load user data from Firestore and update the corresponding TextViews
                //akses collection user dan get document berdasarkan ID
                db.collection("users").document(booking.getUserId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            //inisialisasi variable userName berisi nilai dari field name di firestore
                            String userName = documentSnapshot.getString("name");
                            //textview menjadi nilai dari username
                            namaPasienTextView.setText(userName);

                            // Format date
                            //memformat tanggal
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            //memformat waktu
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm 'WIB'", Locale.getDefault());
                            //
                            tanggalTextView.setText(dateFormat.format(booking.getDate().toDate()));
                            waktuTextView.setText(timeFormat.format(booking.getDate().toDate()));
                        })
                        .addOnFailureListener(e -> Log.d("HomeDoctorActivity", "Error getting user data: ", e));
            }
            else if (layoutId == R.layout.item_antrian) {
                Button selesaiButton = holder.itemView.findViewById(R.id.finishBtn);
                selesaiButton.setOnClickListener(v -> {
                    updateBookingStatus(booking.getId(), "finish");
                });

                ImageView avatarImageView = holder.itemView.findViewById(R.id.avatar);
                TextView namaPasienTextView = holder.itemView.findViewById(R.id.namaPasien);
                TextView tanggalTextView = holder.itemView.findViewById(R.id.tanggal);
                TextView waktuTextView = holder.itemView.findViewById(R.id.waktu);

                // Set avatar using Glide
                Glide.with(holder.itemView.getContext()).load(booking.getAvatarUrl()).into(avatarImageView);

                // Load user data from Firestore and update the corresponding TextViews
                db.collection("users").document(booking.getUserId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String userName = documentSnapshot.getString("name");
                            namaPasienTextView.setText(userName);

                            // Format date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm 'WIB'", Locale.getDefault());
                            tanggalTextView.setText(dateFormat.format(booking.getDate().toDate()));
                            waktuTextView.setText(timeFormat.format(booking.getDate().toDate()));
                        })
                        .addOnFailureListener(e -> Log.d("HomeDoctorActivity", "Error getting user data: ", e));
            } else if (layoutId == R.layout.item_selesai_doctor || layoutId == R.layout.item_ditolak_doctor) {

                ImageView avatarImageView = holder.itemView.findViewById(R.id.avatar);
                TextView namaPasienTextView = holder.itemView.findViewById(R.id.namaPasien);
                TextView tanggalTextView = holder.itemView.findViewById(R.id.tanggal);
                TextView waktuTextView = holder.itemView.findViewById(R.id.waktu);

                // Set avatar using Glide
                Glide.with(holder.itemView.getContext()).load(booking.getAvatarUrl()).into(avatarImageView);

                // Load user data from Firestore and update the corresponding TextViews
                db.collection("users").document(booking.getUserId())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String userName = documentSnapshot.getString("name");
                            namaPasienTextView.setText(userName);

                            // Format date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm 'WIB'", Locale.getDefault());
                            tanggalTextView.setText(dateFormat.format(booking.getDate().toDate()));
                            waktuTextView.setText(timeFormat.format(booking.getDate().toDate()));
                        })
                        .addOnFailureListener(e -> Log.d("HomeDoctorActivity", "Error getting user data: ", e));
            }
        }


        @Override
        public int getItemCount() {
            return bookingList.size();
        }

        class BookingViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView3;
            TextView tanggalDijadwalkan;
            TextView jamDijadwalkan;

            public BookingViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.avatar); // Make sure this ID matches the one in your XML
                textView3 = itemView.findViewById(R.id.textView3);
                tanggalDijadwalkan = itemView.findViewById(R.id.tanggal_dijadwalkan);
                jamDijadwalkan = itemView.findViewById(R.id.jam_dijadwalkan);
            }
        }

        private void updateBookingConfirmation(String documentId) {
            // Update the document in Firestore to set the confirmation to true
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("confirmation", true);

            db.collection("books").document(documentId)
                    .update(updateData)
                    .addOnSuccessListener(aVoid -> {
                        // Show an alert or perform any action after updating the confirmation
                        Toast.makeText(HomeDoctorActivity.this, "Booking confirmed", Toast.LENGTH_SHORT).show();

                        // Reload the bookings
                        loadPendingBookings();

                        // Get additional data for notification
                        db.collection("books").document(documentId)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    String doctorId = documentSnapshot.getString("doctorId");
                                    String userId = documentSnapshot.getString("userId");

                                    // Get doctor's name
                                    db.collection("doctors").document(doctorId)
                                            .get()
                                            .addOnSuccessListener(doctorDocument -> {
                                                String doctorName = doctorDocument.getString("nama");

                                                // Create notification document
                                                Map<String, Object> notificationData = new HashMap<>();
                                                notificationData.put("id", documentId); // or generate a new id
                                                notificationData.put("judul", "Pengajuan Diterima");
                                                notificationData.put("subjudul", "Pengajuan konsultasi diterima oleh " + doctorName);
                                                notificationData.put("status", false);
                                                notificationData.put("date", Timestamp.now());

                                                // Add notification to user's notifications subcollection
                                                db.collection("users").document(userId)
                                                        .collection("notifications")
                                                        .document(documentId) // or use another id
                                                        .set(notificationData)
                                                        .addOnSuccessListener(aVoid1 -> {
                                                            // Successfully added notification
                                                            Log.d("HomeDoctorActivity", "Notification added successfully");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Failed to add notification
                                                            Log.e("HomeDoctorActivity", "Error adding notification", e);
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                // Failed to get doctor's name
                                                Log.e("HomeDoctorActivity", "Error getting doctor's name", e);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to get booking data
                                    Log.e("HomeDoctorActivity", "Error getting booking data", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.d("HomeDoctorActivity", "Error updating document: " + documentId, e);
                    });
        }


        private void updateBookingStatus(String documentId, String status) {
            // Update the document in Firestore to set the status
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("status", status);

            db.collection("books").document(documentId)
                    .update(updateData)
                    .addOnSuccessListener(aVoid -> {
                        // Show an alert or perform any action after updating the status
                        Toast.makeText(HomeDoctorActivity.this, "Booking status updated", Toast.LENGTH_SHORT).show();
                        // Reload the bookings
                        if(Objects.equals(status, "cancel"))
                        {
                            loadPendingBookings();
                        }
                        else if(Objects.equals(status, "finish"))
                        {
                            loadBookingsWithConfirmation("book", true, R.layout.item_antrian);
                        }

                    })
                    .addOnFailureListener(e -> Log.d("HomeDoctorActivity", "Error updating document status: " + documentId, e));
        }
    }
}