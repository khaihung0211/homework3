package com.example.homework3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton rbTwoDigit, rbThreeDigit, rbFourDigit;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        radioGroup = findViewById(R.id.radioGroup);
        rbTwoDigit = findViewById(R.id.rbTwoDigit);
        rbThreeDigit = findViewById(R.id.rbThreeDigit);
        rbFourDigit = findViewById(R.id.rbFourDigit);
        btnStart = findViewById(R.id.btnStart);
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void startGame() {
        int selectedDigits = getSelectedDigits();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("digits", selectedDigits);
        startActivity(intent);
    }

    private int getSelectedDigits() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.rbTwoDigit) {
            return 2;
        } else if (selectedId == R.id.rbThreeDigit) {
            return 3;
        } else if (selectedId == R.id.rbFourDigit) {
            return 4;
        }

        return 2; // Default
    }
}