package com.example.docapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MassaIndexActivity extends AppCompatActivity {

    private EditText weightInput;
    private EditText heightInput;
    private TextView textHasilBMI;
    private TextView textStatusBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massa_index);

        weightInput = findViewById(R.id.weight_input);
        heightInput = findViewById(R.id.height_input);
        textHasilBMI = findViewById(R.id.textHasilBMI);
        textStatusBMI = findViewById(R.id.textStatusBMI);


        Button hitungButton = findViewById(R.id.ButtonHitung);
        hitungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        Button resetButton = findViewById(R.id.ButtonReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calculateBMI() {
        double weight = Double.parseDouble(weightInput.getText().toString());
        double height = Double.parseDouble(heightInput.getText().toString()) / 100; // convert cm to m
        double bmi = weight / (height * height);

        textHasilBMI.setText(String.format("%.2f", bmi));

        if (bmi < 18.5) {
            textStatusBMI.setText("Berat badan kurang");
        } else if (bmi < 25) {
            textStatusBMI.setText("Berat badan normal");
        } else if (bmi < 30) {
            textStatusBMI.setText("Kelebihan berat badan");
        } else if (bmi < 35) {
            textStatusBMI.setText("Obesitas tingkat 1");
        } else if (bmi < 40) {
            textStatusBMI.setText("Obesitas tingkat 2");
        } else {
            textStatusBMI.setText("Obesitas tingkat 3 (obesitas tinggi)");
        }
    }

    private void resetFields() {
        weightInput.setText("");
        heightInput.setText("");
        textHasilBMI.setText("");
        textStatusBMI.setText("");
    }
}
