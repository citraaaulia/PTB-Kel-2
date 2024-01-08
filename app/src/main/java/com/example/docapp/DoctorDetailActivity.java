package com.example.docapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.Timestamp;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;



public class DoctorDetailActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String idDokter;
    private TextView textDokterNama, textRumahSakit, textKonfirmasi, tanggal1, tanggal2, tanggal3, tanggal4, tanggal5;
    private ImageView imgDrlaki;
    private View shapeView1, shapeView2, shapeView3, shapeView4, shapeView5;
    private View currentShapeView;
    private View currentHourView;
    private TextView currentTanggal;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private String selectedTime;
    private int month1, month2, month3, month4, month5;
    private int currentMonth;
    private Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_doctor);

        idDokter = getIntent().getStringExtra("idDokter");

        textDokterNama = findViewById(R.id.textDokterNama);
        textRumahSakit = findViewById(R.id.textRumahSakit);
        textKonfirmasi = findViewById(R.id.textKonfirmasi);
        imgDrlaki = findViewById(R.id.imgDrlaki);
        shapeView1 = findViewById(R.id.shapeView1);
        shapeView2 = findViewById(R.id.shapeView2);
        shapeView3 = findViewById(R.id.shapeView3);
        shapeView4 = findViewById(R.id.shapeView4);
        shapeView5 = findViewById(R.id.shapeView5);
        tanggal1 = findViewById(R.id.tanggal1);
        tanggal2 = findViewById(R.id.tanggal2);
        tanggal3 = findViewById(R.id.tanggal3);
        tanggal4 = findViewById(R.id.tanggal4);
        tanggal5 = findViewById(R.id.tanggal5);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        findViewById(R.id.shapeView6).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView7).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView8).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView9).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView10).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView11).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView12).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView13).setBackgroundColor(getResources().getColor(R.color.disableColor));
        findViewById(R.id.shapeView14).setBackgroundColor(getResources().getColor(R.color.disableColor));
        buttonSubmit.setBackgroundTintList(getResources().getColorStateList(R.color.disableColor));

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadDoctorData();
    }

    View.OnClickListener shapeViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentShapeView != null) {
                currentShapeView.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
            }
            v.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            currentShapeView = v;
            View clickedView = v;

            if (clickedView.getId() == R.id.shapeView1) {
                currentTanggal = tanggal1;
                currentMonth = month1;
            } else if (clickedView.getId() == R.id.shapeView2) {
                currentTanggal = tanggal2;
                currentMonth = month2;
            } else if (clickedView.getId() == R.id.shapeView3) {
                currentTanggal = tanggal3;
                currentMonth = month3;
            } else if (clickedView.getId() == R.id.shapeView4) {
                currentTanggal = tanggal4;
                currentMonth = month4;
            } else if (clickedView.getId() == R.id.shapeView5) {
                currentTanggal = tanggal5;
                currentMonth = month5;
            }

            Log.e("pesanDoktor", "Bulan: "+ currentMonth);

            loadBookSchedule();
        }
    };

    private void loadDoctorData() {
        db.collection("doctors").document(idDokter)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            textDokterNama.setText(document.getString("nama"));
                            textRumahSakit.setText(document.getString("hospital"));
                            textKonfirmasi.setText(document.getString("profil"));
                            Glide.with(this).load(document.getString("avatar")).into(imgDrlaki);

                            // Handle bookSchedule field
                            List<com.google.firebase.Timestamp> bookSchedule = (List<com.google.firebase.Timestamp>) document.get("bookSchedule");
                            if (bookSchedule != null) {
                                for (com.google.firebase.Timestamp timestamp : bookSchedule) {
                                    java.util.Date date = timestamp.toDate(); // Convert Timestamp to Date
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    int dateOfMonth = calendar.get(Calendar.DATE);
                                    int month = calendar.get(Calendar.MONTH) + 1;

                                    switch (dayOfWeek) {
                                        case Calendar.MONDAY:
                                            shapeView1.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                            tanggal1.setText(String.valueOf(dateOfMonth));
                                            shapeView1.setOnClickListener(shapeViewClickListener);
                                            month1 = month;
                                            break;
                                        case Calendar.TUESDAY:
                                            shapeView2.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                            tanggal2.setText(String.valueOf(dateOfMonth));
                                            shapeView2.setOnClickListener(shapeViewClickListener);
                                            month2 = month;
                                            break;
                                        case Calendar.WEDNESDAY:
                                            shapeView3.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                            tanggal3.setText(String.valueOf(dateOfMonth));
                                            shapeView3.setOnClickListener(shapeViewClickListener);
                                            month3 = month;
                                            break;
                                        case Calendar.THURSDAY:
                                            shapeView4.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                            tanggal4.setText(String.valueOf(dateOfMonth));
                                            shapeView4.setOnClickListener(shapeViewClickListener);
                                            month4 = month;
                                            break;
                                        case Calendar.FRIDAY:
                                            shapeView5.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                            tanggal5.setText(String.valueOf(dateOfMonth));
                                            shapeView5.setOnClickListener(shapeViewClickListener);
                                            month5 = month;
                                            break;
                                        // Add cases for other days if needed
                                    }
                                }
                            }

                            // Hide views if no schedule is available for a particular day
                            hideViewIfNoSchedule(shapeView1, tanggal1);
                            hideViewIfNoSchedule(shapeView2, tanggal2);
                            hideViewIfNoSchedule(shapeView3, tanggal3);
                            hideViewIfNoSchedule(shapeView4, tanggal4);
                            hideViewIfNoSchedule(shapeView5, tanggal5);
                        }
                    }
                });
    }


    private void hideViewIfNoSchedule(View shapeView, TextView tanggalView) {
        // Check if the TextView has no date set (or you can check for a default value if you set one)
        if (tanggalView.getText().toString().isEmpty() || tanggalView.getText().toString().equals("default")) {
            shapeView.setBackgroundColor(getResources().getColor(R.color.disableColor));
            tanggalView.setVisibility(View.GONE);
        }
    }

    private void loadBookSchedule() {
        buttonSubmit.setBackgroundTintList(getResources().getColorStateList(R.color.disableColor));
        buttonSubmit.setOnClickListener(null);
        if (currentTanggal == null) {
            return;
        }
        String selectedDate = currentTanggal.getText().toString();
        Log.e("pesanDoktor", "Tanggal: "+ selectedDate);

        // Set all TextViews to empty string
        for (int i = 6; i <= 14; i++) {
            int resId = getResources().getIdentifier("jam" + i, "id", getPackageName());
            TextView jamView = findViewById(resId);
            jamView.setText("");

        }

        for (int i = 6; i <= 14; i++) {
            int viewId = getResources().getIdentifier("shapeView" + i, "id", getPackageName());
            View shapeTanggal = findViewById(viewId);
            shapeTanggal.setOnClickListener(null); // Remove the onClick listener
            shapeTanggal.setBackgroundColor(getResources().getColor(R.color.disableColor));
            currentHourView = null;
        }

        selectedTime = null;

        db.collection("doctors").document(idDokter)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<com.google.firebase.Timestamp> bookSchedule = (List<com.google.firebase.Timestamp>) document.get("bookSchedule");
                            if (bookSchedule != null) {
                                int count = 0;

                                for (com.google.firebase.Timestamp timestamp : bookSchedule) {
                                    Date date = timestamp.toDate(); // Convert Timestamp to Date
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int dateValue = calendar.get(Calendar.DATE);
                                    int monthValue = calendar.get(Calendar.MONTH) + 1; // Tambah 1 untuk mendapatkan bulan yang sesuai

                                    // Check apakah tanggal dan bulan sama
                                    if (dateValue == Integer.parseInt(selectedDate) && monthValue == currentMonth) {
                                        // Set the TextView text to the formatted time string
                                        String timeStr = timeFormat.format(date);
                                        TextView jamView = findViewById(getResources().getIdentifier("jam" + (6 + count), "id", getPackageName()));
                                        jamView.setText(timeStr);

                                        // Set the color of the corresponding shape view
                                        View shapeView = findViewById(getResources().getIdentifier("shapeView" + (6 + count), "id", getPackageName()));
                                        shapeView.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                        count++;

                                        shapeView.setOnClickListener(v -> {
                                            selectedTime = timeStr;
                                            Log.e("pesanDoktor", "Hour: "+ selectedTime);
                                            if (currentHourView != null) {
                                                currentHourView.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
                                            }
                                            shapeView.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                                            currentHourView = shapeView;
                                            buttonSubmit.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
                                            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    createBooking();
                                                }
                                            });
                                        });
                                    }
                                }
