package com.example.docapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Button;
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
import java.util.List;
import java.util.Locale;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;


public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvDikonfirmasi;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Booking> bookingList;
    private BookingAdapter adapter;

    private TextView dijadwalkanTextView;
    private TextView selesaiTextView;
    private TextView dibatalkanTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeuser);

        rvDikonfirmasi = findViewById(R.id.rv_dikonfirmasi);
        rvDikonfirmasi.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList, R.layout.item_dijadwalkan);
        rvDikonfirmasi.setAdapter(adapter);

        Button cariListDokterButton = findViewById(R.id.cari_listdokter);
        cariListDokterButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ListDoctorActivity.class);
            startActivity(intent);
        });

        ImageButton notificationButton = findViewById(R.id.notification);
        notificationButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        Button massaIndexButton = findViewById(R.id.masa_index);
        massaIndexButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MassaIndexActivity.class);
            startActivity(intent);
        });

        loadBookings("book");
        dijadwalkanTextView = findViewById(R.id.textView);
        selesaiTextView = findViewById(R.id.textView2);
        dibatalkanTextView = findViewById(R.id.textView4);

        dijadwalkanTextView.setOnClickListener(v -> {
            try {
                resetTextViews();
                dijadwalkanTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                dijadwalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                loadBookings("book");
            } catch (Exception e) {
                Log.e("HomeActivity", "Error on click: ", e);
            }
        });

        selesaiTextView.setOnClickListener(v -> {
            try {
                resetTextViews();
                selesaiTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                selesaiTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                loadBookings("finish");
            } catch (Exception e) {
                Log.e("HomeActivity", "Error on click: ", e);
            }
        });

        dibatalkanTextView.setOnClickListener(v -> {
            try {
                resetTextViews();
                dibatalkanTextView.setTextColor(getResources().getColor(R.color.primaryColor));
                dibatalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                loadBookings("cancel");
            } catch (Exception e) {
                Log.e("HomeActivity", "Error on click: ", e);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home) {
                // Item Home yang aktif
                return true;
            } else if (item.getItemId() == R.id.action_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                finish(); // Akhiri HomeActivity jika pindah ke ProfileActivity
                return true;
            }
            return false;
        });


    }

    private void resetTextViews() {
        dijadwalkanTextView.setTextColor(getResources().getColor(R.color.black));
        dijadwalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        selesaiTextView.setTextColor(getResources().getColor(R.color.black));
        selesaiTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        dibatalkanTextView.setTextColor(getResources().getColor(R.color.black));
        dibatalkanTextView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
    }

    private void loadBookings(String status) {
        db.collection("books")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getString("userId");
                            String doctorId = document.getString("doctorId");
                            String id = document.getString("id");
                            Timestamp date = document.getTimestamp("date");
                            Booking booking = new Booking(userId, doctorId, date, id);
                            bookingList.add(booking);
                        }
                        int layoutId = status.equals("book") ? R.layout.item_dijadwalkan : status.equals("finish") ? R.layout.item_selesai : R.layout.item_dibatalkan;
                        adapter = new BookingAdapter(bookingList, layoutId);
                        rvDikonfirmasi.setAdapter(adapter);
                    } else {
                        Log.d("HomeActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    private class Booking {
        private String userId;
        private String doctorId;
        private String id;
        private Timestamp date;

        public Booking(String userId, String doctorId, Timestamp date, String id) {
            this.userId = userId;
            this.doctorId = doctorId;
            this.id = id;
            this.date = date;
        }

        // getters and setters
        public String getUserId() { return userId; }
        public String getDoctorId() { return doctorId; }
        public String getId() { return id; }
        public Timestamp getDate() { return date; }
    }

    private class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
        private List<Booking> bookingList;
        private int layoutId;

        public BookingAdapter(List<Booking> bookingList, int layoutId) {
            this.bookingList = bookingList;
            this.layoutId = layoutId;
        }

        @NonNull
        @Override
        public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new BookingViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
            Booking booking = bookingList.get(position);
            // get doctor data
            db.collection("doctors").document(booking.getDoctorId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String doctorName = documentSnapshot.getString("nama");
                        String avatarUrl = documentSnapshot.getString("avatar");
                        holder.textView3.setText(doctorName);
                        Glide.with(HomeActivity.this).load(avatarUrl).into(holder.imageView);
                        // format date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm 'WIB'", Locale.getDefault());
                        holder.tanggalDijadwalkan.setText(dateFormat.format(booking.getDate().toDate()));
                        holder.jamDijadwalkan.setText(timeFormat.format(booking.getDate().toDate()));

                        // If layoutId is item_dijadwalkan, find the button and set its onClickListener
                        if (layoutId == R.layout.item_dijadwalkan) {
                            Button batalkanButton = holder.itemView.findViewById(R.id.batalkan);
                            batalkanButton.setOnClickListener(v -> {
                                // Ensure the document ID is not null
                                String documentId = booking.getId(); // Assuming the booking ID is the document ID, adjust accordingly
                                Log.d("HomeActivity", "Cancelling document: " + documentId);
                                if (documentId != null) {
                                    // Update the document in Firestore to set the status to "cancel"
                                    Map<String, Object> updateData = new HashMap<>();
                                    updateData.put("status", "cancel");

                                    db.collection("books").document(documentId)
                                            .update(updateData)
                                            .addOnSuccessListener(aVoid -> {
                                                // Show an alert that the booking has been cancelled
                                                Toast.makeText(HomeActivity.this, "Pesanan telah dibatalkan", Toast.LENGTH_SHORT).show();
                                                // Reload the bookings
                                                loadBookings("book");
                                            })
                                            .addOnFailureListener(e -> Log.d("HomeActivity", "Error updating document: " + documentId, e));
                                } else {
                                    // Handle the case where the document ID is null
                                    Toast.makeText(HomeActivity.this, "Error: Document ID is null", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Button pesanLagiButton = holder.itemView.findViewById(R.id.ganti_jadwal);
                            pesanLagiButton.setOnClickListener(v -> {
                                Intent intent = new Intent(HomeActivity.this, DoctorDetailActivity.class);
                                intent.putExtra("idDokter", booking.getDoctorId());
                                intent.putExtra("dokumentId", booking.getId());
                                startActivity(intent);
                            });
                        }

                        else if (layoutId == R.layout.item_selesai || layoutId == R.layout.item_dibatalkan) {
                            Button gantiJadwalButton = holder.itemView.findViewById(R.id.ganti_jadwal);
                            gantiJadwalButton.setOnClickListener(v -> {
                                Intent intent = new Intent(HomeActivity.this, DoctorDetailActivity.class);
                                intent.putExtra("idDokter", booking.getDoctorId());
                                startActivity(intent);
                            });
                        }
                    })
                    .addOnFailureListener(e -> Log.d("HomeActivity", "Error getting doctor data: ", e));
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
    }
}