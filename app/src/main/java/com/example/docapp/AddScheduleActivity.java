package com.example.docapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Objects;

import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddScheduleActivity extends AppCompatActivity {

    private static final String TAG = "AddScheduleActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button updateButton;
    private String mondayDate1, mondayDate2, mondayDate3, mondayDate4, mondayDate5, mondayDate6, mondayDate7, mondayDate8, mondayDate9;
    private String tuesdayDate1, tuesdayDate2, tuesdayDate3, tuesdayDate4, tuesdayDate5, tuesdayDate6, tuesdayDate7, tuesdayDate8, tuesdayDate9;
    private String wednesdayDate1, wednesdayDate2, wednesdayDate3, wednesdayDate4, wednesdayDate5, wednesdayDate6, wednesdayDate7, wednesdayDate8, wednesdayDate9;
    private String thursdayDate1, thursdayDate2, thursdayDate3, thursdayDate4, thursdayDate5, thursdayDate6, thursdayDate7, thursdayDate8, thursdayDate9;
    private String fridayDate1, fridayDate2, fridayDate3, fridayDate4, fridayDate5, fridayDate6, fridayDate7, fridayDate8, fridayDate9;

    private String tanggalMonday, tanggalTuesday, tanggalWednesday, tanggalThursday, tanggalFriday;
    private View mondayView1, mondayView2, mondayView3, mondayView4, mondayView5, mondayView6, mondayView7, mondayView8, mondayView9;
    private View tuesdayView1, tuesdayView2, tuesdayView3, tuesdayView4, tuesdayView5, tuesdayView6, tuesdayView7, tuesdayView8, tuesdayView9;
    private View wednesdayView1, wednesdayView2, wednesdayView3, wednesdayView4, wednesdayView5, wednesdayView6, wednesdayView7, wednesdayView8, wednesdayView9;
    private View thursdayView1, thursdayView2, thursdayView3, thursdayView4, thursdayView5, thursdayView6, thursdayView7, thursdayView8, thursdayView9;
    private View fridayView1, fridayView2, fridayView3, fridayView4, fridayView5, fridayView6, fridayView7, fridayView8, fridayView9;

    private TextView mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView;
    // ... (other declarations)
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mondayTextView = findViewById(R.id.dateMonday);
        tuesdayTextView = findViewById(R.id.dateTuesday);
        wednesdayTextView = findViewById(R.id.dateWednesday);
        thursdayTextView = findViewById(R.id.dateThursday);
        fridayTextView = findViewById(R.id.dateFriday);

        mondayView1 = findViewById(R.id.monday1);
        mondayView2 = findViewById(R.id.monday2);
        mondayView3 = findViewById(R.id.monday3);
        mondayView4 = findViewById(R.id.monday4);
        mondayView5 = findViewById(R.id.monday5);
        mondayView6 = findViewById(R.id.monday6);
        mondayView7 = findViewById(R.id.monday7);
        mondayView8 = findViewById(R.id.monday8);
        mondayView9 = findViewById(R.id.monday9);

        tuesdayView1 = findViewById(R.id.tuesday1);
        tuesdayView2 = findViewById(R.id.tuesday2);
        tuesdayView3 = findViewById(R.id.tuesday3);
        tuesdayView4 = findViewById(R.id.tuesday4);
        tuesdayView5 = findViewById(R.id.tuesday5);
        tuesdayView6 = findViewById(R.id.tuesday6);
        tuesdayView7 = findViewById(R.id.tuesday7);
        tuesdayView8 = findViewById(R.id.tuesday8);
        tuesdayView9 = findViewById(R.id.tuesday9);

        wednesdayView1 = findViewById(R.id.wednesday1);
        wednesdayView2 = findViewById(R.id.wednesday2);
        wednesdayView3 = findViewById(R.id.wednesday3);
        wednesdayView4 = findViewById(R.id.wednesday4);
        wednesdayView5 = findViewById(R.id.wednesday5);
        wednesdayView6 = findViewById(R.id.wednesday6);
        wednesdayView7 = findViewById(R.id.wednesday7);
        wednesdayView8 = findViewById(R.id.wednesday8);
        wednesdayView9 = findViewById(R.id.wednesday9);

        thursdayView1 = findViewById(R.id.thursday1);
        thursdayView2 = findViewById(R.id.thursday2);
        thursdayView3 = findViewById(R.id.thursday3);
        thursdayView4 = findViewById(R.id.thursday4);
        thursdayView5 = findViewById(R.id.thursday5);
        thursdayView6 = findViewById(R.id.thursday6);
        thursdayView7 = findViewById(R.id.thursday7);
        thursdayView8 = findViewById(R.id.thursday8);
        thursdayView9 = findViewById(R.id.thursday9);

        fridayView1 = findViewById(R.id.friday1);
        fridayView2 = findViewById(R.id.friday2);
        fridayView3 = findViewById(R.id.friday3);
        fridayView4 = findViewById(R.id.friday4);
        fridayView5 = findViewById(R.id.friday5);
        fridayView6 = findViewById(R.id.friday6);
        fridayView7 = findViewById(R.id.friday7);
        fridayView8 = findViewById(R.id.friday8);
        fridayView9 = findViewById(R.id.friday9);

        setupClickListeners();

        mondayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mondayTextView, "monday");
            }
        });

        tuesdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tuesdayTextView, "tuesday");
            }
        });

        wednesdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(wednesdayTextView, "wednesday");
            }
        });

        thursdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(thursdayTextView, "thursday");
            }
        });

        fridayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fridayTextView, "friday");
            }
        });

        // Mendapatkan user yang sedang login
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            loadScheduleData(userId);
        }

        updateButton = findViewById(R.id.updateButton);

        // Mengatur onClickListener untuk updateButton
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule();
            }
        });

        TextView hapusSemuaTextView = findViewById(R.id.hapusSemua);
        hapusSemuaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil metode untuk menghapus bookSchedule
                deleteBookSchedule();
            }
        });
    }

    private void showDatePickerDialog(final TextView textView, final String hari) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Handle the date selection
                // Update the TextView
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                textView.setText(selectedDate);

                // Update the corresponding tanggal variable
                if(Objects.equals(hari, "monday"))
                {
                    tanggalMonday = selectedDate;
                    Log.d(TAG, "Tanggal Senin :"+tanggalMonday);
                }
                else if(Objects.equals(hari, "tuesday"))
                {
                    tanggalTuesday = selectedDate;
                    Log.d(TAG, "Tanggal Selasa :"+tanggalTuesday);
                }
                else if(Objects.equals(hari, "wednesday"))
                {
                    tanggalWednesday = selectedDate;
                    Log.d(TAG, "Tanggal Rabu :"+tanggalWednesday);
                }
                else if(Objects.equals(hari, "thursday"))
                {
                    tanggalThursday = selectedDate;
                    Log.d(TAG, "Tanggal Kamis :"+tanggalThursday);
                }
                else if(Objects.equals(hari, "friday"))
                {
                    tanggalFriday = selectedDate;
                    Log.d(TAG, "Tanggal Jumat :"+tanggalFriday);
                }
            }
        };

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddScheduleActivity.this,
                dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void loadScheduleData(String userId) {
        DocumentReference userDocRef = db.collection("doctors").document(userId);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Object> bookSchedule = (List<Object>) document.get("bookSchedule");

                    if (bookSchedule != null) {
                        processBookSchedule(bookSchedule);
                    }
                } else {
                    Log.d(TAG, "Document does not exist");
                }
            } else {
                Log.d(TAG, "Failed with ", task.getException());
            }
        });
    }

    private void processBookSchedule(List<Object> bookSchedule) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Object scheduleItem : bookSchedule) {
            if (scheduleItem instanceof com.google.firebase.Timestamp) {
                com.google.firebase.Timestamp timestamp = (com.google.firebase.Timestamp) scheduleItem;
                Date date = timestamp.toDate();

                String formattedDateTime = dateFormat.format(date);
                String dayOfWeek = getDayOfWeek(date);

                switch (dayOfWeek) {
                    case "Monday":
                        setMondayData(formattedDateTime);
                        break;
                    case "Tuesday":
                        setTuesdayData(formattedDateTime);
                        break;
                    case "Wednesday":
                        setWednesdayData(formattedDateTime);
                        break;
                    case "Thursday":
                        setThursdayData(formattedDateTime);
                        break;
                    case "Friday":
                        setFridayData(formattedDateTime);
                        break;
                }
            } else {
                // Handle other types if needed
            }
        }
    }



    private String getDayOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    private void setMondayData(String formattedDateTime) {
        String time = getTimeFromDateTime(formattedDateTime);
        String date = getDateFromDateTime(formattedDateTime);
        if (tanggalMonday == null || tanggalMonday.isEmpty()) {
            tanggalMonday = date;
        }

        if (mondayDate1 == null || mondayDate1.isEmpty()) {
            mondayDate1 = time;
            updateViewColor(mondayTextView, mondayView1, tanggalMonday);
        } else if (mondayDate2 == null || mondayDate2.isEmpty()) {
            mondayDate2 = time;
            updateViewColor(mondayTextView, mondayView2, tanggalMonday);
        } else if (mondayDate3 == null || mondayDate3.isEmpty()) {
            mondayDate3 = time;
            updateViewColor(mondayTextView, mondayView3, tanggalMonday);
        } else if (mondayDate4 == null || mondayDate4.isEmpty()) {
            mondayDate4 = time;
            updateViewColor(mondayTextView, mondayView4, tanggalMonday);
        } else if (mondayDate5 == null || mondayDate5.isEmpty()) {
            mondayDate5 = time;
            updateViewColor(mondayTextView, mondayView5, tanggalMonday);
        } else if (mondayDate6 == null || mondayDate6.isEmpty()) {
            mondayDate6 = time;
            updateViewColor(mondayTextView, mondayView6, tanggalMonday);
        } else if (mondayDate7 == null || mondayDate7.isEmpty()) {
            mondayDate7 = time;
            updateViewColor(mondayTextView, mondayView7, tanggalMonday);
        } else if (mondayDate8 == null || mondayDate8.isEmpty()) {
            mondayDate8 = time;
            updateViewColor(mondayTextView, mondayView8, tanggalMonday);
        } else if (mondayDate9 == null || mondayDate9.isEmpty()) {
            mondayDate9 = time;
            updateViewColor(mondayTextView, mondayView9, tanggalMonday);
        } else {
            // Handle if more than 9 appointments on Monday
        }
    }

    private void setTuesdayData(String formattedDateTime) {
        String time = getTimeFromDateTime(formattedDateTime);
        String date = getDateFromDateTime(formattedDateTime);
        if (tanggalTuesday == null || tanggalTuesday.isEmpty()) {
            tanggalTuesday = date;
        }

        if (tuesdayDate1 == null || tuesdayDate1.isEmpty()) {
            tuesdayDate1 = time;
            updateViewColor(tuesdayTextView, tuesdayView1, tanggalTuesday);
        } else if (tuesdayDate2 == null || tuesdayDate2.isEmpty()) {
            tuesdayDate2 = time;
            updateViewColor(tuesdayTextView, tuesdayView2, tanggalTuesday);
        } else if (tuesdayDate3 == null || tuesdayDate3.isEmpty()) {
            tuesdayDate3 = time;
            updateViewColor(tuesdayTextView, tuesdayView3, tanggalTuesday);
        } else if (tuesdayDate4 == null || tuesdayDate4.isEmpty()) {
            tuesdayDate4 = time;
            updateViewColor(tuesdayTextView, tuesdayView4, tanggalTuesday);
        } else if (tuesdayDate5 == null || tuesdayDate5.isEmpty()) {
            tuesdayDate5 = time;
            updateViewColor(tuesdayTextView, tuesdayView5, tanggalTuesday);
        } else if (tuesdayDate6 == null || tuesdayDate6.isEmpty()) {
            tuesdayDate6 = time;
            updateViewColor(tuesdayTextView, tuesdayView6, tanggalTuesday);
        } else if (tuesdayDate7 == null || tuesdayDate7.isEmpty()) {
            tuesdayDate7 = time;
            updateViewColor(tuesdayTextView, tuesdayView7, tanggalTuesday);
        } else if (tuesdayDate8 == null || tuesdayDate8.isEmpty()) {
            tuesdayDate8 = time;
            updateViewColor(tuesdayTextView, tuesdayView8, tanggalTuesday);
        } else if (tuesdayDate9 == null || tuesdayDate9.isEmpty()) {
            tuesdayDate9 = time;
            updateViewColor(tuesdayTextView, tuesdayView9, tanggalTuesday);
        } else {
            // Handle if more than 9 appointments on Tuesday
        }
    }

    private void setWednesdayData(String formattedDateTime) {
        String time = getTimeFromDateTime(formattedDateTime);
        String date = getDateFromDateTime(formattedDateTime);
        if (tanggalWednesday == null || tanggalWednesday.isEmpty()) {
            tanggalWednesday = date;
        }

        if (wednesdayDate1 == null || wednesdayDate1.isEmpty()) {
            wednesdayDate1 = time;
            updateViewColor(wednesdayTextView, wednesdayView1, tanggalWednesday);
        } else if (wednesdayDate2 == null || wednesdayDate2.isEmpty()) {
            wednesdayDate2 = time;
            updateViewColor(wednesdayTextView, wednesdayView2, tanggalWednesday);
        } else if (wednesdayDate3 == null || wednesdayDate3.isEmpty()) {
            wednesdayDate3 = time;
            updateViewColor(wednesdayTextView, wednesdayView3, tanggalWednesday);
        } else if (wednesdayDate4 == null || wednesdayDate4.isEmpty()) {
            wednesdayDate4 = time;
            updateViewColor(wednesdayTextView, wednesdayView4, tanggalWednesday);
        } else if (wednesdayDate5 == null || wednesdayDate5.isEmpty()) {
            wednesdayDate5 = time;
            updateViewColor(wednesdayTextView, wednesdayView5, tanggalWednesday);
        } else if (wednesdayDate6 == null || wednesdayDate6.isEmpty()) {
            wednesdayDate6 = time;
            updateViewColor(wednesdayTextView, wednesdayView6, tanggalWednesday);
        } else if (wednesdayDate7 == null || wednesdayDate7.isEmpty()) {
            wednesdayDate7 = time;
            updateViewColor(wednesdayTextView, wednesdayView7, tanggalWednesday);
        } else if (wednesdayDate8 == null || wednesdayDate8.isEmpty()) {
            wednesdayDate8 = time;
            updateViewColor(wednesdayTextView, wednesdayView8, tanggalWednesday);
        } else if (wednesdayDate9 == null || wednesdayDate9.isEmpty()) {
            wednesdayDate9 = time;
            updateViewColor(wednesdayTextView, wednesdayView9, tanggalWednesday);
        } else {
            // Handle if more than 9 appointments on Wednesday
        }
    }

    private void setThursdayData(String formattedDateTime) {
        String time = getTimeFromDateTime(formattedDateTime);
        String date = getDateFromDateTime(formattedDateTime);
        if (tanggalThursday == null || tanggalThursday.isEmpty()) {
            tanggalThursday = date;
        }

        if (thursdayDate1 == null || thursdayDate1.isEmpty()) {
            thursdayDate1 = time;
            updateViewColor(thursdayTextView, thursdayView1, tanggalThursday);
        } else if (thursdayDate2 == null || thursdayDate2.isEmpty()) {
            thursdayDate2 = time;
            updateViewColor(thursdayTextView, thursdayView2, tanggalThursday);
        } else if (thursdayDate3 == null || thursdayDate3.isEmpty()) {
            thursdayDate3 = time;
            updateViewColor(thursdayTextView, thursdayView3, tanggalThursday);
        } else if (thursdayDate4 == null || thursdayDate4.isEmpty()) {
            thursdayDate4 = time;
            updateViewColor(thursdayTextView, thursdayView4, tanggalThursday);
        } else if (thursdayDate5 == null || thursdayDate5.isEmpty()) {
            thursdayDate5 = time;
            updateViewColor(thursdayTextView, thursdayView5, tanggalThursday);
        } else if (thursdayDate6 == null || thursdayDate6.isEmpty()) {
            thursdayDate6 = time;
            updateViewColor(thursdayTextView, thursdayView6, tanggalThursday);
        } else if (thursdayDate7 == null || thursdayDate7.isEmpty()) {
            thursdayDate7 = time;
            updateViewColor(thursdayTextView, thursdayView7, tanggalThursday);
        } else if (thursdayDate8 == null || thursdayDate8.isEmpty()) {
            thursdayDate8 = time;
            updateViewColor(thursdayTextView, thursdayView8, tanggalThursday);
        } else if (thursdayDate9 == null || thursdayDate9.isEmpty()) {
            thursdayDate9 = time;
            updateViewColor(thursdayTextView, thursdayView9, tanggalThursday);
        } else {
            // Handle if more than 9 appointments on Thursday
        }
    }

    private void setFridayData(String formattedDateTime) {
        String time = getTimeFromDateTime(formattedDateTime);
        String date = getDateFromDateTime(formattedDateTime);
        if (tanggalFriday == null || tanggalFriday.isEmpty()) {
            tanggalFriday = date;
        }

        if (fridayDate1 == null || fridayDate1.isEmpty()) {
            fridayDate1 = time;
            updateViewColor(fridayTextView, fridayView1, tanggalFriday);
        } else if (fridayDate2 == null || fridayDate2.isEmpty()) {
            fridayDate2 = time;
            updateViewColor(fridayTextView, fridayView2, tanggalFriday);
        } else if (fridayDate3 == null || fridayDate3.isEmpty()) {
            fridayDate3 = time;
            updateViewColor(fridayTextView, fridayView3, tanggalFriday);
        } else if (fridayDate4 == null || fridayDate4.isEmpty()) {
            fridayDate4 = time;
            updateViewColor(fridayTextView, fridayView4, tanggalFriday);
        } else if (fridayDate5 == null || fridayDate5.isEmpty()) {
            fridayDate5 = time;
            updateViewColor(fridayTextView, fridayView5, tanggalFriday);
        } else if (fridayDate6 == null || fridayDate6.isEmpty()) {
            fridayDate6 = time;
            updateViewColor(fridayTextView, fridayView6, tanggalFriday);
        } else if (fridayDate7 == null || fridayDate7.isEmpty()) {
            fridayDate7 = time;
            updateViewColor(fridayTextView, fridayView7, tanggalFriday);
        } else if (fridayDate8 == null || fridayDate8.isEmpty()) {
            fridayDate8 = time;
            updateViewColor(fridayTextView, fridayView8, tanggalFriday);
        } else if (fridayDate9 == null || fridayDate9.isEmpty()) {
            fridayDate9 = time;
            updateViewColor(fridayTextView, fridayView9, tanggalFriday);
        } else {
            // Handle if more than 9 appointments on Friday
        }
    }

    private String getTimeFromDateTime(String formattedDateTime) {
        // Assuming the format is "dd/MM/yyyy HH:mm:ss"
        String[] dateTimeParts = formattedDateTime.split(" ");
        return dateTimeParts[1]; // Returns the time part
    }

    private String getDateFromDateTime(String formattedDateTime) {
        // Assuming the format is "dd/MM/yyyy HH:mm:ss"
        String[] dateTimeParts = formattedDateTime.split(" ");
        return dateTimeParts[0]; // Returns the date part
    }

    private void updateViewColor(TextView dayTextView, View dayView, String tanggal) {
        // Update the TextView with the date
        if(!Objects.equals(tanggal, "none"))
        {
            dayTextView.setText(tanggal);
        }

        // Update the View's background tint to the primary color
        dayView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryColor)));
    }

    private void setupClickListeners() {
        // Monday
        setOnClickListenerForDay(mondayView1, "mondayDate1");
        setOnClickListenerForDay(mondayView2, "mondayDate2");
        setOnClickListenerForDay(mondayView3, "mondayDate3");
        setOnClickListenerForDay(mondayView4, "mondayDate4");
        setOnClickListenerForDay(mondayView5, "mondayDate5");
        setOnClickListenerForDay(mondayView6, "mondayDate6");
        setOnClickListenerForDay(mondayView7, "mondayDate7");
        setOnClickListenerForDay(mondayView8, "mondayDate8");
        setOnClickListenerForDay(mondayView9, "mondayDate9");

        // Tuesday
        setOnClickListenerForDay(tuesdayView1, "tuesdayDate1");
        setOnClickListenerForDay(tuesdayView2, "tuesdayDate2");
        setOnClickListenerForDay(tuesdayView3, "tuesdayDate3");
        setOnClickListenerForDay(tuesdayView4, "tuesdayDate4");
        setOnClickListenerForDay(tuesdayView5, "tuesdayDate5");
        setOnClickListenerForDay(tuesdayView6, "tuesdayDate6");
        setOnClickListenerForDay(tuesdayView7, "tuesdayDate7");
        setOnClickListenerForDay(tuesdayView8, "tuesdayDate8");
        setOnClickListenerForDay(tuesdayView9, "tuesdayDate9");

        // Wednesday
        setOnClickListenerForDay(wednesdayView1, "wednesdayDate1");
        setOnClickListenerForDay(wednesdayView2, "wednesdayDate2");
        setOnClickListenerForDay(wednesdayView3, "wednesdayDate3");
        setOnClickListenerForDay(wednesdayView4, "wednesdayDate4");
        setOnClickListenerForDay(wednesdayView5, "wednesdayDate5");
        setOnClickListenerForDay(wednesdayView6, "wednesdayDate6");
        setOnClickListenerForDay(wednesdayView7, "wednesdayDate7");
        setOnClickListenerForDay(wednesdayView8, "wednesdayDate8");
        setOnClickListenerForDay(wednesdayView9, "wednesdayDate9");

        // Thursday
        setOnClickListenerForDay(thursdayView1, "thursdayDate1");
        setOnClickListenerForDay(thursdayView2, "thursdayDate2");
        setOnClickListenerForDay(thursdayView3, "thursdayDate3");
        setOnClickListenerForDay(thursdayView4, "thursdayDate4");
        setOnClickListenerForDay(thursdayView5, "thursdayDate5");
        setOnClickListenerForDay(thursdayView6, "thursdayDate6");
        setOnClickListenerForDay(thursdayView7, "thursdayDate7");
        setOnClickListenerForDay(thursdayView8, "thursdayDate8");
        setOnClickListenerForDay(thursdayView9, "thursdayDate9");

        // Friday
        setOnClickListenerForDay(fridayView1, "fridayDate1");
        setOnClickListenerForDay(fridayView2, "fridayDate2");
        setOnClickListenerForDay(fridayView3, "fridayDate3");
        setOnClickListenerForDay(fridayView4, "fridayDate4");
        setOnClickListenerForDay(fridayView5, "fridayDate5");
        setOnClickListenerForDay(fridayView6, "fridayDate6");
        setOnClickListenerForDay(fridayView7, "fridayDate7");
        setOnClickListenerForDay(fridayView8, "fridayDate8");
        setOnClickListenerForDay(fridayView9, "fridayDate9");
    }

    private void setOnClickListenerForDay(View dayView, String date) {
        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(dayView, date);
            }
        });
    }

    private void showTimePickerDialog(final View dayView, final String date) {
        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddScheduleActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the time selection
                        String selectedTime = hourOfDay + ":" + minute + ":00";

                        // Update the backgroundTint
                        dayView.setBackgroundColor(getResources().getColor(R.color.primaryColor));

                        // Update the corresponding date variable
                        updateDateVariable(date, selectedTime);
                    }
                },
                0, 0, true
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void updateDateVariable(String date, String selectedTime) {

        Log.d(TAG, "updateDateVariable: "+date+" "+selectedTime);
        // Assuming date is a valid index (e.g., "mondayDate1")
        switch (date) {
            // Monday
            case "mondayDate1":
                mondayDate1 = selectedTime;
                updateViewColor(mondayTextView, mondayView1, "none");
                break;
            case "mondayDate2":
                mondayDate2 = selectedTime;
                updateViewColor(mondayTextView, mondayView2, "none");
                break;
            case "mondayDate3":
                mondayDate3 = selectedTime;
                updateViewColor(mondayTextView, mondayView3, "none");
                break;
            case "mondayDate4":
                mondayDate4 = selectedTime;
                updateViewColor(mondayTextView, mondayView4, "none");
                break;
            case "mondayDate5":
                mondayDate5 = selectedTime;
                updateViewColor(mondayTextView, mondayView5, "none");
                break;
            case "mondayDate6":
                mondayDate6 = selectedTime;
                updateViewColor(mondayTextView, mondayView6, "none");
                break;
            case "mondayDate7":
                mondayDate7 = selectedTime;
                updateViewColor(mondayTextView, mondayView7, "none");
                break;
            case "mondayDate8":
                mondayDate8 = selectedTime;
                updateViewColor(mondayTextView, mondayView8, "none");
                break;
            case "mondayDate9":
                mondayDate9 = selectedTime;
                updateViewColor(mondayTextView, mondayView9, "none");
                break;

            // Tuesday
            case "tuesdayDate1":
                tuesdayDate1 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView1, "none");
                break;
            case "tuesdayDate2":
                tuesdayDate2 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView2, "none");
                break;
            case "tuesdayDate3":
                tuesdayDate3 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView3, "none");
                break;
            case "tuesdayDate4":
                tuesdayDate4 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView4, "none");
                break;
            case "tuesdayDate5":
                tuesdayDate5 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView5, "none");
                break;
            case "tuesdayDate6":
                tuesdayDate6 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView6, "none");
                break;
            case "tuesdayDate7":
                tuesdayDate7 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView7, "none");
                break;
            case "tuesdayDate8":
                tuesdayDate8 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView8, "none");
                break;
            case "tuesdayDate9":
                tuesdayDate9 = selectedTime;
                updateViewColor(tuesdayTextView, tuesdayView9, "none");
                break;

            // Wednesday
            case "wednesdayDate1":
                wednesdayDate1 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView1, "none");
                break;
            case "wednesdayDate2":
                wednesdayDate2 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView2, "none");
                break;
            case "wednesdayDate3":
                wednesdayDate3 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView3, "none");
                break;
            case "wednesdayDate4":
                wednesdayDate4 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView4, "none");
                break;
            case "wednesdayDate5":
                wednesdayDate5 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView5, "none");
                break;
            case "wednesdayDate6":
                wednesdayDate6 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView6, "none");
                break;
            case "wednesdayDate7":
                wednesdayDate7 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView7, "none");
                break;
            case "wednesdayDate8":
                wednesdayDate8 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView8, "none");
                break;
            case "wednesdayDate9":
                wednesdayDate9 = selectedTime;
                updateViewColor(wednesdayTextView, wednesdayView9, "none");
                break;

            // Thursday
            case "thursdayDate1":
                thursdayDate1 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView1, "none");
                break;
            case "thursdayDate2":
                thursdayDate2 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView2, "none");
                break;
            case "thursdayDate3":
                thursdayDate3 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView3, "none");
                break;
            case "thursdayDate4":
                thursdayDate4 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView4, "none");
                break;
            case "thursdayDate5":
                thursdayDate5 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView5, "none");
                break;
            case "thursdayDate6":
                thursdayDate6 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView6, "none");
                break;
            case "thursdayDate7":
                thursdayDate7 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView7, "none");
                break;
            case "thursdayDate8":
                thursdayDate8 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView8, "none");
                break;
            case "thursdayDate9":
                thursdayDate9 = selectedTime;
                updateViewColor(thursdayTextView, thursdayView9, "none");
                break;

            // Friday
            case "fridayDate1":
                fridayDate1 = selectedTime;
                updateViewColor(fridayTextView, fridayView1, "none");
                break;
            case "fridayDate2":
                fridayDate2 = selectedTime;
                updateViewColor(fridayTextView, fridayView2, "none");
                break;
            case "fridayDate3":
                fridayDate3 = selectedTime;
                updateViewColor(fridayTextView, fridayView3, "none");
                break;
            case "fridayDate4":
                fridayDate4 = selectedTime;
                updateViewColor(fridayTextView, fridayView4, "none");
                break;
            case "fridayDate5":
                fridayDate5 = selectedTime;
                updateViewColor(fridayTextView, fridayView5, "none");
                break;
            case "fridayDate6":
                fridayDate6 = selectedTime;
                updateViewColor(fridayTextView, fridayView6, "none");
                break;
            case "fridayDate7":
                fridayDate7 = selectedTime;
                updateViewColor(fridayTextView, fridayView7, "none");
                break;
            case "fridayDate8":
                fridayDate8 = selectedTime;
                updateViewColor(fridayTextView, fridayView8, "none");
                break;
            case "fridayDate9":
                fridayDate9 = selectedTime;
                updateViewColor(fridayTextView, fridayView9, "none");
                break;

        }

    }

    // Metode untuk menghapus bookSchedule
    private void deleteBookSchedule() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("doctors").document(userId);

        // Hapus field bookSchedule pada dokumen dengan ID userId
        docRef.update("bookSchedule", FieldValue.delete())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle sukses hapus
                        showToast("Berhasil menghapus semua jadwal");

                        // Pindah ke HomeDoctorActivity
                        startActivity(new Intent(AddScheduleActivity.this, HomeDoctorActivity.class));
                        finish(); // Optional: Menutup aktivitas saat ini jika diperlukan
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle gagal hapus
                        showToast("Gagal menghapus jadwal. Silakan coba lagi.");
                    }
                });
    }

    private void updateSchedule() {
        // Membuat array baru untuk menyimpan timestamp
        List<Object> scheduleArray = new ArrayList<>();

        // Contoh penanganan pada tanggalMonday
        if (tanggalMonday != null && !tanggalMonday.isEmpty()) {
            // Contoh penanganan untuk mondayDate1
            if (mondayDate1 != null && !mondayDate1.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate1);
                scheduleArray.add(combinedDateTime);
            }

            // Lakukan hal yang sama untuk mondayDate2, mondayDate3, ..., mondayDate9
            if (mondayDate2 != null && !mondayDate2.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate2);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate3 != null && !mondayDate3.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate3);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate4 != null && !mondayDate4.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate4);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate5 != null && !mondayDate5.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate5);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate6 != null && !mondayDate6.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate6);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate7 != null && !mondayDate7.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate7);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate8 != null && !mondayDate8.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate8);
                scheduleArray.add(combinedDateTime);
            }
            if (mondayDate9 != null && !mondayDate9.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalMonday, mondayDate9);
                scheduleArray.add(combinedDateTime);
            }

        }

        if (tanggalTuesday != null && !tanggalTuesday.isEmpty()) {
            // Contoh penanganan untuk mondayDate1
            if (tuesdayDate1 != null && !tuesdayDate1.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate1);
                scheduleArray.add(combinedDateTime);
            }

            // Lakukan hal yang sama untuk mondayDate2, mondayDate3, ..., mondayDate9
            if (tuesdayDate2 != null && !tuesdayDate2.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate2);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate3 != null && !tuesdayDate3.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate3);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate4 != null && !tuesdayDate4.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate4);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate5 != null && !tuesdayDate5.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate5);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate6 != null && !tuesdayDate6.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate6);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate7 != null && !tuesdayDate7.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate7);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate8 != null && !tuesdayDate8.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate8);
                scheduleArray.add(combinedDateTime);
            }
            if (tuesdayDate9 != null && !tuesdayDate9.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalTuesday, tuesdayDate9);
                scheduleArray.add(combinedDateTime);
            }
        }

        if (tanggalWednesday != null && !tanggalWednesday.isEmpty()) {
            if (wednesdayDate1 != null && !wednesdayDate1.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate1);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate2 != null && !wednesdayDate2.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate2);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate3 != null && !wednesdayDate3.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate3);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate4 != null && !wednesdayDate4.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate4);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate5 != null && !wednesdayDate5.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate5);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate6 != null && !wednesdayDate6.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate6);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate7 != null && !wednesdayDate7.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate7);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate8 != null && !wednesdayDate8.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate8);
                scheduleArray.add(combinedDateTime);
            }

            if (wednesdayDate9 != null && !wednesdayDate9.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalWednesday, wednesdayDate9);
                scheduleArray.add(combinedDateTime);
            }
        }

        // Menangani tanggalThursday
        if (tanggalThursday != null && !tanggalThursday.isEmpty()) {
            if (thursdayDate1 != null && !thursdayDate1.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate1);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate2 != null && !thursdayDate2.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate2);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate3 != null && !thursdayDate3.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate3);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate4 != null && !thursdayDate4.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate4);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate5 != null && !thursdayDate5.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate5);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate6 != null && !thursdayDate6.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate6);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate7 != null && !thursdayDate7.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate7);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate8 != null && !thursdayDate8.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate8);
                scheduleArray.add(combinedDateTime);
            }

            if (thursdayDate9 != null && !thursdayDate9.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalThursday, thursdayDate9);
                scheduleArray.add(combinedDateTime);
            }
        }

        // Menangani tanggalFriday
        if (tanggalFriday != null && !tanggalFriday.isEmpty()) {
            if (fridayDate1 != null && !fridayDate1.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate1);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate2 != null && !fridayDate2.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate2);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate3 != null && !fridayDate3.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate3);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate4 != null && !fridayDate4.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate4);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate5 != null && !fridayDate5.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate5);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate6 != null && !fridayDate6.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate6);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate7 != null && !fridayDate7.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate7);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate8 != null && !fridayDate8.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate8);
                scheduleArray.add(combinedDateTime);
            }

            if (fridayDate9 != null && !fridayDate9.isEmpty()) {
                Date combinedDateTime = combineDateTime(tanggalFriday, fridayDate9);
                scheduleArray.add(combinedDateTime);
            }
        }

        updateFirestore(scheduleArray);


    }

    private Date combineDateTime(String date, String time) {
        try {
            // Menggabungkan tanggal dan waktu menjadi objek Date
            String combinedDateTimeString = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return dateFormat.parse(combinedDateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Atau lakukan penanganan kesalahan lainnya sesuai kebutuhan aplikasi Anda
        }
    }


    private void updateFirestore(List<Object> scheduleArray) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("doctors").document(userId);

        // Update field bookSchedule pada dokumen dengan ID userId
        docRef.update("bookSchedule", scheduleArray)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle sukses update
                        // Misalnya, tampilkan pesan sukses atau lakukan tindakan setelah berhasil
                        showToast("Schedule updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle gagal update
                        // Misalnya, tampilkan pesan gagal atau lakukan tindakan penanganan kesalahan
                        showToast("Failed to update schedule: " + e.getMessage());
                    }
                });
    }

    // Fungsi utilitas untuk menampilkan pesan toast
    private void showToast(String message) {
        Toast.makeText(AddScheduleActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}