// ...

                            }
                        }
                    }
                });
    }

    private void createBooking() {
        if (selectedTime == null || currentTanggal == null) {
            // Handle case where no time or date is selected
            return;
        }

        String selectedDate = currentTanggal.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> booking = new HashMap<>();
        booking.put("doctorId", idDokter);
        booking.put("userId", userId);
        booking.put("status", "book");
        booking.put("confirmation", false);

        // Parse the selectedTime into hours and minutes
        String[] timeParts = selectedTime.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        // Create a Calendar instance and set the date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, currentMonth - 1);  // Adjust month to be 0-based
        calendar.set(Calendar.DATE, Integer.parseInt(selectedDate));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND,0);

        // Create a Timestamp from the Calendar instance
        Timestamp timestamp = new Timestamp(calendar.getTime());
        booking.put("date", timestamp);

        Log.e("pesanDoktor", "Date: "+ timestamp);

        String documentId = getIntent().getStringExtra("dokumentId");
        if (documentId != null && !documentId.isEmpty()) {
            // Update existing document
            db.collection("books").document(documentId)
                    .set(booking)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Booking", "DocumentSnapshot successfully updated!");
                            Toast.makeText(DoctorDetailActivity.this, "Pemesanan Konsultasi berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Booking", "Error updating document", e);
                        }
                    });
        } else {
            // Create new document
            db.collection("books").add(booking)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Booking", "DocumentSnapshot written with ID: " + documentReference.getId());
                            Toast.makeText(DoctorDetailActivity.this, "Pemesanan Konsultasi berhasil dibuat", Toast.LENGTH_SHORT).show();

                            // Update the document with its own ID
                            documentReference.update("id", documentReference.getId())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Booking", "DocumentSnapshot successfully updated with its own ID!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Booking", "Error updating document", e);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Booking", "Error adding document", e);
                        }
                    });
        }
    }




}
