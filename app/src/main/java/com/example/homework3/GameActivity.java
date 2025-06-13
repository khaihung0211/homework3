package com.example.homework3;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvLastGuess;
    private TextView tvRemainingAttempts;
    private TextView tvHint;
    private EditText etGuess;
    private Button btnConfirm;

    private int targetNumber;
    private int digits;
    private int attemptsLeft = 10;
    private List<Integer> guessList;
    private int lastGuess = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        digits = getIntent().getIntExtra("digits", 2);
        guessList = new ArrayList<>();

        generateTargetNumber();
        initViews();
        setupClickListeners();
        updateUI();
    }

    private void initViews() {
        tvLastGuess = findViewById(R.id.tvLastGuess);
        tvRemainingAttempts = findViewById(R.id.tvRemainingAttempts);
        tvHint = findViewById(R.id.tvHint);
        etGuess = findViewById(R.id.etGuess);
        btnConfirm = findViewById(R.id.btnConfirm);
    }

    private void setupClickListeners() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeGuess();
            }
        });
    }

    private void generateTargetNumber() {
        Random random = new Random();
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        targetNumber = random.nextInt(max - min + 1) + min;
    }

    private void makeGuess() {
        String guessText = etGuess.getText().toString().trim();

        if (guessText.isEmpty()) {
            Toast.makeText(this, "Please enter a " + digits + "-digit number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (guessText.length() != digits) {
            Toast.makeText(this, "Please enter a " + digits + "-digit number", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int guess = Integer.parseInt(guessText);

            guessList.add(guess);
            lastGuess = guess;
            attemptsLeft--;

            if (guess == targetNumber) {
                // Win
                showCongratulations();
                return;
            }

            if (attemptsLeft <= 0) {
                // Game Over - Show Dialog
                showGameOverDialog();
                return;
            }

            // Show hint
            showHint(guess);
            updateUI();
            etGuess.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void showHint(int guess) {
        if (guess < targetNumber) {
            tvHint.setText("Try a bigger number");
        } else {
            tvHint.setText("Try a smaller number");
        }
        tvHint.setVisibility(View.VISIBLE);
    }

    private void updateUI() {
        tvRemainingAttempts.setText("Remaining attempts: " + attemptsLeft);

        if (lastGuess != -1) {
            tvLastGuess.setText("Your last guess: " + lastGuess);
            tvLastGuess.setVisibility(View.VISIBLE);
        }
    }

    private void showCongratulations() {
        Toast.makeText(this, "Congratulations! You guessed correctly!", Toast.LENGTH_LONG).show();
        showGameOverDialog(); // Cũng hiện dialog khi thắng
    }

    private void showGameOverDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game_over);
        dialog.setCancelable(false);

        // Setup dialog views
        TextView tvMyNumber = dialog.findViewById(R.id.tvMyNumber);
        TextView tvGuesses = dialog.findViewById(R.id.tvGuesses);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        // Set data
        tvMyNumber.setText("My number was: " + targetNumber);

        StringBuilder guessesText = new StringBuilder();
        for (int i = 0; i < guessList.size(); i++) {
            if (i > 0) {
                guessesText.append(", ");
            }
            guessesText.append(guessList.get(i));
        }
        tvGuesses.setText(guessesText.toString());

        // Setup button listeners
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                playAgain();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private void playAgain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}